package com.littlefox.storybook.lib.object;

import java.util.ArrayList;

public class VideoBaseResult
{
	public ArrayList<VideoInformationResult> list = new ArrayList<VideoInformationResult>();
	
	public String code = "";
	
	public String message = "";
	
	public String product_all ="";
	
	
	public class VideoInformationResult
	{
		/**
		 * 해당 컨텐츠의 fc_id
		 */
		public String fc_id = "";
		
		/**
		 * 서버에서 받는 상품 코드
		 */
		public String iap = "";
		
		/**
		 * 서버에서 받는 URL, 
		 */
		public String url = "";
		
		/**
		 * 자막의 버젼
		 */
		public String caption_ver = "";
		
		/**
		 * 해당 타이틀
		 */
		public String title = "";
		
		/**
		 * 파일 생성시간
		 */
		public String change_date = "";
		
		/**
		 * MD5 암호화 된 프리인지 아닌지의 여부 
		 */
		public String type = "";
	}

}
