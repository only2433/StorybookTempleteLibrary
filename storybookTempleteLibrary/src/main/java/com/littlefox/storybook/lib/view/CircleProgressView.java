package com.littlefox.storybook.lib.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.storybook.lib.api.StorybookTempleteAPI;
import com.littlefox.storybook.lib.common.CommonUtils;

public class CircleProgressView extends ImageView
{
	private static final float MAX_ARC = 360.0f;
	
	

	
	private int mCurrentPercent	= 0;
	private Paint mPaint;
	private Bitmap mCurrentBitmap = null;
	private Bitmap mCurrentCoverBitmap = null;
	private RectF mProgressRectF = null;
	private Context mContext;
	public CircleProgressView(Context context)
	{
		super(context);
		init(context);
	}

	public CircleProgressView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}
	
	public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init(context);
	}
	

	
	private void init(Context context)
	{
		mContext = context;

		mPaint = new Paint();
		mPaint.setColor(getResources().getColor(R.color.percent_color));
		mPaint.setAntiAlias(true);

		if(StorybookTempleteAPI.IS_TABLET)
		{
			mCurrentBitmap = CommonUtils.getInstance(mContext).drawableToBitmap(getResources().getDrawable(R.drawable.tablet_thumbnail_download));
			mCurrentBitmap = Bitmap.createScaledBitmap(mCurrentBitmap, CommonUtils.getInstance(mContext).getPixel(92), CommonUtils.getInstance(mContext).getPixel(92), true);
		}
		else
		{
			mCurrentBitmap = CommonUtils.getInstance(mContext).drawableToBitmap(getResources().getDrawable(R.drawable.thumbnail_download));
			mCurrentBitmap = Bitmap.createScaledBitmap(mCurrentBitmap, CommonUtils.getInstance(mContext).getPixel(145), CommonUtils.getInstance(mContext).getPixel(145), true);

		}
	
		mCurrentCoverBitmap = CommonUtils.getInstance(mContext).drawableToBitmap(getResources().getDrawable(R.drawable.thumbnail_download_s));
		
		if(StorybookTempleteAPI.IS_TABLET)
		{
			mCurrentCoverBitmap = Bitmap.createScaledBitmap(mCurrentCoverBitmap, CommonUtils.getInstance(mContext).getPixel(65), CommonUtils.getInstance(mContext).getPixel(65), true);
			mProgressRectF = new RectF(CommonUtils.getInstance(mContext).getPixel(7),CommonUtils.getInstance(mContext).getPixel(4), CommonUtils.getInstance(mContext).getPixel(88), CommonUtils.getInstance(mContext).getPixel(86));
		}
		else
		{
			mCurrentCoverBitmap = Bitmap.createScaledBitmap(mCurrentCoverBitmap, CommonUtils.getInstance(mContext).getPixel(110), CommonUtils.getInstance(mContext).getPixel(110), true);
			mProgressRectF = new RectF(CommonUtils.getInstance(mContext).getPixel(9), CommonUtils.getInstance(mContext).getPixel(5), CommonUtils.getInstance(mContext).getPixel(140), CommonUtils.getInstance(mContext).getPixel(140));
		}
		

		
		invalidate();
	}
	

	
	public void setPercent(int percent)
	{
		mCurrentPercent = percent;
		invalidate();
	}

	/**
	 * 다운로드 퍼센테이지에 따라 화면을 그리는 부분 
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawBitmap(mCurrentBitmap, 0, 0, null);
		canvas.drawArc(mProgressRectF, -90, mCurrentPercent * (MAX_ARC/100), true, mPaint);
		
		if(StorybookTempleteAPI.IS_TABLET)
		{
			canvas.drawBitmap(mCurrentCoverBitmap, CommonUtils.getInstance(mContext).getPixel(15), CommonUtils.getInstance(mContext).getPixel(14), null);
		}
		else
		{
			canvas.drawBitmap(mCurrentCoverBitmap, CommonUtils.getInstance(mContext).getPixel(19), CommonUtils.getInstance(mContext).getPixel(18), null);
		}
		
		
	}
	
	
	
}
