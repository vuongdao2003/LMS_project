package com.example.OnlinePay.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VnpayRefundResponse {
    private String responseCode;
    private String message;
    private String txnRef;
    private String amount;
    private String transactionNo;
    private String transactionStatus;
}
