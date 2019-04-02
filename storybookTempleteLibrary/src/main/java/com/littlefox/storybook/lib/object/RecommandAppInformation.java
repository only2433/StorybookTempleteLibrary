package com.littlefox.storybook.lib.object;

import java.io.Serializable;

import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.api.StorybookTempleteAPI;
import com.littlefox.storybook.lib.common.Common;

public class RecommandAppInformation implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String title = "";
	private String link_url = "";
	private String no_ad_app_id = "";
	private String icon_url = "";
	private String sort_newest = "";
	private String sort_popularity = "";
	
	private boolean isFileDownloadComplete = false;
	private String fileDownloadPath = "";
	
	/**
	 * 인스톨 되어 있는지의 여부
	 */
	private boolean isInstalled = false;
	
	public String getAppName()
	{
		return title;
	}
	
	public String getLinkUrl()
	{
		return link_url;
	}
	
	public String getPackageName()
	{
		return no_ad_app_id;
	}
	
	public String getIconUrl()
	{
		return icon_url;
	}
	
	public int getNewestOrder()
	{
		return Integer.valueOf(sort_newest);
	}
	
	public int getPopularOrder()
	{
		return Integer.valueOf(sort_popularity);
	}
	
	public boolean isPackageInstalled()
	{
		return isInstalled;
	}
	
	public boolean isFileDownloadComplete()
	{
		return isFileDownloadComplete;
	}
	
	public void setFileDownloadComplete(boolean isDownloadComplete)
	{
		isFileDownloadComplete = isDownloadComplete;
	}
	
	public void setInstalled(boolean isInstalled)
	{
		this.isInstalled = isInstalled;
	}
	
	public String getFileDownloadPath()
	{
		int prefileIndex = no_ad_app_id.lastIndexOf(".");
		String fileName = no_ad_app_id.substring(prefileIndex + 1, no_ad_app_id.length()) + ".png";
		return StorybookTempleteAPI.PATH_APP_RECOMMOND_ICON_ROOT + fileName;
	}
	
}
