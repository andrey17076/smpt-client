package by.bsuir.csan;

import java.io.*;
import java.net.Socket;
import java.util.Date;

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

    protected void connect() throws IOException {
        smtpSocket = new Socket(host, port);
        smtpSocket.setSoTimeout(SOCKET_READ_TIMEOUT);
        in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
        out = new OutputStreamWriter(smtpSocket.getOutputStream());
    }

    protected String sendCommand(String commandString) throws IOException {
        out.write(commandString + "\n");
        out.flush();
        String response = getResponse();
        return response;

    }

    protected void doCommand(String commandString, char expectedResponseStart) throws IOException {
        String response = sendCommand(commandString);
        checkServerResponse(response, expectedResponseStart);
    }

    protected void checkServerResponse(String response, char expectedResponseStart) throws IOException {
        if (response.charAt(0) != expectedResponseStart)
            throw new IOException(response);
    }

    protected String getResponse() throws IOException {
        String response = "";

        String line;

        do {
            line = in.readLine();
            if ((line == null) || (line.length() < 3))
                throw new IOException("Bad response from server");
            response += line + "\n";
        } while ((line.length() > 3) && (line.charAt(3) == '-'));

        System.out.println("Server response: " + response); //debug
        return response;
    }

    protected String getMessageHeaders() {
        String headers = "";
        headers += "Date: " + new Date().toString() + "\n";
        headers += "Sender: " + sender + "\n";
        headers += "From: " + sender + "\n";
        headers += "To: " + recipient + "\n";
        headers += "Subject: " + subject +"\n";

        return headers + "\n\n";
    }

    public void sendMessage() throws IOException {
        connect();

        String response = getResponse();
        checkServerResponse(response, '2');

        doCommand("HELO localhost", '2');
        doCommand("MAIL FROM: <" + sender + ">", '2');
        doCommand("RCPT TO: <" + recipient + ">", '2');

        doCommand("DATA", '3');

        out.write(getMessageHeaders());
        BufferedReader msgBodyReader = new BufferedReader(new StringReader(message));
        String line;

        while ((line = msgBodyReader.readLine()) != null) {
            if (line.startsWith("."))
                out.write('.');
            out.write(line + "\n");
        }

        doCommand(".", '2');

        close();
    }
}
