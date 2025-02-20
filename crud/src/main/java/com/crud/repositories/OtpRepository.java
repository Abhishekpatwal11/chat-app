package com.crud.repositories;

import com.crud.model.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OtpRepository extends MongoRepository<OTP,String> {

    Optional<OTP> findByUserEmailAndOtpAndStatus(String email, String otp, int i);
}
