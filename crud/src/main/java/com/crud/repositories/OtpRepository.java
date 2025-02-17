package com.crud.repositories;

import com.crud.model.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends MongoRepository<OTP,Long> {
    public void findByOtp(String Otp);
}
