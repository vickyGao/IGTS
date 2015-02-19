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

import com.ntu.igts.exception.BaseException;
import com.ntu.igts.i18n.MessageBuilder;
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

        if (exception instanceof BaseException) {
            BaseException baseException = (BaseException) exception;
            String code = baseException.getCode();
            JSONObject responseJson = new JSONObject();
            String message = messageBuilder.buildMessage(code, baseException.getParam(), baseException.getMessage(),
                            locale);
            Status status = Status.INTERNAL_SERVER_ERROR;
            responseJson.put("type", "warning");
            responseJson.put("message", message);
            JSONObject moreJson = new JSONObject();
            Collection<?> details = baseException.getDetails();
            JSONArray detailJsonArray = new JSONArray();
            detailJsonArray.addAll(details);
            moreJson.put("detail", detailJsonArray);
            moreJson.put("cause", getExceptionStackTrace(exception));
            responseJson.put("more", moreJson);
            LOGGER.error(responseJson.toString());
            LOGGER.error(message, baseException);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).status(status).build();
        } else {
            Status status = Status.INTERNAL_SERVER_ERROR;
            JSONObject responseJson = new JSONObject();
            responseJson.put("type", "error");
            responseJson.put("message", exception.getMessage());
            responseJson.put("cause", getExceptionStackTrace(exception));
            LOGGER.error(responseJson.toString());
            LOGGER.warn(exception.getMessage(), exception);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).status(status).build();
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
