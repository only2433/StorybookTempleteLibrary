package com.littlefox.storybook.lib.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Mp4InformationResult implements Parcelable
{

	public String fc_id = "";
	

	public String mp4 = "";
	

	public String enc = "";
	
	public Mp4InformationResult(Parcel in)
	{
		fc_id 	= in.readString();
		mp4		= in.readString();
		enc		= in.readString();
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
		dest.writeString(fc_id);
		dest.writeString(mp4);
		dest.writeString(enc);
		
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public Object createFromParcel(Parcel source)
		{
			// TODO Auto-generated method stub
			return new Mp4InformationResult(source);
		}

		@Override
		public Object[] newArray(int size)
		{
			// TODO Auto-generated method stub
			return new Mp4InformationResult[size];
		}
		
	};
	
	
}
