package com.group24.easyHomes.service;

import com.group24.easyHomes.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotService {

    private final SendMailService sendMailService;
    private final AppUserRepository appUserRepository;

    @Autowired
    private final Environment env;

    public void setOtp(int otp) {
        this.otp = otp;
    }

    private int otp;

    public String verifyEmail(String userEmail){
        boolean userExists = appUserRepository.findByEmail(userEmail).isPresent();

        if(userExists){
            Random random = new Random();
            otp = random.nextInt(Integer.parseInt(Objects.requireNonNull(env.getProperty("random.number.bound"))));

            String message = env.getProperty("email.body") + otp;
            sendMailService.send(userEmail, message);
            return env.getProperty("verify.success");
        }else{
            return env.getProperty("verify.fail");
        }
    }

    public String verifyOTP(int userOTP){
        if(userOTP == otp){
            return env.getProperty("verify.otp.success");
        }else{
            return env.getProperty("verify.otp.fail");
        }
    }

    public String newPassword(String email, String newPassword) {

        String password = new BCryptPasswordEncoder().encode(newPassword);
        appUserRepository.setNewPassword(email,password);
        return "Password changed.";
    }


}
