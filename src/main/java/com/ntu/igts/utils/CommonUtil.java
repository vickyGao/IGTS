package com.ntu.igts.utils;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

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

    public static boolean isLegalMoneyNumber(double money) {
        if (money > 0) {
            BigDecimal bigDecimal = new BigDecimal(money);
            double formattedMondy = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (formattedMondy == money) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getEntityFromResponse(Response response, Class<?> clazz) {
        String returnEntityJson = response.readEntity(String.class);
        return (T) JsonUtil.getPojoFromJsonString(returnEntityJson, clazz);
    }

    public static String getFormattedToken(String token) {
        if (!StringUtil.isEmpty(token)) {
            if (token.length() > 36) {
                token = token.replaceAll("\"", "");
            }
        }
        return token;
    }

    /**
     * Delete an empty dir
     * 
     * @param dir
     */
    public static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * Recurrence and delete folder together with files under it
     * 
     * @param dir
     * 
     * @return boolean Returns "true" if all deletions were successful. If a deletion fails, the method stops attempting
     *         to delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
