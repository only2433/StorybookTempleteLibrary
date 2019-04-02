package com.littlefox.storybook.lib.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.api.StorybookTempleteAPI;
import com.littlefox.storybook.lib.object.DisPlayMetricsObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;



public class CommonUtils
{
	public static CommonUtils sCommonUtils = null;
	public static Context sContext = null;
	
	public static CommonUtils getInstance(Context context)
	{
		if(sCommonUtils == null)
		{
			sCommonUtils = new CommonUtils();
		}
		sContext = context;
		
		return sCommonUtils;
	}
	
	/**
	 * 밀리세컨드를 시간 String  으로 리턴한다.
	 * @param timeMs
	 * @return
	 */
	public String getTime(int timeMs) {
		
		StringBuilder mFormatBuilder = new StringBuilder();
		Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
		
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * <pre>
     * Window 의 정보를 얻어온다.
     * </pre>

     * @return
     */
    public void getWindowInfo()
    {
    	DisplayMetrics displayMetrics  = new DisplayMetrics();
        ((Activity) sContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DisPlayMetricsObject object;
        
        //TODO : OS에서 Display 정보 잘못 들어오는 부분 수정
        if(displayMetrics.widthPixels < displayMetrics.heightPixels)
        {
        	object = new DisPlayMetricsObject(displayMetrics.heightPixels, displayMetrics.widthPixels);
        }
        else
        {
        	object = new DisPlayMetricsObject(displayMetrics.widthPixels, displayMetrics.heightPixels);
        }
        
        setPreferenceObject(Common.PARAMS_DISPLAY_METRICS, object);
    }
    
    public void showDeviceInfo()
    {
    	Log.f("BRAND : "+Build.BRAND);
    	Log.f("DEVICE : "+ Build.DEVICE);
    	Log.f("MODEL : "+ Build.MODEL);
    	Log.f("VERSION SDK : "+ Build.VERSION.SDK_INT);
    	Log.f("APP VERSION : "+getPackageVersionName(StorybookTempleteAPI.PACKAGE_NAME));
    	DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
    	Log.f("WIDTH PIXEL : " + object.widthPixel +", HEIGHT PIXEL : " + object.heightPixel);
    	Log.f("IsTablet : " + isTablet());
    }
   

    /**
     * 1080 * 1920  기준으로 멀티 해상도의 픽셀을 계산한다.
     * @param value 1080 * 1920  의 픽셀
     * @return
     */
    public int getPixel(int value)
    {
    	
    	DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
		float displayFactor = object.widthPixel / 1920.0f;
        
        return (int) (value * displayFactor);
    }

    /**
     * 1080 * 1920  기준으로 멀티 해상도의 픽셀을 계산한다.
     * @param value 1080 * 1920  의 픽셀
     * @return
     */
	public float getPixel(float value)
	{
		DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
		float displayFactor = object.widthPixel / 1920.0f;

		return value * displayFactor;
	}

    /**
     * 1920 * 1080 기준으로 멀티 해상도의 픽셀을 계산한다.
     * @param value 1080 * 1920 의 픽셀
     * @return
     */
    public int getHeightPixel(int value)
    {
		DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
		float displayFactor = object.heightPixel / 1080.0f;

		return (int) (value * displayFactor);
    }

    /**
     * 1080 * 1920기준으로 멀티 해상도의 픽셀을 계산한다.
     * @param value 1080 * 1920 의 픽셀
     * @return
     */
    public float getHeightPixel(float value)
    {
		DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
		float displayFactor = object.heightPixel / 1080.0f;

		return value * displayFactor;
    }
    
    public float getDisplayWidth()
    {
    	DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
    	if(object != null)
    	{
    		return object.widthPixel;
    	}
    	return 0f;
    }
    
    public float getDisplayHeight()
    {
    	DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
    	if(object != null)
    	{
    		return object.heightPixel;
    	}
    	return 0f;
    }


    
    /**
     * 오브젝트 클래스를 불러오는 프리퍼런스

     * @param key 키값
     * @param className 클래스 네임
     * @return
     */
    public Object getPreferenceObject(String key, Class className)
    {
    	Object result = null;
    	 SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(sContext);
    	 String loadObjectString = pref.getString(key, "");
    	 
    	 if(loadObjectString.equals("") == false)
    	 {
    		 result = new Gson().fromJson(loadObjectString, className);
    	 }
    	
    	 return result;
    }
    
    /**
     * 오브젝트 클래스를 저장하는 프리퍼런스
     * @param key 키값
     * @param object 저장할 오브젝트
     */
    public void setPreferenceObject(String key, Object object)
    {
    	String saveObjectString = "";
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(sContext);
        SharedPreferences.Editor editor = pref.edit();
        
        if(object != null)
        {
        	saveObjectString = new Gson().toJson(object);
        }
        
        editor.putString(key, saveObjectString);
        editor.commit();
    }

	/**
	 * 현재 모델이 타블릿인지 아닌지 확인
	 * @return
	 */
	public boolean isTablet()
	{
		if (Build.VERSION.SDK_INT >= 19)
		{
			return checkTabletDeviceWithScreenSize(sContext) &&
					checkTabletDeviceWithProperties() &&
					checkTabletDeviceWithUserAgent(sContext);
		}
		else
		{
			return checkTabletDeviceWithScreenSize(sContext) &&
					checkTabletDeviceWithProperties() ;

		}
	}

	private  boolean checkTabletDeviceWithScreenSize(Context context) {
		boolean device_large = ((context.getResources().getConfiguration().screenLayout &
				Configuration.SCREENLAYOUT_SIZE_MASK) >=
				Configuration.SCREENLAYOUT_SIZE_LARGE);

		Log.f("device_large : "+device_large);
		if (device_large)
		{
			DisplayMetrics metrics = new DisplayMetrics();
			Activity activity = (Activity) context;
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			Log.f("metrics.densityDpi : "+metrics.densityDpi);

			if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
					|| metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
					|| metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
					|| metrics.densityDpi == DisplayMetrics.DENSITY_TV
					|| metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH
					|| metrics.densityDpi == DisplayMetrics.DENSITY_360) {
				return true;
			}
		}
		return false;
	}

	private  boolean checkTabletDeviceWithProperties()
	{
		try
		{
			InputStream ism = Runtime.getRuntime().exec("getprop ro.build.characteristics").getInputStream();
			byte[] bts = new byte[1024];
			ism.read(bts);
			ism.close();

			boolean isTablet = new String(bts).toLowerCase().contains("tablet");
			return isTablet;
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			return false;
		}
	}

	private  boolean checkTabletDeviceWithUserAgent(Context context)
	{
		try
		{
			WebView webView = new WebView(context);
			String ua = webView.getSettings().getUserAgentString();
			webView = null;
			if (ua.contains("Mobile Safari"))
			{
				return false;
			} else
			{
				return true;
			}
		} catch (Exception e)
		{
			return false;
		}
	}

	
	public boolean isMinimumDisplayWidth()
	{
		return (float)Common.MINIMUM_DISPLAY_WIDTH >= getDisplayWidth() ? true : false;
	}
	
	public Animation getAnimationHideBottom(int duration)
	{
		Animation anim = null;
		anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 1);
		anim.setDuration(duration);
		anim.setFillAfter(true);

		return anim;
	}
	
