package com.courier.org.dto.events;

import com.courier.org.model.PackageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageEvent {
    private String packageId;
    private String trackingNumber;
    private PackageStatus status;
    private String userId;
    private String message;
}
