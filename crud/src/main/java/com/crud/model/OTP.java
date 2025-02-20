package com.crud.model;


import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "OTP")
public class OTP {
    private long otpId;
    private User user;
    private String otp;
    private LocalDateTime expirationDate;
    private int status;


    public OTP() {
    }

    public OTP(long otpId, User user, String otp, LocalDateTime expirationDate, int status) {
        this.otpId = otpId;
        this.user = user;
        this.otp = otp;
        this.expirationDate = expirationDate;
        this.status = status;
    }

    public long getOtpId() {
        return otpId;
    }

    public void setOtpId(long otpId) {
        this.otpId = otpId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