	public Animation getAnimationShowBottom(int duration)
	{
		Animation anim = null;
		anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
		anim.setDuration(duration);
		anim.setFillAfter(true);

		return anim;
	}

	public Animation getAnimationShowTop(int duration)
	{
		Animation anim = null;
		anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, -1, Animation.RELATIVE_TO_PARENT, 0);
		anim.setDuration(duration);
		anim.setFillAfter(false);
		
		return anim;
	}
	
	public Animation getAnimationHideTop(int duration)
	{
		Animation anim = null;
		anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, -1);
		anim.setDuration(duration);
		anim.setFillAfter(false);

		return anim;
	}
	
	public Animation getTranslateAnimationY(float before ,float after, boolean isInfinite, int duration)
	{
		Animation anim = null;
		anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, before, Animation.ABSOLUTE, after);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		if(isInfinite == true)
		{
			anim.setRepeatCount(Animation.INFINITE);
			anim.setRepeatMode(Animation.REVERSE);
		}
			
		//anim.setInterpolator(new DecelerateInterpolator());
		return anim;
	}
	
	public Animation getTranslateAnimationY(float before ,float after)
	{
		return getTranslateAnimationY(before, after, false, 500);
	}
	
	public Animation getAlphaAnimation(boolean isVisible)
	{
		return getAlphaAnimation(isVisible, null);
	}
	
	public Animation getAlphaAnimation(boolean isVisible, AnimationListener animationListner)
	{
		Animation anim = null;
		if(isVisible == true)
		{
			anim = new AlphaAnimation(0.1f, 1.0f);
			anim.setDuration(500);
		}
		else
		{
			anim = new AlphaAnimation(1.0f, 0.1f);
			anim.setDuration(500);
		}
		
		anim.setFillAfter(true);
		if(animationListner != null)
		{
			anim.setAnimationListener(animationListner);
		}
		return anim;
	}
	
	public Animation getRotationAnimation()
	{
		RotateAnimation anim = new RotateAnimation(0.0f,360.f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(1000);
		
		return anim;
	}
	
	public AnimationDrawable getFrameAnimation(int[] frameImage, boolean isInfinite, int duration)
	{
		AnimationDrawable aniDrawable = new AnimationDrawable();
		for(int i = 0 ; i < frameImage.length; i++)
		{
			aniDrawable.addFrame(sContext.getResources().getDrawable(frameImage[i]), duration);
		}
		aniDrawable.setOneShot(isInfinite);
		return aniDrawable;
	}

	public int getRatioViewHeight(int nSourceWidth, int nSourceHeight, int nTargetWidth, int nTargetHeight)
	{
		int nHeight = 0;
		if (nTargetWidth > 720 && nTargetHeight < 1290) 
			nTargetWidth = 720;

		if (nTargetWidth >= 1020 && nTargetHeight < 1430)
			nTargetWidth = 910;

		nHeight = (nSourceHeight * nTargetWidth) / nSourceWidth;
		return nHeight;
	}
	
	/**
	 * 저장한 프리퍼런스를 불러온다.
	 * @param key  해당 값의 키값
	 * @param type 데이터의 타입
	 * @return
	 */
	public Object getSharedPreference(String key, int type)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(sContext);

		switch (type)
		{
		case Common.TYPE_PARAMS_BOOLEAN:
			return pref.getBoolean(key, false);
		case Common.TYPE_PARAMS_INTEGER:
			return pref.getInt(key, -1);
		case Common.TYPE_PARAMS_STRING:
			return pref.getString(key, "");
		}

		return pref.getBoolean(key, false);
	}
	
	/**
	 * 저장한 프리퍼런스를 불러온다.
	 * @param key 해당 값의 키값
	 * @param type 데이터의 타입
	 * @param defaultValue 디폴트 값
	 * @return
	 */
	public Object getSharedPreference(String key, int type, Object defaultValue)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(sContext);

		switch (type)
		{
		case Common.TYPE_PARAMS_BOOLEAN:
			return pref.getBoolean(key, (Boolean) defaultValue);
		case Common.TYPE_PARAMS_INTEGER:
			return pref.getInt(key, (Integer) defaultValue);
		case Common.TYPE_PARAMS_STRING:
			return pref.getString(key, (String) defaultValue);
		}

		return pref.getBoolean(key, false);
	}
	
	/**
	 * 해당 프리퍼런스를 저장한다.
	 * @param key 해당 값의 키값
	 * @param object 저장할 데이터
	 */
	public void setSharedPreference(String key, Object object)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(sContext);
		SharedPreferences.Editor editor = pref.edit();
		
		if(object instanceof Boolean)
		{
			editor.putBoolean(key, (Boolean) object);
		}
		else if(object instanceof Integer)
		{
			editor.putInt(key, (Integer) object);
		}
		else if(object instanceof String)
		{
			editor.putString(key, (String) object);

		}
		
		editor.commit();

	}
	
	/**
	 * Update language
	 * 
	 * @param code
	 *            The language code. Like: en, cz, iw, ...
	 */
	public void updateLanguage(String code) {
		Locale locale = new Locale(code);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		sContext.getResources().updateConfiguration(config, sContext.getResources().getDisplayMetrics());
	}
	
	
	/**
	 * Print hash key
	 */
	public void printHashKey() {
		try {
			PackageInfo info = sContext.getPackageManager().getPackageInfo(StorybookTempleteAPI.PACKAGE_NAME, PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
				Log.d("keyHash: " + keyHash);
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}
	
	/**
	 * 패키지버젼코드 확인
	 * @return
	 */
	public int getPackageVersionCode()
	{
		int result = -1;
		try
		{
			PackageInfo pi = sContext.getPackageManager().getPackageInfo(StorybookTempleteAPI.PACKAGE_NAME, 0);
			if (pi != null)
				result = pi.versionCode;
		}
		catch (Exception ex)
		{
		}
		return result;
	}
	
	/**
	 * 패키지 버전 네임 확인
	 * @return
	 */
	public String getPackageVersionName()
	{
		String result = "";
		try
		{
			PackageInfo pi = sContext.getPackageManager().getPackageInfo(StorybookTempleteAPI.PACKAGE_NAME, 0);
			if (pi != null)
				result = pi.versionName;
		}
		catch (Exception ex)
		{
		}
		return result;
	}
	
	   /**
     * 패키지 버전 네임 확인
     * @param packageName 패키지 버전 네임
     * @return 
     */
    public String getPackageVersionName(String packageName)
    {
        String result = "";
        try
        {
            PackageInfo pi = sContext.getPackageManager().getPackageInfo(packageName, 0);
            if (pi != null)
                result = pi.versionName;
        }
        catch (Exception ex)
        {
        }
        return result;
    }
	
	/**
	 * 인스톨 되어있나 검색한다.
	 * @param packageName 해당 패키지 명 
	 * @return
	 */
	public boolean isInstalledPackage(String packageName)
	{
		boolean result = true;
		try
		{
			Intent intent = sContext.getPackageManager().getLaunchIntentForPackage(packageName);
			if(intent == null)
			{
				result = false;
			}
		}catch(Exception e)
		{
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Check the device to make sure it has the Google Play Services APK. If it doesn't, display a dialog that allows users to download the APK from the Google Play Store or enable it in the device's
	 * system settings.
	 */

	public static boolean checkPlayServices()
	{
		final int googlePlayServicesCheck = GooglePlayServicesUtil.isGooglePlayServicesAvailable((Activity) sContext);
		switch (googlePlayServicesCheck)
		{
		case ConnectionResult.SUCCESS:
			return true;
		case ConnectionResult.SERVICE_DISABLED:
		case ConnectionResult.SERVICE_INVALID:
		case ConnectionResult.SERVICE_MISSING:
		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCheck, (Activity) sContext, 0);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				@Override
				public void onCancel(DialogInterface dialogInterface)
				{
					((Activity) sContext).finish();
				}
			});
			dialog.show();
		}
		return false;
	}

    /**
     * 해당 파일을 저장된 폴더에서 불러와 CationObject 로 변경하여 리턴한다.
     * @param filePath 파일 위치
     * @param classname 클래스 
     * @return
     */
    public Object convertObjectFromFile(String filePath, Class classname)
    {
    	Object result;
    	try
    	{
    		BufferedReader br = new BufferedReader(new FileReader(filePath));
    		result = new Gson().fromJson(br, classname);
    		
    	}catch(Exception e)
    	{
    		Log.w("e :"+e.getMessage());
    		result = null;
    	}
    	
    	return result;
    }
    
    
    public Object convertObjectFromAsset(String fileName, Class classname)
    {
    	Object result;
    	
    	try
    	{
    		InputStream is = sContext.getAssets().open(fileName);
    		int size = is.available();
    		byte[] buffer = new byte[size];
    		is.read(buffer);
    		is.close();
    		String bufferString = new String(buffer);
    		result = new Gson().fromJson(bufferString, classname);
    	}catch(Exception e)
    	{
    		result = null;
    	}
    	return result;
    }
    
    /**
     * Drawable 을 Bitmap 형태로 변경
     * @param drawable
     * @return
     */
	public Bitmap drawableToBitmap(Drawable drawable)
	{
		if (drawable instanceof BitmapDrawable)
		{
			return ((BitmapDrawable) drawable).getBitmap();
		}
		
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	


	/**
	 * 맥 어드레스를 받아온다.
	 * @return
	 */
	public String getMacAddress()
	{
		WifiManager wifimanager = (WifiManager)sContext.getSystemService(Context.WIFI_SERVICE);
		return wifimanager.getConnectionInfo().getMacAddress();
	}
	
	/**
	 * ImageLoader를 사용하기 위해 처음 실행시 초기화
	 */

	public boolean downloadFile(String url, String dest_file_path)
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
	
	/**
	 * This method converts dp unit to equivalent pixels, depending on device density. 
	 * 
	 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public float convertDpToPixel(float dp){
	    Resources resources = sContext.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @return A float value to represent dp equivalent to px value
	 */
	public float convertPixelsToDp(float px){
	    Resources resources = sContext.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}
	
	public long getAvailableStorageSize()
	{
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		long result;
		if(Build.VERSION.SDK_INT > Common.JELLYBEAN_CODE_4_3)
		{
			result = (long)stat.getAvailableBlocksLong()* (long)stat.getBlockSizeLong();
		}
		else
		{
			result = (long)stat.getAvailableBlocks()* (long)stat.getBlockSize();
		}
		
		
		return result/(1024 * 1024);
	}
	
	public int getDrawableResourceFromString(String name)
	{
		return sContext.getResources().getIdentifier(name, "drawable", sContext.getApplicationContext().getPackageName());
	}
	
	public Drawable getDrawableFromAsset(String filename)
	{
		InputStream inputStream = null;
		try
		{
			inputStream = sContext.getAssets().open(Common.PATH_THUMBNAIL+filename);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Drawable.createFromStream(inputStream, null);
	}
	
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap)
	{
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 12;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	
	public Drawable getRoundedCornerRect(int width, int height, int color)
	{
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		
		
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);
		

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, getPixel(20), getPixel(20), paint);

		return getDrawableFromBitmap(output);
	}

	public Bitmap getBitmapFromDrawable(Drawable mDrawable, int width, int height)
	{
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);

		mDrawable.setBounds(0, 0, width, height);

		mDrawable.draw(canvas);

		return bitmap;

	}

	public Drawable getDrawableFromBitmap(Bitmap mBitmap)
	{
		BitmapDrawable bitmapDrawable = new BitmapDrawable(sContext.getResources(), mBitmap);

		return (Drawable) bitmapDrawable;
	}
	
	public Drawable getScaledDrawable(int width, int height, int drawable)
	{
		Bitmap bitmap 	= BitmapFactory.decodeResource(sContext.getResources(), drawable);
		bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
		return getDrawableFromBitmap(bitmap);
	}
	

	/**
	 * The files will be copied at the location targetFolder+path so if you
	 * enter path="abc" and targetfolder="sdcard" the files will be located in
	 * "sdcard/abc"
	 * 
	 * @param assetManager
	 * @param path
	 * @param targetFolder
	 * @return
	 * @throws Exception
	 */
	public boolean copyAssets(AssetManager assetManager, String path,
	        File targetFolder) throws Exception {
	    Log.i( "Copying " + path + " to " + targetFolder);
	    String sources[] = assetManager.list(path);
	    if (sources.length == 0) { // its not a folder, so its a file:
	        copyAssetFileToFolder(assetManager, path, targetFolder);
	    } else { // its a folder:
	        if (path.startsWith("images") || path.startsWith("sounds")
	                || path.startsWith("webkit")) {
	            Log.i( "  > Skipping " + path);
	            return false;
	        }
	        File targetDir = new File(targetFolder, path);
	        targetDir.mkdirs();
	        for (String source : sources) {
	            String fullSourcePath = path.equals("") ? source : (path
	                    + File.separator + source);
	            copyAssets(assetManager, fullSourcePath, targetFolder);
	        }
	    }
	    return true;
	}

	private void copyAssetFileToFolder(AssetManager assetManager,
	        String fullAssetPath, File targetBasePath) throws IOException {
	    InputStream in = assetManager.open(fullAssetPath);
	    OutputStream out = new FileOutputStream(new File(targetBasePath,
	            fullAssetPath));
	    byte[] buffer = new byte[16 * 1024];
	    int read;
	    while ((read = in.read(buffer)) != -1) {
	        out.write(buffer, 0, read);
	    }
	    in.close();
	    out.flush();
	    out.close();
	}
	
	/**
	 * Splits a string to multiple strings each of which does not exceed the width
	 * of maxWidthPx.
	 * Width 에 맞게 라인을 잘라서 보내준다.
	 */
	public List<String> getSplitIntoStringsThatFit(String source, float maxWidthPx, Paint paint) {
	    if(TextUtils.isEmpty(source)) {
	    	Log.i("paint.measureText(source)  : "+paint.measureText(source) +", maxWidthPx : "+maxWidthPx);
	        return Arrays.asList(source);
	    }
	    Log.i("source : "+source+", getTextSize ; "+ paint.getTextSize());
	    ArrayList<String> result = new ArrayList<String>();
	    int start = 0;
	    for(int i = 1; i <= source.length(); i++) {
	        String substr = source.substring(start, i);
	        if(paint.measureText(substr) >= maxWidthPx || substr.contains("\n")) {
	            //this one doesn't fit, take the previous one which fits
	            String fits = source.substring(start, i - 1);
	            result.add(fits);
	            Log.i("1 result size : "+fits+" ,maxWidthPx : "+maxWidthPx);
	            if(substr.contains("\n") == false)
	            {
	            	start = i - 1;
	            }
	            else
	            {
	            	start = i;
	            }
	           
	        }
	        if (i == source.length()) {
	            String fits = source.substring(start, i);
	            Log.i("2 result size : "+fits+" ,maxWidthPx : "+maxWidthPx);
	            result.add(fits);
	        }
	    }
	    Log.i("result size : "+ result.size());
	    return result;
	}
	
	public void startLinkMove(String link)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(link));
		sContext.startActivity(intent);
	}
	
	/**
	 * 이전에 봤던 위치로 돌아가기 위해 사용하는 함수 , 폰과 타블릿에 따라 정보가 달라진다
	 * @param position
	 * @return
	 */
	public int getMoveScrollValue(int position)
	{
		int movecount;
		if(isTablet())
		{
			movecount = position / Common.TABLET_THUMB_MAX_IN_LINE;
			
			return getPixel(movecount * Common.TABLET_TERM_FRAME_HEIGHT);
		}
		else
		{
			movecount = position / Common.PHONE_THUMB_MAX_IN_LINE;
			
			return getPixel(movecount * Common.PHONE_TERM_FRAME_HEIGHT);
		}
	}
	/**
	 * 앱버젼이 같은지 확인
	 * @param appVersion 서버의 버젼
	 * @return TRUE : 현재 로컬버젼과 같다. </p> FALSE : 현재 로컬버젼과 다르다.
	 */
	public boolean isAppVersionEqual(String appVersion)
	{
		if(appVersion.equals(getPackageVersionName()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
    public String getDateTime(long timeMs) {

    	Date date = new Date(timeMs); 
    	String todayString  = new SimpleDateFormat("MM/dd/yyyy").format(date);
    	return todayString;
    }
	
	/**
	 * 현재 버젼과 업데이트 되는 버젼을 비교하기 위해
	 * 이전에 기억하는 팝업 정보도 삭제한다.
	 * @return TRUE : 현재 버젼과 업데이트 되는 버젼과 같다. <p> FALSE : 현재 버젼과 업데이트 되는 버젼과 다르다.
	 */
	public boolean verifyCurrentVersionCode()
	{
		int registerVersion = (Integer)getSharedPreference( Common.PARAMS_REGISTER_APP_VERSION, Common.TYPE_PARAMS_INTEGER);
		
		int currentVersion = getPackageVersionCode();
		
		Log.i("registerVersion : "+registerVersion+", currentVersion : "+currentVersion);
		
		if(currentVersion != registerVersion)
		{
			setSharedPreference(Common.PARAMS_REGISTER_APP_VERSION, currentVersion);
			setSharedPreference(Common.PARAMS_BEFORE_WATCHED_MOVIE, -1);
			return false;
		}
		
		return true;
	}
	public void launchOtherApplication(String packageName)
	{
		Intent intent = sContext.getPackageManager().getLaunchIntentForPackage(packageName);
		sContext.startActivity(intent);
	}
	
	public String getThumbnailPath(int index)
	{
		String path = "";
		
		if(index < 10)
		{
			path = StorybookTempleteAPI.PATH_THUMBNAIL + "thumbnail_0" + index + ".jpg";
		}
		else
		{
			path = StorybookTempleteAPI.PATH_THUMBNAIL + "thumbnail_"+ index + ".jpg";
		}
		
		return path;
	}
	
	
	public void saveFileFromDrawable(String filename, int drawableId)
	{
		Bitmap bm = BitmapFactory.decodeResource(sContext.getResources(), drawableId);

		File file = FileUtils.getMakedFile(filename);
		
		if(file != null)
		{
			try {
			    FileOutputStream outStream = new FileOutputStream(file);
			    bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			    outStream.flush();
			    outStream.close();
			} catch (Exception e) 
			{ 
				throw new RuntimeException(e); 
			}
		}
		
	}
	
	
}
