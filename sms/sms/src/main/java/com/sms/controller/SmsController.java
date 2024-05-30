package com.sms.controller;

import com.sms.OtpUtil;
import com.sms.dto.SmsDto;
import com.sms.entity.SmsEntity;
import com.sms.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sms")
public class SmsController {

    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private SmsService smsService;

    @PostMapping("/send")
    public String sendMessage(@RequestBody SmsDto smsDto) {
        logger.info("Program Started....");

        // Generate random OTP
        String otpmessage = OtpUtil.generateOtp(6); // For example, generate a 6-digit OTP
        logger.info("Generated OTP: {}", otpmessage);

        String apiKey = "QYX1V75W9cI3fhegGSonlzrktEBOjKZb4CuvpxPdiN68JUFLDM6EBkZzrWM3FdHTaLo8epOJ7vRQDqnV";
        String number = String.valueOf(smsDto.getMobNumber());

        smsService.sendSms(otpmessage, number, apiKey);

        // Save OTP in database
        SmsEntity smsEntity = new SmsEntity();
        smsEntity.setMobNumber(smsDto.getMobNumber());
        smsEntity.setOtp(otpmessage);
        smsService.saveOtp(smsEntity);

        return "OTP sent successfully";
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestBody SmsDto smsDto) {
        boolean isValid = smsService.verifyOtp(smsDto);
        if (isValid) {
            return "OTP is valid";
        } else {
            return "OTP is not valid";
        }
    }
}