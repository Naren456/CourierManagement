package com.courier.org.service;

import com.courier.org.dto.events.NotificationEvent;
import com.courier.org.dto.events.PackageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final EmailService emailService;

    @KafkaListener(topics = "notifications", groupId = "courier-group")
    public void consumeNotification(NotificationEvent event) {
        log.info("Consumed notification event from Kafka: {}", event);
        try {
            emailService.sendEmail(event.getTo(), event.getSubject(), event.getBody());
        } catch (Exception e) {
            log.error("Failed to process notification event: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "package-events", groupId = "courier-group")
    public void consumePackageEvent(PackageEvent event) {
        log.info("Consumed package event from Kafka: {}", event);
    }
}
