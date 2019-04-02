package com.littlefox.storybook.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.Font;
import com.ssomai.android.scalablelayout.ScalableLayout;


public class FlexibleDialog extends Dialog
{
	/** 다이얼로그 이벤트를 굳이 받지않아도 되는 경우 사용 */
	public static final int DIALOG_EVENT_DEFAULT 				= 0;
	/** Facebook 관련 다이얼로그 이벤트 Flag */
	public static final int DIALOG_EVENT_FACEBOOK 				= 1;
	/** IAC 관련 다이얼로그 이벤트 Flag */
	public static final int DIALOG_EVENT_IAC					= 2;
	/** 인터넷오류 관련 다이얼로그 이벤트 Flag */
	public static final int DIALOG_EVENT_INTERNET_ERROR 		= 3;
	/** 한편을 결제 할때 경고 관련 다이얼로그 이벤트 Flag */
	public static final int DIALOG_EVENT_ONE_ITEM_PAY_WARNING 	= 4;
	/** 이전에 봣던 화면으로 갈것인가의 여부를 결정하는  관련 다이얼로그 이벤트 Flag */
	public static final int DIALOG_EVENT_BEFORE_WATCHED_MOVE	= 5;
	/** LTE환경에서 다운로드 관련 다이얼로그 이벤트 (다운로드 도중 연결상태가 변경됫을 시) Flag */
	public static final int DIALOG_EVENT_MOBILE_DOWNLOAD_SUBMIT = 6;
	/** LTE환경에서 다운로드 관련 다이얼로그 이벤트 (다운로드 아이템을 클릭했을 시) */
	public static final int DIALOG_EVENT_MOBILE_DOWNLOAD_SUBMIT_ITEM_CLICK = 7;
	/** 하나의 아이템을 결제후 다운로드 관련 다이어로그 Flag */
	public static final int DIALOG_EVENT_DOWNLOAD_ONE_ITEM 		= 8;
	/** 모든 아이템을 결제 후 다운로드 관련 다이얼로그 Flag */ 
	public static final int DIALOG_EVENT_DOWNLOAD_ALL_ITEM		= 9;
	/**부모 확인 질문의 재시도에 관련된 다이얼로그 Flag */
	public static final int DIALOG_EVENT_PARENT_QUESTION_RETRY	= 10;
	/** 종료하기 전에 종료할것인지 묻는 팝업 */
	public static final int DIALOG_EVENT_END_APP				= 11;
	
	/** 버젼체크 관련 팝업 */
	public static final int DIALOG_EVENT_UPDATE_APP				= 12;
	
	public static final int DEFAULT_BUTTON_TYPE_1 = 0;
	public static final int DEFAULT_BUTTON_TYPE_2 = 1;
	
	public static final int MODE_TEXT 				= 0;
	public static final int MODE_EDIT 				= 1;
	public static final int MODE_TITLE_HAVE_TEXT 	= 2;
	
	private static final int TOTAL_BOX_WIDTH_SIZE = 872;
	private static final int TOTAL_BOX_HEIGHT_SIZE = 264;
	
	private static final int TOTAL_EDITBOX_INCLUDE_HEIGHT_SIZE = 314;
	
	private static final int TOP_BOX_WIDTH_SIZE = 872;
	private static final int TOP_BOX_HEIGHT_SIZE = 164;
	
	private static final int BOTTOM_BOX_WIDTH_SIZE = 435;
	private static final int BOTTOM_BOX_HEIGHT_SIZE = 98;
	
	public static final int TEXT_BOX_WIDTH_SIZE = 840;
	public static final int TEXT_BOX_HEIGHT_SIZE = 50;
	private static final int TEXT_BOX_MARGIN_LEFT = 16;
	private static final int TEXT_BOX_MARGIN_TOP = 57;
	
	public static final int TEXT_SIZE = 36;
	
	private static final int INTERVAL_BUTTON_BY_BUTTON = 2;
	
	private static final int FIRST_BUTTON_TAG = 0;
	private static final int SECOND_BUTTON_TAG = 1;
	
	private Context mContext;
	private ScalableLayout _BaseLayout;
	private TextView _TitleText;
	private TextView _FirstButton;
	private TextView _SecondButton;
	private TextView _ContentText;
	private EditText _EditText;
	private DialogListener mDialogListener;
	private String mFlexibleText;
	private String mFirstButtonText;
	private String mSecondButtonText;
	private String mEditTitle;
	private int mTextLineAddCount = -1;
	private int mButtonCount = -1;
	private int mCurrentDialogMode = -1;
	private int mCurrentDialogMessageSubType = -1;
	
