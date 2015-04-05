package com.ntu.igts.resource;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sf.json.JSONObject;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.model.User;
import com.ntu.igts.services.UserService;
import com.ntu.igts.test.OrderedRunner;
import com.ntu.igts.utils.JsonUtil;
import com.ntu.igts.utils.StringUtil;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(OrderedRunner.class)
public abstract class TestBase extends JerseyTest {

    private static final String CONTAINER_GRIZZLY = "org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory";
    @Resource
    private UserService userService;

    protected static User user;
    protected static String userToken;
    protected static String userId;
    protected static String userName;
    protected static String adminToken;

    @Override
    protected Application configure() {
        set(TestProperties.CONTAINER_FACTORY, CONTAINER_GRIZZLY);
        return new ResourceConfig().packages("com.ntu.igts");
    }

    @Before
    public void Login() {

        StringBuilder postBody = new StringBuilder();
        postBody.append("{");
        postBody.append("   \"login\": {");
        postBody.append("       \"username\": \"user\",");
        postBody.append("       \"password\": \"password\"");
        postBody.append("   }");
        postBody.append("}");
        Response userLoginResponse = doPost("login/user", StringUtil.EMPTY, postBody.toString());
        Response adminLoginResponse = doPost("login/admin", StringUtil.EMPTY, postBody.toString());
        assertEquals("Login fail", Status.OK.getStatusCode(), userLoginResponse.getStatus());
        assertEquals("Login fail", Status.OK.getStatusCode(), adminLoginResponse.getStatus());
        String userLoginResponseJson = userLoginResponse.readEntity(String.class);
        String adminLoginResponseJson = adminLoginResponse.readEntity(String.class);
        SessionContext userSessionContext = JsonUtil.getPojoFromJsonString(userLoginResponseJson, SessionContext.class);
        SessionContext adminSessionContext = JsonUtil.getPojoFromJsonString(adminLoginResponseJson,
                        SessionContext.class);
        userToken = userSessionContext.getToken();
        userId = userSessionContext.getUserId();
        userName = userSessionContext.getUserName();
        adminToken = adminSessionContext.getToken();
    }

    protected void mockUpUser() {
        if (user == null) {
            String randomNumber = UUID.randomUUID().toString().replace("-", "");

            User testUser = new User();
            testUser.setUserName("testUser" + randomNumber);
            testUser.setPassword("password");
            User insertedUser = userService.create(testUser);
            assertNotNull("Create user failed", insertedUser);

            user = insertedUser;
        }
    }

    protected void tearDownUser() {
        if (user != null) {
            boolean flag = userService.delete(user.getId());
            assertTrue("Delete user failed", flag);
        }
    }

    protected Response doPost(String path, String token, String postBody) {
        return target(path).request().header(Constants.HEADER_X_AUTH_HEADER, token)
                        .post(Entity.entity(postBody, MediaType.APPLICATION_JSON_TYPE));
    }

    protected Response doPost(String path, String token) {
        return doPost(path, token, "");
    }

    protected Response doPost(String path, String token, JSONObject postBody) {
        return doPost(path, token, postBody.toString());
    }

    protected Response doPut(String path, String token, String postBody) {
        return target(path).request().header(Constants.HEADER_X_AUTH_HEADER, token)
                        .put(Entity.entity(postBody, MediaType.APPLICATION_JSON_TYPE));
    }

    protected Response doPut(String path, String token) {
        return doPut(path, token, "");
    }

    protected Response doPut(String path, String token, JSONObject postBody) {
        return doPut(path, token, postBody.toString());
    }

    protected Response doDelete(String path, String token) {
        return target(path).request().header(Constants.HEADER_X_AUTH_HEADER, token).delete();
    }

    protected Response doGet(String path, String token) {
        return target(path).request().header(Constants.HEADER_X_AUTH_HEADER, token).get();
    }

    protected Response doGetWithQueryParam(String path, String token, Map<String, String> queryParam) {
        WebTarget webTarget = target(path);
        for (Entry<String, String> entry : queryParam.entrySet()) {
            webTarget = webTarget.queryParam(entry.getKey(), entry.getValue());
        }
        return webTarget.request().header(Constants.HEADER_X_AUTH_HEADER, token).get();
    }
}
