package net.dev.alts.gui;

import net.dev.alts.*;
import net.dev.alts.gui.actions.*;
import net.dev.alts.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChangeHwidGui extends JFrame{
    public void ChangeHwidGui(){
        this.setTitle("Azure 163-Alts 验证系统 - 更新Hwid");
        this.setSize(480, 200);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setFont(new Font(null,Font.PLAIN,14));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,10,10);
        Font jb = new Font(null,Font.PLAIN,14);
        this.setLayout(fl);
        Dimension dim1 = new Dimension(380,30);
        Dimension dim2 = new Dimension(100,30);
        JLabel username = new JLabel("账号：");
        username.setFont(jb);
        this.add(username);
        JTextField text_name = new JTextField();
        text_name.setPreferredSize(dim1);
        this.add(text_name);
        JLabel password = new JLabel("密码：");
        password.setFont(jb);
        this.add(password);
        JTextField text_password = new JTextField();
        text_password.setPreferredSize(dim1);
        this.add(text_password);
        JLabel email = new JLabel("邮箱：");
        email.setFont(jb);
        this.add(email);
        JTextField text_email = new JTextField();
        text_email.setPreferredSize(dim1);
        this.add(text_email);
        JButton button2 = new JButton();
        button2.setText("注册");
        button2.setFont(jb);
        button2.setSize(dim2);
        this.add(button2);
        JButton button1 = new JButton();
        button1.setText("修改");
        button1.setFont(jb);
        button1.setSize(dim2);
        this.add(button1);
        this.setVisible(true);
        JButton button3 = new JButton();
        button3.setText("返回登录");
        button3.setFont(jb);
        button3.setSize(dim2);
        this.add(button3);
        this.setVisible(true);
        button3.addActionListener(new GoToLoginActions(this));
        button2.addActionListener(new GoToRegisterActions(this));
        button1.addActionListener(new setHwidActions(this,text_name,text_password,text_email));
    }
    private static class setHwidActions implements ActionListener {
        private JFrame gui;
        private JTextField username,password,email;
        public setHwidActions(JFrame gui,JTextField username,JTextField password,JTextField email){
            this.username = username;
            this.password = password;
            this.email = email;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(username.getText().isEmpty()&&password.getText().isEmpty()&&email.getText().isEmpty()){
                JOptionPaneManager.sendERROR("修改失败","输入的账户信息不能为空！");
            }else{
                if(DatabaseUtils.loginAuthUsername(username.getText(),password.getText(),true)){
                    if(DatabaseUtils.getRegisterEmailUserName(username.getText()).equals(email.getText())){
                        if(DatabaseUtils.hasBeenLocked(username.getText())){
                            JOptionPaneManager.sendERROR("修改失败","您的账号涉嫌泄露已被锁定！\n如有疑问，请尝试联系帮助！");
                            return;
                        }
                        DatabaseUtils.changeHwid(username.getText());
                        JOptionPaneManager.sendERROR("修改成功","您的Hwid已被修改！");
                    }else{
                        JOptionPaneManager.sendERROR("修改失败","您输入的账户信息是错误的！");
                    }
                }else{
                    JOptionPaneManager.sendERROR("修改失败","您输入的账户信息是错误的！");
                }
            }
        }
    }
}
