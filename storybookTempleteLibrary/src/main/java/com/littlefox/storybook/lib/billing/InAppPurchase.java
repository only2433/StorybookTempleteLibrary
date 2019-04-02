package com.littlefox.storybook.lib.billing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabHelper.OnConsumeFinishedListener;
import com.android.vending.billing.util.IabHelper.OnIabPurchaseFinishedListener;
import com.android.vending.billing.util.IabHelper.OnIabSetupFinishedListener;
import com.android.vending.billing.util.IabHelper.QueryInventoryFinishedListener;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.api.StorybookTempleteAPI;

import android.app.Activity;
import android.content.Context;


/**
 *  앱결제를 위한 Helper 클래스
 * @author 정재현
 *
 */
public class InAppPurchase
{

	public static final int STATUS_SET_UP_FINISHED = 0;
	public static final int STATUS_QUERY_INVENTORY_FINISHED = 1;
	public static final int STATUS_PURCHASE_FINISHED = 2;
	public static final int STATUS_CONSUME_FINISHED = 3;

	public static final String ERROR_MESSAGE_DEVELOPER_PAYLOAD = "Current Payload is Developer.";


	/**
	 * 구매 흐름에 관한 Request Code
	 */
	public static final int REQUEST_CODE = 10001;

	private static InAppPurchase sInAppPurchase = null;

	/**
	 * 결제를 도와주는 Helper Class
	 */
	private IabHelper mIabHelper = null;



	/**
	 * 현재 서버에 등록된 상품 코드 리스트
	 */
	private ArrayList<String> mServerProductCodeList = new ArrayList<String>();

	/**
	 * 결제 관련 정보를 리턴하는 Listener
	 */
	private IBillingStatusListener mIBillingStatusListener;

	/**
	 * 현재 Google에서 받아온 Inventory 정보
	 */
	private Inventory mInventory = null;

	/**
	 * Developer 결제 테스트 계정인지 확인 하기 위해 사용
	 */
	private String mPayLoad = null;

	private Context mContext;
	
	/**
	 * 현재 결제 하는 Sku
	 */
	private String mCurrentPaySku ="";

	public static InAppPurchase getInstance()
	{
		if (sInAppPurchase == null)
		{
			sInAppPurchase = new InAppPurchase();
		}

		return sInAppPurchase;
	}



	public IabHelper getInAppHelper()
	{
		return mIabHelper;
	}

	/**
	 * 인앱 결제 초기화
	 */
	public void init(Context context)
	{
		mContext = context;
		mPayLoad = UUID.randomUUID().toString();
		mIabHelper = new IabHelper(mContext, StorybookTempleteAPI.IN_APP_BILLING_KEY);
		mIabHelper.enableDebugLogging(false);
		mServerProductCodeList = new ArrayList<String>();
	}
	
	/**
	 * 구글에 등록되어 있는 결제 정보를 받기위해 호출
	 */
	public void settingPurchaseInforamtionToGoogle()
	{
		try
		{
			if(mIabHelper!= null)
			{
				mIabHelper.startSetup(mOnIabSetupFinishedListener);
			}
			
		}catch(IllegalStateException e)
		{
			
		}
		
	}
	
	public List<String> getOwnedPurchaseItemList()
	{
		if(mInventory != null)
		{
			return mInventory.getAllOwnedSkus();
		}
		
		return null;
	}

	/**
	 * 등록된 상품코드 리스트를 세팅한다.
	 * 
	 * @param productList
	 */
	public void setServerProductList(ArrayList<String> productList)
	{
		mServerProductCodeList = productList;
	}
	
	/**
	 * 등록된 상품가격을 알아오기 위해 사용
	 * @param productCode
	 */
	public void setServerProduct(String productCode)
	{
		Log.i("productCode : "+productCode.trim());
		mServerProductCodeList.add(productCode.trim());
	}

	

	/**
	 * 아이템 구매를 요청한다.
	 * 
	 * @param activity
	 *            구매 요청할 ACTIVITY
	 * @param skuCode
	 *            구매할 상품 코드
	 */
	public void purchaseItem(Activity activity, String skuCode)
	{
		mCurrentPaySku = skuCode;
		
		Log.f("결제 skuCode : "+skuCode);
		if (mIabHelper == null)
		{
			mIBillingStatusListener.inFailure(STATUS_QUERY_INVENTORY_FINISHED, mContext.getResources().getString(R.string.product_is_null));
			return;
		}

		if (mInventory == null)
		{
			mIBillingStatusListener.inFailure(STATUS_QUERY_INVENTORY_FINISHED, mContext.getResources().getString(R.string.product_is_null));
			return;
		}

		if (mInventory.hasPurchase(skuCode))
		{
			mIBillingStatusListener.inFailure(STATUS_QUERY_INVENTORY_FINISHED, mContext.getResources().getString(R.string.product_already_paid));
			return;
		}
		
		if(mIabHelper != null)
		{
			try
			{
				mIabHelper.launchPurchaseFlow(activity, skuCode,IabHelper.ITEM_TYPE_INAPP, REQUEST_CODE, mOnIabPurchaseFinishedListener, mPayLoad);

			}catch(Exception e)
			{
				mIabHelper.flagEndAsync();
				mIabHelper.launchPurchaseFlow(activity, skuCode,IabHelper.ITEM_TYPE_INAPP, REQUEST_CODE, mOnIabPurchaseFinishedListener, mPayLoad);
			}
		}
		
		
	}
	
