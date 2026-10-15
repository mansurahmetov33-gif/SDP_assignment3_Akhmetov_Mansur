package org.notificationgateway;

import org.notificationgateway.sender.*;
import org.notificationgateway.notification.*;

import java.util.List;

public class Main {

    public static void main(String[] args){
        MessageSender telegram = new TelegramMessageSender();
        MessageSender email = new EmailMessageSender();
        MessageSender sms = new SmsMessageSender();
        MessageSender push = new PushNotificationSender();

        Notification encrypted = new EncryptedNotification(email);

        encrypted.send("user@example.com", "My secret message");

        ScheduledBatchNotification batch = new ScheduledBatchNotification(sms);

        batch.sendToAll(
                List.of("+77000000001", "+77000000002", "+77000000003"),
                "Your appointment is tomorrow!");

        Notification urgent = new UrgentNotification(telegram, sms);

        urgent.send("user123", "Server is down!");

        urgent.setSender(email);
        urgent.send("user@example.com", "Server is down!");

        Notification marketing = new MarketingNotification(push);

        marketing.send("user123", "Get 20% off your next purchase!");
    }



}
