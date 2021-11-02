/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

/**
 *
 * @author Admin
 */
public class Server extends Connection {

    private ServerSocket mServer;

    /**
     * Constructor
     * @param port PORT ma Server bind va listen
     * @param notification khu vuc hien thi thong bao cho user
     * @param chatArea vung hien thi message gui va nhan
     */
    public Server(int port, javax.swing.JTextArea notification, javax.swing.JTextArea chatArea) {
        super(port, notification, chatArea);
    }

    /**
     * Khoi tao Server va bat dau lang nghe Client
     */
    @Override
    public void start() {
        try {
            // Kiem tra mPort ma user nhap, neu khac 0 thi lang nghe Server tai mPort,
            // Nguoc lai lang nghe Server tai port bat ki
            if (mPort != 0) {
                mServer = new ServerSocket(mPort);
            } else {
                mServer = new ServerSocket();
            }
            AppendNotification("Server is waiting to accept user...");

            // Bat dau lang nghe va doi Client connect
            mSocket = mServer.accept();
            AppendNotification("Client accepted");

            // Khoi tao mInput va mOutput de gui va nhan message
            mInput = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mOuput = new PrintWriter(mSocket.getOutputStream());

            // Bat dau vong lap nhan message
            isRunning = true;
            ReceiveLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
