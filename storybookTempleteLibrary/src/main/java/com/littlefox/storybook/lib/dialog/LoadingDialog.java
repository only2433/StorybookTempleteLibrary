package com.littlefox.storybook.lib.dialog;


import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.storybook.lib.common.CommonUtils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class LoadingDialog extends Dialog
{
	private static final int[] FRAME_ANIMATION_LIST = {R.drawable.spinner, R.drawable.spinner_02, R.drawable.spinner_03, R.drawable.spinner_04, R.drawable.spinner_05, R.drawable.spinner_06};
	private static final int DURATION_FRAME = 50;
	public static final int TYPE_ROTATION 	= 0;
	public static final int TYPE_FRAME		= 1;
	private static final int DIALOG_SIZE = 150;
	private ProgressBar _LoadingImage;
	private int mProgressType = -1;
	private Context mContext;
	public LoadingDialog(Context context, int type)
	{
		super(context, R.style.TransparentProgressDialog);
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams winParams = getWindow().getAttributes();
		winParams.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(winParams);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		
		setContentView(R.layout.loading_dialog);
		mProgressType = type;
		_LoadingImage = (ProgressBar)findViewById(R.id.loading_image);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtils.getInstance(mContext).getPixel(DIALOG_SIZE), CommonUtils.getInstance(mContext).getPixel(DIALOG_SIZE));
		_LoadingImage.setLayoutParams(params);
	}
	@Override
	public void show()
	{
		super.show();
		
		AnimationDrawable frameDrawable;
		if(mProgressType == TYPE_ROTATION)
		{
		}
		else
		{
			frameDrawable = CommonUtils.getInstance(mContext).getFrameAnimation(FRAME_ANIMATION_LIST, true, DURATION_FRAME);
			_LoadingImage.setBackgroundDrawable(frameDrawable);
			frameDrawable.start();
		}
		
	}
	
	

}
