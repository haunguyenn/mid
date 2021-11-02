/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class Connection {

    protected Socket mSocket;   // Instanceof Socket
    protected BufferedReader mInput;    // Doi tuong dung de nhan message
    protected PrintWriter mOuput;   // Doi tuong dung de gui message
    protected javax.swing.JTextArea mNotification;  // Vung hien thi thong bao cho user
    protected javax.swing.JTextArea mChatArea;  // Vung hien thi message chat
    protected Thread mThreadRead;   // Viec nhan message se duoc thuc hien trong thread nay
    protected boolean isRunning;    // Bien trang thai cua Connection
    protected int mPort;    // Port ma Server lang nghe (voi Client khong anh huong)

    /**
     * Khoi tao Connection voi cac tham so
     * @param port doi voi Client co the de gia tri 0
     * @param notification
     * @param chatArea
     */
    public Connection(int port, javax.swing.JTextArea notification, javax.swing.JTextArea chatArea) {
        mPort = port;
        mNotification = notification;
        mChatArea = chatArea;
    }

    /**
     * Hien thi thong bao cho user
     * @param text noi dung thong bao
     */
    public void AppendNotification(String text) {
        mNotification.append(text + "\n");
    }

    /**
     * Thuc hien doc message nhan duoc va hien thi cho user.
     * Viec nay duoc tach ra thuc hien trong thread rieng thuc hien dong thoi voi viec gui message
     */
    public void ReceiveLoop() {
        // Khoi tao thead cho viec doc messasge
        mThreadRead = new Thread(() -> {
            // Kiem tra trang thai va thoat thi ket noi da ngung
            while (isRunning) {
                try {
                    // Doc message
                    final String msg = mInput.readLine();
                    if (msg == null) {
                        // Khong nhan duoc gi thi tiep tuc vong lap doc message
                        continue;
                    }

                    // Neu nhan duoc message bao ket thuc thi dung lai
                    if (msg == "!!!OVER!!!") {
                        stop();
                    }

                    // Hien thi message len vung chat cho user
                    mChatArea.append("RECEIVED: " + msg + "\n\n");
                } catch (Exception e) {
                    e.printStackTrace();
                    stop();
                }
            }
        });

        // Start thread bat dau vong lap doc message
        mThreadRead.start();
    }

    /**
     * Gui di message
     * @param msg noi dung message
     */
    public void Send(String msg) {
        // Kiem tra trang thai neu ket noi da ngung thi huy bo
        if (isRunning) {
            try {
                // Gui message qua socket
                mOuput.println(msg);
                mOuput.flush();

                // Hien thi mssage da gui len vung chat cho user
                mChatArea.append("SEND: " + msg + "\n\n");
            } catch (Exception e) {
                e.printStackTrace();
                stop();
            }

        }
    }

    /**
     * Khoi tao ket noi qua socket.
     * Ham nay can duoc overload boi Server va Client
     */
    public void start() {
    }

    /**
     * Dong ket noi socket
     */
    public void stop() {
        // Neu ket noi da dong thi bo qua
        if (mSocket == null) {
            return;
        }
        try {
            // Dong ket noi socket
            mSocket.close();
            mSocket = null;
            isRunning = false;

            // Hien thi thong bao cho user
            AppendNotification("Disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
