package com.crud.model;


import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class OTP {
    private long otpId;
    private User user;
    private String otp;
    private LocalDateTime experationDate;
    private int status;

    public OTP(LocalDateTime experationDate, long otpId, User user, String otp, int status) {
        this.experationDate = experationDate;
        this.otpId = otpId;
        this.user = user;
        this.otp = otp;
        this.status = status;
    }

    public OTP() {
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

    public LocalDateTime getExperationDate() {
        return experationDate;
    }

    public void setExperationDate(LocalDateTime experationDate) {
        this.experationDate = experationDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
