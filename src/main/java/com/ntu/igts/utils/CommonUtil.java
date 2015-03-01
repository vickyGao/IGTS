package com.ntu.igts.utils;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.RoleEnum;
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
}
