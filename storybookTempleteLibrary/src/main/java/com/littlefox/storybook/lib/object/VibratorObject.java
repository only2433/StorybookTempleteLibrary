package com.littlefox.storybook.lib.object;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class VibratorObject implements Parcelable
{
	public int index = 0;
	public ArrayList<VibratorInformation> vibrator_list = new ArrayList<VibratorInformation>();

	public VibratorObject(Parcel in)
	{
		index = in.readInt();
		vibrator_list = (ArrayList<VibratorInformation>) in.readSerializable();
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
		dest.writeInt(index);
		dest.writeSerializable(vibrator_list);
	}
	
	public  static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public Object createFromParcel(Parcel source)
		{
			// TODO Auto-generated method stub
			return new VibratorObject(source);
		}

		@Override
		public Object[] newArray(int size)
		{
			// TODO Auto-generated method stub
			return new VibratorObject[size];
		}
		
	};

	
	public class VibratorInformation implements Serializable
	{
		/**
		 * 진동 발생 시간
		 */
		public String start_time ="";
		
		/**
		 * 진동의 길이를 패턴으로 할수 있다. ex) 1000,100,2000  (첫 인자는 진동 길이 시간이며, 두번째 인자는 진동간의 Term 시간이다.)
		 */
		public String vibrate_pattern = "";
	}
}
