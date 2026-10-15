package org.notificationgateway.sender;

public class EmailMessageSender implements MessageSender {

    private boolean available = true;

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) {
            System.out.println("Email is unavailable!");
            return false;
        }

        System.out.println("=== Email ===");
        System.out.println("To: " + recipient);
        System.out.println("Subject: " + title);
        System.out.println("Body: " + body);
        System.out.println("Best regards,");
        System.out.println("Notification Gateway");
        System.out.println("Email sent successfully!");

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Email";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}