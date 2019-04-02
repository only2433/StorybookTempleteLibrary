package com.littlefox.storybook.lib.common;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

public class Font
{
	private static Font _self = null;
	
	/**
	 * Noto sans KR bold
	 */
	private static Typeface tfRobotoBold = null;
	
	/**
	 * Noto sans KR regular
	 */
	private Typeface tfRobotoRegular = null;
	
	/**
	 * Noto sans KR medium
	 */
	private Typeface tfRobotoMedium = null;
	
	public static Font getInstance(Context context)
	{
		if(_self == null)
		{
			_self = new Font(context);
		}
		return _self;
	}
	

	private Font(Context context)
	{
		AssetManager mgr = context.getAssets();
		
		tfRobotoBold = Typeface.createFromAsset(mgr, "fonts/Roboto-Bold.ttf");
		tfRobotoRegular = Typeface.createFromAsset(mgr, "fonts/Roboto-Regular.ttf");
		tfRobotoMedium = Typeface.createFromAsset(mgr, "fonts/Roboto-Medium.ttf");
		
		if(tfRobotoBold == null)
		{
			tfRobotoBold = Typeface.DEFAULT_BOLD;
		}
		
		if(tfRobotoRegular == null)
		{
			tfRobotoRegular = Typeface.DEFAULT;
		}
		
		if(tfRobotoMedium == null)
		{
			tfRobotoMedium = Typeface.DEFAULT;
		}
	}
	
	public Typeface getRobotoBold()
	{
		return tfRobotoBold;
	}
	
	public Typeface getRobotoRegular()
	{
		return tfRobotoRegular;
	}
	
	public Typeface getRobotoMedium()
	{
		return tfRobotoMedium;
	}
}
