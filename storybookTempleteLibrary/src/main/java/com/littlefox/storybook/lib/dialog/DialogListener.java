package com.littlefox.storybook.lib.dialog;

public interface DialogListener
{
	/**
	 * Custom Dialog에서 사용하는 메소드 ( Flexible Dialog 를 제외한 Dialog)
	 * @param messageType Common에 명시되어있는 Dialog Status Type
	 * @param sendObject 보낼 객체
	 */
	public void onItemClick(int messageType, Object sendObject);
	
	/**
	 * Flexible Dialog에서 사용하는 메소드 
	 * @param messageButtonType 선택한 버튼 
	 * @param subMessageType 보낼 Dialog Status Type
	 * @param sendObject 보낼 객체
	 */
	public void onItemClick(int messageButtonType, int subMessageType, Object sendObject);
}
