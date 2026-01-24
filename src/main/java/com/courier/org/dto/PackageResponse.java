package com.courier.org.dto;

import com.courier.org.model.*;

import java.time.LocalDateTime;

public class PackageResponse {
    private String id;
    private String trackingNumber;
    private SenderDetails sender;
    private ReceiverDetails receiver;
    private PackageType packageType;
    private Double weight;
    private String description;
    private Double amount;
    private boolean paid;
    private PackageStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deliveredAt;

    public PackageResponse() {
    }

    public static PackageResponse fromEntity(CourierPackage pkg) {
        PackageResponse response = new PackageResponse();
        response.setId(pkg.getId());
        response.setTrackingNumber(pkg.getTrackingNumber());
        response.setSender(pkg.getSender());
        response.setReceiver(pkg.getReceiver());
        response.setPackageType(pkg.getPackageType());
        response.setWeight(pkg.getWeight());
        response.setDescription(pkg.getDescription());
        response.setAmount(pkg.getAmount());
        response.setPaid(pkg.isPaid());
        response.setStatus(pkg.getStatus());
        response.setCreatedAt(pkg.getCreatedAt());
        response.setUpdatedAt(pkg.getUpdatedAt());
        response.setDeliveredAt(pkg.getDeliveredAt());
        return response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public SenderDetails getSender() {
        return sender;
    }

    public void setSender(SenderDetails sender) {
        this.sender = sender;
    }

    public ReceiverDetails getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverDetails receiver) {
        this.receiver = receiver;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public PackageStatus getStatus() {
        return status;
    }

    public void setStatus(PackageStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
}
