package net.dev.alts.utils;

import net.dev.alts.encrypt.*;

import java.io.*;
import java.util.*;

public class UserInfoUtils {
    public static void creatProperties() throws IOException{
        File file = new File(System.getProperty("user.dir"),File.separator+"userinfo.properties");
        if (!file.exists()) {
            Properties properties = new Properties();
            properties.setProperty("username", "");
            properties.setProperty("password", "");
            Set<Map.Entry<Object,Object>> entrys = properties.entrySet();
            properties.store(new FileWriter(System.getProperty("user.dir")+File.separator+"userinfo.properties"), "userinfo");
        }
    }

    public static void setusername(String username) throws IOException{
        Properties properties = new Properties();
        properties.load(new FileReader(System.getProperty("user.dir")+File.separator+"userinfo.properties"));
        properties.setProperty("username",username);
        properties.store(new FileWriter(System.getProperty("user.dir")+File.separator+"userinfo.properties"), "userinfo");
    }

    public static void setpassword(String password) throws IOException{
        Properties properties = new Properties();
        properties.load(new FileReader(System.getProperty("user.dir")+File.separator+"userinfo.properties"));
        properties.setProperty("password", AESUtils.EnCode(MD5Utils.get32MD5Codes(password)));
        properties.store(new FileWriter(System.getProperty("user.dir")+File.separator+"userinfo.properties"), "userinfo");
    }
    public static String getUserName(){
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(System.getProperty("user.dir")+File.separator+"userinfo.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("username");
    }

    public static String getPassWord(){
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(System.getProperty("user.dir")+File.separator+"userinfo.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String Password = properties.getProperty("password");
        String AES = AESUtils.DeCode(Password);
        return AES;
    }
}
