package com.ntu.igts.services;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.ntu.igts.test.OrderedRunner;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(OrderedRunner.class)
public abstract class TestBase extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig().packages("com.ntu.igts");
    }
}
