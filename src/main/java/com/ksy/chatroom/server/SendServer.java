package com.ksy.chatroom.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Admin
 * @PackageName com.ksy.chatroom.service
 * @ClassName chatroom
 * @Description
 * @create 2022-03-12 20:06
 */

public class SendServer {
    SendServer(ArrayList<Socket> userList, Object message, String info) throws IOException {
        //添加信息头标记
        String messages = info + message;
        PrintWriter pwOut = null;
        //将信息发送给每个客户端
        for(Socket s : userList){
            pwOut = new PrintWriter(s.getOutputStream(),true);
            pwOut.println(messages);
        }
    }


}
