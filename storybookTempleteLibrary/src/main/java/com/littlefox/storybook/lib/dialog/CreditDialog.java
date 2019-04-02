package com.littlefox.storybook.lib.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.littlefox.library.storybooktempletelibrary.R;
import com.ssomai.android.scalablelayout.ScalableLayout;


public class CreditDialog extends Dialog
{
	private Context mContext;
	private ImageView _CloseButton;
	
	private ScalableLayout _CreditStaffLayout;
	private ImageView _CreditTitleImage;
	private ImageView _CreditTextImage;
	private ImageView _CreditStaffImage;
	
	
	public CreditDialog(Context context, int theme)
	{
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_credit);
		mContext = context;
		init();
	}

	private void init()
	{
		_CloseButton 		= (ImageView)findViewById(R.id.credit_close_btn);
		_CloseButton.setOnClickListener(mClickListener);
		_CreditStaffLayout = (ScalableLayout)findViewById(R.id.credit_staff_layout);
		_CreditTitleImage 	= (ImageView)findViewById(R.id.credit_title_img);
		_CreditTextImage 	= (ImageView)findViewById(R.id.credit_text_img);
		_CreditStaffImage 	= (ImageView)findViewById(R.id.credit_staff_image);
		initInformationImage();
	}
	
	private void initInformationImage()
	{
		int titleImageId = mContext.getResources().getIdentifier("writer_photo", "drawable", mContext.getPackageName());
		int textImageId = mContext.getResources().getIdentifier("writer_name", "drawable", mContext.getPackageName());
		int staffImageId = mContext.getResources().getIdentifier("writer_tex", "drawable", mContext.getPackageName());
		
		_CreditTitleImage.setBackgroundResource(titleImageId);
		_CreditTextImage.setBackgroundResource(textImageId);
		
		Drawable staffImageDrawable = mContext.getResources().getDrawable(staffImageId);
		BitmapDrawable bitmapDrawable = (BitmapDrawable)staffImageDrawable;
		int width = bitmapDrawable.getBitmap().getWidth();
		int height = bitmapDrawable.getBitmap().getHeight();
		
		_CreditStaffLayout.setScaleHeight(height);
		_CreditStaffLayout.moveChildView(_CreditStaffImage, 0, 0, width, height);
		_CreditStaffImage.setBackgroundResource(staffImageId);

	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if(v.getId() == R.id.credit_close_btn)
			{
				dismiss();
			}
		}
	};
	
	
}
