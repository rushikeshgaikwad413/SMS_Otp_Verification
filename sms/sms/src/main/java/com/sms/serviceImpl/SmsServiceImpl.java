package com.sms.serviceImpl;
import com.sms.dto.SmsDto;
import com.sms.entity.SmsEntity;
import com.sms.repository.SmsRepo;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import com.sms.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

//@Service
//public class SmsServiceImpl implements SmsService {
//    @Override
//    public void sendSms(String message, String number, String apiKey) {
//        try
//        {
//            String sendId="FSTSMS";
//            String language="english";
//            String route="p";
//
//            message= URLEncoder.encode(message, StandardCharsets.UTF_8);    //Important Step
//
//            String myUrl="https://www.fast2sms.com/dev/bulkV2?authorization="+apiKey+"&sender_id="+sendId+"&message="+message+"&language="+language+"&route="+route+"&numbers="+number;
//
//            URL url=new URL(myUrl);
//            HttpsURLConnection con= (HttpsURLConnection)url.openConnection();
//
//            con.setRequestMethod("GET");
//
//            con.setRequestProperty("User-Agent","Mozilla/5.0");
//            con.setRequestProperty("cache-control", "no-cache");
//
//            int responseCode=  con.getResponseCode();
//
//            StringBuffer response=new StringBuffer();
//
//            BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//            while(true)
//            {
//                String line=br.readLine();
//
//                if(line==null)
//                {
//                    break;
//                }
//
//                response.append(line);
//            }
//
//
//            System.out.println(response);
//
//        }
//        catch(Exception e)
//        {
//            System.out.println(e);
//        }
//    }
//}

@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Autowired
    private SmsRepo smsRepo;

    @Override
    public void sendSms(String message, String number, String apiKey) {
        try {
            String sendId = "FSTSMS";
            String language = "english";
            String route = "p";

            message = URLEncoder.encode(message, StandardCharsets.UTF_8); // Important Step

            String myUrl = "https://www.fast2sms.com/dev/bulkV2?authorization=" + apiKey
                    + "&sender_id=" + sendId + "&message=" + message + "&language=" + language
                    + "&route=" + route + "&numbers=" + number;

            URL url = new URL(myUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("cache-control", "no-cache");

            int responseCode = con.getResponseCode();
            logger.info("Response Code: {}", responseCode);

            StringBuffer response = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            logger.info("Response from SMS API: {}", response.toString());

        } catch (Exception e) {
            logger.error("Error while sending SMS: ", e);
        }
    }

    @Override
    public void saveOtp(SmsEntity smsEntity) {
        smsEntity.setCreatedAt(LocalDateTime.now()); // Set current timestamp
        smsRepo.save(smsEntity);
    }


    @Override
    public boolean verifyOtp(SmsDto smsDto) {
        SmsEntity smsEntity = smsRepo.findByMobNumberAndOtp(smsDto.getMobNumber(), smsDto.getOtp());
        if (smsEntity != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdAt = smsEntity.getCreatedAt();
            // Check if OTP is expired (more than 2 minutes old)
            if (ChronoUnit.MINUTES.between(createdAt, now) <= 2) {
                return true; // OTP is valid
            } else {
                // If OTP has expired, delete it from the database
                smsRepo.delete(smsEntity);
            }
        }
        return false; // OTP is not valid
    }


}
