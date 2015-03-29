package com.ntu.igts.utils;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.model.Role;

public class CommonUtil {

    private static final Logger LOGGER = Logger.getLogger(CommonUtil.class);

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

    public static boolean isRoleAllowed(List<Role> roles, RoleEnum allowedRole) {
        if (roles != null) {
            for (Role role : roles) {
                if (allowedRole.value().equals(role.getRoleStandardName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getRequiredRoleIdFromRoles(List<Role> roles, RoleEnum requiredRole) {
        if (roles != null) {
            for (Role role : roles) {
                if (requiredRole.value().equals(role.getRoleStandardName())) {
                    return role.getId();
                }
            }
        }
        return StringUtil.EMPTY;
    }

    public static String getRandomNumber() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static synchronized String getIndentNumber() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        String indentNumber = String.valueOf(year) + String.valueOf(month) + String.valueOf(date)
                        + cal.getTimeInMillis();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            LOGGER.error("Stop thread for generating indent number failed", e);
        }
        return indentNumber;
    }
}
