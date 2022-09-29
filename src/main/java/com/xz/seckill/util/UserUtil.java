package com.xz.seckill.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xz.seckill.pojo.User;
import com.xz.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户工具类
 * @author xz
 */
public class UserUtil {
    public static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>();

        // 生成用户
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setUserId(String.valueOf(15688880000L + i));
            user.setPassword(MD5Util.md5("123456"));
            user.setNickname("user" + i);
            users.add(user);
        }

//        // 获取JDBC连接
//        Connection connection = getConn();
//        String sql = "INSERT INTO user (user_id, password, nickname) VALUES (?, ?, ?)";
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//
//        // sql
//        for (User user : users) {
//              preparedStatement.setString(1, user.getUserId());
//              preparedStatement.setString(2, user.getPassword());
//              preparedStatement.setString(3, user.getNickname());
//              preparedStatement.addBatch();
//        }
//        preparedStatement.executeBatch();
//        preparedStatement.clearParameters();
//        preparedStatement.close();
//        connection.close();

        // 登录, 生成 userTicket
        String httpUrl = "http://localhost:8080/login/doLogin";
        File file = new File("G:\\Java\\Jmeter\\Cookie\\createCookies.txt");
        if (file.exists()) {
            file.delete();
        }

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        for (User user : users) {
            URL url = new URL(httpUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            String params = "mobile=" + user.getUserId() + "&" + "password=" + "123456";
            outputStream.write(params.getBytes());
            outputStream.flush();

            InputStream inputStream = httpURLConnection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                byteArrayOutputStream.write(buff, 0, len);
            }
            inputStream.close();
            byteArrayOutputStream.close();

            String response = new String(byteArrayOutputStream.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);

            System.out.println("user:" + user.getUserId());

            String line = user.getUserId() + "," + MD5Util.md5("123456");
            raf.seek(raf.length());
            raf.write(line.getBytes());
            raf.write("\r\n".getBytes());
        }
        raf.close();
        System.out.println("over");
    }

    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://10.1.1.128:3306/seckill?useSSL=false";
        String username = "root";
        String password = "secret";
        String driver = "com.mysql.cj.jdbc.Driver";

        Class.forName(driver);

        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }
}
