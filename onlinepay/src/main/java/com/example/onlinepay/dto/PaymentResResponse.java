package com.example.onlinepay.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentResResponse implements Serializable {
    private String status;
    private String message;
    private String URL;
}
