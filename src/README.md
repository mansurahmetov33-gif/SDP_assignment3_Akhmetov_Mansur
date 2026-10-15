# Multi-Channel Notification Gateway

## 1. Project Overview

Multi-Channel Notification Gateway is a Java application developed to demonstrate the **Bridge Design Pattern**.

The system allows different notification types to send messages through multiple communication providers without creating separate classes for every possible combination.

The project uses simulated message delivery, so no real Telegram, Email, SMS, Kafka, or Push APIs are required.

**Technologies:**
- Java 17+
- Object-Oriented Programming (OOP)
- Bridge Design Pattern
- JUnit 5
- PlantUML

## 2. Bridge Design Pattern

The Bridge Pattern separates an abstraction from its implementation so that both can be developed and extended independently.

In this project, the pattern consists of two independent dimensions:

**Abstraction — Notification Types**
- `Notification` — abstract base class
- `UrgentNotification`
- `EncryptedNotification`
- `ScheduledBatchNotification`
- `MarketingNotification`

**Implementation — Message Providers**
- `MessageSender` — interface
- `TelegramMessageSender`
- `EmailMessageSender`
- `SmsMessageSender`
- `KafkaEventSender`
- `PushNotificationSender`

The abstract `Notification` class contains a reference to the `MessageSender` interface:

```java
protected MessageSender sender;
```

This reference forms the Bridge between notification behavior and message delivery.

As a result, notification types can use different providers without modifying their internal logic.

## 3. Project Structure

```text
Multi-Channel-Notification-Gateway/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── org/
│   │           └── notificationgateway/
│   │               ├── notification/
│   │               │   ├── Notification.java
│   │               │   ├── UrgentNotification.java
│   │               │   ├── EncryptedNotification.java
│   │               │   ├── ScheduledBatchNotification.java
│   │               │   └── MarketingNotification.java
│   │               ├── sender/
│   │               │   ├── MessageSender.java
│   │               │   ├── TelegramMessageSender.java
│   │               │   ├── EmailMessageSender.java
│   │               │   ├── SmsMessageSender.java
│   │               │   ├── KafkaEventSender.java
│   │               │   └── PushNotificationSender.java
│   │               └── Main.java
│   └── test/
│       └── java/
│           └── org/
│               └── notificationgateway/
│                   └── NotificationGatewayTest.java
├── uml.puml
├── pom.xml
└── README.md
```

## 4. MessageSender Interface

The `MessageSender` interface defines the common operations for all message providers.

```java
public interface MessageSender {
    boolean sendMessage(String recipient, String title, String body);
    boolean isAvailable();
    String getChannelName();
}
```

Every provider implements these methods.

| Provider | Functionality |
|---|---|
| TelegramMessageSender | Simulates sending Telegram messages |
| EmailMessageSender | Simulates sending emails with a title and signature |
| SmsMessageSender | Simulates SMS delivery with a 160-character limit |
| KafkaEventSender | Simulates publishing notification events |
| PushNotificationSender | Simulates sending push notifications |

Providers can also simulate availability changes using `setAvailable()` when implemented.

## 5. Notification Types

### UrgentNotification

Adds the `[URGENT]` prefix to messages.

If the primary provider is unavailable or message delivery fails, the notification attempts to use a fallback provider.

### EncryptedNotification

Transforms the original message using Base64 encoding before sending it.

Base64 is used for demonstration purposes and is not cryptographically secure encryption.

### ScheduledBatchNotification

Sends the same message to multiple recipients.

After processing, it displays a summary containing:
- Total recipients
- Successful deliveries
- Failed deliveries

### MarketingNotification

Adds the `[PROMOTION]` prefix to the message and uses the title `Special Offer`.

This class demonstrates how a new notification type can be added without modifying existing message providers.

## 6. Main Features

The application demonstrates:

1. Sending an urgent notification through Telegram.
2. Sending an encoded notification through Email.
3. Sending batch notifications through SMS.
4. Switching the provider from Telegram to Email at runtime.
5. Using SMS as a fallback when Telegram is unavailable.
6. Sending a marketing notification through Push.
7. Handling unavailable providers and failed message delivery.

## 7. Runtime Provider Switching

The Bridge Pattern allows the provider to be changed while the program is running.

Example:

```java
MessageSender telegram = new TelegramMessageSender();
MessageSender email = new EmailMessageSender();
MessageSender sms = new SmsMessageSender();

Notification notification = new UrgentNotification(telegram, sms);

notification.send("user123", "Server is down!");

notification.setSender(email);

notification.send("user123", "Server is down!");
```

The same notification object first uses Telegram and then Email.

No new notification class is required.

## 8. Fallback Mechanism

`UrgentNotification` supports a fallback provider.

Example:

```java
TelegramMessageSender telegram = new TelegramMessageSender();
SmsMessageSender sms = new SmsMessageSender();

telegram.setAvailable(false);

UrgentNotification urgent = new UrgentNotification(telegram, sms);

urgent.send("+77001234567", "System failure!");
```

