package com.courier.org.service;

import com.courier.org.dto.*;
import com.courier.org.dto.events.NotificationEvent;
import com.courier.org.dto.events.PackageEvent;
import com.courier.org.exception.ResourceNotFoundException;
import com.courier.org.model.*;
import com.courier.org.repository.PackageRepository;
import com.courier.org.repository.TrackingEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final KafkaProducerService kafkaProducerService;
    private final java.security.SecureRandom random = new java.security.SecureRandom();

    public PackageService(PackageRepository packageRepository,
            TrackingEventRepository trackingEventRepository,
            KafkaProducerService kafkaProducerService) {
        this.packageRepository = packageRepository;
        this.trackingEventRepository = trackingEventRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    public PackageResponse createPackage(CreatePackageRequest request, String userId) {
        CourierPackage pkg = new CourierPackage();
        pkg.setTrackingNumber(generateTrackingNumber());
        pkg.setUserId(userId);
        pkg.setSender(mapToSenderDetails(request.getSender()));
        pkg.setReceiver(mapToReceiverDetails(request.getReceiver()));
        pkg.setPackageType(request.getPackageType());
        pkg.setWeight(request.getWeight());
        pkg.setDescription(request.getDescription());
        pkg.setAmount(calculatePrice(request.getWeight(), request.getPackageType()));
        pkg.setStatus(PackageStatus.CREATED);
        pkg.setDeliveryOtp(generateOtp());

        CourierPackage saved = packageRepository.save(pkg);
        createTrackingEvent(saved, "Package created");

        // Send OTP email via Kafka
        NotificationEvent notificationEvent = new NotificationEvent(
                saved.getSender().getEmail(),
                "Delivery OTP for your package: " + saved.getTrackingNumber(),
                "Dear Customer,\n\nYour package with tracking number " + saved.getTrackingNumber() +
                        " has been booked.\nOTP: " + saved.getDeliveryOtp(),
                "OTP");
        kafkaProducerService.sendNotification(notificationEvent);

        // Also send package update event
        kafkaProducerService.sendPackageEvent(new PackageEvent(
                saved.getId(),
                saved.getTrackingNumber(),
                saved.getStatus(),
                saved.getUserId(),
                "Package created"));

        return PackageResponse.fromEntity(saved);
    }

    private String generateOtp() {
        return String.format("%06d", random.nextInt(1000000));
    }

    public PackageResponse getPackageById(String id) {
        CourierPackage pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
        return PackageResponse.fromEntity(pkg);
    }

    public PackageResponse getPackageByTrackingNumber(String trackingNumber) {
        CourierPackage pkg = packageRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Package not found with tracking number: " + trackingNumber));
        return PackageResponse.fromEntity(pkg);
    }

    public List<PackageResponse> getPackagesByUserId(String userId) {
        return packageRepository.findByUserId(userId).stream()
                .map(PackageResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<PackageResponse> getPackagesByUserId(String userId, Pageable pageable) {
        return packageRepository.findByUserId(userId, pageable)
                .map(PackageResponse::fromEntity);
    }

    public Page<PackageResponse> getAllPackages(Pageable pageable) {
        return packageRepository.findAll(pageable)
                .map(PackageResponse::fromEntity);
    }

    public PackageResponse updatePackage(String id, UpdatePackageRequest request, String userId) {
        CourierPackage pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));

        if (!pkg.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to update this package");
        }

        if (pkg.getStatus() != PackageStatus.CREATED) {
            throw new RuntimeException("Cannot update package after it has been shipped");
        }

        if (request.getReceiver() != null) {
            pkg.setReceiver(mapToReceiverDetails(request.getReceiver()));
        }
        if (request.getPackageType() != null) {
            pkg.setPackageType(request.getPackageType());
        }
        if (request.getWeight() != null) {
            pkg.setWeight(request.getWeight());
        }
        if (request.getDescription() != null) {
            pkg.setDescription(request.getDescription());
        }

        pkg.setAmount(calculatePrice(pkg.getWeight(), pkg.getPackageType()));
        pkg.setUpdatedAt(LocalDateTime.now());

        CourierPackage saved = packageRepository.save(pkg);
        return PackageResponse.fromEntity(saved);
    }

    public PackageResponse updateStatus(String id, PackageStatus status, String location, String remarks, String otp) {
        CourierPackage pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));

        if (status == PackageStatus.DELIVERED) {
            if (pkg.getDeliveryOtp() == null || !pkg.getDeliveryOtp().equals(otp)) {
                throw new RuntimeException("Invalid or missing Delivery OTP. Verification failed.");
            }
            pkg.setDeliveredAt(LocalDateTime.now());
        }

        pkg.setStatus(status);
        pkg.setUpdatedAt(LocalDateTime.now());

        CourierPackage saved = packageRepository.save(pkg);
        createTrackingEvent(saved, location, remarks);

        // Send update event to Kafka
        kafkaProducerService.sendPackageEvent(new PackageEvent(
                saved.getId(),
                saved.getTrackingNumber(),
                saved.getStatus(),
                null,
                remarks));

        return PackageResponse.fromEntity(saved);
    }

    private void createTrackingEvent(CourierPackage pkg, String remarks) {
        createTrackingEvent(pkg, null, remarks);
    }

    private void createTrackingEvent(CourierPackage pkg, String location, String remarks) {
        com.courier.org.model.TrackingEvent event = new com.courier.org.model.TrackingEvent(
                pkg.getId(),
                pkg.getTrackingNumber(),
                pkg.getStatus(),
                location,
                remarks);
        trackingEventRepository.save(event);
    }

    public PackageResponse cancelPackage(String id, String userId) {
        CourierPackage pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));

        if (!pkg.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to cancel this package");
        }

        if (pkg.getStatus() == PackageStatus.DELIVERED || pkg.getStatus() == PackageStatus.CANCELLED) {
            throw new RuntimeException("Cannot cancel package with status: " + pkg.getStatus());
        }

        pkg.setStatus(PackageStatus.CANCELLED);
        pkg.setUpdatedAt(LocalDateTime.now());

        CourierPackage saved = packageRepository.save(pkg);
        return PackageResponse.fromEntity(saved);
    }

    public PriceCalculationResponse calculatePriceDetails(Double weight, PackageType packageType) {
        double basePrice = getBasePrice(packageType);
        double weightCharge = weight * getWeightRate(packageType);
        double totalAmount = basePrice + weightCharge;

        return new PriceCalculationResponse(weight, packageType, basePrice, weightCharge, totalAmount);
    }

    private Double calculatePrice(Double weight, PackageType packageType) {
        double basePrice = getBasePrice(packageType);
        double weightCharge = weight * getWeightRate(packageType);
        return basePrice + weightCharge;
    }

    private double getBasePrice(PackageType type) {
        return switch (type) {
            case NORMAL_POST -> 50.0;
            case SPEED_POST -> 100.0;
            case EXPRESS -> 200.0;
            case OVERNIGHT -> 500.0;
        };
    }

    private double getWeightRate(PackageType type) {
        return switch (type) {
            case NORMAL_POST -> 10.0;
            case SPEED_POST -> 20.0;
            case EXPRESS -> 40.0;
            case OVERNIGHT -> 80.0;
        };
    }

    private String generateTrackingNumber() {
        return "CMS" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    private SenderDetails mapToSenderDetails(CreatePackageRequest.SenderDetailsDto dto) {
        Address address = new Address(
                dto.getAddress().getLine1(),
                dto.getAddress().getLine2(),
                dto.getAddress().getCity(),
                dto.getAddress().getState(),
                dto.getAddress().getCountry(),
                dto.getAddress().getPincode());
        return new SenderDetails(dto.getName(), dto.getPhone(), dto.getEmail(), address);
    }

    private ReceiverDetails mapToReceiverDetails(CreatePackageRequest.ReceiverDetailsDto dto) {
        Address address = new Address(
                dto.getAddress().getLine1(),
                dto.getAddress().getLine2(),
                dto.getAddress().getCity(),
                dto.getAddress().getState(),
                dto.getAddress().getCountry(),
                dto.getAddress().getPincode());
        return new ReceiverDetails(dto.getName(), dto.getPhone(), dto.getEmail(), address);
    }
}
