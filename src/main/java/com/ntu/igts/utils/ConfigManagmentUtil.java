package com.ntu.igts.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ntu.igts.constants.Constants;

public class ConfigManagmentUtil {

    private static final Logger LOGGER = Logger.getLogger(ConfigManagmentUtil.class);

    private static Properties configProperties;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        if (configProperties != null) {
            return;
        }
        // The path of configuration file
        String filePath = Constants.MGMT_PROPS_FILE;
        InputStream is = null;
        try {
            is = ConfigManagmentUtil.class.getClassLoader().getResourceAsStream(filePath);
            configProperties = new Properties();
            configProperties.load(is);
        } catch (FileNotFoundException e) {
            LOGGER.error("Can not find file " + filePath, e);
        } catch (IOException e1) {
            LOGGER.error("Failed to load " + filePath, e1);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LOGGER.error("Failed to close input stream");
            }
        }
    }

    public static String getConfigProperties(String key) {
        return configProperties.getProperty(key);
    }
}
