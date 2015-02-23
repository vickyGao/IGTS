package com.ntu.igts.utils;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.model.Role;

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

    public static boolean isRoleAllowed(List<Role> roles, String[] allowedRoles) {
        for (String allowdRole : allowedRoles) {
            if (roles.contains(allowdRole)) {
                return true;
            }
        }
        return false;
    }
}
