package com.ksy.chatroom.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Admin
 * @PackageName com.ksy.chatroom.service
 * @ClassName chatroom
 * @Description
 * @create 2022-03-12 20:04
 */

public class ReceiveServer implements Runnable{
    private Socket socket;
    private ArrayList<Socket> userList;
    private Vector<String> userName;

    public ReceiveServer(Socket s,ArrayList<Socket> userList,Vector<String> userName) {
        this.socket = s;
        this.userList = userList;
        this.userName = userName;
    }

    @Override
    public void run() {
        try {
            //读取信息流
            BufferedReader brIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true){
                //先读取信息流的首字符，并判断信息类型
                char info = (char)brIn.read();
                //读取信息流的信息内容
                String line = brIn.readLine();
                //1代表收到的是信息
                if(info == '1'){
                    //将信息添加到服务端聊天记录中
                    WindowServer.textMessage.append(line + "\r\n");
                    //设置消息显示最新一行，也就是滚动条出现在末尾，显示最新一条输入的信息
                    WindowServer.textMessage.setCaretPosition(WindowServer.textMessage.getText().length());
                    //将信息转发给客户端
                    new SendServer(userList, line, "1");
                }
                //2代表有新客户端建立连接
                if(info == '2'){
                    //将新客户端用户名添加到容器中
                    userName.add(line);
                    //更新服务端用户列表
                    WindowServer.user.setListData(userName);
                    //将用户列表以字符串的形式发给客户端
                    new SendServer(userList, userName, "2");
                }
                //3代表有用户端退出连接
                if(info == '3'){
                    //移除容器中已退出的客户端用户名
                    userName.remove(line);
                    //移除容器中已退出的客户端的socket
                    userList.remove(socket);
                    //更新服务端用户列表
                    WindowServer.user.setListData(userName);
                    //将用户列表以字符串的形式发给客户端
                    new SendServer(userList, userName, "3");
                    socket.close();//关闭该客户端的socket
                    break;//结束该客户端对于的信息接收线程
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
