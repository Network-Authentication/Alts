package net.dev.alts.gui;

import net.dev.alts.*;
import net.dev.alts.gui.actions.*;
import net.dev.alts.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterGui extends JFrame {
    public void RegisterGui(){
        this.setTitle("Azure 163-Alts 验证系统 - 注册账号");
        this.setSize(480, 242);
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
        text_name.setText(UserInfoUtils.getUserName());
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
        JLabel code = new JLabel("激活码：");
        code.setFont(jb);
        this.add(code);
        JTextField text_code = new JTextField();
        text_code.setPreferredSize(new Dimension(366,30));
        this.add(text_code);
        JButton button1 = new JButton();
        button1.setText("获取激活码");
        button1.setFont(jb);
        button1.setSize(dim2);
        this.add(button1);
        JButton button2 = new JButton();
        button2.setText("注册");
        button2.setFont(jb);
        button2.setSize(dim2);
        this.add(button2);
        JButton button3 = new JButton();
        button3.setText("返回登录");
        button3.setFont(jb);
        button3.setSize(dim2);
        this.add(button3);
        this.setVisible(true);
        button3.addActionListener(new GoToLoginActions(this));
        button2.addActionListener(new RegisterGui.RegisterActions(this,text_name,text_password,text_email,text_code));
    }
    private static class RegisterActions implements ActionListener {
        private final JFrame gui;
        private final JTextField username;
        private final JTextField password;
        private final JTextField email;
        private final JTextField code;
        public RegisterActions(JFrame gui,JTextField username,JTextField password,JTextField email,JTextField code){
            this.gui = gui;
            this.username = username;
            this.password = password;
            this.email = email;
            this.code = code;
        }
        public boolean contains(String c,String... s){
            for(String a:s)
                if(c.contains(a))
                    return true;
            return false;
        }
        public void actionPerformed(ActionEvent e){
            String username = this.username.getText(),password = this.password.getText(),email = this.email.getText(),code = this.code.getText();
            if(!username.isEmpty()&&!password.isEmpty()&&!email.isEmpty()&&!code.isEmpty()){
                if(contains(username,";","--","%20","==",">","<","@","'","`","(",")")){
                    JOptionPaneManager.sendERROR("注册失败","用户名包含非法字符！");
                    return;
                }
                if(email.contains("@")){
                    if(DatabaseUtils.hasUser(username)){
                        JOptionPaneManager.sendERROR("注册失败","用户名已被注册！");
                    }else{
                        if(DatabaseUtils.hasEmail(email)){
                            JOptionPaneManager.sendERROR("注册失败","输入的注册邮箱已被注册！");
                        }else{
                            if(DatabaseUtils.hasCode(code)){
                                DatabaseUtils.registerUser(code,username,password,email);
                                JOptionPaneManager.sendDEFAULT("注册成功！");
                            }else{
                                JOptionPaneManager.sendERROR("注册失败","激活码不存在或激活码已被使用！");
                            }
                        }
                    }
                }else{
                    JOptionPaneManager.sendERROR("注册失败","输入的注册邮箱格式错误！");
                }
            }else{
                JOptionPaneManager.sendERROR("注册失败","输入的注册信息不能为空！");
            }
        }
    }
}
