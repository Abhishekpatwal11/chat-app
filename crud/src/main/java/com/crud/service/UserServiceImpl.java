package com.crud.service;

import com.crud.Exception.ResourceNotFoundException;
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
            Optional<User> data = userRepository.findByUserName(userName);

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
        String userName = loginDTO.getUserName().trim();
        String password = loginDTO.getPassword().trim();

        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("User does not exist"));
        }

        if (!userOptional.get().getPassword().matches(password)) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Password not match"));
        }
        return ResponseEntity.ok(new SuccessResponse("Login successful"));
    }



    @Override
    public ResponseEntity<?> forgetPassword(String email) {
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (!checkEmail.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("EMAIL_NOT_FOUND"));
        }

        int otp = otpGenerator();
        emailService.sendEmail(email, "Forget Password", otp);
        saveOtp(email, otp);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("success", "EMAIL_VERIFIED,LINK_SENT_ON_E-MAIL"));
    }

    public int otpGenerator() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }

    @Override
    public void saveOtp(String email, int otp) {
        int OTP_VALID_DURATION = 20;
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->new ResourceNotFoundException("USER_NOT_FOUND"));

        OTP newOtp = new OTP();
        newOtp.setUser(user);
        newOtp.setOtp(String.valueOf(otp));
        newOtp.setExperationDate(LocalDateTime.now().plusMinutes(OTP_VALID_DURATION));
        newOtp.setStatus(0);
        otpRepository.save(newOtp);

    }
}
