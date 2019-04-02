package com.littlefox.storybook.lib.download;

public interface DownloadPlayListener
{
	public void onCanceled();
	public void setMaxProgress(int maxProgress);
	public void downloadProgress(int progress);
	public void downloadComplete();
	public void playVideo();
}
