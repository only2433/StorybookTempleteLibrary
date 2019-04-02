package com.littlefox.storybook.lib.object;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class InitItemResult implements Parcelable
{
	public InitItemInApp inapp 		= new InitItemInApp();
	public InitAppVersion version 	= new InitAppVersion();
	public InitAppVibrator vibrator = new InitAppVibrator();
	public ArrayList<RecommandAppInformation> banner_list = new ArrayList<RecommandAppInformation>();
	
	public String code 			="";
	public String message 		="";
	public String device_id 	="";
	public String api_ver 		="";
	public String app_price		="";

	public InitItemResult(Parcel in)
	{
		code 			= in.readString();
		message 		= in.readString();
		device_id 		= in.readString();
		inapp			= (InitItemInApp) in.readSerializable();
		version			= (InitAppVersion) in.readSerializable();
		vibrator		= (InitAppVibrator) in.readSerializable();
		banner_list		= (ArrayList<RecommandAppInformation>) in.readSerializable();
		app_price		= in.readString();
	}
	
	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(code);
		dest.writeString(message);
		dest.writeString(device_id);
		dest.writeSerializable(inapp);
		dest.writeSerializable(version);
		dest.writeSerializable(vibrator);
		dest.writeSerializable(banner_list);
		dest.writeString(app_price);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public Object createFromParcel(Parcel source)
		{
			// TODO Auto-generated method stub
			return new InitItemResult(source);
		}

		@Override
		public Object[] newArray(int size)
		{
			// TODO Auto-generated method stub
			return new InitItemResult[size];
		}
		
	};
	

	
	public class InitItemInApp implements Serializable
	{
		public String inapp_code 	= "";
		public String btn1_mode 	= "";
		public String btn1_text 	= "";
		
		public String btn2_mode 	= "";
		public String btn2_text 	= "";
		public String btn2_useyn 	= "";
		
		public String iac_title 	= "";
		public String iac_content 	= "";
		public String btn_link		= "";
	}
	
	public class InitAppVersion implements Serializable
	{
		/**
		 * 최신버젼 ex) 1.0.0
		 */
		public String newest 		="";
		
		/**
		 * 	C: Critical  무조건 업데이트 진행
			N: Normal  사용자 선택 (Yes/No)
		 */
		public String update_type 	="";
		
		/**
		 * 앱 스토어 URL
		 */
		public String store_url		="";
	}
	
	public class InitAppVibrator implements Serializable
	{
		/**
		 * vibrator 파일 버젼 정보, 로컬값과 다르면 서버 통신해서 Json 정보 받아온다.
		 */
		public String version = "";
		
		/**
		 * vibrator 정보가 있는 URL
		 */
		public String url = "";
	}
	
	
}
