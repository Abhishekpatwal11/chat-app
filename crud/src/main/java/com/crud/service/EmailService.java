package com.crud.service;

public interface EmailService {
  void sendEmail(String email, String body, int otp);
}
