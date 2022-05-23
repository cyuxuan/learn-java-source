package club.beenest.simple.client;

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
        // 获取socket输入流
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        // 获取用户输入
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        // 变量接收客户端输入信息
        String msg;
        while ((msg = bufferedReader.readLine()) != null) {
            System.out.println("发送信息给服务端："+msg);
            printWriter.println(msg);
            printWriter.flush();
        }
    }
}
