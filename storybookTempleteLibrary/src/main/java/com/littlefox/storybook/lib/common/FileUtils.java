package com.littlefox.storybook.lib.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.enc.SimpleCrypto;

import android.content.Context;

public class FileUtils {

	
	
	public static boolean writeFile(Object obj, String path){
		if (path.equals("")) return false;
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		
		//String encriptString = SimpleCrypto.encrypt(json);
		
		File f = new File(path);
		File f2 = new File(f.getParent());
		if(!f2.exists())
			f2.mkdirs();
		f2 = null;

		try {
			f.createNewFile();
			if (f.canWrite()){
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(json.getBytes(), 0, json.getBytes().length);
				//fo.write(json.getBytes(), 0, json.getBytes().length);
				fo.flush();
				fo.close();
			}
		} catch (IOException e) {
			Log.exception(e);
			Log.f("e : "+ e.toString());
			
			return false;
		}
		return true;
	}
	
	public static File getMakedFile(String path)
	{
		File file = new File(path);
		File fileDirectory = new File(file.getParent());
		
		if(!fileDirectory.exists())
			fileDirectory.mkdirs();
		
		try
		{
			file.createNewFile();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
		
		
		return file;
		
	}
	
	public static ArrayList<String> getFilelist(String dirpath,String prefix){
		ArrayList<String> ret = new ArrayList<String>();
		File f = new File(dirpath);
		if (!f.exists()) return ret;
		File[] files = f.listFiles();
		Arrays.sort(files);
		for (File subfile : files){
			if (subfile.toString().contains(prefix)){
				try {
					ret.add(subfile.getAbsolutePath());
				} catch (JsonParseException e) {
					e.printStackTrace();
					
				}
			}
		}
		return ret;
	}
	
	public static String getFile(String dirPath, String prefix)
	{
		String ret = "";
		File f = new File(dirPath);
		if (!f.exists()) return ret;
		File[] files = f.listFiles();
		Arrays.sort(files);
		for (File subfile : files){
			if (subfile.toString().contains(prefix)){
				try {
					ret = subfile.getAbsolutePath();
				} catch (JsonParseException e) {
					e.printStackTrace();
					
				}
			}
		}
		return ret;
	}
	
	public static String getStringFromFile(String path)
	{
		String result = "";
		
		File file = new File(path);
		
		if(file.exists() == false)
		{
			return result;
		}
		
		try
		{
			FileInputStream fileInputStream = new FileInputStream(file);
			
			if(fileInputStream != null)
			{
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString ="";
				StringBuilder stringBuilder = new StringBuilder();
				
				while((receiveString = bufferedReader.readLine()) != null)
				{
					stringBuilder.append(receiveString);
				}
				
				inputStreamReader.close();
				result = stringBuilder.toString();
				
			}
		}catch(Exception e)
		{
			
		};
		Log.i("result 1 : "+result);
		//result = SimpleCrypto.decrypt(result);
		Log.i("result 2 : "+result);
		
		return result;
	}
	
	public static void deleteFile(String fileName)
	{
		File f = new File(fileName);
		
		if(f.exists())
		{
			f.delete();
		}
	}
	
	public static boolean checkFile(String path) {
		File f = new File(path);
		
		if(f.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void DeleteAllWithSuffix(File dir, String suffix) {
		if(suffix==null || suffix.length() == 0) return;

	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++)  {
	        	if(children[i].length()>=suffix.length() && children[i].endsWith(suffix)) {
		        	File temp =  new File(dir, children[i]);
		        	if(temp.isFile()) {
		        		// 파일만 삭제
		        		temp.delete();
		        	}
	        	}
	        }
	    } else {
	    	// 폴더 아니면 패스
	    }
	}
	
	// 데이터 삭제
    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        try {
        } catch (Exception e) {
        }
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib") && !(s.equals("shared_prefs"))) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty or this is a file so delete it
        return dir.delete();
    }
}
