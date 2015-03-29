package com.ntu.igts.constants;

import java.util.Locale;

public class Constants {

    /** Field name id used in BaseModel */
    public static String FIELD_ID = "id";
    /** Field name deletedYN used in BaseModel */
    public static String FIELD_DELETED_YN = "deletedYN";

    /** Filed name userId */
    public static String FIELD_USERID = "userId";

    /** I18N */
    public static final String I18N_LOCALE_ATTRIBUTE = "request-locale";
    public static final String I18N_USER_LOCALE = "i18n.user.locale";
    public static final String I18N_DEFAULT_LANGUAGE = "zh-CN";
    public static final Locale I18N_DEFAULT_BUNDLE_LOCALE = Locale.ROOT;
    public static final String I18N_BUNDLE_BASE_NAME = "messages";

    /** HEADER */
    public static final String HEADER_X_AUTH_HEADER = "x-auth-token";

    /** Roles */
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    /** Time format */
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** Configuration file */
    public static final String MGMT_PROPS_FILE = "igts.properties";
    public static final String IMAGE_STORAGE_BASE_PATH = "igts.image.location";
}
