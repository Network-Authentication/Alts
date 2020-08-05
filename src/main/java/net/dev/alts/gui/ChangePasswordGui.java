package net.dev.alts.gui;

import net.dev.alts.*;
import net.dev.alts.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChangePasswordGui extends JFrame {
    private String email;
    public void ChangePasswordGui(String email){
        this.setTitle("Azure 163-Alts 验证系统 - 设置密码");
        this.setSize(480, 125);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setFont(new Font(null,Font.PLAIN,14));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,10,10);
        Font jb = new Font(null,Font.PLAIN,14);
        this.setLayout(fl);
        Dimension dim1 = new Dimension(380,30);
        Dimension dim2 = new Dimension(100,30);
        JLabel password = new JLabel("新密码：");
        password.setFont(jb);
        this.add(password);
        JTextField text_password = new JTextField();
        text_password.setPreferredSize(dim1);
        this.add(text_password);
        JButton button3 = new JButton();
        button3.setText("设置新密码");
        button3.setFont(jb);
        button3.setSize(dim2);
        this.add(button3);
        this.setVisible(true);
        button3.addActionListener(new setPasswordActions(this,email,text_password));
    }
    private static class setPasswordActions implements ActionListener {
        private JFrame gui;
        private String email;
        private JTextField password;
        public setPasswordActions(JFrame gui,String email,JTextField password){
            this.gui = gui;
            this.email = email;
            this.password = password;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            DatabaseUtils.changePassword(DatabaseUtils.getRegisterEmailUserName(email),password.getText());
            JOptionPaneManager.sendDEFAULT("修改成功！");
            gui.dispose();
            new LoginGui().LoginGui();
        }
    }
}
