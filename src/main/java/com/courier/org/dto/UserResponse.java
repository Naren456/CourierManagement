package com.courier.org.dto;

public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private boolean active;

    public UserResponse() {
    }

    public UserResponse(String id, String name, String email, String phone, String role, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static UserResponseBuilder builder() {
        return new UserResponseBuilder();
    }

    public static class UserResponseBuilder {
        private String id;
        private String name;
        private String email;
        private String phone;
        private String role;
        private boolean active;

        public UserResponseBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserResponseBuilder role(String role) {
            this.role = role;
            return this;
        }

        public UserResponseBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(id, name, email, phone, role, active);
        }
    }
}
