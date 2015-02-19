package com.ntu.igts.exception;

import java.util.Collection;

public class ServiceWarningException extends BaseException {

    private static final long serialVersionUID = 8613153537245959202L;

    public ServiceWarningException(String message, String code, Collection<?> details) {
        super(message, code, details);
        // TODO Auto-generated constructor stub
    }

    public ServiceWarningException(String message, String code, Object[] param, Collection<?> details, Throwable cause) {
        super(message, code, param, details, cause);
        // TODO Auto-generated constructor stub
    }

    public ServiceWarningException(String message, String code, Object[] param, Collection<?> details) {
        super(message, code, param, details);
        // TODO Auto-generated constructor stub
    }

    public ServiceWarningException(String message, String code) {
        super(message, code);
        // TODO Auto-generated constructor stub
    }

    public ServiceWarningException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public ServiceWarningException(String message, String code, Object[] param) {
        super(message, code, param);
        // TODO Auto-generated constructor stub
    }

}
