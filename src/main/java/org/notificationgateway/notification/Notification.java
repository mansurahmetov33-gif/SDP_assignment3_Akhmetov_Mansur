
package org.notificationgateway.notification;

import org.notificationgateway.sender.MessageSender;

public abstract class Notification {

    protected MessageSender sender;

    public Notification(MessageSender sender) {
        this.sender = sender;
    }

    public void setSender(MessageSender sender) {
        this.sender = sender;
    }

    public abstract boolean send(String recipient, String message);
}
