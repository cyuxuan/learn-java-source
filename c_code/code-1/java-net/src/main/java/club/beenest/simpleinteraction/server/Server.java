package club.beenest.simpleinteraction.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Server {

    public static List<Socket> socketList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("建立连接中。。。。");
        // 开启socket监听对应端口
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("建立连接完成");

        // 循环打印数据
        String msg;
        System.out.println("准备获取客户端信息");
        while (true) {
            // 获取socket连接
            Socket socket = serverSocket.accept();
            // 存储socket对象
            socketList.add(socket);
            // 开启对应处理线程
            MsgSender msgSender = new MsgSender(socket);
            msgSender.start();
        }
    }

    static class MsgSender extends Thread {

        /**
         * 当前线程的socket连接对象
         */
        private Socket socket;

        /**
         * 构造函数：初始换socket丽连接对象
         *
         * @param socket 连接对象
         */
        MsgSender(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 获取输入流
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // 回复对应信息
                String msg;
                while ((msg = bufferedReader.readLine()) != null) {
                    // 输出客户端数据
                    System.out.println("客户端【" + Thread.currentThread().getId() + "】说：" + msg);
                    // 群发消息
                    for (Socket socketItem : Server.socketList) {
                        if(this.socket == socketItem) {
                            continue;
                        } else {
                            PrintWriter printWriter = new PrintWriter(socketItem.getOutputStream());
                            printWriter.println("客户端【" + Thread.currentThread().getId() + "】说：" + msg);
                            printWriter.flush();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
