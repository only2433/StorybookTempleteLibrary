package com.littlefox.storybook.lib.adapter;

import java.util.ArrayList;

import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.storybook.lib.common.Font;
import com.littlefox.storybook.lib.object.PromotionInformationResult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PromotionListAdapter extends BaseAdapter
{
	private static final int STATUS_SELECT 		= 0;
	private static final int STATUS_NOT_SELECT 	= 1;
	private static final int STATUS_DISABLE		= 2;
	private ArrayList<PromotionInformationResult> mPromotionInformationResultList = new ArrayList<PromotionInformationResult>();
	private ArrayList<PromotionAdapterObject> mPromotionAdapterObjectList = new ArrayList<PromotionAdapterObject>();
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	
	public PromotionListAdapter(Context context, ArrayList<PromotionInformationResult> promotionInformationResultList)
	{
		mContext		 		= context;
		mPromotionInformationResultList 	= promotionInformationResultList;
		mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		makePromotionAdapterObjectList();
	}
	
	private void makePromotionAdapterObjectList()
	{
		mPromotionAdapterObjectList.clear();
		for(int i = 0; i < mPromotionInformationResultList.size(); i++)
		{
			if(mPromotionInformationResultList.get(i).isAlreadyHave == true)
			{
				mPromotionAdapterObjectList.add(new PromotionAdapterObject(mPromotionInformationResultList.get(i).product_name, STATUS_DISABLE));

			}
			else
			{
				mPromotionAdapterObjectList.add(new PromotionAdapterObject(mPromotionInformationResultList.get(i).product_name, STATUS_NOT_SELECT));
			}
		}
	}
	@Override
	public int getCount()
	{
		return mPromotionInformationResultList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mPromotionInformationResultList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
		ViewHolder holder;
		
		if(convertView == null)
		{
			view = mLayoutInflater.inflate(R.layout.dialog_promotion_code_result_list_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)view.getTag();
		}
		
		holder.getPromotionItemText().setText(mPromotionAdapterObjectList.get(position).title);
		if(mPromotionAdapterObjectList.get(position).status == STATUS_SELECT)
		{
			holder.getPromotionItemRadioButton().setImageDrawable(mContext.getResources().getDrawable(R.drawable.radio_button_on_promotion));
		}
		else if(mPromotionAdapterObjectList.get(position).status == STATUS_NOT_SELECT)
		{
			holder.getPromotionItemRadioButton().setImageDrawable(mContext.getResources().getDrawable(R.drawable.radio_button_off_promotion));
		}else if(mPromotionAdapterObjectList.get(position).status == STATUS_DISABLE)
		{
			holder.getPromotionItemRadioButton().setImageDrawable(mContext.getResources().getDrawable(R.drawable.radio_button_no_check));
		}
		return view;
	}
	
	public void setSelectItem(int index)
	{
		for(int i = 0; i < mPromotionAdapterObjectList.size(); i++)
		{
			if(i == index)
			{
				if(mPromotionInformationResultList.get(i).isAlreadyHave == true)
				{
					mPromotionAdapterObjectList.get(i).status = STATUS_DISABLE;
				}
				else
				{
					mPromotionAdapterObjectList.get(i).status = STATUS_SELECT;
				}
				
			}
			else
			{
				if(mPromotionInformationResultList.get(i).isAlreadyHave == true)
				{
					mPromotionAdapterObjectList.get(i).status = STATUS_DISABLE;
				}
				else
				{
					mPromotionAdapterObjectList.get(i).status = STATUS_NOT_SELECT;
				}
				
			}
		}	
		notifyDataSetChanged();
	}
	
	class ViewHolder
	{
		View base;
		LinearLayout _PromotionItemLayout = null;
		TextView _PromotionItemText = null;
		ImageView _PromotionItemRadioButton = null;
		
		ViewHolder(View base)
		{
			this.base = base;
		}
		
		public LinearLayout getPromotionLayout()
		{
			if(_PromotionItemLayout == null)
			{
				_PromotionItemLayout = (LinearLayout)base.findViewById(R.id.promotion_list_item_layout);
			}
			return _PromotionItemLayout;
		}
		
		public TextView getPromotionItemText()
		{
			if(_PromotionItemText == null)
			{
				Font font = Font.getInstance(mContext);
				_PromotionItemText = (TextView)base.findViewById(R.id.promotion_list_item_text);
				_PromotionItemText.setTypeface(font.getRobotoRegular());
			}
			return _PromotionItemText;
		}
		
		public ImageView getPromotionItemRadioButton()
		{
			if(_PromotionItemRadioButton == null)
			{
				_PromotionItemRadioButton = (ImageView)base.findViewById(R.id.promotion_list_item_radio_button);
			}
			return _PromotionItemRadioButton;
		}
	}
	
	class PromotionAdapterObject 
	{
		
		String title;
		int status;
		
		public PromotionAdapterObject(String title, int status)
		{
			this.title = title;
			this.status = status;
		}
	}

}
