package com.example.ceedlive.dday;

public class Constant {

    public static final int LOADING_DELAY_MILLIS = 3000;

    /**
     * 인텐트: 메인화면 리퀘스트 코드
     */
    public static final int REQUEST_CODE_MAIN_ACTIVITY = 10;

    public static final String SHARED_PREFERENCES_NAME = "ceedliveAppDday";

    public static final String SHARED_PREFERENCES_KEY_PREFIX = "ceedlive.dday";

    public static final String INTENT_DATA_NAME_SHARED_PREFERENCES = "sharedPreferencesDataKey";

    public static final String INTENT_DATA_SQLITE_TABLE_DDAY_ID = "dday_id";

    public static final String CALENDAR_STRING_FORMAT_SLASH = "%d/%d/%d";

    public static final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

    public static final String SQLITE_DB_FILE_NAME = "dday.db";

    public static final int SQLITE_DB_VERSION = 2;

    public static class NOTIFICATION {
        public static final int UNREGISTERED = 0;
        public static final int REGISTERED = 1;

        public static final boolean TO_BE_NOTIFIED = true;
        public static final boolean TO_BE_CANCELLED = false;
    }

    public static class FLOATING_ACTION_BUTTON {
        public static final boolean VISIBLE = true;
        public static final boolean INVISIBLE = false;
    }

}