	public Inventory getInventory()
	{
		return mInventory;
	}
	
	public String getCurrentPaySku()
	{
		return mCurrentPaySku;
	}

	public void setOnBillingStatusListener(IBillingStatusListener IBillingStatusListener)
	{
		mIBillingStatusListener = IBillingStatusListener;
	}

	/**
	 * Billing 결제를 위한 초반 setup Listener. 한번만 호출하면 된다.
	 */
	private IabHelper.OnIabSetupFinishedListener mOnIabSetupFinishedListener = new OnIabSetupFinishedListener()
	{

		@Override
		public void onIabSetupFinished(IabResult result)
		{
			Log.i("");

			if (mIabHelper == null)
			{
				mIBillingStatusListener.inFailure(STATUS_SET_UP_FINISHED, mContext.getResources().getString(R.string.product_is_null));
			}

			if (result.isSuccess() == true)
			{
				
				mIBillingStatusListener.OnIabSetupFinished(result);
				
				List<String> list = new ArrayList<String>();
				list.add(StorybookTempleteAPI.ONE_SKU_ITEM_NAME);
				list.add(StorybookTempleteAPI.ALL_SKU_ITEM_NAME);

				mIabHelper.queryInventoryAsync(true, list, mQueryInventoryFinishedListener);
				
			}
			else
			{
				mIBillingStatusListener.inFailure(STATUS_SET_UP_FINISHED, result.getMessage());
			}
		}

	};

	/**
	 * 등록한 상품에 대한 정보를 받아올 때 사용하는 Listener.
	 */
	private IabHelper.QueryInventoryFinishedListener mQueryInventoryFinishedListener = new QueryInventoryFinishedListener()
	{

		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inventory)
		{
			Log.i("result.isSuccess() : "+result.isSuccess() +", mIabHelper : "+mIabHelper);

			if (mIabHelper == null)
			{
				mIBillingStatusListener.inFailure(STATUS_QUERY_INVENTORY_FINISHED, mContext.getResources().getString(R.string.product_is_null));
			}

			if (result.isSuccess() == true)
			{
				mInventory = inventory;
				mIBillingStatusListener.onQueryInventoryFinished(result);
			}
			else
			{
				mIBillingStatusListener.inFailure(STATUS_QUERY_INVENTORY_FINISHED, result.getMessage());
			}
		}

	};

	/**
	 * 구매가 완료되었을 때 호출되는 Listener.
	 */
	private IabHelper.OnIabPurchaseFinishedListener mOnIabPurchaseFinishedListener = new OnIabPurchaseFinishedListener()
	{

		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase purchase)
		{
			Log.i("");
			if (mIabHelper == null)
			{
				mIBillingStatusListener.inFailure(STATUS_PURCHASE_FINISHED, mContext.getResources().getString(R.string.product_is_null));
			}

			if (result.isSuccess() == true)
			{
				Log.i("결제 완료 개발자 결제? : " +purchase.getDeveloperPayload()+" id : "+purchase.getOrderId()+", sku : "+purchase.getSku());
				Log.i("verityDeveloperPayload(purchase) : " +verityDeveloperPayload(purchase));
				if (verityDeveloperPayload(purchase) == true)
				{
					mIBillingStatusListener.inFailure(STATUS_PURCHASE_FINISHED, ERROR_MESSAGE_DEVELOPER_PAYLOAD);
				}
				else
				{
					mIBillingStatusListener.onIabPurchaseFinished(result);
				}

			}
			else
			{
				mIBillingStatusListener.inFailure(STATUS_PURCHASE_FINISHED, result.getMessage());
			}
		}

	};

	/**
	 * 소비되는 구매를 할려고 호출하는 Listener. 지속적으로 구매가능게하는 리스너
	 */
	private IabHelper.OnConsumeFinishedListener mOnConsumeFinishedListener = new OnConsumeFinishedListener()
	{

		@Override
		public void onConsumeFinished(Purchase purchase, IabResult result)
		{
			if (mIabHelper == null)
			{
				mIBillingStatusListener.inFailure(STATUS_CONSUME_FINISHED, mContext.getResources().getString(R.string.product_is_null));
			}

			if (result.isSuccess() == true)
			{
				mIBillingStatusListener.onConsumeFinished(result);
			}
			else
			{
				mIBillingStatusListener.inFailure(STATUS_CONSUME_FINISHED, result.getMessage());
			}
		}

	};

	/**
	 * 현재 결제하는 계정이 테스트 계정인지 확인.
	 * 
	 * @param purchase
	 * @return TRUE : 테스트 계정 , FALSE : 일반 계정
	 */
	private boolean verityDeveloperPayload(Purchase purchase)
	{
		if (purchase == null)
		{
			return false;
		}
		Log.i("mPayLoad : "+mPayLoad+", purchase.getDeveloperPayload() : "+purchase.getDeveloperPayload());
		/*if (mPayLoad.equals(purchase.getDeveloperPayload()) == true)
		{
			return true;
		}*/

		return false;
	}

}
