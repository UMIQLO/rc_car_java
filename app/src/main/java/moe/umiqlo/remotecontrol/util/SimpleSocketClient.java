package moe.umiqlo.remotecontrol.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClient {

    public void start() {

    }

    private Runnable Connection = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                InetAddress serverIp = InetAddress.getByName("192.168.9.6");
                int serverPort = 2001;
                clientSocket = new Socket(serverIp, serverPort);
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while (clientSocket.isConnected()) {
                    tmp = br.readLine();
                    if (tmp != null) {
                        System.out.println(tmp);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
