
package org.notificationgateway.notification;

import org.notificationgateway.sender.MessageSender;
import java.util.List;

public class ScheduledBatchNotification extends Notification {

    public ScheduledBatchNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public boolean send(String recipient, String message) {
        return sender.sendMessage(
                recipient,
                "Batch Notification",
                message
        );
    }

    public void sendToAll(List<String> recipients, String message) {
        int successful = 0;
        int failed = 0;

        for (String recipient : recipients) {
            boolean sent = send(recipient, message);

            if (sent) {
                successful++;
            } else {
                failed++;
            }
        }

        System.out.println("\n=== Batch Summary ===");
        System.out.println("Total: " + recipients.size());
        System.out.println("Successful: " + successful);
        System.out.println("Failed: " + failed);
    }
}
