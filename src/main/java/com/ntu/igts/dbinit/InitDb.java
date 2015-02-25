package com.ntu.igts.dbinit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.ntu.igts.utils.JdbcUtil;

public class InitDb {

    private static Logger logger = Logger.getLogger(InitDb.class);

    public static void main(String[] args) {
        createDatabase();
    }

    private static void createDatabase() {
        String sql = "CREATE DATABASE `idle_goods_trading_system`";
        try {
            Connection conn = JdbcUtil.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            logger.info("Created database idle_goods_trading_system");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
