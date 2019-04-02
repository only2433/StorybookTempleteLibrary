package com.littlefox.storybook.lib.common;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.google.gson.Gson;
import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.api.StorybookTempleteAPI;
import com.littlefox.storybook.lib.object.ItemResult;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NetworkUtil
{

	public static final int GET_METHOD 		= 0;
	public static final int POST_METHOD 	= 1;
	public static final int PUT_METHOD 		= 2;
	public static final int DELETE_METHOD 	= 3;
	
	public static final int TYPE_WIFI = 1;
	public static final int TYPE_MOBILE = 2;
	public static final int TYPE_NOT_CONNECTED = 0;

	public static final int CONNECTION_TIMEOUT = 15000;
	public static final int SOCKET_TIMEOUT = 15000;

	public static int getConnectivityStatus(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork)
		{
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}
	
	public static boolean isConnectNetwork(Context context)
	{
        return getConnectivityStatus(context) != NetworkUtil.TYPE_NOT_CONNECTED;
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

	public static String getErrorJson(String code, String msg)
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


	public static String requestServerPair(Context context, String strUrl, ContentValues postDataList, int connectionType, String apiVersion)
	{
		if(postDataList != null)
		{
			Log.f("request URL : "+ strUrl +", data : "+ postDataList.toString()+", connectionType : "+connectionType);
		}
		else
		{
			Log.f("request URL : "+ strUrl +", connectionType : "+connectionType);
		}
		
		String response = "";
		
		if(isConnectNetwork(context) == false)
		{
			response = getErrorJson("601", "Signals an error in the HTTP protocol.");
			return response;
		}
		
		URL url;
		HttpURLConnection connection;
		try
		{
			
			url = new URL(strUrl);
			trustAllHosts();
			
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });

			
			connection = (HttpURLConnection)url.openConnection();
			connection.setReadTimeout(10000);
			connection.setConnectTimeout(10000);
			
			connection.setRequestProperty("User-Agent", StorybookTempleteAPI.HTTP_HEADER_APP_NAME+":"+CommonUtils.getInstance(context).getPackageVersionName()+File.separator+Build.MODEL+File.separator+Common.HTTP_HEADER_ANDROID+":"+Build.VERSION.RELEASE);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

			switch(connectionType)
			{
			case GET_METHOD:
				connection.setRequestMethod("GET");
				break;
			case POST_METHOD:
				connection.setRequestMethod("POST");
				break;
			case PUT_METHOD:
				connection.setRequestMethod("PUT");
				break;
			case DELETE_METHOD:
				connection.setRequestMethod("DELETE");
				break;
			}
			
			
			if (connectionType == GET_METHOD)
			{
				connection.setDoOutput(false);
				connection.setDoInput(true);
			} 
			else
			{
				connection.setDoOutput(true);
				connection.setDoInput(true);
			}
			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);

			
			if (postDataList != null)
			{
				OutputStream outputStream = connection.getOutputStream();
				BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
				bufferedWriter.write(getURLQuery(postDataList));
				bufferedWriter.flush();
				bufferedWriter.close();
				outputStream.close();
			}
			
			connection.connect();

			StringBuilder responseStringBuilder = new StringBuilder();
			Reader reader;
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK 
					|| connection.getResponseCode() == HttpURLConnection.HTTP_CREATED)
			{
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				for (;;)
				{
					String stringLine = bufferedReader.readLine();
					if (stringLine == null)
						break;
					responseStringBuilder.append(stringLine + '\n');
				}
				bufferedReader.close();
			}

			connection.disconnect();

			response = responseStringBuilder.toString();
			Log.f("connection.getResponseCode() : " + connection.getResponseCode());
			Log.f("Response : " + response);

		}
		catch (UnsupportedEncodingException e)
		{
			Log.exception(e);
			response = getErrorJson("600", "Thrown when a program asks for a particular character converter that is unavailable.");
		}
		catch (SocketTimeoutException e)
		{
			Log.exception(e);
			response = getErrorJson("602", "This exception is thrown when a timeout expired on a socket read or accept operation");
		}
		catch (IOException e)
		{
			Log.exception(e);
			response = getErrorJson("603", "Signals a general, I/O-related error.");
		}

		return response;
	}
	

	
	public static String requestServerPair(Context context, String strUrl, ContentValues postDataList, int connectionType)
	{
		return requestServerPair(context, strUrl, postDataList, connectionType, null);
	}
	
	private static String getURLQuery(ContentValues params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (Map.Entry<String, Object> entry : params.valueSet())
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
	    }

	    return result.toString();
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
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain,String authType) {}

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



	public static boolean downloadFile(String url, String dest_file_path)
	{
		int  count = 0;
		try
		{
			File dest_file = new File(dest_file_path);
			
			File folderPath = new File(dest_file.getParent());
			
			if(folderPath.exists() == false)
			{
				folderPath.mkdir();
			}
			
			dest_file.createNewFile();
			
			URL resultUrl = new URL(url);
			URLConnection conn = resultUrl.openConnection();
			conn.connect();
			
			
			InputStream input = new BufferedInputStream(resultUrl.openStream());
			OutputStream output = new FileOutputStream(dest_file);
			
			byte[] data = new byte[1024];
			
			
			while ((count = input.read(data)) != -1)
			{
				output.write(data, 0, count);
			}
			
			output.flush();
			output.close();
			input.close();
			
		}
		catch (FileNotFoundException e)
		{
			return false;
		}
		catch (Exception e)
		{
			return false;
		}
		
		return true;

	}


}
