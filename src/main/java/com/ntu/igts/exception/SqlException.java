package com.ntu.igts.exception;

import java.util.Collection;

public class SqlException extends BaseException {

    private static final long serialVersionUID = 3975050811482297807L;

    public SqlException(String message, String code, Collection<?> details) {
        super(message, code, details);
        // TODO Auto-generated constructor stub
    }

    public SqlException(String message, String code, Object[] param, Collection<?> details, Throwable cause) {
        super(message, code, param, details, cause);
        // TODO Auto-generated constructor stub
    }

    public SqlException(String message, String code, Object[] param, Collection<?> details) {
        super(message, code, param, details);
        // TODO Auto-generated constructor stub
    }

    public SqlException(String message, String code, Object[] param) {
        super(message, code, param);
        // TODO Auto-generated constructor stub
    }

    public SqlException(String message, String code) {
        super(message, code);
        // TODO Auto-generated constructor stub
    }

    public SqlException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public SqlException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public SqlException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
