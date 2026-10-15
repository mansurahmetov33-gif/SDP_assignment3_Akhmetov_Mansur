
package org.notificationgateway.notification;

import org.notificationgateway.sender.MessageSender;

public class UrgentNotification extends Notification {

    private final MessageSender fallbackSender;

    public UrgentNotification(
            MessageSender sender,
            MessageSender fallbackSender) {
        super(sender);
        this.fallbackSender = fallbackSender;
    }

    @Override
    public boolean send(String recipient, String message) {
        String urgentMessage = "[URGENT] " + message;

        if (sender.isAvailable()) {
            if (sender.sendMessage(recipient, "Urgent Notification",
                    urgentMessage)) {
                return true;
            }
        }

        System.out.println("Primary sender failed. Trying fallback...");

        if (fallbackSender != null && fallbackSender.isAvailable()) {
            return fallbackSender.sendMessage(
                    recipient, "Urgent Notification", urgentMessage);
        }

        System.out.println("All senders failed!");
        return false;
    }
}
