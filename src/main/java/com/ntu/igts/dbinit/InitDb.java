package com.ntu.igts.dbinit;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.exception.ServiceErrorException;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.ConfigManagmentUtil;
import com.ntu.igts.utils.JdbcUtil;

public class InitDb {

    private static Logger logger = Logger.getLogger(InitDb.class);

    public static void main(String[] args) {
        deleteDatabase();
        deleteIndex();
        createDatabase();
    }

    public static void deleteDatabase() {
        String sql = "DROP DATABASE `idle_goods_trading_system`";
        try {
            Connection conn = JdbcUtil.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            logger.info("Deleted database idle_goods_trading_system");
        } catch (SQLException e) {
            logger.warn("Delete database idle_goods_trading_system failed");
        }
    }

    public static void createDatabase() {
        String sql = "CREATE DATABASE `idle_goods_trading_system`";
        try {
            Connection conn = JdbcUtil.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            logger.info("Created database idle_goods_trading_system");
        } catch (SQLException e) {
            logger.error("Create database idle_goods_trading_system failed", e);
            throw new ServiceErrorException("Create data base failed", e);
        }
    }

    public static void deleteIndex() {
        String path = ConfigManagmentUtil.getConfigProperties(Constants.INDEX_STORAGE_PATH);
        File existingFile = new File(path);
        if (existingFile != null && existingFile.exists()) {
            if (CommonUtil.deleteDir(existingFile)) {
                logger.info("Deleted index");
            }
        }
    }
}
