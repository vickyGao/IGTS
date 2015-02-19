package com.ntu.igts.jaxrs;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.utils.StringUtil;

@Provider
@PreMatching
public class I18NRequestFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(I18NRequestFilter.class);

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String language = StringUtil.EMPTY;
        if (requestContext != null) {
            language = requestContext.getHeaderString(Constants.I18N_LOCALE_ATTRIBUTE);
        }
        if (StringUtil.isEmpty(language)) {
            language = Constants.I18N_DEFAULT_LANGUAGE;
        }
        if (request != null) {
            HttpSession session = request.getSession();
            session.setAttribute(Constants.I18N_LOCALE_ATTRIBUTE, language);
            LOGGER.info("current language is " + language);
        }
        LOGGER.warn("put language into session failed");
    }

}
