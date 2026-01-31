package com.courier.org.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private String to;
    private String subject;
    private String body;
    private String type; // e.g., "OTP", "STATUS_UPDATE"
}
