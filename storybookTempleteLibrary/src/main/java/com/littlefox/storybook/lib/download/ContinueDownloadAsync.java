package com.littlefox.storybook.lib.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.os.AsyncTask;

import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.api.StorybookTempleteAPI;

/**
 * 다운로드를 이어 받기 할 수 있게 하는 AsyncTask
 * @author 정재현
 *
 */
public class ContinueDownloadAsync extends AsyncTask<Void, Void, Boolean>
{
	
	private static boolean isRunning = false;
	private static Object mSync = new Object();
	
	private String mCaptionUrl 				= "";
	private String mVideoUrl				= "";
	private String mSaveFileVideoPath		= "";
	private String mSaveFileCaptionPath 	= "";
	private DownloadPlayListener mDownloadPlayListener;
	private Context mContext;
	private boolean isCancel;
	public ContinueDownloadAsync(Context context, String downUrl, String savePath , DownloadPlayListener downloadPlayListener)
	{
		Log.i("downUrl : "+downUrl+", savePath : "+savePath);
		mContext					= context;
		mVideoUrl 					= downUrl;
		mSaveFileVideoPath			= savePath;
		mDownloadPlayListener		= downloadPlayListener;
	}
	
	public ContinueDownloadAsync(Context context, String downUrl, String captionUrl, String saveVideoPath, String saveJsonPath, DownloadPlayListener downloadPlayListener)
	{
		mContext					= context;
		mVideoUrl 					= downUrl;
		mCaptionUrl 				= captionUrl;
		mSaveFileVideoPath			= saveVideoPath;
		mSaveFileCaptionPath		= saveJsonPath;
		mDownloadPlayListener		= downloadPlayListener;
	}
	
	
	@Override
	protected Boolean doInBackground(Void... params)
	{
		
		if(isRunning == true)
		{
			return false;
		}
		
		synchronized (mSync)
		{
			isRunning = true;
			
			try
			{
				if(mCaptionUrl.equals("") == false)
				{
					startContinueDownload(mCaptionUrl, StorybookTempleteAPI.PATH_JSON+mSaveFileCaptionPath,true);
				}
				return startContinueDownload(mVideoUrl, StorybookTempleteAPI.PATH_MP4+mSaveFileVideoPath,false);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		
		isRunning = false;
		
	}

	@Override
	protected void onPostExecute(Boolean result)
	{
		super.onPostExecute(result);
		
		isRunning = false;
		
	}

	@Override
	protected void onProgressUpdate(Void... values)
	{
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled()
	{
		super.onCancelled();
		
		isRunning = false;
		if(mDownloadPlayListener != null)
		{
			mDownloadPlayListener.onCanceled();
		}
		
	}
	
	public void setCancel(boolean iscancel)
	{
		isCancel = iscancel;
	}
	
	/**
	 * 이어 받기가 가능한 메소드
	 * @param strUrl 다운로드 할 URL
	 * @param strPath 저장할 Local Path
	 * @return
	 * @throws IOException
	 */
	private boolean startContinueDownload(String strUrl, String strPath,boolean isCaption) throws IOException
	{
		int DOWNLOAD_DONE = 0;
		int DEFAULT_TIMEOUT = 50000;
		long fileSize = 0, remains = 0, lenghtOfFile = 0;

	
		File file = new File(strPath);
		if (file.exists() == false)
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		else if(file.exists() == true)
		{
			file.delete();
			file.createNewFile();
		}

		RandomAccessFile moutput = new RandomAccessFile(file.getAbsolutePath(), "rw");
		FileChannel fc = moutput.getChannel();
		fileSize = file.length();
		// moutput.seek(fileSize);
		byte dummyb[] = new byte[16];
		int readbyte = dummyb.length;
		int noffset = 0;
		if (fileSize > 1024 * 1024 * 2)
		{

			MappedByteBuffer MappByteBuff = fc.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);

			MappByteBuff.position(0);
			
			if (fileSize < noffset + readbyte)
			{
				readbyte = (int) fileSize - noffset;
			}

			MappByteBuff.position(noffset);
			MappByteBuff.get(dummyb, 0, readbyte);

			int nCompareCnt = 0;

			for (int n = 0; n < readbyte; n++)
			{
				if (dummyb[n] == '1')
					nCompareCnt++;
			}

			if (nCompareCnt == readbyte)
			{
				fileSize = noffset - readbyte;
				MappByteBuff.clear();
				MappByteBuff = null;
			}

			noffset = noffset + readbyte;
		}
		else
		{
			// file.delete();
			// file.createNewFile();
		}

		URL url = new URL(strUrl);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("Range", "bytes=" + String.valueOf(fileSize) + '-');
		conn.connect();
		conn.setConnectTimeout(DEFAULT_TIMEOUT);
		conn.setReadTimeout(DEFAULT_TIMEOUT);
		remains = conn.getContentLength();
		lenghtOfFile = remains + fileSize;
		
		if(isCaption == false)
		{
			mDownloadPlayListener.setMaxProgress((int) (lenghtOfFile/1000));
		}

		if (remains <= DOWNLOAD_DONE)
		{

			moutput.close();
			fc.close();
			if(isCaption == false)
			{
				mDownloadPlayListener.playVideo();
			}
			
			return true; // 다운로드 필요 없음
		}

		byte data1[] = new byte[1024 * 2];
		for (int x = 0; x < data1.length; x++)
		{
			data1[x] = '1';
		}
		int dummy = data1.length, dummyRemains = (int) remains;
		moutput.seek(0);
		while (dummyRemains > 0 && fileSize == 0)
		{
			if (dummyRemains <= dummy)
			{
				dummy = dummyRemains;
			}
			moutput.write(data1, 0, dummy);
			dummyRemains = dummyRemains - dummy;
		}

		InputStream input = conn.getInputStream();
		byte data[] = new byte[1024 * 2];
		int count = 0;

		int mode = 0;
		moutput.seek(fileSize);
		
		try
		{
			while ((count = input.read(data)) != -1)
			{

				moutput.write(data, 0, count);
				fileSize = count + fileSize;
				
				
				if(isCaption == false)
				{
					if (fileSize > 1024 * 1024 * 2 && mode == 0)
					{
						mode++;
					}


					// nCurPosition
					if (mode != 0 && fileSize < lenghtOfFile)
					{
						long l = fileSize / 1000;
						long maxFileSize = lenghtOfFile/1000;
						mDownloadPlayListener.downloadProgress((int)(l * (100/(float)maxFileSize)));
					}
					else if (fileSize >= lenghtOfFile)
					{
						isRunning = false;
						mDownloadPlayListener.downloadComplete();
					}
				}

				
				
				if(isCancel == true)
				{
					break;
				}

			}
		}catch(Exception e)
		{
			
		}


		moutput.close();
		input.close();
		fc.close();
		return true;

	}
	
	

}
