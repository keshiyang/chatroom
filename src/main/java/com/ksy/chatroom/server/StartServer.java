package com.ksy.chatroom.server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Admin
 * @PackageName com.ksy.chatroom.service
 * @ClassName chatroom
 * @Description
 * @create 2022-03-12 20:03
 */

public class StartServer implements Runnable{
    private int port;
    public static ArrayList<Socket> userList = null;
    public static Vector<String> userName = null;
    public static ServerSocket ss = null;
    public static boolean flag = true;

    //传入端口号
    public StartServer(int port) throws IOException{
        this.port = port;
    }

    @Override
    public void run() {
        Socket s = null;
        userList = new ArrayList<Socket>();
        userName = new Vector<String>();

        try {
            ss = new ServerSocket(port);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        while (flag) {
            try {
                s = ss.accept();
                userList.add(s);
                //打印客户端信息
                String id = s.getInetAddress().getHostName();
                System.out.println(id + "-----------connected");
                System.out.println("当前客户端个数为：" + userList.size());
                //启动与客户端相对应的信息接收线程
                new Thread(new ReceiveServer(s,userList,userName)).start();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(WindowServer.window, "服务端退出！");
            }
        }
    }

}
