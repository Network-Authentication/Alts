package net.dev.alts.gui;

import net.dev.alts.*;
import net.dev.alts.gui.actions.*;
import net.dev.alts.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ForgetPasswordGui extends JFrame {
    public static String code;
    public void ForgetPasswordGui(){
        this.setTitle("Azure 163-Alts 验证系统 - 忘记密码");
        this.setSize(480, 165);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setFont(new Font(null,Font.PLAIN,14));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,10,10);
        Font jb = new Font(null,Font.PLAIN,14);
        this.setLayout(fl);
        Dimension dim1 = new Dimension(380,30);
        Dimension dim2 = new Dimension(100,30);
        JLabel email = new JLabel("邮箱：");
        email.setFont(jb);
        this.add(email);
        JTextField text_email = new JTextField();
        text_email.setPreferredSize(dim1);
        this.add(text_email);
        JLabel username = new JLabel("验证码：");
        username.setFont(jb);
        this.add(username);
        JTextField text_name = new JTextField();
        text_name.setPreferredSize(new Dimension(255,30));
        this.add(text_name);
        JButton button4 = new JButton();
        button4.setText("获取验证码");
        button4.setFont(jb);
        button4.setSize(dim2);
        this.add(button4);
        JButton button1 = new JButton();
        button1.setText("注册账号");
        button1.setFont(jb);
        button1.setSize(dim2);
        this.add(button1);
        JButton button2 = new JButton();
        button2.setText("找回密码");
        button2.setFont(jb);
        button2.setSize(dim2);
        this.add(button2);
        JButton button3 = new JButton();
        button3.setText("返回登录");
        button3.setFont(jb);
        button3.setSize(dim2);
        this.add(button3);
        this.setVisible(true);
        button1.addActionListener(new GoToRegisterActions(this));
        button2.addActionListener(new setPasswordActions(this,text_name,text_email,button2));
        button3.addActionListener(new GoToLoginActions(this));
        button4.addActionListener(new ForgetPasswordActions(this,text_email,button4));
    }
    private static class setPasswordActions implements ActionListener{
        private JFrame gui;
        private JTextField code;
        private JTextField email;
        private JButton set;
        public setPasswordActions(JFrame gui,JTextField code,JTextField email,JButton set){
            this.gui = gui;
            this.code = code;
            this.email = email;
            this.set = set;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(code.getText().isEmpty()){
                JOptionPaneManager.sendERROR("请求被拒绝","请输入验证码！");
            }else{
                if(code.getText().equals(ForgetPasswordGui.code)){
                    gui.dispose();
                    new ChangePasswordGui().ChangePasswordGui(email.getText());
                    ForgetPasswordGui.code = new Utils().getStringRandom(new Utils().getRandomInt(6,8));
                }else{
                    JOptionPaneManager.sendERROR("请求被拒绝","请输入正确验证码！");
                }
            }
        }
    }
    private static class ForgetPasswordActions implements ActionListener {
        private JFrame gui;
        private JTextField email;
        private JButton send;
        public ForgetPasswordActions(JFrame gui,JTextField email,JButton send){
            this.gui = gui;
            this.email = email;
            this.send = send;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            send.setText("正在尝试发送...");
            if(email.getText().isEmpty()){
                send.setText("发送失败");
                JOptionPaneManager.sendERROR("发送失败","输入的邮箱信息不能为空！");
            }else{
                if(email.getText().contains("@")){
                    if(EmailUtils.cooldown==0){
                        if(DatabaseUtils.hasEmail(email.getText())){
                            code = new Utils().getStringRandom(new Utils().getRandomInt(6,8));
                            if(!new EmailUtils(email.getText()).sendEmail("请求重置密码", "一名未知的用户想要重置163 Alts 程序的账户密码", "用于重置密码的验证码", "一名未知的用户想要重置163 Alts 程序的账户密码 因此，我们向您发送这封电子邮件，以确保该操作是您本人所为。", "验证码：",code)){
                                send.setText("发送失败");
                                JOptionPaneManager.sendERROR("发送失败","请检查您的联系信息是否有误，如果无误请加售后群寻找管理员联系帮助！");
                                return;
                            }else{
                                send.setText("发送成功");
                                EmailUtils.cooldown = 120;
                                new Thread(()->{
                                    while(EmailUtils.cooldown!=0){
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException f) {
                                            LogUtils.writeLog(f.getMessage());
                                        }
                                        EmailUtils.cooldown = EmailUtils.cooldown-1;
                                    }
                                }).start();
                            }
                        }else{
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                        JOptionPaneManager.sendWORING("重置密码","如果服务器存在这个注册邮箱，系统将会发送一封邮件以重置您的密码。\n请时刻检查您的邮箱和您的垃圾箱以防错过我们的联系信息！");
                    }else{
                        send.setText("发送失败");
                        JOptionPaneManager.sendERROR("发送失败","等待冷却："+EmailUtils.cooldown+"秒");
                    }
                }else{
                    send.setText("发送失败");
                    JOptionPaneManager.sendERROR("发送失败","输入的联系邮箱格式错误！");
                }
            }
            send.setText("获取验证码");
        }
    }
}
