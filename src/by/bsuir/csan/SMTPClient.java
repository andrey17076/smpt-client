package by.bsuir.csan;


public class SMTPClient {

    public static void main(String[] args) {
        SMTPSession smtp = new SMTPSession("localhost", 25, "foo@bar.com",
                "baz@qux.com", "subj", "Message text bla bla");

        try {
            System.out.println("Sending e-mail...");
            smtp.sendMessage();
            System.out.println("E-mail sent");
        } finally {
            smtp.close();
        }
    }
}
