package com.example.ceedlive.dday;

public class Constant {

    public static final int LOADING_DELAY_MILLIS = 2000;

    /**
     * 인텐트: 메인화면 리퀘스트 코드
     */
    public static final int REQUEST_CODE_MAIN_ACTIVITY = 10;

    /**
     * 인텐트: 상세화면(상단 노티바 고정 체크 시) 리퀘스트 코드
     */
    public static final int REQUEST_CODE_DETAIL_ACTIVITY_NOTIFICATION = 21;

    public static final String SHARED_PREFERENCES_NAME = "ceedliveAppDday";

    public static final String SHARED_PREFERENCES_KEY_PREFIX = "ceedlive.dday";

    public static final String INTENT_DATA_NAME_SHARED_PREFERENCES = "sharedPreferencesDataKey";

    public static final String CALENDAR_STRING_FORMAT_SLASH = "%d/%d/%d";

    public static final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
}
