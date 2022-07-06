package com.gupaoedu.demo.bio;

import java.io.*;
import java.net.Socket;

public class ClintSocket {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",8080);

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write("我是客户端\n");
        bufferedWriter.flush();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s = bufferedReader.readLine();
        System.out.println(s);


    }
}
