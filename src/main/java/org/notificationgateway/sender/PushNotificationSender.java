package org.notificationgateway.sender;

public class PushNotificationSender implements MessageSender {

    private boolean available = true;

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) {
            System.out.println("Push notification service is unavailable!");
            return false;
        }

        System.out.println("=== Push Notification ===");
        System.out.println("Device/User: " + recipient);
        System.out.println("Title: " + title);
        System.out.println("Notification: " + body);
        System.out.println("Push notification sent successfully!");

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Push";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}