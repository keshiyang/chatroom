package com.ksy.chatroom.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Admin
 * @PackageName com.ksy.chatroom.client
 * @ClassName chatroom
 * @Description
 * @create 2022-03-12 20:09
 */

public class StartClient {
    public StartClient(Socket s) throws UnknownHostException, IOException {
        new Thread(new ReceiveClient(s)).start();
    }

}