Because Telegram is unavailable, the notification attempts to send the message through SMS.

The fallback mechanism works through the `MessageSender` interface without using `instanceof`.

## 9. How to Run

### Requirements

- Java Development Kit (JDK) 17 or newer
- IntelliJ IDEA or another Java IDE
- Maven for dependency management
- JUnit 5 for unit testing

### Steps

1. Clone or download the repository.
2. Open the project in IntelliJ IDEA.
3. Configure JDK 17 or newer.
4. Load the Maven dependencies from `pom.xml`.
5. Open `Main.java`.
6. Run the `main()` method to see the notification demonstrations.

No external services or API credentials are required.

## 10. Unit Testing

The project uses **JUnit 5** for unit testing.

The test cases cover the following scenarios:

1. Successful Telegram message delivery.
2. Message delivery when Telegram is unavailable.
3. Fallback when the primary provider is unavailable.
4. Fallback when the primary provider fails.
5. SMS rejection when the message exceeds 160 characters.
6. Successful SMS delivery at the 160-character limit.
7. Base64 message transformation.
8. Batch notification processing and summary.
9. Runtime provider switching.
10. Push notification delivery.
11. Marketing notification formatting.
12. Failure when both the primary and fallback providers are unavailable.

Tests are located in:

`src/test/java/org/notificationgateway/NotificationGatewayTest.java`

To run tests using Maven:

```bash
mvn test
```

Alternatively, run `NotificationGatewayTest.java` directly in IntelliJ IDEA.

## 11. UML Class Diagram

The UML diagram is provided in the `uml.puml` file and was created using PlantUML.

It illustrates:
- The abstract `Notification` class
- The `MessageSender` interface
- Concrete notification types
- Concrete message providers
- Inheritance and interface implementation relationships
- The Bridge between notifications and providers
- The fallback provider relationship

The diagram can be viewed using a PlantUML plugin in IntelliJ IDEA or through the PlantUML online editor.

## 12. Design Questions

### 1. What are the two independent dimensions?

The two independent dimensions are **notification types** and **message providers**.

Notification types determine what behavior is applied to a message, such as adding an urgent prefix, transforming the message, or sending it to multiple recipients.

Message providers determine how the message is delivered, such as through Telegram, Email, SMS, Kafka, or Push.

Both dimensions can be extended independently.

### 2. Where exactly is the Bridge?

The Bridge is the reference to the `MessageSender` interface inside the abstract `Notification` class:

```java
protected MessageSender sender;
```

This reference connects the notification abstraction with the message delivery implementation.

The `Notification` class does not depend on a specific provider, which allows different notification types to work with different sending channels.

### 3. Why is inheritance problematic here?

Using inheritance alone would require creating a separate class for every combination of notification type and provider.

For example:
- `UrgentTelegramNotification`
- `UrgentEmailNotification`
- `EncryptedTelegramNotification`
- `EncryptedEmailNotification`

As the number of notification types and providers increases, the number of classes grows significantly.

With 4 notification types and 5 providers, inheritance-based combinations could require 20 separate classes.

The Bridge Pattern avoids this class explosion by separating notification behavior from message delivery.

### 4. What happens when a new provider is added?

A new provider can be added by creating a class that implements the `MessageSender` interface.

Existing notification classes do not need to be modified.

For example, `PushNotificationSender` was added by implementing `MessageSender`.

It can be used with `UrgentNotification`, `EncryptedNotification`, `ScheduledBatchNotification`, or `MarketingNotification`.

This follows the **Open/Closed Principle**, because existing notification classes remain unchanged while the system is extended.

### 5. What happens when a new Notification type is added?

A new notification type can be added by creating a class that extends the abstract `Notification` class and implements its `send()` method.

Existing message providers do not need to be modified.

For example, `MarketingNotification` was added to apply promotional formatting to messages.

It can use Telegram, Email, SMS, Kafka, or Push through the same `MessageSender` interface.

This demonstrates independent extension of the abstraction hierarchy.

### 6. Why is this Bridge rather than Adapter?

The Bridge Pattern separates two independent dimensions of a system so that both can evolve independently.

In this project, notification types and message providers are designed as separate hierarchies connected through the `MessageSender` interface.

The Adapter Pattern has a different purpose: it allows classes with incompatible interfaces to work together.

This project is not adapting an incompatible existing interface. Instead, it is deliberately separating notification behavior from message delivery.

Therefore, **Bridge is the appropriate design pattern**.

## 13. Conclusion

The Multi-Channel Notification Gateway demonstrates the practical application of the Bridge Design Pattern in Java.

By separating notification types from message providers, the system supports runtime provider switching, fallback delivery, batch processing, message transformation, and independent extension of both hierarchies.

This approach reduces unnecessary subclasses, improves maintainability, and makes the application easier to extend.
