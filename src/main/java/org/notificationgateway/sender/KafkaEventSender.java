package org.notificationgateway.sender;

public class KafkaEventSender implements MessageSender {

    private boolean available = true;

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) {
            System.out.println("Kafka broker is unavailable!");
            return false;
        }

        System.out.println("=== Kafka Event ===");
        System.out.println("Topic: notifications");
        System.out.println("Event recipient: " + recipient);
        System.out.println("Event title: " + title);
        System.out.println("Event body: " + body);
        System.out.println("Event published successfully!");

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Kafka";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}