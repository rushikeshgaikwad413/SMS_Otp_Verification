package com.sms;


import java.security.SecureRandom;

public class OtpUtil {

    private static final SecureRandom random = new SecureRandom();
    private static final String digits = "0123456789";

    public static String generateOtp(int length) {
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            otp.append(digits.charAt(random.nextInt(digits.length())));
        }
        return otp.toString();
    }
}

