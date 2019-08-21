package com.littlefox.storybook.lib.api;

import android.content.Context;
import android.os.Environment;

import com.littlefox.storybook.lib.object.DisPlayMetricsObject;

public class StorybookTempleteAPI
{
	private static Context sContext;
	
	/**
	 * 사용하려는 패키지 네임
	 */
	public static String PACKAGE_NAME = "";
	
	/**
	 * API에게 전달하려는 HEADER
	 */
	public static String HTTP_HEADER_APP_NAME = "";
	
	/**
	 * 사용하려는 어플리케이션 이름
	 */
	public static String APP_NAME	= "";
	
	
	/**
	 * 한개의  상품 결제시 사용하는 SKU NAME
	 */
	public static String ONE_SKU_ITEM_NAME = "";
	
	/**
	 * 전체 상품 결제시 사용하는 SKU NAME
	 */
	public static String ALL_SKU_ITEM_NAME = "";
	
	public static String IN_APP_BILLING_KEY = "";
	
	/**
	 * 사용하는 해당 단말기의 Width Pixel
	 */
	public static float DISPLAY_WIDTH_PIXELS = 0.0f;
	
	/**
	 * 사용하는 해당 단말기의 Height Pixel
	 */
	public static float DISPLAY_HEIGHT_PIXELS = 0.0f;
	
	
	
	public static String PATH_APP_ROOT	= "";
	public static String PATH_EXTERNAL_VIDEO_INFORMATION_ROOT = Environment.getExternalStorageDirectory()+"/LittleFox/VideoInformation/"; 
	
	public static String PATH_VIDEO_INFORMATION_ROOT = "";
	public static String PATH_MP4 		="";
	public static String PATH_JSON		= "";
	public static String PATH_THUMBNAIL = "";

	public static boolean IS_TABLET = false;

	public static String PATH_VIBRATOR_ROOT	= PATH_APP_ROOT;
	public static final String PATH_EXTERNAL_VIBRATOR_ROOT = Environment.getExternalStorageDirectory()+"/LittleFox/Vibrator/";
	public static String PATH_APP_RECOMMOND_ICON_ROOT = "";
	
	
	public static final String ANALYTICS_CATEGORY_RECOMMONAD_ICON		= "추천 앱";
	
	public static final String ANALYTICS_ACTION_POPULAR_TAB				= "인기순";
	public static final String ANALYTICS_ACTION_NEWEST_TAB				= "최신순";
	public static final String ANALYTICS_ACTION_NAME_TAB				= "이름순";
	public static final String ANALYTICS_ACTION_EXCUTE 			= "실행";
	public static final String ANALYTICS_ACTION_APP_STORE_MOVE 	= "앱스토어 이동";
	public static String ANALYTICS_PROPERTY_ID = "";
	
	
	private static StorybookTempleteAPI sStorybookTempleteAPI = null;
	
	public static StorybookTempleteAPI getInstance(Context context)
	{
		sContext = context;
		if(sStorybookTempleteAPI == null)
		{
			sStorybookTempleteAPI = new StorybookTempleteAPI();
		}
		return sStorybookTempleteAPI;
	}
	
	public void init(String appName, String packageName, String httpHeaderName, String oneSkuItemName, String allSkuItemName, String inAppBillingKey,String googleAnaylticsID,  DisPlayMetricsObject disPlayMetricsObject, boolean isTablet)
	{
		setInformation(appName, packageName, httpHeaderName, inAppBillingKey, oneSkuItemName, allSkuItemName, googleAnaylticsID, disPlayMetricsObject, isTablet);
	}
	
	private void setInformation(String appName, String packageName, String httpHeaderName, String inAppBillingKey, String oneSkuItemName, String allSkuItemName, String googleAnaylticsID, DisPlayMetricsObject disPlayMetricsObject, boolean isTablet)
	{
		StorybookTempleteAPI.APP_NAME = appName;
		StorybookTempleteAPI.PACKAGE_NAME = packageName;
		StorybookTempleteAPI.HTTP_HEADER_APP_NAME = httpHeaderName;
		StorybookTempleteAPI.ONE_SKU_ITEM_NAME = oneSkuItemName;
		StorybookTempleteAPI.ALL_SKU_ITEM_NAME = allSkuItemName;
		StorybookTempleteAPI.IN_APP_BILLING_KEY = inAppBillingKey;
		StorybookTempleteAPI.ANALYTICS_PROPERTY_ID = googleAnaylticsID;

		StorybookTempleteAPI.PATH_EXTERNAL_VIDEO_INFORMATION_ROOT = Environment.getExternalStorageDirectory()+"/LittleFox/VideoInformation/"+StorybookTempleteAPI.APP_NAME+"/"; 
		StorybookTempleteAPI.PATH_APP_ROOT =  "/data/data/" + packageName + "/files/";
		StorybookTempleteAPI.PATH_MP4 = StorybookTempleteAPI.PATH_APP_ROOT + "mp4/";
		StorybookTempleteAPI.PATH_JSON = StorybookTempleteAPI.PATH_APP_ROOT + "json/";
		StorybookTempleteAPI.PATH_VIBRATOR_ROOT = PATH_APP_ROOT;
		StorybookTempleteAPI.PATH_VIDEO_INFORMATION_ROOT = PATH_APP_ROOT;
		StorybookTempleteAPI.PATH_APP_RECOMMOND_ICON_ROOT = PATH_APP_ROOT + "icons/"; 
		StorybookTempleteAPI.PATH_THUMBNAIL = PATH_APP_ROOT + "thumbnail/";

		StorybookTempleteAPI.IS_TABLET = isTablet;
	}

	public static Context getApplicationContext()
	{
		return sContext;
	}
	
}
