package com.littlefox.storybook.lib.object;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;


public class VibratorBaseObject
{
	/**
	 * 각각의 무비들의 진동정보 리스트, 각각 무비 마다 진동 관련 리스트 가 있어야한다. 
	 */
	public ArrayList<VibratorObject> movie_list= new ArrayList<VibratorObject>();

}

