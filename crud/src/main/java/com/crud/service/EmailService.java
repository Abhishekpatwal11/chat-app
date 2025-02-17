package com.crud.service;

import org.springframework.http.ResponseEntity;

public interface EmailService {
public void  sendEmail(String email, String body, int otp);
}
