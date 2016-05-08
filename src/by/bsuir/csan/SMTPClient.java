package by.bsuir.csan;

import java.io.IOException;

public class SMTPClient {

    public static void main(String[] args) {
        SMTPSession smtp = new SMTPSession("localhost", 25, "foo@bar.com",
                "baz@qux.com", "subj", "Message text bla bla");

        try {
            System.out.println("Sending e-mail...");
            smtp.sendMessage();
            System.out.println("E-mail sent");
        } catch (IOException e) {
            smtp.close();
            System.out.println("Can't send e-mail!");
            e.printStackTrace();
        }
    }
}
