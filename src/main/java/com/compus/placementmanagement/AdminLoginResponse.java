package com.compus.placementmanagement;

public class AdminLoginResponse {

    private boolean success;
    private String message;
    private Long adminId;
    private String adminName;
    private String token;


    // =========================
    // GETTER AND SETTER - SUCCESS
    // =========================

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    // =========================
    // GETTER AND SETTER - MESSAGE
    // =========================

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    // =========================
    // GETTER AND SETTER - ADMIN ID
    // =========================

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }


    // =========================
    // GETTER AND SETTER - ADMIN NAME
    // =========================

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }


    // =========================
    // GETTER AND SETTER - TOKEN
    // =========================

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}