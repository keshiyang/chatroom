package com.ksy.chatroom.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Admin
 * @PackageName com.ksy.chatroom.client
 * @ClassName chatroom
 * @Description
 * @create 2022-03-12 20:10
 */

public class SendClient {
    SendClient(Socket s, Object message, String info) throws IOException {
        String messages = info + message;
        PrintWriter pwOut = new PrintWriter(s.getOutputStream(),true);
        pwOut.println(messages);
    }

}
