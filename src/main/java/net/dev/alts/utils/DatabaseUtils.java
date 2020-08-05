package net.dev.alts.utils;

import com.alibaba.fastjson.*;
import net.dev.alts.*;
import net.dev.alts.database.*;
import net.dev.alts.encrypt.*;
import net.dev.alts.user.*;

import java.util.*;

public class DatabaseUtils {
    public static DataBase db;
    public static void loadDatabase(){
        db=DataBase.create("hostname",3306,"163alts","username","password");
    }
    public static boolean loginAuthUsername(String username, String password, boolean md5){
        String userinfo = db.dbSelectFirst("userinfo","userinfo",new KeyValue(){{ this.add("username",username); }});
        UserInfo user1 = JSON.parseObject(userinfo, UserInfo.class);
        try{
            boolean suc;
            if(md5)
                suc = user1.getPassword().equals(MD5Utils.get32MD5Codes(password));
            else
                suc = user1.getPassword().equals(password);
            return suc;
        }catch (Throwable e){
            return false;
        }
    }
    public static boolean loginAuthEmail(String email, String password, boolean md5){
        try{
            return loginAuthUsername(getRegisterEmailUserName(email),password,md5);
        }catch (Throwable e){
            return false;
        }
    }
    public static boolean hasUser(String username){
        String user = db.dbSelectFirst("userinfo","userinfo",new KeyValue(){{ this.add("username",username); }});
        return user != null;
    }
    public static boolean hasEmail(String email){
        String emails = db.dbSelectFirst("regemails","username",new KeyValue(){{ this.add("email",email); }});
        return emails != null;
    }
    public static boolean hasCode(String code){
        String codes = db.dbSelectFirst("codes","userinfo",new KeyValue(){{ this.add("code",code); }});
        return codes != null && codes.equalsIgnoreCase("null");
    }
    public static boolean hasBeenLocked(String username){
        String user = db.dbSelectFirst("userinfo","locked",new KeyValue(){{ this.add("username",username); }});
        return Boolean.parseBoolean(user);
    }
    public static String getRegisterEmailUserName(String email){
        String user = db.dbSelectFirst("regemails","username",new KeyValue(){{ this.add("email",email); }});
        return user;
    }
    public static String getUserHwid(String username){
        String userinfo = db.dbSelectFirst("userinfo","userinfo",new KeyValue(){{ this.add("username",username); }});
        UserInfo user1 = JSON.parseObject(userinfo, UserInfo.class);
        return user1.getHwid();
    }
    public static String getUserHwidEmail(String email){
        return getUserHwid(getRegisterEmailUserName(email));
    }
    public static String getUserPassword(String username){
        String userinfo = db.dbSelectFirst("userinfo","userinfo",new KeyValue(){{ this.add("username",username); }});
        UserInfo user1 = JSON.parseObject(userinfo, UserInfo.class);
        return user1.getPassword();
    }
    public static String getUserEmail(String username){
        String userinfo = db.dbSelectFirst("userinfo","userinfo",new KeyValue(){{ this.add("username",username); }});
        UserInfo user1 = JSON.parseObject(userinfo, UserInfo.class);
        return user1.getEmail();
    }
    public static void changeUserLocked(String username,boolean lock){
        db.dbUpdate("userinfo",new KeyValue(){{ this.add("locked",lock); }},new KeyValue(){{ this.add("username",username); }});
    }
    public static void changePassword(String username,String newPassword){
        UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(MD5Utils.get32MD5Codes(newPassword));
        user.setEmail(getUserEmail(username));
        user.setHwid(getUserHwid(username));
        String userJson = JSON.toJSONString(user);
        db.dbUpdate("userinfo",new KeyValue(){{ this.add("userinfo",userJson); }},new KeyValue(){{ this.add("username",username); }});
    }
    public static void changeHwid(String username){
        UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(getUserPassword(username));
        user.setEmail(getUserEmail(username));
        user.setHwid(Main.hwid);
        String userJson = JSON.toJSONString(user);
        db.dbUpdate("userinfo",new KeyValue(){{ this.add("userinfo",userJson); }},new KeyValue(){{ this.add("username",username); }});
    }
    public static void registerUser(String code,String username,String password,String email){
        UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(MD5Utils.get32MD5Codes(password));
        user.setEmail(email);
        user.setHwid(Main.hwid);
        String userJson = JSON.toJSONString(user);
        Date date = new Date();
        String time = String.valueOf(date.getTime());
        db.dbUpdate("codes",new KeyValue(){{ this.add("userinfo",userJson); }},new KeyValue(){{ this.add("code",code); }});
        db.dbUpdate("codes",new KeyValue(){{ this.add("time",time); }},new KeyValue(){{ this.add("code",code); }});
        db.dbInsert("regemails",new KeyValue(){{
            this.add("username",username);
            this.add("email",email);
        }});
        db.dbInsert("userinfo",new KeyValue(){{
            this.add("username",username);
            this.add("userinfo",userJson);
            this.add("getaccounts",0);
            this.add("locked",false);
        }});
    }
}
