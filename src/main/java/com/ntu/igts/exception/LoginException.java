package com.ntu.igts.exception;

import java.util.Collection;

public class LoginException extends BaseException {

    private static final long serialVersionUID = 3980977523323115923L;

    public LoginException(String message, String code, Collection<?> details) {
        super(message, code, details);
        // TODO Auto-generated constructor stub
    }

    public LoginException(String message, String code, Object[] param, Collection<?> details, Throwable cause) {
        super(message, code, param, details, cause);
        // TODO Auto-generated constructor stub
    }

    public LoginException(String message, String code, Object[] param, Collection<?> details) {
        super(message, code, param, details);
        // TODO Auto-generated constructor stub
    }

    public LoginException(String message, String code, Object[] param) {
        super(message, code, param);
        // TODO Auto-generated constructor stub
    }

    public LoginException(String message, String code) {
        super(message, code);
        // TODO Auto-generated constructor stub
    }

    public LoginException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

}
