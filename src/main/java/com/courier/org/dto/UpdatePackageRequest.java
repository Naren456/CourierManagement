package com.courier.org.dto;

import com.courier.org.model.PackageType;
import jakarta.validation.Valid;

public class UpdatePackageRequest {

    @Valid
    private CreatePackageRequest.ReceiverDetailsDto receiver;

    private PackageType packageType;
    private Double weight;
    private String description;

    public UpdatePackageRequest() {
    }

    public CreatePackageRequest.ReceiverDetailsDto getReceiver() {
        return receiver;
    }

    public void setReceiver(CreatePackageRequest.ReceiverDetailsDto receiver) {
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
}
