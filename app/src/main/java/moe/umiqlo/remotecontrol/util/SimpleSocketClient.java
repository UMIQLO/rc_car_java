package moe.umiqlo.remotecontrol.util;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import moe.umiqlo.remotecontrol.config.Config;

public class SimpleSocketClient implements Runnable {

    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String host, response;
    private int port;
    private boolean runFlag;

    public SimpleSocketClient(String host, int port) {
        this.host = host;
        this.port = port;
        runFlag = true;
    }

    @Override
    public void run() {
        while (runFlag) {
            System.out.println("-----Start-----");
            try {
                socket = new Socket(host, port);
                receive();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("-----End Start-----");
        }
    }

    public void send(String string) {
        try {
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(string + "\r\n");
            bw.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void receive() {
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!socket.isClosed()) {
                response = br.readLine();
                if (response != null) {
                    System.out.println("-----Received-----");
                    System.out.println(response);
                    System.out.println("--------END-------");

                    // set Response to CarResponse Object
                    CarResponse carResponse;
                    carResponse = CarResponse.getInstance();
                    carResponse = new Gson().fromJson(response, CarResponse.class);
                    carResponse.setInstance(); // use gson deserialized class to replace
                    System.out.println("FromJson = " + carResponse.toString());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void kill() {
        try {
            System.out.println("--------Kill-------");
            //br.close();
            System.out.println("--------br Kill-------");
            if (bw != null) {
                bw.close();
                System.out.println("--------bw Kill-------");
            }

            socket.close();
            System.out.println("--------socket Kill-------");

        } catch (Exception e) {
            e.printStackTrace();
            runFlag = false;
        } finally {
            System.out.println("--------runFlag false-------");
            runFlag = false;
        }
    }
}
