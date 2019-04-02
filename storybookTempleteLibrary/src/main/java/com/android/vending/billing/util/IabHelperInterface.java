package com.android.vending.billing.util;

import java.util.List;

import android.app.Activity;
import android.content.Intent;

import com.android.vending.billing.util.IabHelper.OnConsumeFinishedListener;
import com.android.vending.billing.util.IabHelper.OnConsumeMultiFinishedListener;
import com.android.vending.billing.util.IabHelper.OnIabPurchaseFinishedListener;
import com.android.vending.billing.util.IabHelper.OnIabSetupFinishedListener;
import com.android.vending.billing.util.IabHelper.QueryInventoryFinishedListener;

public interface IabHelperInterface {

	/**
	 * Enables or disable debug logging through LogCat.
	 */
	public abstract void enableDebugLogging(boolean enable, String tag);

	public abstract void enableDebugLogging(boolean enable);

	/**
	 * Starts the setup process. This will start up the setup process asynchronously.
	 * You will be notified through the listener when the setup process is complete.
	 * This method is safe to call from a UI thread.
	 *
	 * @param listener The listener to notify when the setup process is complete.
	 */
	public abstract void startSetup(final OnIabSetupFinishedListener listener);

	/**
	 * Dispose of object, releasing resources. It's very important to call this
	 * method when you are done with this object. It will release any resources
	 * used by it such as service connections. Naturally, once the object is
	 * disposed of, it can't be used again.
	 */
	public abstract void dispose();

	/** Returns whether subscriptions are supported. */
	public abstract boolean subscriptionsSupported();

	public abstract void launchPurchaseFlow(Activity act, String sku,
			int requestCode, OnIabPurchaseFinishedListener listener);

	public abstract void launchPurchaseFlow(Activity act, String sku,
			int requestCode, OnIabPurchaseFinishedListener listener,
			String extraData);

	public abstract void launchSubscriptionPurchaseFlow(Activity act,
			String sku, int requestCode, OnIabPurchaseFinishedListener listener);

	public abstract void launchSubscriptionPurchaseFlow(Activity act,
			String sku, int requestCode,
			OnIabPurchaseFinishedListener listener, String extraData);

	/**
	 * Initiate the UI flow for an in-app purchase. Call this method to initiate an in-app purchase,
	 * which will involve bringing up the Google Play screen. The calling activity will be paused while
	 * the user interacts with Google Play, and the result will be delivered via the activity's
	 * {@link android.app.Activity#onActivityResult} method, at which point you must call
	 * this object's {@link #handleActivityResult} method to continue the purchase flow. This method
	 * MUST be called from the UI thread of the Activity.
	 *
	 * @param act The calling activity.
	 * @param sku The sku of the item to purchase.
	 * @param itemType indicates if it's a product or a subscription (ITEM_TYPE_INAPP or ITEM_TYPE_SUBS)
	 * @param requestCode A request code (to differentiate from other responses --
	 *     as in {@link android.app.Activity#startActivityForResult}).
	 * @param listener The listener to notify when the purchase process finishes
	 * @param extraData Extra data (developer payload), which will be returned with the purchase data
	 *     when the purchase completes. This extra data will be permanently bound to that purchase
	 *     and will always be returned when the purchase is queried.
	 */
	public abstract void launchPurchaseFlow(Activity act, String sku,
			String itemType, int requestCode,
			OnIabPurchaseFinishedListener listener, String extraData);

	/**
	 * Handles an activity result that's part of the purchase flow in in-app billing. If you
	 * are calling {@link #launchPurchaseFlow}, then you must call this method from your
	 * Activity's {@link android.app.Activity@onActivityResult} method. This method
	 * MUST be called from the UI thread of the Activity.
	 *
	 * @param requestCode The requestCode as you received it.
	 * @param resultCode The resultCode as you received it.
	 * @param data The data (Intent) as you received it.
	 * @return Returns true if the result was related to a purchase flow and was handled;
	 *     false if the result was not related to a purchase, in which case you should
	 *     handle it normally.
	 */
	public abstract boolean handleActivityResult(int requestCode,
			int resultCode, Intent data);

	public abstract Inventory queryInventory(boolean querySkuDetails,
			List<String> moreSkus) throws IabException;

	/**
	 * Queries the inventory. This will query all owned items from the server, as well as
	 * information on additional skus, if specified. This method may block or take long to execute.
	 * Do not call from a UI thread. For that, use the non-blocking version {@link #refreshInventoryAsync}.
	 *
	 * @param querySkuDetails if true, SKU details (price, description, etc) will be queried as well
	 *     as purchase information.
	 * @param moreItemSkus additional PRODUCT skus to query information on, regardless of ownership. 
	 *     Ignored if null or if querySkuDetails is false.
	 * @param moreSubsSkus additional SUBSCRIPTIONS skus to query information on, regardless of ownership. 
	 *     Ignored if null or if querySkuDetails is false.
	 * @throws IabException if a problem occurs while refreshing the inventory.
	 */
	public abstract Inventory queryInventory(boolean querySkuDetails,
			List<String> moreItemSkus, List<String> moreSubsSkus)
			throws IabException;

	/**
	 * Asynchronous wrapper for inventory query. This will perform an inventory
	 * query as described in {@link #queryInventory}, but will do so asynchronously
	 * and call back the specified listener upon completion. This method is safe to
	 * call from a UI thread.
	 *
	 * @param querySkuDetails as in {@link #queryInventory}
	 * @param moreSkus as in {@link #queryInventory}
	 * @param listener The listener to notify when the refresh operation completes.
	 */
	public abstract void queryInventoryAsync(final boolean querySkuDetails,
			final List<String> moreSkus,
			final QueryInventoryFinishedListener listener);

	public abstract void queryInventoryAsync(
			QueryInventoryFinishedListener listener);

	public abstract void queryInventoryAsync(boolean querySkuDetails,
			QueryInventoryFinishedListener listener);

	/**
	 * Asynchronous wrapper to item consumption. Works like {@link #consume}, but
	 * performs the consumption in the background and notifies completion through
	 * the provided listener. This method is safe to call from a UI thread.
	 *
	 * @param purchase The purchase to be consumed.
	 * @param listener The listener to notify when the consumption operation finishes.
	 */
	public abstract void consumeAsync(Purchase purchase,
			OnConsumeFinishedListener listener);

	/**
	 * Same as {@link consumeAsync}, but for multiple items at once.
	 * @param purchases The list of PurchaseInfo objects representing the purchases to consume.
	 * @param listener The listener to notify when the consumption operation finishes.
	 */
	public abstract void consumeAsync(List<Purchase> purchases,
			OnConsumeMultiFinishedListener listener);

}