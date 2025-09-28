package com.example.OnlinePay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VnpayQueryRequest {
    private String transaction;    // Mã đơn hàng (vnp_TxnRef)
    private String transDate;
}
