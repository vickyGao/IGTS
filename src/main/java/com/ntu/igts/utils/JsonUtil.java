package com.ntu.igts.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ntu.igts.exception.JsonTransferException;

public class JsonUtil {

    private static final Logger LOGGER = Logger.getLogger(JsonUtil.class);

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    private JsonUtil() {
    }

    public static String getJsonStringFromPojo(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("transfer fail", e);
            throw new JsonTransferException("transfer fail");
        }
    }

    public static <T> T getPojoFromJsonString(String jsonString, Class<T> targetClass) {
        try {
            T object = objectMapper.readValue(jsonString, targetClass);
            return object;
        } catch (IOException e) {
            LOGGER.error("transfer fail", e);
            throw new JsonTransferException("transfer fail");
        }
    }
}
