package com.example.onlinepay.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VnpayRefundRequest {
    private String transactionType; // trantype
    private String transaction;         // vnp_TxnRef
    private Long amount;            // đơn vị VNĐ (chưa *100)
    private String transDate;       // vnp_TransactionDate (yyyyMMddHHmmss)
    private String user;            // vnp_CreateBy
    private String transactionNo;
}
