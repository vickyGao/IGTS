package com.ntu.igts.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ntu.igts.constants.Constants;

public class CommonUtil {

    public static Locale getLocaleFromRequest(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession();
            if (session != null) {
                String language = (String) session.getAttribute(Constants.I18N_LOCALE_ATTRIBUTE);
                if (language != null) {
                    return Locale.forLanguageTag(language);
                }
            }
        }
        return Locale.forLanguageTag(Constants.I18N_DEFAULT_LANGUAGE);
    }
}
