package net.dev.alts.gui;

import net.dev.alts.*;
import net.dev.alts.gui.actions.*;
import net.dev.alts.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ContactGui extends JFrame {
    public void ContactGui(){
        this.setTitle("Azure 163-Alts 验证系统 - 联系");
        this.setSize(480, 428);
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
        this.setVisible(true);
        JLabel email = new JLabel("邮箱：");
        email.setFont(jb);
        this.add(email);
        JTextField text_email = new JTextField();
        text_email.setPreferredSize(dim1);
        this.add(text_email);
        JLabel question = new JLabel("您有什么需要帮助的：");
        question.setFont(jb);
        this.add(question);
        JComboBox cmb=new JComboBox();
        cmb.addItem("--请选择--");
        cmb.addItem("账号问题");
        cmb.addItem("付款问题");
        cmb.addItem("代替付款");
        cmb.addItem("广告");
        cmb.addItem("刷取的163账号问题");
        cmb.addItem("其它提议");
        this.add(cmb);
        JLabel full = new JLabel("请详细描述一下你遇到的问题：");
        full.setFont(jb);
        this.add(full);
        JPanel jp=new JPanel();
        JTextArea jta=new JTextArea(10,40);
        jta.setLineWrap(true);
        JScrollPane jsp=new JScrollPane(jta);
        jp.add(jsp);
        this.add(jp);
        JButton button1 = new JButton();
        button1.setText("注册账号");
        button1.setFont(jb);
        button1.setSize(dim2);
        this.add(button1);
        JButton button2 = new JButton();
        button2.setText("发送");
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
        button3.addActionListener(new GoToLoginActions(this));
        button2.addActionListener(new ContactActions(this, text_name, text_email,cmb,jta,button2));
    }
    private static class ContactActions implements ActionListener {
        private JFrame gui;
        private JTextField username;
        private JTextField email;
        private JComboBox questions;
        private JTextArea full;
        private JButton send;
        public ContactActions(JFrame gui,JTextField username,JTextField email,JComboBox questions,JTextArea full,JButton send){
            this.gui = gui;
            this.username = username;
            this.email = email;
            this.questions = questions;
            this.full = full;
            this.send = send;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            send.setText("正在尝试发送...");
            if(!username.getText().isEmpty()&&!email.getText().isEmpty()&&!full.getText().isEmpty()){
                if(email.getText().contains("@")){
                    if(questions.getSelectedItem().equals("--请选择--")){
                        send.setText("发送失败");
                        JOptionPaneManager.sendERROR("发送失败","请选择您需要帮助的内容！");
                        send.setText("发送");
                        return;
                    }
                    if(!DatabaseUtils.hasUser(username.getText())){
                        send.setText("发送失败");
                        JOptionPaneManager.sendERROR("发送失败","账号不存在！");
                        send.setText("发送");
                        return;
                    }
                    if(EmailUtils.cooldown==0){
                        for(String staffEmail:Main.staffEmailList){
                            if(!new EmailUtils(staffEmail).sendEmail("有一名新的联系用户", username.getText()+" 请求联系。", "他的问题是："+questions.getSelectedItem(), "他的HWID:"+Main.hwid+ " 他的邮箱:"+email.getText(), "描述：",full.getText())){
                                send.setText("发送失败");
                                JOptionPaneManager.sendERROR("发送失败","请检查您的联系信息是否有误，如果无误请加售后群寻找管理员联系帮助！");
                                send.setText("发送");
                                return;
                            }
                        }
                        if(new EmailUtils(email.getText()).sendEmail("我们已经收到了您的联系",username.getText()+" 我们已经收到了您的联系","请耐心的等待回复","","","")){
                            send.setText("发送成功");
                            JOptionPaneManager.sendWORING("发送成功","请时刻检查您的邮箱和您的垃圾箱以防错过我们的联系信息！");
                            send.setText("发送");
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
                        }else{
                            send.setText("发送失败");
                            JOptionPaneManager.sendERROR("发送失败","请检查您的联系信息是否有误，如果无误请加售后群寻找管理员联系帮助！");
                            send.setText("发送");
                        }
                    }else{
                        send.setText("发送失败");
                        JOptionPaneManager.sendERROR("发送失败","等待冷却："+EmailUtils.cooldown+"秒");
                        send.setText("发送");
                    }
                }else{
                    send.setText("发送失败");
                    JOptionPaneManager.sendERROR("发送失败","输入的联系邮箱格式错误！");
                    send.setText("发送");
                }
            }else{
                send.setText("发送失败");
                JOptionPaneManager.sendERROR("发送失败","输入的联系信息不能为空！");
                send.setText("发送");
            }
        }
    }
}
