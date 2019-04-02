package com.littlefox.storybook.lib.object;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.littlefox.logmonitor.Log;

/**
 * 다시보지 않기를 한 IAC 아이템을 24시간 후에 다시 보이게 하기 위해 만든 오브젝트
 * @author 정재현
 *
 */
public class IACInformation
{
	private ArrayList<IACTable> list = new ArrayList<IACTable>();
	
	public IACInformation(){}
	
	public ArrayList<IACTable> getIACTableList()
	{
		return list;
	}
	
	/**
	 * 다시보지않기를 한 IAC아이템을 24시간 지난것만 간추려서 삭제한다.
	 */
	public void awakeIACItem()
	{
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
		Date date;
		Date saveDate;
		for(int i = 0 ; i < list.size(); i++)
		{
			date = new Date(currentTime);
			saveDate = new Date(list.get(i).iacSleepTime);
			
			String currentDateFormat 	= CurDateFormat.format(date);
			String savedDateFormat		= CurDateFormat.format(saveDate);
			
			Log.i("currentDateFormat : " + savedDateFormat+", currentDateFormat : "+savedDateFormat);
			if(currentDateFormat.equals(savedDateFormat) == false)
			{
				list.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * IAC 코드가 현재 다시보지않기를 하여 보이지 않아야할 상황인지 체크
	 * @param iacCode 체크 할 IAC Code
	 * @return
	 */
	public boolean isSleepItem(String iacCode)
	{
		for(int i = 0 ; i < list.size(); i++)
		{
			if(iacCode.equals(list.get(i).iacCode))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void addIACSleep(String iacCode)
	{
		list.add(new IACTable(iacCode, System.currentTimeMillis()));
	}

	public class IACTable
	{
		/** IAC 각각의 고유 식별자 */
		public String iacCode ="";
		
		/** IAC를 다시 보지않기를 눌렀을 때의 시간 */
		public long iacSleepTime = -1;
		
		public IACTable(String iacCode, long iacSleepTime)
		{
			this.iacCode 		= iacCode;
			this.iacSleepTime 	= iacSleepTime;
		}
	}
	
}
