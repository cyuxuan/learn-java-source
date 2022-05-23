package club.beenest.simpleinteraction.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Socket样例，客户端
 *
 * @author cyuxuan
 */

public class Client {
    public static void main(String[] args) throws IOException {
        // 建立Socket连接
        Socket socket = new Socket("127.0.0.1", 9999);

        // 开启信息接受
        MsgReciver msgReciver = new MsgReciver(socket);
        msgReciver.start();

        // 获取socket输入流
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        // 获取用户输入
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        // 变量接收客户端输入信息
        String msg;
        while ((msg = bufferedReader.readLine()) != null) {
            printWriter.println(msg);
            printWriter.flush();
        }
    }

    static class MsgReciver extends Thread {
        /**
         * socket连接对象
         */
        private Socket socket;

        /**
         * 构造方法：传入socket连接对象
         *
         * @param socket
         */
        MsgReciver(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 获取输入流
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // 循环获取服务端数据
                String msg;
                while ((msg = bufferedReader.readLine()) != null) {
                    System.out.println("服务端返回信息：" + msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
