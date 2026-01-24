package com.courier.org.dto;

import com.courier.org.model.PackageType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreatePackageRequest {

    @Valid
    @NotNull(message = "Sender details are required")
    private SenderDetailsDto sender;

    @Valid
    @NotNull(message = "Receiver details are required")
    private ReceiverDetailsDto receiver;

    @NotNull(message = "Package type is required")
    private PackageType packageType;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private Double weight;

    private String description;

    public CreatePackageRequest() {
    }

    public SenderDetailsDto getSender() {
        return sender;
    }

    public void setSender(SenderDetailsDto sender) {
        this.sender = sender;
    }

    public ReceiverDetailsDto getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverDetailsDto receiver) {
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

    public static class SenderDetailsDto {
        @NotBlank(message = "Sender name is required")
        private String name;
        @NotBlank(message = "Sender phone is required")
        private String phone;
        private String email;
        @Valid
        @NotNull(message = "Sender address is required")
        private AddressDto address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public AddressDto getAddress() {
            return address;
        }

        public void setAddress(AddressDto address) {
            this.address = address;
        }
    }

    public static class ReceiverDetailsDto {
        @NotBlank(message = "Receiver name is required")
        private String name;
        @NotBlank(message = "Receiver phone is required")
        private String phone;
        private String email;
        @Valid
        @NotNull(message = "Receiver address is required")
        private AddressDto address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public AddressDto getAddress() {
            return address;
        }

        public void setAddress(AddressDto address) {
            this.address = address;
        }
    }

    public static class AddressDto {
        @NotBlank(message = "Address line 1 is required")
        private String line1;
        private String line2;
        @NotBlank(message = "City is required")
        private String city;
        @NotBlank(message = "State is required")
        private String state;
        @NotBlank(message = "Country is required")
        private String country;
        @NotBlank(message = "Pincode is required")
        private String pincode;

        public String getLine1() {
            return line1;
        }

        public void setLine1(String line1) {
            this.line1 = line1;
        }

        public String getLine2() {
            return line2;
        }

        public void setLine2(String line2) {
            this.line2 = line2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }
    }
}
