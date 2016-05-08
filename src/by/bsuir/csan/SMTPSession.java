package by.bsuir.csan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SMTPSession {
    public static final int SOCKET_READ_TIMEOUT = 15*1000; // 15 sec

    private String host;
    private int port;
    private String recipient;
    private String sender;
    private String subject;
    private String message;

    protected Socket smtpSocket;
    protected BufferedReader in;
    protected OutputStreamWriter out;

    public SMTPSession(String host, int port, String recipient, String sender,
                       String subject, String message) {
        this.host = host;
        this.port = port;
        this.recipient = recipient;
        this.sender = sender;
        this.subject = subject;
        this.message = message;
    }

    public void close() {
        try {
            in.close();
            out.close();
            smtpSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void connect() {

    }

    protected String sendCommand(String commandString) {
        return null;
    }

    protected void doCommand(String commandString, char expectedResponseStart) {

    }

    protected void checkServerResponse(String response, char expectedResponseStart) {

    }

    protected String getResponse() {
        return null;
    }

    protected String getMessageHeaders() {
        return null;
    }

    public void sendMessage() {

    }


}
