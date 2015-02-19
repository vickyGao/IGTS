package com.ntu.igts.constants;

import java.util.Locale;

public class Constants {

    /** Field name id used in BaseModel */
    public static String FIELD_ID = "id";
    /** Field name deletedYN used in BaseModel */
    public static String FIELD_DELETED_YN = "deletedYN";

    /** USER **/
    public static final String USER = "user";
    public static final String USER_LOGIN_TOKEN = "token";
    public static final String USER_IDENTIFIER = "useridentifier";

    /** TIME **/
    public static final String FMT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";

    /** PATH **/
    public static final String PATH_WADL = "application.wadl";
    public static final String PATH_LOGIN = "login";
    public static final String PATH_LOGOUT = "logout";

    /** I18N **/
    public static final String I18N_LOCALE_ATTRIBUTE = "request-locale";
    public static final String I18N_USER_LOCALE = "i18n.user.locale";
    public static final String I18N_DEFAULT_LANGUAGE = "zh-CN";
    public static final Locale I18N_DEFAULT_BUNDLE_LOCALE = Locale.ROOT;
    public static final String I18N_BUNDLE_BASE_NAME = "message";

    /** SESSION **/
    public static final String SESSION_USER_TOKEN = "session.user.token";

    /** HEADER **/
    public static final String HEADER_X_AUTH_HEADER = "x-auth-token";
}
