package com.littlefox.storybook.lib.common;

import java.io.File;

import android.os.Environment;

/**
 * @author 정재현
 *
 */
public class Common
{
	public static final int MINIMUM_DISPLAY_WIDTH		= 1024;
	public static final int TARGET_DISPLAY_WIDTH 		= 1920;

	
	public static final long MAXIMUM_LOG_FILE_SIZE = 1024 * 1024 * 2L;
	
	
	
    public static final String PACKAGE_KAKAO_TALK 	= "com.kakao.talk";
    public static final String PACKAGE_KAKAO_STORY 	= "com.kakao.story";
	
	public static final String URL_INIT_APP 	= "init_app";
	public static final String URL_PROMO_QUERY 	= "promo_query";
	public static final String URL_PROMO_USE	= "promo_use";
	public static final String URL_APP_START 	= "app_start";
	public static final String URL_STORY_LIST	= "story_list";
	public static final String URL_RESTORE_LIST = "restore_list";
	public static final String URL_STORE_USE	= "store_use";
	public static final String URL_SNS_STORY_DESC = "sns_story_desc";
	
	public static final String URL_MP4_GET_JSON 		= "http://www.littlefox.net/app/api/mobile_m3u8/MzAwMDUyMzY0MzAyMDIxREVWMjAxMzA3MjUxMzQyMDAwOTExNTZAMTQyMjMyNTMyNjQ4MTQzMjAxNTA5Mjh8fHxXfDgwMCoxMjA1fGtvfFVTfDYuNzl8MTc";
	public static final String URL_PLAY_COMPLTE			= "http://www.littlefox.net/app/api/mobile_studylog";
	public static final String COMMAND_MP4_INFORMATION 	= "mp4_info";
	public static final String COMMAND_PLAY_COMPLETE	= "play_complete";

	/**
	 * API 서버와 통신하기 위한 기본 URI
	 */
	public static final String BASE_URI 					= "https://app.littlefox.com/api_storybook/";
	

	
	

	
	//TEST
	//public static final String PATH_APP_ROOT			= MP4_FILE_PATH;
	public static final String PATH_THUMBNAIL			= "thumbnail/";

	public static final String FILE_NOT_USE_VIDEO_INFORMATION 	= "video_information.txt";
	
	/** 기존의 Video Information이 문제가 있어 쓰지않고 새로 파일을 저장하여 사용한다. 기존 video_information.txt을 가지고 있는 사람은 해당파일 삭제 및 데이터 삭제 , 캐쉬 삭제를 진행한다.*/
	public static final String FILE_NEW_VIDEO_INFORMATION = "video_information_new.txt";
	
	public static final String FILE_IAC_INFORMATION		= "sleep_iac_info.txt";
	public static final String FILE_VIBRATOR_INFORMATION = "vibrator_information.txt";
	
	public static final int TYPE_PARAMS_BOOLEAN = 0;
	public static final int TYPE_PARAMS_INTEGER = 1;
	public static final int TYPE_PARAMS_STRING	= 2;
	
	public static final String PARAMS_CURRENT_PLAY_POSITION 		= "current_play_position";
	public static final String PARAMS_GCM_REGISTERATION_ID 			= "gcm_registeration_id";
	public static final String PARAMS_REGISTER_APP_VERSION 			= "current_app_version";
	public static final String PARAMS_APP_USER_PK					= "app_user_pk";
	public static final String PARAMS_IN_APP_CAMPAIGN_PK 			= "in_app_campaign_pk";
	public static final String PARAMS_BEFORE_WATCHED_MOVIE			= "before_watched_movie";
	public static final String PARAMS_CAPTION_STATUS				= "caption_status";
	
	public static final String PARAMS_VIBRATOR_VERSION				= "vibrator_version";
	public static final String PARAMS_ACTION_STORY_MODE				= "action_story_mode";
	public static final String PARAMS_BACKGROUND_SOUND				= "background_sound";
	public static final String PARAMS_FREE_ITEM_COUNT				= "free_item_count";
	
	public static final String PARAMS_INIT_INFO						= "init_info";
	/** 플레이어에서 플레이 다된 포지션을 메인에게 전달하기 위해 */
	public static final String INTENT_PARAMS_PLAY_COMPLETE_POSITION = "play_complete_position";
	/** 별점주기 팝업띄우기를 하기위해 플레이한 포지션을 전달하기 위해 */
	public static final String INTENT_PARAMS_PLAYED_POSITION		= "played_position";
	
	public static String APP_USER_PK = "";
	

	
	/** 허용범위의 다운로드 가능 사이즈 */
	public static long AVAILABLE_DOWNLOAD_STORAGE_SIZE = 200;
	
	/** 프로모션 타입 전체 상품 */
	public static final int PROMOTION_PRODUCT_TYPE_ALL = 1;
	/** 프로모션 타입 고정 상품 */
	public static final int PROMOTION_PRODUCT_TYPE_FIX = 2;
	/** 프로모션 타입 유동 상품 */
	public static final int PROMOTION_PRODUCT_TYPE_FLUID = 3;
	
	/** 프로모션 코드 사용 정상 */
	public static final int PROMOTION_STATE_NORMAL 		= 1;
	/** 프로모션 코드 사용 기간 만료 */
	public static final int PROMOTION_STATE_END_TIME 	= 2;
	/** 프로모션 코드 사용 이미 사용 */
	public static final int PROMOTION_STATE_USE_BEFORE 	= 3;
	/** 프로모션 코드 사용 코드 에러 */
	public static final int PROMOTION_STATE_CODE_ERROR 	= 4;
	
