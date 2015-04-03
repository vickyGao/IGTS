package com.ntu.igts.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.log4j.Logger;

public class MD5Util {
    private static final Logger LOGGER = Logger.getLogger(MD5Util.class);

    /**
     * Get MD5 for string
     * 
     * @param source
     *            The source string
     * @return The MD5 of the string
     */
    public static String getMd5(String source) {

        byte[] btInput = source.getBytes();
        return getMd5(btInput);
    }

    /**
     * Get MD5 for input stream
     * 
     * @param inputStream
     *            The source input stream
     * @return The MD5 of the input stream
     */
    public static String getMd5(InputStream inputStream) {
        byte[] btInput = toByteArray(inputStream);
        return getMd5(btInput);
    }

    /**
     * Get byte array for input stream
     * 
     * @param inputStream
     *            The source input stream
     * @return The byte array of input stream
     */
    public static byte[] toByteArray(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        try {
            while ((n = inputStream.read()) != -1) {
                outputStream.write(n);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Get MD5 for inputStream fail", e);
            return buffer;
        }
    }

    /**
     * Get MD5 for byte array
     * 
     * @param source
     *            The source byte array
     * @return The MD5 of the byte array
     */
    public static String getMd5(byte[] source) {
        // Transfer from byte to hexadecimal character
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
                                      // 用字节表示就是 16 个字节

            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
                                           // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                                           // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                                                         // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            return new String(str); // 换后的结果转换为字符串
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Get MD5 for byte array fail", e);
            return UUID.randomUUID().toString();
        }
    }
}
