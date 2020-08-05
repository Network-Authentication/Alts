package net.dev.alts.utils;

import com.sun.mail.util.*;
import net.dev.alts.*;

import javax.mail.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.*;
import java.net.*;
import java.util.*;

public class EmailUtils {
    public static String emailCode;
    static {
        try {
            emailCode = HttpsUtils.readURLUTF8("https://gitee.com/azurepvp/wyalts/raw/master/email");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static int cooldown = 0;
    private String useremail;
    public EmailUtils(String useremail){
        this.useremail = useremail;
    }
    public boolean sendEmail(String title,String message_1,String message_2,String message_3,String message_4,String code){
        try{
            if(cooldown==0){
                Properties properties = System.getProperties();
                String host = "smtp.qq.com";
                properties.setProperty("mail.smtp.host", host);
                properties.put("mail.smtp.auth", "true");
                MailSSLSocketFactory sf;
                sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.ssl.socketFactory", sf);
                Session session = Session.getDefaultInstance(properties,new Authenticator(){
                    public PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication("minecraftalts@qq.com", "npwoynhyefxlbfhg");
                    }
                });
                MimeMessage message = new MimeMessage(session);
                String sendEmail = "minecraftalts@qq.com";
                message.setFrom(new InternetAddress(sendEmail));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(useremail));
                message.setSubject(title.replace("%message_1%",message_1).replace("%message_2%",message_2).replace("%message_3%",message_3).replace("%message_4%",message_4).replace("%code%",code));
                message.setContent(emailCode.replace("%message_1%",message_1).replace("%message_2%",message_2).replace("%message_3%",message_3).replace("%message_4%",message_4).replace("%code%",code),"text/html;charset=GB2312");
                Transport.send(message);
                return true;
            }else{
                JOptionPaneManager.sendERROR("正在冷却中...","发信失败，请等待:"+cooldown+"秒后再次发送。");
                return false;
            }
        }catch (Throwable e){
            LogUtils.writeLog(e.getMessage());
            return false;
        }
    }
}
