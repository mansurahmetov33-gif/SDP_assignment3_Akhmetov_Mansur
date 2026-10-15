package org.notificationgateway.notification;

import org.notificationgateway.sender.MessageSender;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptedNotification extends Notification {

    public EncryptedNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public boolean send(String recipient, String message) {
        String encryptedMessage = Base64.getEncoder()
                .encodeToString(message.getBytes(StandardCharsets.UTF_8));

        return sender.sendMessage(
                recipient,
                "Encrypted Notification",
                encryptedMessage
        );
    }
}
