package com.sms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;

@Entity
@Data
public class SmsEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer smsId;
    private Long mobNumber;
    private String otp;
    private LocalDateTime createdAt;


    public SmsEntity() {
        this.createdAt = LocalDateTime.now();
    }
}
