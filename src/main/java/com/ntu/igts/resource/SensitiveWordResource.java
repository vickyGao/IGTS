package com.ntu.igts.resource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.springframework.stereotype.Component;

import com.ntu.igts.i18n.MessageBuilder;

@Component
@Path("sensitiveword")
public class SensitiveWordResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private MessageBuilder messageBuilder;

}
