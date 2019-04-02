package com.littlefox.storybook.lib.object;

import java.util.ArrayList;

public class CaptionInformation
{

	public ArrayList<CaptionData> caption = new ArrayList<CaptionData>();
	
	public class CaptionData
	{
		/**
		 * 자막이 보일 시간
		 */
		public String start_time ="";
		
		/**
		 * 자막의 텍스트
		 */
		public String text 	= "";
		
		/**
		 * 자막이 끝나는 시간
		 */
		public String end_time = "";
	}

}
