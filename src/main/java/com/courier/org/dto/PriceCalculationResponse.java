package com.courier.org.dto;

import com.courier.org.model.PackageType;

public class PriceCalculationResponse {

    private Double weight;
    private PackageType packageType;
    private Double basePrice;
    private Double weightCharge;
    private Double totalAmount;

    public PriceCalculationResponse() {
    }

    public PriceCalculationResponse(Double weight, PackageType packageType, Double basePrice, Double weightCharge,
            Double totalAmount) {
        this.weight = weight;
        this.packageType = packageType;
        this.basePrice = basePrice;
        this.weightCharge = weightCharge;
        this.totalAmount = totalAmount;
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

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getWeightCharge() {
        return weightCharge;
    }

    public void setWeightCharge(Double weightCharge) {
        this.weightCharge = weightCharge;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
