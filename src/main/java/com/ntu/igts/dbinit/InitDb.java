package com.ntu.igts.dbinit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.ntu.igts.exception.ServiceErrorException;
import com.ntu.igts.utils.JdbcUtil;

public class InitDb {

    private static Logger logger = Logger.getLogger(InitDb.class);

    public static void main(String[] args) {
        deleteDatabase();
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
}
