package com.ntu.igts.jaxrs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.ntu.igts.exception.LoginException;
import com.ntu.igts.exception.ServiceErrorException;
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.exception.UnAuthenticationException;
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.SpringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Provider
public class ExceptionMapperSupport implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = Logger.getLogger(ExceptionMapperSupport.class);

    @Context
    private HttpServletRequest webRequest;

    @Override
    public Response toResponse(Exception exception) {
        MessageBuilder messageBuilder = SpringUtil.getBean(MessageBuilder.class);
        Locale locale = CommonUtil.getLocaleFromRequest(webRequest);

        if (exception instanceof ServiceErrorException) {
            ServiceErrorException serviceErrorException = (ServiceErrorException) exception;
            String code = serviceErrorException.getCode();
            JSONObject responseJson = new JSONObject();
            String message = messageBuilder.buildMessage(code, serviceErrorException.getParam(),
                            serviceErrorException.getMessage(), locale);
            Status status = Status.INTERNAL_SERVER_ERROR;
            responseJson.put("type", "warning");
            responseJson.put("message", message);
            JSONObject moreJson = new JSONObject();
            Collection<?> details = serviceErrorException.getDetails();
            JSONArray detailJsonArray = new JSONArray();
            detailJsonArray.addAll(details);
            moreJson.put("detail", detailJsonArray);
            moreJson.put("cause", getExceptionStackTrace(exception));
            responseJson.put("more", moreJson);
            LOGGER.error(responseJson.toString());
            LOGGER.error(message, serviceErrorException);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).status(status).build();
        } else if (exception instanceof ServiceWarningException) {
            ServiceWarningException serviceWarningException = (ServiceWarningException) exception;
            String code = serviceWarningException.getCode();
            JSONObject responseJson = new JSONObject();
            String message = messageBuilder.buildMessage(code, serviceWarningException.getParam(),
                            serviceWarningException.getMessage(), locale);
            Status status = Status.BAD_REQUEST;
            responseJson.put("type", "warning");
            responseJson.put("message", message);
            JSONObject moreJson = new JSONObject();
            Collection<?> details = serviceWarningException.getDetails();
            JSONArray detailJsonArray = new JSONArray();
            detailJsonArray.addAll(details);
            moreJson.put("detail", detailJsonArray);
            moreJson.put("cause", getExceptionStackTrace(exception));
            responseJson.put("more", moreJson);
            LOGGER.error(responseJson.toString());
            LOGGER.error(message, serviceWarningException);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).status(status).build();
        } else if (exception instanceof UnAuthenticationException) {
            Status status = Status.FORBIDDEN;
            String message = messageBuilder.buildMessage(MessageKeys.FORBIDDEN, "403 Forbidden", locale);
            LOGGER.warn(message, exception);
            return Response.ok(message, MediaType.TEXT_PLAIN).status(status).build();
        } else if (exception instanceof LoginException) {
            Status status = Status.UNAUTHORIZED;
            String message = messageBuilder.buildMessage(MessageKeys.USER_NAME_OR_PASSWORD_IS_WRONG,
                            "User Name or password is wrong", locale);
            LOGGER.warn(message, exception);
            return Response.ok(message, MediaType.TEXT_PLAIN).status(status).build();
        } else {
            Status status = Status.INTERNAL_SERVER_ERROR;
            String message = messageBuilder.buildMessage(MessageKeys.INTERNAL_SERVER_ERROR, "Internal server error",
                            locale);
            LOGGER.error(exception.getMessage(), exception);
            return Response.ok(message, MediaType.TEXT_PLAIN).status(status).build();
        }
    }

    private String getExceptionStackTrace(Exception exception) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e2) {
            return "bad getErrorInfoFromException";
        }

    }
}
