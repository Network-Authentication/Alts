package net.dev.alts;

import net.dev.alts.encrypt.*;
import net.dev.alts.gui.*;
import net.dev.alts.hwid.*;
import net.dev.alts.utils.*;

import java.io.*;
import java.net.*;
import java.util.*;

public final class Main{
    public static List<String> staffEmailList=new ArrayList<>();
    public static String hwid = MD5Utils.get16MD5Codes(Base64Utils.EnCode(HWID.bytesToHex(HWID.generateHWID())));
    public static double version = 1.00;
    public static void main(String[] args) {
        staffEmailList.add("368289112@qq.com");
        staffEmailList.add("1006800345@qq.com");
        LogUtils.log();
        LogUtils.writeLog("程序启动...");
        try {
            new ServerSocket(1);
        } catch (Throwable e) {
            e.printStackTrace();
            LogUtils.writeLog(e.getMessage());
            JOptionPaneManager.sendERROR("检测到程序已运行...","尝试关闭已运行的程序然后再次尝试...");
            System.exit(0);
        }
        LogUtils.writeLog("本机HWID："+hwid);
        LogUtils.writeLog("程序正在初始化中...");
        LogUtils.writeLog("程序版本："+version+" 正在检查更新...");
        try {
            String v = HttpsUtils.get("https://gitee.com/azurepvp/wyalts/raw/master/version");
            if(Double.parseDouble(v) > version){
                LogUtils.writeLog("有新版本更新，请前往售后群下载最新版本... 最新版本："+v);
                JOptionPaneManager.sendERROR("请前往售后群下载最新版本...","发现您的程序版本并非为最新版本，最新版本为："+v+" 您的版本为："+version);
                System.exit(0);
            }else{
                LogUtils.writeLog("本次启动的版本为最新版本...");
            }
        } catch (MalformedURLException e) {
            LogUtils.writeLog("版本检查错误，程序自毁... 错误代码："+e.getMessage());
            JOptionPaneManager.sendERROR("版本检查错误","请保持您的网络通顺然后再次尝试或前往售后群寻求帮助...");
            System.exit(0);
        }
        LogUtils.writeLog("正在加载用户配置...");
        try {
            UserInfoUtils.creatProperties();
        } catch (IOException e) {
            LogUtils.writeLog("用户配置加载失败...");
        }
        LogUtils.writeLog("正在连接数据库中...");
        DatabaseUtils.loadDatabase();
        LogUtils.writeLog("连接数据库成功！");
        LogUtils.writeLog("程序初始化完毕，正在获取账号登录信息...");
        LogUtils.writeLog( "账号信息获取完毕，正在加载...");
        try{
            if(DatabaseUtils.hasBeenLocked(UserInfoUtils.getUserName())){
                new LoginGui().LoginGui();
            }else{
                if(DatabaseUtils.loginAuthUsername(UserInfoUtils.getUserName(),UserInfoUtils.getPassWord(),false) || DatabaseUtils.loginAuthEmail(UserInfoUtils.getUserName(),UserInfoUtils.getPassWord(),false)){
                    if(Main.hwid.equals(DatabaseUtils.getUserHwid(UserInfoUtils.getUserName()))){
                        //登录成功
                        new LoginGui().LoginGui();
                    }else{
                        DatabaseUtils.changeUserLocked(UserInfoUtils.getUserName(),true);
                        new LoginGui().LoginGui();
                    }
                }else{
                    new LoginGui().LoginGui();
                }
            }
        }catch (Throwable e){
            new LoginGui().LoginGui();
        }
        /*User user = new User();
        user.setUsername("xLikeWATCHCAT");
        user.setPassword("123456789");
        user.setEmail("333@qq.com");
        user.setHwid(hwid);
        String userJson = JSON.toJSONString(user);
        System.out.println(userJson);
        User user1 = JSON.parseObject(userJson, User.class);
        System.out.println(user1.getUsername());
        System.out.println(user1.getPassword());
        System.out.println(user1.getEmail());
        System.out.println(user1.getHwid());*/
    }
}
