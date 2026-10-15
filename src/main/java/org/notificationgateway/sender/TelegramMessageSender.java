package org.notificationgateway.sender;

public class TelegramMessageSender implements MessageSender {

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
        return true;
    }

    @Override
    public String getChannelName() {
        return "Telegram";
    }
}