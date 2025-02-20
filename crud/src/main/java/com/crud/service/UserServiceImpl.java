package com.crud.service;

import com.crud.dto.*;
import com.crud.model.OTP;
import com.crud.model.User;
import com.crud.repositories.OtpRepository;
import com.crud.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    //    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final EmailService emailService;

    private final OtpRepository otpRepository;

    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository, EmailService emailService, OtpRepository otpRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.otpRepository = otpRepository;
    }


    @Override
    public ResponseEntity<?> registerUser(RegisterDTO registerDTO) {
        try {
            String userName = registerDTO.getUserName();
            Optional<User> data = userRepository.findByUsername(userName);

            if (data.isPresent()) {
                return ResponseEntity.status(400).body(new ErrorResponse("User Name is Taken"));
            }

            User toSave = modelMapper.map(registerDTO, User.class);
            User savedUser = userRepository.save(toSave);
            return ResponseEntity.ok(new SuccessResponse("Register Successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Register Failed"));
        }
    }

    @Override
    public ResponseEntity<?> login(LoginDTO loginDTO) {
        String userName = loginDTO.getUsername().trim();
        String password = loginDTO.getPassword().trim();

        Optional<User> userOptional = userRepository.findByUsername(userName);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("User does not exist"));
        }

        if (!userOptional.get().getPassword().matches(password)) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Password not match"));
        }
        return ResponseEntity.ok(new SuccessResponse("Login successful"));
    }

    public boolean validateOtp(OtpValidationRequest request) {
        String otp = request.getOtp();
        String email = request.getEmail();
        if (otp == null || otp.isEmpty()|| email == null || email.isEmpty()) {
            return false;
        }

        Optional<OTP> otpData = otpRepository.findByUserEmailAndOtpAndStatus(email, otp, 0);

        if (otpData.isPresent()) {
            OTP otpRecord = otpData.get();
            if (otpRecord.getExpirationDate().isBefore(LocalDateTime.now())) {
                return false;
            }

            otpRecord.setStatus(1); // Mark as used or validated
            otpRepository.save(otpRecord); // Save the updated OTP status to the database
            return true;
        }

        return false; // OTP not found or invalid
    }

    @Override
    public ResponseEntity<?> forgetPassword(String email) {
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("EMAIL_NOT_FOUND"));
        }

        int otp = otpGenerator();
        try {
            emailService.sendEmail(email, "Forget Password", otp);
            saveOtp(email, otp);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SuccessResponse("Success: LINK_SENT_ON_E-MAIL"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("FAILED_TO_SEND_EMAIL"));
        }
    }


    public int otpGenerator() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }

    @Override
    public void saveOtp(String email, int otp) {
        int OTP_VALID_DURATION = 20;
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + email));
        OTP newOtp = new OTP();
        newOtp.setUser(user);
        newOtp.setOtp(String.valueOf(otp));
        newOtp.setExpirationDate(LocalDateTime.now().plusMinutes(OTP_VALID_DURATION));
        newOtp.setStatus(0);
        otpRepository.save(newOtp);
    }

    @Override
    public ResponseEntity<?> verifyOTP(OtpValidationRequest request) {
        boolean isValid = validateOtp(request); // Renamed to 'isValid' for clarity

        if (isValid) {
            // OTP successfully validated
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("OTP verified successfully"));
        } else {
            // OTP validation failed
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid OTP"));
        }
    }

    @Override
    public ResponseEntity<?> passwordChange(ChangePassDTO changePassDTO) {
        if (!changePassDTO.getNewPassword().equals(changePassDTO.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Passwords do not match"));
        }

        User user = userRepository.findByEmail(changePassDTO.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + changePassDTO.getEmail()));

        user.setPassword(changePassDTO.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("Password changed successfully"));
    }

}