	/** 메세지 타입 DialogListener 에서 사용 */
	public static final int DIALOG_MESSAGE_PROMOTION_CODE_CONFIRM 				= 0;
	public static final int DIALOG_MESSAGE_PROMOTION_LIST_ITEM_CONFIRM 			= 1;
	public static final int DIALOG_MESSAGE_PAYMENT_ONE_PAY_CLICK				= 2;
	public static final int DIALOG_MESSAGE_PAYMENT_ALL_PAY_CLICK				= 3;
	
	/** FlexiableDialog 에서 사용하는 이벤트 </p>
	 *  첫번째 버튼을 눌렀을때 이벤트를 받으면 FlexiableDialog </p>
	 *  내부에 있는 이벤트를 전달받아서 사용하여야한다.
	 */
	public static final int DIALOG_MESSAGE_FLAXIBLE_FIRST_BUTTON_CLICK			= 4;
	
	/** FlexiableDialog 에서 사용하는 이벤트 </p>
	 *  두번째 버튼을 눌렀을때 이벤트를 받으면 FlexiableDialog </p>
	 *  내부에 있는 이벤트를 전달받아서 사용하여야한다.
	 */
	public static final int DIALOG_MESSAGE_FLAXIBLE_SECOND_BUTTON_CLICK			= 5;
	
	
	public static final int DIALOG_MESSAGE_APPRAISAL_NEXT_NOT_AGAIN				= 6;
	public static final int DIALOG_MESSAGE_APPRAISAL_GO_RATE					= 7;
	public static final int DIALOG_MESSAGE_PARENT_CONFIRM						= 8;
	
	
	
	/** 초기에 다운로드 되지않고 바로볼수 있는 1편에 대한 String */
	public static final String PREVIEW_CAPTION 	= "caption/preview_play_01.json";
	public static final String PREVIEW_VIDEO	= "preview_play_01.mp4";
	
	public static final String PREVIEW_TEST_VIBRATOR = "vibrator/test_vibrator.json";

	
	/** 개발자 이메일 */
	public static final String DEVELOPER_EMAIL = "app.support@littlefox.com";
	
	/** 회사 링크 연결 */
	public static final String INC_LINK = "https://play.google.com/store/apps/developer?id=LITTLE+FOX+INC.";

	/**
	 * 16:9의 최소 테블릿 비율 
	 */
	public static final float MINIMUM_TABLET_DISPLAY_RADIO = 1.4f;
	
	
	public static final int ICE_CREAM_SANDWICH 	= 14;
	public static final int JELLYBEAN_CODE 		= 16;
	public static final int JELLYBEAN_CODE_4_3 	= 18;
    public static final int KITKAT				= 19;
    public static final int LOLLIPOP			= 21;
    public static final int MALSHMALLOW			= 23;
	
	/** Player의 종료시 상태값을 받아 화면 구성을 처리 해야하기 때문에 사용하는 Result */
	public static final int INTENT_RESULT_PLAY_EXIT = 100;
	
	/** Intro가 끝난 후 결과값을 받아서 화면 구성을 한다. */
	public static final int INTENT_RESULT_INTRO_COMPLETE = 101;
	
	/** IAC Button State */
	public static final String IAC_LINK_MOVE = "N";
	public static final String IAC_NEVER_SEE_AGAIN = "E"; 
	
	/** Thumbnail Info*/
	
	public static final int TABLET_THUMB_MAX_IN_LINE 	= 4;
	public static final int PHONE_THUMB_MAX_IN_LINE 	= 2;
	public static final int TABLET_TERM_FRAME_HEIGHT 	= 289;
	public static final int PHONE_TERM_FRAME_HEIGHT 	= 576;	

	public static final String PARAMS_DISPLAY_METRICS		= "display_metrics";
	
	/** 다운가능하게 만드는 아이템이 결제아이템 인지 프로모션 아이템인지의 여부 */
	public static final int ITEM_TYPE_PAID 		= 0;
	public static final int ITEM_TYPE_PROMOTION = 1;
	
	/**
	 * 업데이트 관련
	 */
	public static final String CRITICAL_UPDATE_KEY ="C";
	public static final String NORMAL_UPDATE_KEY = "N";
	
	public static final String BACK_PRESSED = "back_pressed";
	
	
	public static final String HTTP_HEADER_ANDROID	= "Android";
	
	
	/**
	 * 진동모드를 실행할것인가의 여부
	 */
	public static boolean IS_VIBRATE_ENABLE = false;
	
	/**
	 * 배경음을 실행 할 것인가의 여부
	 */
	public static boolean IS_BACKGROUND_SOUND_ENABLE = false;
	
	
	public static final String APP_KAKAO_STORY 	="KakaoStory";
	public static final String APP_FACEBOOK		="Facebook";
	
	public static String COUPON_TEXT_TO_WEB = "";
	
	public static final int STORY_TITLE_EPISODE = 0;
	public static final int STORY_TITLE_CHAPTER	= 1;
	
	/**
	 * 이전의 video_information 데이터가 오류가 있어 삭제 후 자동으로 사용자를 구매복원하는 변수
	 */
	public static boolean INIT_DATA_RESTORE = false;
	
}
