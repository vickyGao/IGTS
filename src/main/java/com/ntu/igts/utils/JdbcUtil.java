package com.ntu.igts.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcUtil {
    private static String url;
    private static String username;
    private static String password;
    private static String driver;
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    static {
        Properties prop = new Properties();
        InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
        try {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        url = prop.getProperty("jdbc.url");
        username = prop.getProperty("username");
        password = prop.getProperty("password");
        driver = prop.getProperty("jdbc.driver");
    }

    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 让每个线程都对应唯一的一个Connection 对象
    public static Connection getConnection() {
        Connection conn = threadLocal.get();
        if (conn == null) {
            // 第一次启动该线程没有绑定conn
            try {
                conn = DriverManager.getConnection(url, username, password);
                // 将conn绑定到threadLocal中。
                threadLocal.set(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Statement state) {
        if (state != null) {
            try {
                state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void begin() {
        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void commit() {
        Connection conn = getConnection();
        try {
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback() {
        Connection conn = getConnection();
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        Connection conn = getConnection();
        JdbcUtil.close(conn);
        // 将该线程与所绑定的Connection对象分离
        threadLocal.remove();
    }
}
