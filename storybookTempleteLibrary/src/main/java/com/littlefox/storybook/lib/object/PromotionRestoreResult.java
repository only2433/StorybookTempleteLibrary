package com.littlefox.storybook.lib.object;

import java.util.ArrayList;

public class PromotionRestoreResult
{
	public ArrayList<PromotionInformationResult> list = new ArrayList<PromotionInformationResult>();
	
	/**
	 * 응답 코드
	 */
	String code = "";
	
	/**
	 * 응답 메세지
	 */
	String message = "";
}
