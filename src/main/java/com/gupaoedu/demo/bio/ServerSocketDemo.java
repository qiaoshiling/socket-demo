package com.gupaoedu.demo.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 咕泡学院，只为更好的你
 * 咕泡学院-Mic: 2227324689
 * http://www.gupaoedu.com
 **/
public class ServerSocketDemo {


    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            //localhost: 8080
            serverSocket = new ServerSocket(8080);
            while (true) {
                // 阻塞点
                final Socket socket = serverSocket.accept(); //监听客户端连接(连接阻塞）
                System.out.println(socket.getPort());

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//输入流
                    String clientStr = bufferedReader.readLine(); //被阻塞了
                    System.out.println("接收到客户端的信息：" + clientStr);
                    //写回去
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bufferedWriter.write("我收到了信息\n");
                    bufferedWriter.flush();

                    bufferedReader.close();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //TODO
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
