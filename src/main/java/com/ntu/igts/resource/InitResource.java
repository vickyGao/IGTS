package com.ntu.igts.resource;

import java.util.Calendar;
import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ntu.igts.dbinit.InitData;

@Component
@Path("init")
public class InitResource {

    private static final Logger LOGGER = Logger.getLogger(InitResource.class);

    @Resource
    private InitData initData;

    @GET
    @Path("{inittype}")
    @Produces(MediaType.TEXT_PLAIN)
    public String initDb(@PathParam("inittype") String initType) {
        long startTime = System.currentTimeMillis();
        if ("SAMPLE".equals(initType)) {
            initData.createStandardData();
            initData.createSampleData();
        } else {
            initData.createStandardData();
        }
        long endTime = System.currentTimeMillis();
        long durationTime = endTime - startTime;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(durationTime);
        String message = "Init DB finished, total time consuming is " + cal.get(Calendar.MINUTE) + " minute(s) "
                        + cal.get(Calendar.SECOND) + " second(s)";
        LOGGER.info(message);
        return message;
    }
}
