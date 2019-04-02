package com.littlefox.storybook.lib.object;

import java.util.ArrayList;

public class PromotionBaseResult
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
	
	/**
	 * 1: 전체 상품, 2: 고정 상품, 3: 유동 상품
	 */
	String promo_type = "";
	
	/**
	 * 전체 상품 일 경우 iap code
	 */
	String iap_code 	= "";
	
	/**
	 * 1: 정상, 2: 기간종료, 3: 이미사용 , 4: 코드 오류
	 */
	String code_state 	= "";
	
	/**
	 * 코드 메세지
	 */
	String code_msg 	= "";
	
	
	/**
	 * 1: 전체 상품, 2: 고정 상품, 3: 유동 상품
	 */
	public String getPromotype()
	{
		return promo_type;
	}

	/**
	 * 전체 상품 일 경우 iap code
	 */
	public String getIapcode()
	{
		return iap_code;
	}

	/**
	 * 1: 정상, 2: 기간종료, 3: 이미사용 , 4: 코드 오류
	 */
	public String getCodestate()
	{
		return code_state;
	}


	

}
