
package org.notificationgateway;

import org.junit.jupiter.api.Test;
import org.notificationgateway.notification.*;
import org.notificationgateway.sender.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NotificationGatewayTest {

    static class RecordingSender implements MessageSender {
        private boolean available = true;
        private final Set<String> failedRecipients;
        private final List<String> recipients = new ArrayList<>();
        private String lastBody;
        private String lastTitle;

        RecordingSender() {
            this(Set.of());
        }

        RecordingSender(Set<String> failedRecipients) {
            this.failedRecipients = failedRecipients;
        }

        @Override
        public boolean sendMessage(String recipient, String title, String body) {
            recipients.add(recipient);
            lastTitle = title;
            lastBody = body;

            return available && !failedRecipients.contains(recipient);
        }

        @Override
        public boolean isAvailable() {
            return available;
        }

        @Override
        public String getChannelName() {
            return "Test";
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }
    }

    @Test
    void telegramSendsMessageSuccessfully() {
        TelegramMessageSender sender = new TelegramMessageSender();

        assertTrue(sender.sendMessage("user123", "Alert", "Hello"));
    }

    @Test
    void unavailableTelegramFails() {
        TelegramMessageSender sender = new TelegramMessageSender();
        sender.setAvailable(false);

        assertFalse(sender.sendMessage("user123", "Alert", "Hello"));
    }

    @Test
    void urgentNotificationUsesFallbackWhenPrimaryUnavailable() {
        TelegramMessageSender telegram = new TelegramMessageSender();
        telegram.setAvailable(false);

        RecordingSender fallback = new RecordingSender();
        UrgentNotification notification =
                new UrgentNotification(telegram, fallback);

        assertTrue(notification.send("user123", "Server is down!"));
        assertEquals("[URGENT] Server is down!", fallback.lastBody);
    }

    @Test
    void urgentNotificationUsesFallbackWhenPrimarySendFails() {
        RecordingSender primary =
                new RecordingSender(Set.of("user123"));
        RecordingSender fallback = new RecordingSender();

        UrgentNotification notification =
                new UrgentNotification(primary, fallback);

        assertTrue(notification.send("user123", "Important"));
        assertEquals("[URGENT] Important", fallback.lastBody);
    }

    @Test
    void smsRejectsMessageLongerThan160Characters() {
        SmsMessageSender sender = new SmsMessageSender();

        assertFalse(sender.sendMessage(
                "+77001234567", "", "a".repeat(161)
        ));
    }

    @Test
    void smsAcceptsMessageOfExactly160Characters() {
        SmsMessageSender sender = new SmsMessageSender();

        assertTrue(sender.sendMessage(
                "+77001234567", "", "a".repeat(160)
        ));
    }

    @Test
    void encryptedNotificationSendsBase64EncodedMessage() {
        RecordingSender sender = new RecordingSender();
        EncryptedNotification notification =
                new EncryptedNotification(sender);

        assertTrue(notification.send("user123", "Secret"));

        String expected = Base64.getEncoder().encodeToString(
                "Secret".getBytes(StandardCharsets.UTF_8)
        );

        assertEquals(expected, sender.lastBody);
    }

    @Test
    void batchNotificationSendsToEveryRecipientAndReportsResults() {
        RecordingSender sender =
                new RecordingSender(Set.of("user2"));
        ScheduledBatchNotification batch =
                new ScheduledBatchNotification(sender);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(
                    output, true, StandardCharsets.UTF_8
            ));

            batch.sendToAll(
                    List.of("user1", "user2", "user3"),
                    "Meeting tomorrow"
            );
        } finally {
            System.setOut(originalOut);
        }

        assertEquals(
                List.of("user1", "user2", "user3"),
                sender.recipients
        );

        String summary = output.toString(StandardCharsets.UTF_8);
        assertTrue(summary.contains("Total: 3"));
        assertTrue(summary.contains("Successful: 2"));
        assertTrue(summary.contains("Failed: 1"));
    }

    @Test
    void changingSenderChangesDeliveryChannel() {
        RecordingSender first = new RecordingSender();
        RecordingSender second = new RecordingSender();

        Notification notification =
                new MarketingNotification(first);

        notification.send("user123", "Sale");
        notification.setSender(second);
        notification.send("user456", "Sale");

        assertEquals(List.of("user123"), first.recipients);
        assertEquals(List.of("user456"), second.recipients);
    }

    @Test
    void pushSenderSendsSuccessfully() {
        PushNotificationSender sender = new PushNotificationSender();

        assertTrue(sender.sendMessage(
                "device123", "Welcome", "Hello!"
        ));
    }

    @Test
    void marketingNotificationAddsPromotionPrefix() {
        RecordingSender sender = new RecordingSender();
        MarketingNotification notification =
                new MarketingNotification(sender);

        assertTrue(notification.send("user123", "20% discount"));

        assertEquals("[PROMOTION] 20% discount", sender.lastBody);
        assertEquals("Special Offer", sender.lastTitle);
    }

    @Test
    void urgentNotificationFailsWhenBothSendersUnavailable() {
        TelegramMessageSender telegram = new TelegramMessageSender();
        telegram.setAvailable(false);

        SmsMessageSender sms = new SmsMessageSender();
        sms.setAvailable(false);

        UrgentNotification notification =
                new UrgentNotification(telegram, sms);

        assertFalse(notification.send("user123", "Emergency"));
    }
}
