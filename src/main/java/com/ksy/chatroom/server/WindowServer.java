package com.ksy.chatroom.server;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
import java.io.IOException;

/**
 * @author Admin
 * @PackageName com.ksy.chatroom.service
 * @ClassName chatroom
 * @Description
 * @create 2022-03-12 20:01
 */

public class WindowServer {

    public static JFrame window;
    /**
     * 聊天记录
     */
    public static JTextArea textMessage;
    /**
     * 用户列表
     */
    public static JList<String> user;
    public static int ports;
    JButton start,send,exit;
    JTextField portServer,message,name;

    /**
     * 主函数入口
     */
    public static void main(String[] args) {
        new WindowServer();
    }

    //初始化窗体
    public WindowServer() {
        init();
    }

    /**
     * 初始化内容 init
     */
    public void init(){//采用绝对布局
        window = new JFrame("服务端");
        window.setLayout(null);
        window.setBounds(200, 200, 500, 400);
        //不可改变大小
        window.setResizable(false);

        JLabel label1 = new JLabel("端口号:");
        label1.setBounds(10, 8, 50, 30);
        window.add(label1);

        portServer = new JTextField();
        portServer.setBounds(60, 8, 100, 30);
        portServer.setText("30000");
        window.add(portServer);

        JLabel names = new JLabel("用户名:");
        names.setBounds(180, 8, 55, 30);
        window.add(names);

        name = new JTextField();
        name.setBounds(230, 8, 60, 30);
        name.setText("服务端");
        window.add(name);

        start = new JButton("启动");
        start.setBounds(300, 8, 80, 30);
        window.add(start);

        exit = new JButton("关闭");
        exit.setBounds(390, 8, 80, 30);
        window.add(exit);


        JLabel label2 = new JLabel("用户列表");
        label2.setBounds(40, 40, 80, 30);
        window.add(label2);


        user = new JList<String>();
        JScrollPane scrollPane = new JScrollPane(user);//添加滚动条
        scrollPane.setBounds(10, 70, 120, 220);
        window.add(scrollPane);

        textMessage = new JTextArea();
        textMessage.setBounds(135, 70, 340, 220);
        //设置标题
        textMessage.setBorder(new TitledBorder("聊天记录"));
        //不可编辑
        textMessage.setEditable(false);
        //文本内容换行的两个需要配合着用
        //设置文本内容自动换行，在超出文本区域时，可能会切断单词
        textMessage.setLineWrap(true);
        //设置以自动换行，以单词为整体，保证单词不会被切断
        textMessage.setWrapStyleWord(true);
        //设置滚动条
        JScrollPane scrollPane1 = new JScrollPane(textMessage);
        scrollPane1.setBounds(135, 70, 340, 220);
        window.add(scrollPane1);

        message = new JTextField();
        message.setBounds(10, 300, 360, 50);

        window.add(message);

        send = new JButton("发送");
        send.setBounds(380, 305, 70, 40);
        window.add(send);

        myEvent();	//添加监听事件
        window.setVisible(true);
    }

    public void myEvent(){
        window.addWindowListener(new WindowAdapter() {//关闭窗体
            @Override
            public void windowClosing(WindowEvent e){
                if(StartServer.userList != null && StartServer.userList.size() != 0){
                    try {
                        new SendServer(StartServer.userList, "" , "4");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(StartServer.ss == null || StartServer.ss.isClosed()){//如果已退出，弹窗提醒
                    JOptionPane.showMessageDialog(window, "服务器已关闭");
                }else {
                    //发信息告诉客户端，要退出
                    if(StartServer.userList != null && StartServer.userList.size() != 0){
                        try {
                            new SendServer(StartServer.userList, "" , 4 + "");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    try {
                        start.setText("启动");
                        exit.setText("已关闭");
                        StartServer.ss.close();//关闭服务端
                        StartServer.ss = null;
                        StartServer.userList = null;
                        StartServer.flag = false;//改变服务端循环标记
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        //开启服务端
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(StartServer.ss != null && !StartServer.ss.isClosed()){
                    JOptionPane.showMessageDialog(window, "服务器已经启动");
                }else {
                    ports = getPort();//获取端口号
                    if(ports != 0){
                        try {
                            StartServer.flag = true;
                            new Thread(new StartServer(ports)).start();
                            start.setText("已启动");
                            exit.setText("关闭");
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(window, "启动失败");
                        }
                    }
                }
            }
        });

        //点击按钮发送消息
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMsg();
            }
        });

        //按回车发送消息
        message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    sendMsg();
                }
            }
        });
    }

    //发送消息方法
    public void sendMsg(){
        String messages = message.getText();
        //判断内容是否为空
        if("".equals(messages)){
            JOptionPane.showMessageDialog(window, "内容不能为空！");
            //判断是否已经连接成功
        }else if(StartServer.userList == null || StartServer.userList.size() == 0){
            JOptionPane.showMessageDialog(window, "未连接成功，不能发送消息！");
        }else {
            try {
                //将信息发送给所有客户端
                new SendServer(StartServer.userList, getName() + "：" + messages, 1 + "");
                //将信息添加到客户端聊天记录中
                WindowServer.textMessage.append(getName() + "：" + messages + "\r\n");
                //消息框设置为空
                message.setText(null);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(window, "发送失败！");
            }
        }
    }

    //获取端口号
    public int getPort(){
        String port = portServer.getText();
        if("".equals(port)){//判断端口号是否为空
            JOptionPane.showMessageDialog(window, "端口号为口");
            return 0;
        }else {
            return Integer.parseInt(port);//返回整形的端口号
        }
    }

    //获取服务端名称
    public String getName(){
        return name.getText();
    }

}
