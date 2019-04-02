package com.littlefox.storybook.lib.dialog;

import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.Font;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class PromotionDialog extends Dialog
{
	private DialogListener mDialogListener;
	
	private TextView 	_PromotionTitle;
	private EditText 	_PromotionEdit;
	private ImageView	_ConfirmButton;
	private ImageView 	_ExitButton;
	private Context 	mContext;
	public PromotionDialog(Context context, DialogListener dialogListener)
	{
		super(context, R.style.BackgroundDialog);
		mContext = context;
		mDialogListener = dialogListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_promotion_code);
		init();
		initFont();
		
	}

	private void init()
	{
		_PromotionTitle = (TextView)findViewById(R.id.promotion_title);
		_PromotionEdit  = (EditText)findViewById(R.id.promotion_edit);
		_ConfirmButton	= (ImageView)findViewById(R.id.promotion_confirm);
		_ConfirmButton.setOnClickListener(mOnClickListener);
		_ExitButton		= (ImageView)findViewById(R.id.promotion_close);
		_ExitButton.setOnClickListener(mOnClickListener);
		
	}

	private void initFont()
	{
		Font font = Font.getInstance(mContext);
		_PromotionTitle.setTypeface(font.getRobotoBold());
		_PromotionEdit.setTypeface(font.getRobotoMedium());
	}
	
	public void setPromotionInfo(String promotionText)
	{
		_PromotionEdit.setText(promotionText);
	}
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			int id = v.getId();
			if (id == R.id.promotion_confirm)
			{
				if(_PromotionEdit.getText().toString().equals("") == true)
				{
					Toast.makeText(mContext, mContext.getResources().getString(R.string.promotion_code_error), Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					mDialogListener.onItemClick(Common.DIALOG_MESSAGE_PROMOTION_CODE_CONFIRM, _PromotionEdit.getText().toString());
					dismiss();
				}
			} else if (id == R.id.promotion_close)
			{
				dismiss();
			}
		}
	};
	
	
	
}
