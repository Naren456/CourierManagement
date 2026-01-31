package com.courier.org.service;

import com.courier.org.dto.events.NotificationEvent;
import com.courier.org.dto.events.PackageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String NOTIFICATION_TOPIC = "notifications";
    private static final String PACKAGE_TOPIC = "package-events";

    public void sendNotification(NotificationEvent event) {
        log.info("Sending notification event to Kafka: {}", event);
        kafkaTemplate.send(NOTIFICATION_TOPIC, event.getTo(), event);
    }

    public void sendPackageEvent(PackageEvent event) {
        log.info("Sending package event to Kafka: {}", event);
        kafkaTemplate.send(PACKAGE_TOPIC, event.getPackageId(), event);
    }
}
