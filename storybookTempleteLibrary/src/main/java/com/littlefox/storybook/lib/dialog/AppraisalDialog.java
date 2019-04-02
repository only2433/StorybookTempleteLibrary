package com.littlefox.storybook.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.Font;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.util.Locale;


public class AppraisalDialog extends Dialog
{
	private ScalableLayout _BaseLayout;
	private ImageView _CloseButton;
	private TextView _StarGiveButton;
	private DialogListener mDialogListener;
	private Context mContext;
	public AppraisalDialog(Context context)
	{
		super(context, R.style.BackgroundDialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_appraisal);
		mContext = context;
		init();
		initFont();
	}
	
	private void init()
	{
		_BaseLayout		= (ScalableLayout)findViewById(R.id.dialog_root_appraisal);
		_CloseButton 	= (ImageView)findViewById(R.id.close_appraisal);
		_StarGiveButton = (TextView)findViewById(R.id.btn_star_give_appraisal);
		_CloseButton.setOnClickListener(mOnClickListener);
		_StarGiveButton.setOnClickListener(mOnClickListener);
		
		_BaseLayout.setBackgroundResource(getAppraisalBackgroundByLocale());
	}
	
	public void initBackgroundImage(Drawable image)
	{
		_BaseLayout.setBackground(image);
	}
	
	private void initFont()
	{
		_StarGiveButton.setTypeface(Font.getInstance(mContext).getRobotoBold());
	}
	
	public void setDialogListener(DialogListener dialogListener)
	{
		mDialogListener = dialogListener;
	}
	
	private int getAppraisalBackgroundByLocale()
	{
		int id = 0;
		if(Locale.getDefault().toString().equals(Locale.KOREA.toString()))
		{
			id = mContext.getResources().getIdentifier("popup_appraisal_kr", "drawable", mContext.getPackageName());
		}
		else if(Locale.getDefault().toString().equals(Locale.JAPAN.toString()))
		{
			id = mContext.getResources().getIdentifier("popup_appraisal_jp", "drawable", mContext.getPackageName());
		}
		else if(Locale.getDefault().toString().equals(Locale.SIMPLIFIED_CHINESE.toString()))
		{
			id = mContext.getResources().getIdentifier("popup_appraisal_cn", "drawable", mContext.getPackageName());
		}
		else if(Locale.getDefault().toString().equals(Locale.TRADITIONAL_CHINESE.toString()))
		{
			id = mContext.getResources().getIdentifier("popup_appraisal_tw", "drawable", mContext.getPackageName());
		}
		else
		{
			id = mContext.getResources().getIdentifier("popup_appraisal_en", "drawable", mContext.getPackageName());
		}
		
		return id;
	}
	


	private View.OnClickListener mOnClickListener = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			int id = v.getId();
			if (id == R.id.close_appraisal)
			{
				dismiss();
				mDialogListener.onItemClick(Common.DIALOG_MESSAGE_APPRAISAL_NEXT_NOT_AGAIN, null);
			} else if (id == R.id.btn_star_give_appraisal)
			{
				mDialogListener.onItemClick(Common.DIALOG_MESSAGE_APPRAISAL_GO_RATE, null);
				dismiss();
			}
		}
	};

}
