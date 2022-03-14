package com.ksy.chatroom.client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author Admin
 * @PackageName com.ksy.chatroom.client
 * @ClassName chatroom
 * @Description
 * @create 2022-03-12 20:10
 */

public class ReceiveClient implements Runnable{
    private Socket s;
    public ReceiveClient(Socket s) {
        this.s = s;
    }
    @Override
    public void run() {
        try {
            //信息接收流
            BufferedReader brIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while(true){
                char info = (char)brIn.read();
                String line = brIn.readLine();

                if(info == '1'){
                    WindowClient.textMessage.append(line + "\r\n");
                    //设置消息显示最新一行，也就是滚动条出现在末尾，显示最新一条输入的信息
                    WindowClient.textMessage.setCaretPosition(WindowClient.textMessage.getText().length());
                }

                if(info == '2' || info == '3'){
                    String sub = line.substring(1, line.length()-1);
                    String[] data = sub.split(",");
                    WindowClient.user.clearSelection();
                    WindowClient.user.setListData(data);
                }

                if(info == '4'){
                    WindowClient.link.setText("连接");
                    WindowClient.exit.setText("已退出");
                    WindowClient.socket.close();
                    WindowClient.socket = null;
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(WindowClient.window, "客户端已退出连接");
        }
    }

}
