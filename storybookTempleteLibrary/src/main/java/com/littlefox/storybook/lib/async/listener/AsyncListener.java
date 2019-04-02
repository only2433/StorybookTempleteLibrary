package com.littlefox.storybook.lib.async.listener;

public interface AsyncListener
{
	public void onRunningStart();
	public void onRunningEnd(Object mObject);
	public void onRunningCanceled();
	public void onRunningProgress(Integer progress);
	public void onErrorListener(String code, String message);
	
}
