package com.ntu.igts.exception;

import java.util.Collection;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -2403127005556057112L;

    private String code;
    private Object param[];
    private Collection<?> details;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, String code, Object[] param, Collection<?> details, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.param = param;
        this.details = details;
    }

    public BaseException(String message, String code, Object[] param, Collection<?> details) {
        super(message);
        this.code = code;
        this.param = param;
        this.details = details;
    }

    public BaseException(String message, String code, Collection<?> details) {
        super(message);
        this.code = code;
        this.details = details;
    }

    public BaseException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BaseException(String message, String code, Object[] param) {
        super(message);
        this.code = code;
        this.param = param;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object[] getParam() {
        return param;
    }

    public void setParam(Object[] param) {
        this.param = param;
    }

    public Collection<?> getDetails() {
        return details;
    }

    public void setDetails(Collection<?> details) {
        this.details = details;
    }
}
