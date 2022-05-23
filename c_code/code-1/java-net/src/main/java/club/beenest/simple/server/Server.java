package club.beenest.simple.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        System.out.println("建立连接中。。。。");
        // 开启socket监听对应端口
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("建立连接完成");

        // 获取socket连接
        Socket socket = serverSocket.accept();

        // 获取输入流
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // 循环打印数据
        String msg;
        System.out.println("准备获取客户端信息");
        while ((msg = bufferedReader.readLine()) != null) {
            System.out.println("客户端信息：" + msg);
        }
    }
}
