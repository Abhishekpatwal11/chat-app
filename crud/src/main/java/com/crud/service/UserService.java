package com.crud.service;

import com.crud.dto.ChangePassDTO;
import com.crud.dto.LoginDTO;
import com.crud.dto.OtpValidationRequest;
import com.crud.dto.RegisterDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> registerUser(RegisterDTO registerDTO);

    ResponseEntity<?> login(LoginDTO loginDTO);

    ResponseEntity<?> forgetPassword(String email);

    void saveOtp(String email, int otp);

    ResponseEntity<?> verifyOTP(OtpValidationRequest request);

    ResponseEntity<?> passwordChange(ChangePassDTO changePassDTO);
}
