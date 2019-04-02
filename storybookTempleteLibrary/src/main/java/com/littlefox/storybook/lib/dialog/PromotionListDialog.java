package com.littlefox.storybook.lib.dialog;

import java.util.ArrayList;

import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.adapter.PromotionListAdapter;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.Font;
import com.littlefox.storybook.lib.object.PromotionInformationResult;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class PromotionListDialog extends Dialog
{
	private Context mContext;
	private PromotionListAdapter mPromotionListAdapter;
	private ArrayList<PromotionInformationResult> mPromotionInformationResult;
	private TextView _TitleText;
	private ListView _ListView;
	private ImageView _ExitButton;
	private ImageView _ConfirmButton;
	private DialogListener mDialogListener;
	private int mCurrentSelectPosition = -1;
	public PromotionListDialog(Context context, ArrayList<PromotionInformationResult> promotionInformationResult, DialogListener dialogListener)
	{
		super(context, R.style.BackgroundDialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext						= context;
		mPromotionInformationResult 	= promotionInformationResult;
		mDialogListener 				= dialogListener;
		
		setContentView(R.layout.dialog_promotion_code_result_list);
		init();
	}
	
	private void init()
	{
		Log.i("");
		_TitleText = (TextView)findViewById(R.id.promotion_list_title);
		_TitleText.setTypeface(Font.getInstance(mContext).getRobotoBold());
		_ListView = (ListView)findViewById(R.id.promotion_listview);
		_ExitButton = (ImageView)findViewById(R.id.promotion_exit_button);
		_ExitButton.setOnClickListener(mViewClickListener);
		_ConfirmButton = (ImageView)findViewById(R.id.promotion_confirm);
		_ConfirmButton.setOnClickListener(mViewClickListener);
		mPromotionListAdapter = new PromotionListAdapter(mContext, mPromotionInformationResult);
		_ListView.setAdapter(mPromotionListAdapter);
		_ListView.setOnItemClickListener(mOnItemClickListener);
	}
	
	
	
	@Override
	public void show()
	{
		super.show();
		Log.i("");
	}



	private View.OnClickListener mViewClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			int id = v.getId();
			if (id == R.id.promotion_exit_button)
			{
				dismiss();
			} else if (id == R.id.promotion_confirm)
			{
				if(mCurrentSelectPosition == -1)
				{
					Toast.makeText(mContext, mContext.getResources().getString(R.string.promotion_choice_item),  Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(mPromotionInformationResult.get(mCurrentSelectPosition).isAlreadyHave == true)
					{
						Toast.makeText(mContext, mContext.getResources().getString(R.string.promotion_choice_item),  Toast.LENGTH_SHORT).show();
						return;
					}
					
					mDialogListener.onItemClick(Common.DIALOG_MESSAGE_PROMOTION_LIST_ITEM_CONFIRM, mCurrentSelectPosition);
					dismiss();
				}
			}
		}
		
	};
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			mCurrentSelectPosition = position;
			mPromotionListAdapter.setSelectItem(position);
		}
		
	};

}
