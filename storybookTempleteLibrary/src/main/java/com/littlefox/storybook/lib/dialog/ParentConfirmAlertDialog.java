package com.littlefox.storybook.lib.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.storybook.lib.api.StorybookTempleteAPI;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.CommonUtils;
import com.littlefox.storybook.lib.common.Font;

import java.util.Random;


public class ParentConfirmAlertDialog extends TempleteAlertDialog
{
	public static final int MESSAGE_ANSWER_CORRECT = 0;
	public static final int MESSAGE_ANSWER_NOT_CORRECT = 1;
	
	private int mQuestionRandNum = 0;
	private Context mContext;
	
	private LinearLayout _BaseEditLayout;
	private LinearLayout _BaseTitleLayout;
	private TextView _TitleText;
	private EditText _EditText;
	
	public ParentConfirmAlertDialog(Context context)
	{
		super(context);
		init(context);
	}
	
	public ParentConfirmAlertDialog(Context context, String message)
	{	
		super(context, message);
		init(context);
	}
	
	public ParentConfirmAlertDialog(Context context, String title, String message)
	{
		super(context, title, message);
		init(context);
	}
	
	private void init(Context context)
	{
		mContext = context;
		initView();
		showQuestionText();
	}
	
	
	private void showQuestionText()
	{
		mQuestionRandNum = new Random().nextInt(10);
		
		String[] questionArray 	= mContext.getResources().getStringArray(R.array.arr_question);
		
		String tempText = mContext.getResources().getString(R.string.parent_confirm)+" ("+ questionArray[mQuestionRandNum]+")";
		SpannableString sText = new SpannableString(mContext.getResources().getString(R.string.parent_confirm)+" ("+ questionArray[mQuestionRandNum]+")");
		int colorIndex = tempText.indexOf("(");
		
		sText.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0,colorIndex, 0);
		sText.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.parent_confirm_question_color)), colorIndex, sText.length(), 0);
		_TitleText.append(sText);
	}
	
	private void checkAnswer()
	{
		String[] answerArray	= mContext.getResources().getStringArray(R.array.arr_answer);
		
		if(answerArray[mQuestionRandNum].equals(_EditText.getText().toString()))
		{
			mDialogListener.onItemClick(Common.DIALOG_MESSAGE_PARENT_CONFIRM,MESSAGE_ANSWER_CORRECT, null);
		}
		else
		{
			mDialogListener.onItemClick(Common.DIALOG_MESSAGE_PARENT_CONFIRM,MESSAGE_ANSWER_NOT_CORRECT, null);
		}
	}
	
	private void initView()
	{
		_TitleText = new TextView(mContext);
		_TitleText.setTextColor(mContext.getResources().getColor(R.color.black));
		
		if(CommonUtils.getInstance(mContext).isMinimumDisplayWidth() && StorybookTempleteAPI.IS_TABLET)
		{
			_TitleText.setTextSize(CommonUtils.getInstance(mContext).getPixel(40));
		}
		else
		{
			_TitleText.setTextSize(CommonUtils.getInstance(mContext).getPixel(20));
		}
		
		_TitleText.setGravity(Gravity.CENTER);
		_TitleText.setTypeface(Font.getInstance(mContext).getRobotoBold());
		
		_BaseTitleLayout = new LinearLayout(mContext);
		_BaseTitleLayout.setOrientation(LinearLayout.VERTICAL);
		_BaseTitleLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		_BaseTitleLayout.setPadding(0, CommonUtils.getInstance(mContext).getPixel(15), 0, 0);
		_BaseTitleLayout.addView(_TitleText);
	
		_BaseEditLayout = new LinearLayout(mContext);
		_BaseEditLayout.setOrientation(LinearLayout.VERTICAL);
		_BaseEditLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		_EditText		= new EditText(mContext);
		_EditText.setSelection(_BaseEditLayout.getWidth()/2);
		_EditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		_BaseEditLayout.setPadding(CommonUtils.getInstance(mContext).getPixel(10), CommonUtils.getInstance(mContext).getPixel(15), CommonUtils.getInstance(mContext).getPixel(10), 0);
		_BaseEditLayout.addView(_EditText);
	}

	@Override
	public void show()
	{
		mAlertDialogBuilder = new AlertDialog.Builder(mContext);
		mAlertDialogBuilder.setCustomTitle(_BaseTitleLayout);
		
		mAlertDialogBuilder.setView(_BaseEditLayout);
		
		if(isCancelable)
		{
			mAlertDialogBuilder.setCancelable(true);
		}
		else
		{
			mAlertDialogBuilder.setCancelable(false);
		}
		
		if(mIconResource != -1)
		{
			mAlertDialogBuilder.setIcon(mIconResource);
		}
		
		mAlertDialogBuilder.setNegativeButton(mFirstButtonText, new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				
			}
		});
		if(mButtonCount == 2)
		{
			mAlertDialogBuilder.setPositiveButton(mSecondButtonText, new OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					checkAnswer();
				}
			});
		}
		
		mAlertDialogBuilder.show();
	
	}
	
	

}
