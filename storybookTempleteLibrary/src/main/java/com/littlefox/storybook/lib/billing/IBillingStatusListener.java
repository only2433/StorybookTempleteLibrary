package com.littlefox.storybook.lib.billing;

import com.android.vending.billing.util.IabResult;

public interface IBillingStatusListener
{
	public void inFailure(int status, String reason);

	public void OnIabSetupFinished(IabResult result);

	public void onQueryInventoryFinished(IabResult result);

	public void onIabPurchaseFinished(IabResult result);

	public void onConsumeFinished(IabResult result);
}