	public FlexibleDialog(Context context, int lineAddCount, String titleText)
	{
		super(context, R.style.BackgroundDialog_lite);
		mContext 			= context;
		mTextLineAddCount 	= lineAddCount-1;
		mFlexibleText	 	= titleText;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	public FlexibleDialog(Context context, int lineAddCount, String title, String text)
	{
		super(context, R.style.BackgroundDialog_lite);
		mContext 			= context;
		mTextLineAddCount 	= lineAddCount-1;
		mEditTitle			= title;  
		mFlexibleText 		= text;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	/**
	 * 다어얼로그 형태를 결정한다.
	 * @param mode  MODE_EDIT : 에디트 팝업, MODE_TEXT : 텍스트 팝업, MODE_TITLE_HAVE_TEXT : 타이틀이 있는 텍스트
	 */
	public void setDialogMode(int mode)
	{
		mCurrentDialogMode = mode;
	}
	
	/**
	 * 리스너에서 전달해주는 Event 타입
	 * @param type Event타입
	 */
	public void setDialogMessageSubType(int type)
	{
		mCurrentDialogMessageSubType = type;
	}
	
	public void setButtonText(int buttonType)
	{
		if(buttonType == DEFAULT_BUTTON_TYPE_1)
		{
			setButtonText(mContext.getResources().getString(R.string.btn_confirm),"");
		}
		else
		{
			setButtonText(mContext.getResources().getString(R.string.btn_confirm), mContext.getResources().getString(R.string.btn_cancel));
		}
	}
	
	public void setButtonText(String firstButtonText)
	{
		setButtonText(firstButtonText, "");
	}
	
	public void setButtonText(String firstButtonText, String secondButtonText)
	{
		mFirstButtonText = firstButtonText;
		mSecondButtonText = secondButtonText;
		if(secondButtonText.equals(""))
		{
			mButtonCount = 1;
		}
		else
		{
			mButtonCount = 2;
		}
		initMakeView();
	}
	
	public void setDialogListener(DialogListener dialogListener)
	{
		mDialogListener = dialogListener;
	}
	
	private void initMakeView()
	{
		if(mCurrentDialogMode == MODE_TEXT)
		{
			_BaseLayout = new ScalableLayout(mContext, TOTAL_BOX_WIDTH_SIZE, TOTAL_BOX_HEIGHT_SIZE + TEXT_BOX_HEIGHT_SIZE * mTextLineAddCount);
			_BaseLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			_TitleText = new TextView(mContext);
			_BaseLayout.addView(_TitleText, (float)TEXT_BOX_MARGIN_LEFT, (float)TEXT_BOX_MARGIN_TOP,  (float)TEXT_BOX_WIDTH_SIZE, (float)TEXT_BOX_HEIGHT_SIZE + TEXT_BOX_HEIGHT_SIZE * mTextLineAddCount);
			_BaseLayout.setScale_TextSize(_TitleText, TEXT_SIZE);
			_TitleText.setText(mFlexibleText);
			_TitleText.setGravity(Gravity.CENTER);
			_TitleText.setTextColor(mContext.getResources().getColor(R.color.black));
			_TitleText.setTypeface(Font.getInstance(mContext).getRobotoMedium());
			
			makeButton();
		}
		else if(mCurrentDialogMode == MODE_EDIT)
		{
			_BaseLayout = new ScalableLayout(mContext, TOTAL_BOX_WIDTH_SIZE, TOTAL_EDITBOX_INCLUDE_HEIGHT_SIZE + TEXT_BOX_HEIGHT_SIZE * mTextLineAddCount);
			_BaseLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			
			_TitleText = new TextView(mContext);
			_BaseLayout.addView(_TitleText, (float)TEXT_BOX_MARGIN_LEFT, (float)TEXT_BOX_MARGIN_TOP/2,  (float)TEXT_BOX_WIDTH_SIZE, (float)TEXT_BOX_HEIGHT_SIZE);
			_BaseLayout.setScale_TextSize(_TitleText, TEXT_SIZE);
			_TitleText.setText(mEditTitle);
			_TitleText.setGravity(Gravity.CENTER);
			_TitleText.setTextColor(mContext.getResources().getColor(R.color.black));
			_TitleText.setTypeface(Font.getInstance(mContext).getRobotoBold());
			
			_EditText = new EditText(mContext);
			_BaseLayout.addView(_EditText, (float)TEXT_BOX_MARGIN_LEFT, (float)TEXT_BOX_MARGIN_TOP+TEXT_BOX_HEIGHT_SIZE,  (float)TEXT_BOX_WIDTH_SIZE, (float)TEXT_BOX_HEIGHT_SIZE + TEXT_BOX_HEIGHT_SIZE * mTextLineAddCount);
			_BaseLayout.setScale_TextSize(_EditText, TEXT_SIZE);
			_EditText.setText(mFlexibleText);
			_EditText.setGravity(Gravity.LEFT);
			_EditText.setTextColor(mContext.getResources().getColor(R.color.black));
			
			_EditText.setBackgroundResource(R.drawable.input_box_9);
			
			_EditText.setTypeface(Font.getInstance(mContext).getRobotoRegular());
			
			
			makeButton();
		}
		else if(mCurrentDialogMode == MODE_TITLE_HAVE_TEXT)
		{
			_BaseLayout = new ScalableLayout(mContext, TOTAL_BOX_WIDTH_SIZE, TOTAL_EDITBOX_INCLUDE_HEIGHT_SIZE + TEXT_BOX_HEIGHT_SIZE * mTextLineAddCount);
			_BaseLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			
			_TitleText = new TextView(mContext);
			_BaseLayout.addView(_TitleText, (float)TEXT_BOX_MARGIN_LEFT, (float)TEXT_BOX_MARGIN_TOP/2,  (float)TEXT_BOX_WIDTH_SIZE, (float)TEXT_BOX_HEIGHT_SIZE);
			_BaseLayout.setScale_TextSize(_TitleText, TEXT_SIZE);
			_TitleText.setText(mEditTitle);
			_TitleText.setGravity(Gravity.CENTER);
			_TitleText.setTextColor(mContext.getResources().getColor(R.color.black));
			_TitleText.setTypeface(Font.getInstance(mContext).getRobotoBold());
			
			_ContentText = new TextView(mContext);
			_BaseLayout.addView(_ContentText, (float)TEXT_BOX_MARGIN_LEFT, (float)TEXT_BOX_MARGIN_TOP+TEXT_BOX_HEIGHT_SIZE,  (float)TEXT_BOX_WIDTH_SIZE, (float)TEXT_BOX_HEIGHT_SIZE + TEXT_BOX_HEIGHT_SIZE * mTextLineAddCount);
			_BaseLayout.setScale_TextSize(_ContentText, TEXT_SIZE);
			_ContentText.setText(mFlexibleText);
			_ContentText.setGravity(Gravity.CENTER_HORIZONTAL);
			_ContentText.setTextColor(mContext.getResources().getColor(R.color.black));
			
			
			_ContentText.setTypeface(Font.getInstance(mContext).getRobotoRegular());
			
			makeButton();
			
		}

		
		setContentView(_BaseLayout);
	}
	
	private void makeButton()
	{
		switch(mCurrentDialogMode)
		{
		case MODE_TEXT:
			_FirstButton = new TextView(mContext);
			_FirstButton.setTag(FIRST_BUTTON_TAG);
			if(mButtonCount == 1)
			{
				_BaseLayout.addView(_FirstButton, 0, (float)(TOTAL_BOX_HEIGHT_SIZE + TEXT_BOX_HEIGHT_SIZE * mTextLineAddCount) -  (float)BOTTOM_BOX_HEIGHT_SIZE, (float)TOTAL_BOX_WIDTH_SIZE , (float)BOTTOM_BOX_HEIGHT_SIZE);
			}
			else
			{
				_BaseLayout.addView(_FirstButton, 0, (float)(TOTAL_BOX_HEIGHT_SIZE + TEXT_BOX_HEIGHT_SIZE * mTextLineAddCount) -  (float)BOTTOM_BOX_HEIGHT_SIZE, (float)BOTTOM_BOX_WIDTH_SIZE , (float)BOTTOM_BOX_HEIGHT_SIZE);
			}
			_BaseLayout.setScale_TextSize(_FirstButton, TEXT_SIZE);
			_FirstButton.setBackgroundResource(R.drawable.selector_flexiable_button);
			_FirstButton.setTextColor(mContext.getResources().getColor(R.color.white));
			_FirstButton.setTypeface(Font.getInstance(mContext).getRobotoMedium());
			_FirstButton.setText(mFirstButtonText);
			_FirstButton.setGravity(Gravity.CENTER);
			_FirstButton.setOnClickListener(mOnClickListener);
			
			if(mButtonCount == 2)
			{
				_SecondButton = new TextView(mContext);
				_SecondButton.setTag(SECOND_BUTTON_TAG);
				_BaseLayout.addView(_SecondButton, (float)BOTTOM_BOX_WIDTH_SIZE + (float)INTERVAL_BUTTON_BY_BUTTON, (float)(TOTAL_BOX_HEIGHT_SIZE + TEXT_BOX_HEIGHT_SIZE * mTextLineAddCount) -  (float)BOTTOM_BOX_HEIGHT_SIZE, (float)BOTTOM_BOX_WIDTH_SIZE , (float)BOTTOM_BOX_HEIGHT_SIZE);
				_BaseLayout.setScale_TextSize(_SecondButton, TEXT_SIZE);
				_SecondButton.setBackgroundResource(R.drawable.selector_flexiable_button);
				_SecondButton.setTextColor(mContext.getResources().getColor(R.color.white));
				_SecondButton.setTypeface(Font.getInstance(mContext).getRobotoMedium());
				_SecondButton.setText(mSecondButtonText);
				_SecondButton.setGravity(Gravity.CENTER);
				_SecondButton.setOnClickListener(mOnClickListener);
			}
			break;
		case MODE_EDIT:
		case MODE_TITLE_HAVE_TEXT:
			_FirstButton = new TextView(mContext);
			_FirstButton.setTag(FIRST_BUTTON_TAG);
			if(mButtonCount == 1)
			{
				_BaseLayout.addView(_FirstButton, 0, (float)(TOTAL_EDITBOX_INCLUDE_HEIGHT_SIZE + TEXT_BOX_HEIGHT_SIZE * mTextLineAddCount) -  (float)BOTTOM_BOX_HEIGHT_SIZE, (float)TOTAL_BOX_WIDTH_SIZE , (float)BOTTOM_BOX_HEIGHT_SIZE);
			}
			else
			{
				_BaseLayout.addView(_FirstButton, 0, (float)(TOTAL_EDITBOX_INCLUDE_HEIGHT_SIZE + TEXT_BOX_HEIGHT_SIZE * mTextLineAddCount) -  (float)BOTTOM_BOX_HEIGHT_SIZE, (float)BOTTOM_BOX_WIDTH_SIZE , (float)BOTTOM_BOX_HEIGHT_SIZE);
			}
			_BaseLayout.setScale_TextSize(_FirstButton, TEXT_SIZE);
			
			_FirstButton.setBackgroundResource(R.drawable.selector_flexiable_button);
			_FirstButton.setTextColor(mContext.getResources().getColor(R.color.white));
			_FirstButton.setTypeface(Font.getInstance(mContext).getRobotoMedium());
			_FirstButton.setText(mFirstButtonText);
			_FirstButton.setGravity(Gravity.CENTER);
			_FirstButton.setOnClickListener(mOnClickListener);
			
			if(mButtonCount == 2)
			{
				_SecondButton = new TextView(mContext);
				_SecondButton.setTag(SECOND_BUTTON_TAG);
				_BaseLayout.addView(_SecondButton, (float)BOTTOM_BOX_WIDTH_SIZE + (float)INTERVAL_BUTTON_BY_BUTTON, (float)(TOTAL_EDITBOX_INCLUDE_HEIGHT_SIZE + TEXT_BOX_HEIGHT_SIZE * mTextLineAddCount) -  (float)BOTTOM_BOX_HEIGHT_SIZE, (float)BOTTOM_BOX_WIDTH_SIZE , (float)BOTTOM_BOX_HEIGHT_SIZE);
				_BaseLayout.setScale_TextSize(_SecondButton, TEXT_SIZE);
				_SecondButton.setBackgroundResource(R.drawable.selector_flexiable_button);
				_SecondButton.setTextColor(mContext.getResources().getColor(R.color.white));
				_SecondButton.setTypeface(Font.getInstance(mContext).getRobotoMedium());
				_SecondButton.setText(mSecondButtonText);
				_SecondButton.setGravity(Gravity.CENTER);
				_SecondButton.setOnClickListener(mOnClickListener);
			}
			break;
		}
	}
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if(v.getTag().equals(FIRST_BUTTON_TAG))
			{
				if(mCurrentDialogMode == MODE_EDIT)
				{
					mDialogListener.onItemClick(Common.DIALOG_MESSAGE_FLAXIBLE_FIRST_BUTTON_CLICK, mCurrentDialogMessageSubType, _EditText.getText().toString());
				}
				else
				{
					mDialogListener.onItemClick(Common.DIALOG_MESSAGE_FLAXIBLE_FIRST_BUTTON_CLICK, mCurrentDialogMessageSubType, null);
				}
				dismiss();
			}else if(v.getTag().equals(SECOND_BUTTON_TAG))
			{
				mDialogListener.onItemClick(Common.DIALOG_MESSAGE_FLAXIBLE_SECOND_BUTTON_CLICK, mCurrentDialogMessageSubType, null);
				dismiss();
			}
		}
	};

	@Override
	public void dismiss()
	{
		super.dismiss();
		
		if(mCurrentDialogMessageSubType == DIALOG_EVENT_BEFORE_WATCHED_MOVE)
		{
			mDialogListener.onItemClick(Common.DIALOG_MESSAGE_FLAXIBLE_SECOND_BUTTON_CLICK, mCurrentDialogMessageSubType, null);
		}
		
		
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		if(mCurrentDialogMessageSubType == DIALOG_EVENT_UPDATE_APP)
		{
			mDialogListener.onItemClick(Common.DIALOG_MESSAGE_FLAXIBLE_FIRST_BUTTON_CLICK, mCurrentDialogMessageSubType, Common.BACK_PRESSED);
		}
	}
	
	
	
}
