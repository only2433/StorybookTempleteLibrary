package com.littlefox.storybook.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.library.view.text.SeparateTextView;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.CommonUtils;
import com.littlefox.storybook.lib.common.Font;
import com.ssomai.android.scalablelayout.ScalableLayout;


public class PaymentDialog extends Dialog
{
	private ScalableLayout _BaseLayout;
	private TextView _OneBuyTitle;
	private TextView _AllPlayItemCountTitle;
	private SeparateTextView _AllBuyPriceText;
	private SeparateTextView _OneBuyPriceText;
	private ImageView _OneBuyButton;
	private ImageView _AllBuyButton;
	private ImageView _CloseButton;
	private DialogListener mDialogListener;
	private Context mContext;
	
	public PaymentDialog(Context context)
	{
		super(context, R.style.BackgroundDialog_Big);
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_payment);
		init();
	}
	public void setItemText(String title, DialogListener dialogListener)
	{
		_OneBuyTitle.setText(title);
		mDialogListener = dialogListener;
	}
	
	
	public void setAppPrice(String onePrice, String allPrice)
	{
		String[] onePriceText = getPriceInformationText(onePrice);
		String[] allPriceText = getPriceInformationText(allPrice);
		
		_OneBuyPriceText.setSeparateText(onePriceText[0], onePriceText[1])
		.setSeparateTextSize(CommonUtils.getInstance(mContext).getPixel(40), CommonUtils.getInstance(mContext).getPixel(70))
		.showView();
		
		_AllBuyPriceText.setSeparateText(allPriceText[0], allPriceText[1])
		.setSeparateTextSize(CommonUtils.getInstance(mContext).getPixel(50), CommonUtils.getInstance(mContext).getPixel(80))
		.showView();
	}
	private void init()
	{
		_BaseLayout  = (ScalableLayout)findViewById(R.id.background_layout);
		_OneBuyTitle = (TextView)findViewById(R.id.payment_one_buy_title);
		_OneBuyButton = (ImageView)findViewById(R.id.payment_one_buy_button);
		_OneBuyButton.setOnClickListener(mViewOnClickListener);
		_AllBuyButton = (ImageView)findViewById(R.id.payment_all_buy_button);
		_AllBuyButton.setOnClickListener(mViewOnClickListener);
		_CloseButton = (ImageView)findViewById(R.id.payment_close);
		_CloseButton.setOnClickListener(mViewOnClickListener);	
		_AllPlayItemCountTitle = (TextView)findViewById(R.id.payment_all_buy_price_title);
		_AllBuyPriceText	= (SeparateTextView)findViewById(R.id.payment_all_buy_base);
		_OneBuyPriceText	= (SeparateTextView)findViewById(R.id.payment_one_buy_base);
		
		initFont();

	}
	
	private void initFont()
	{
		Font font = Font.getInstance(mContext);
		((TextView)findViewById(R.id.payment_one_buy_base)).setTypeface(font.getRobotoBold());
		((TextView)findViewById(R.id.payment_one_buy_title)).setTypeface(font.getRobotoBold());
		((TextView)findViewById(R.id.payment_all_buy_name)).setTypeface(font.getRobotoBold());
		((TextView)findViewById(R.id.payment_one_buy_name)).setTypeface(font.getRobotoBold());
		((TextView)findViewById(R.id.payment_all_buy_base)).setTypeface(font.getRobotoBold());
		((TextView)findViewById(R.id.payment_one_buy_base)).setTypeface(font.getRobotoBold());
		((TextView)findViewById(R.id.payment_all_buy_price_title)).setTypeface(font.getRobotoBold());
	}
	
	
	public void initBGImage(Drawable bgImage)
	{
		_BaseLayout.setBackground(bgImage);
	}
	
	
	public void initPriceTitle(String title)
	{
		_AllPlayItemCountTitle.setText(title);	
	}

	private View.OnClickListener mViewOnClickListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			int id = v.getId();
			if (id == R.id.payment_one_buy_button)
			{
				mDialogListener.onItemClick(Common.DIALOG_MESSAGE_PAYMENT_ONE_PAY_CLICK, null);
				dismiss();
			} else if (id == R.id.payment_all_buy_button)
			{
				mDialogListener.onItemClick(Common.DIALOG_MESSAGE_PAYMENT_ALL_PAY_CLICK, null);
				dismiss();
			} else if (id == R.id.payment_close)
			{
				dismiss();
			}
		}
		
	};
	
	private String[] getPriceInformationText(String price)
	{
		String[] priceInformation = new String[2];
		priceInformation[0] = price.substring(0, 1)+" ";
		priceInformation[1] = price.substring(1, price.length());
		
		return  priceInformation;
	}

}
