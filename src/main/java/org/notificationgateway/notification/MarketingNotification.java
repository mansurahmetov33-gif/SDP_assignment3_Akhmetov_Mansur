package org.notificationgateway.notification;

import org.notificationgateway.sender.MessageSender;

public class MarketingNotification extends Notification {

    public MarketingNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public boolean send(String recipient, String message) {
        String marketingMessage = "[PROMOTION] " + message;

        return sender.sendMessage(
                recipient,
                "Special Offer",
                marketingMessage
        );
    }
}
