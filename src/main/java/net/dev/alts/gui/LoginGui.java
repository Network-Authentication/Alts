package net.dev.alts.gui;

import net.dev.alts.*;
import net.dev.alts.gui.actions.*;
import net.dev.alts.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoginGui extends JFrame {
    public void LoginGui(){
        this.setTitle("Azure 163-Alts 验证系统 - 登录");
        this.setSize(480, 165);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setFont(new Font(null,Font.PLAIN,14));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,10,10);
        this.setLayout(fl);
        Font jb = new Font(null,Font.PLAIN,14);
        Dimension dim1 = new Dimension(380,30);
        JLabel username = new JLabel("账号：");
        username.setFont(jb);
        this.add(username);
        JTextField text_name = new JTextField();
        text_name.setPreferredSize(dim1);
        text_name.setText(UserInfoUtils.getUserName());
        this.add(text_name);
        JLabel password = new JLabel("密码：");
        password.setFont(jb);
        this.add(password);
        JPasswordField text_password = new JPasswordField();
        text_password.setPreferredSize(dim1);
        this.add(text_password);
        Dimension dim2 = new Dimension(100,30);
        JButton button1 = new JButton();
        button1.setText("忘记密码");
        button1.setFont(jb);
        button1.setSize(dim2);
        this.add(button1);
        JButton button3 = new JButton();
        button3.setText("注册");
        button3.setFont(jb);
        button3.setSize(dim2);
        this.add(button3);
        JButton button2 = new JButton();
        button2.setText("登录");
        button2.setFont(jb);
        button2.setSize(dim2);
        this.add(button2);
        JButton button5 = new JButton();
        button5.setText("联系");
        button5.setFont(jb);
        button5.setSize(dim2);
        this.add(button5);
        JButton button4 = new JButton();
        button4.setText("更改HWID");
        button4.setFont(jb);
        button4.setSize(dim2);
        this.add(button4);
        this.setVisible(true);
        button1.addActionListener(new GoToForgetPasswordActions(this));
        button2.addActionListener(new LoginActions(this, text_name, text_password));
        button3.addActionListener(new GoToRegisterActions(this));
        button4.addActionListener(new GoToChangeHwidActions(this));
        button5.addActionListener(new GoToContactActions(this));
    }
    private static class LoginActions implements ActionListener {
        private JFrame gui;
        private JTextField username;
        private JPasswordField password;
        public LoginActions(JFrame gui,JTextField username,JPasswordField password){
            this.gui = gui;
            this.username = username;
            this.password = password;
        }
        public void actionPerformed(ActionEvent e){
            if(!username.getText().isEmpty()&&!password.getText().isEmpty()){
                if(DatabaseUtils.hasUser(username.getText())){
                    if(DatabaseUtils.loginAuthUsername(username.getText(),password.getText(),true)||DatabaseUtils.loginAuthEmail(username.getText(),password.getText(),true)){
                        if(DatabaseUtils.hasBeenLocked(username.getText())){
                            JOptionPaneManager.sendERROR("登录失败","您的账号涉嫌泄露已被锁定！\n如有疑问，请尝试联系帮助！");
                            return;
                        }
                        if(Main.hwid.equals(DatabaseUtils.getUserHwid(username.getText()))){
                            try {
                                UserInfoUtils.setusername(username.getText());
                                UserInfoUtils.setpassword(password.getText());
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            gui.dispose();
                        }else{
                            DatabaseUtils.changeUserLocked(username.getText(),true);
                            JOptionPaneManager.sendERROR("登录失败","您的账号涉嫌泄露已被锁定！\n如有疑问，请尝试联系帮助！");
                        }
                    }else{
                        JOptionPaneManager.sendERROR("登录失败","您输入的账号或密码错误！");
                    }
                }else{
                    JOptionPaneManager.sendERROR("登录失败","您输入的账号或密码错误！");
                }
            }else{
                JOptionPaneManager.sendERROR("登录失败","输入的账号或密码不能为空！");
            }
        }
    }
}
