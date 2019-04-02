package com.littlefox.storybook.lib.common;
/*
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.google.gson.Gson;
import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.api.StorybookTempleteAPI;
import com.littlefox.storybook.lib.object.ItemResult;

public class NetworkUtil
{

	public static final int TYPE_WIFI = 1;
	public static final int TYPE_MOBILE = 2;
	public static final int TYPE_NOT_CONNECTED = 0;

	public static final int CONNECTION_TIMEOUT = 15000;
	public static final int SOCKET_TIMEOUT = 15000;

	public static int getConnectivityStatus(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null)
		{
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		
		Log.f("Network Error");
		return TYPE_NOT_CONNECTED;
	}

	public static String getConnectivityStatusString(Context context)
	{
		int conn = NetworkUtil.getConnectivityStatus(context);
		String status = null;
		if (conn == NetworkUtil.TYPE_WIFI)
		{
			status = "Wifi enabled";
		}
		else if (conn == NetworkUtil.TYPE_MOBILE)
		{
			status = "Mobile data enabled";
		}
		else if (conn == NetworkUtil.TYPE_NOT_CONNECTED)
		{
			status = "Not connected to Internet";
		}
		return status;
	}

	public static String getErrorJson(String command, String code, String msg)
	{
		String result = "";

		try
		{
			ItemResult item = new ItemResult();
			item.code = code;
			item.message = msg;

			result = "{\"result\":" + new Gson().toJson(item) + "}";
		}
		catch (Exception e)
		{

		}

		return result;
	}
	
	public static String requestPost(Context context, String strUrl, String postData, final String command)
	{
		String response = "";
		String strRtn = "";
		
		Log.f("strUrl : "+strUrl);
		Log.f("postData : "+postData);
		StringBuffer b =new StringBuffer();
		try {

			String lineEnd = "\r\n";
			String twoHyphens = "--";


			URL connectUrl = null;
			connectUrl = new URL(strUrl);
			trustAllHosts();
			
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connectUrl.openConnection();
            httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });


			HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(8000);
			conn.setRequestProperty("User-Agent", StorybookTempleteAPI.HTTP_HEADER_APP_NAME+":"+CommonUtils.getInstance(context).getPackageVersionName()+File.separator+Build.MODEL+File.separator+Common.HTTP_HEADER_ANDROID+":"+Build.VERSION.RELEASE);
			Log.i("User Agent :" + StorybookTempleteAPI.HTTP_HEADER_APP_NAME+":"+CommonUtils.getInstance(context).getPackageVersionName()+File.separator+Build.MODEL+File.separator+Common.HTTP_HEADER_ANDROID+":"+Build.VERSION.RELEASE);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

			DataOutputStream dos = null;
			dos = new DataOutputStream(conn.getOutputStream());

			StringEntity entity = new StringEntity(postData, HTTP.UTF_8);
			dos.writeBytes("parm_string=" + lineEnd+lineEnd);
			entity.writeTo(dos);

			dos.flush();
			dos.close();

			int ch;
			InputStream is = conn.getInputStream();

			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
			}

			strRtn = b.toString();
			b = null;

		}
		catch (UnsupportedEncodingException e)
		{
			Log.exception(e);
			response = getErrorJson(command, "600", "Thrown when a program asks for a particular character converter that is unavailable.");
		}
		catch (ClientProtocolException e)
		{
			Log.exception(e);
			response = getErrorJson(command, "601", "Signals an error in the HTTP protocol.");
		}
		catch (SocketTimeoutException e)
		{
			Log.exception(e);
			response = getErrorJson(command, "602", "This exception is thrown when a timeout expired on a socket read or accept operation");
		}
		catch (IOException e)
		{
			Log.exception(e);
			response = getErrorJson(command, "603", "Signals a general, I/O-related error.");
		}
		Log.f("response : "+strRtn );
		return strRtn;

	}

	public static String httpPostData(String requestUrl, String data, final String command) throws UnknownHostException
	{

		final ResponseHandler<String> responseHandler = new ResponseHandler<String>()
		{
			@Override
			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException
			{
				String result = "";

				if (response.getStatusLine().getStatusCode() == 200)
				{
					HttpEntity entity = response.getEntity();

					InputStream is = entity.getContent();
					byte[] buffer = new byte[2048 * 20];
					int n;

					while ((n = is.read(buffer, 0, 2048 * 20)) != -1)
					{
						result += new String(buffer, 0, n);
					}

			
					if (result.length() == 0)
					{
						result = getErrorJson(command, "" + response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
					}
				}
				else
				{
					result = getErrorJson(command, "" + response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
				}

				return result;
			}
		};

		HttpClient client = new DefaultHttpClient();
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		HttpEntity rEntity;
		String response = "";

		try
		{
			if (requestUrl.contains("http://") == false)
			{
				requestUrl = "http://" + requestUrl;
			}

			rEntity = new StringEntity(data, "UTF-8");
			HttpPost httpPost = new HttpPost(requestUrl);
			httpPost.addHeader("Content-Type", "application/json");
			
			httpPost.setParams(params);
			
			httpPost.setEntity(rEntity);
			response = client.execute(httpPost, responseHandler);
		
		}
		catch (UnsupportedEncodingException e)
		{
			Log.exception(e);
			response = getErrorJson(command, "600", "Thrown when a program asks for a particular character converter that is unavailable.");
		}
		catch (ClientProtocolException e)
		{
			Log.exception(e);
			response = getErrorJson(command, "601", "Signals an error in the HTTP protocol.");
		}
		catch (SocketTimeoutException e)
		{
			Log.i("TEST_SOCKET", "SocketTimeOutException");
			Log.exception(e);
			response = getErrorJson(command, "602", "This exception is thrown when a timeout expired on a socket read or accept operation");
		}
		catch (IOException e)
		{
			Log.exception(e);
			response = getErrorJson(command, "603", "Signals a general, I/O-related error.");
		}

		return response;
	}
	
	private static void trustAllHosts()
	{
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
		{
			public java.security.cert.X509Certificate[] getAcceptedIssuers()
			{
				return new java.security.cert.X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)throws java.security.cert.CertificateException{}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain,String authType)throws java.security.cert.CertificateException{}

		} };

		// Install the all-trusting trust manager

		try
		{
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
*/