package com.courier.org.dto;

import com.courier.org.model.PackageType;

public class PriceCalculationRequest {

    private Double weight;
    private PackageType packageType;

    public PriceCalculationRequest() {
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }
}
