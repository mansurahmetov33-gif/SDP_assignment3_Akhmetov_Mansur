package org.notificationgateway.sender;

public class TelegramMessageSender implements MessageSender {

    private boolean available = true;

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) {
            System.out.println("Telegram is unavailable!");
            return false;
        }

        System.out.println("Sending Telegram message to: " + recipient);
        System.out.println("Title: " + title);
        System.out.println("Message: " + body);

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Telegram";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
