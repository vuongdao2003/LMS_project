package com.example.onlinepay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VnpayQueryResponse {
    private String responseCode;   // Mã phản hồi từ VNPay (vd: "00" = thành công)
    private String message;        // Thông điệp mô tả
    private String txnRef;         // Mã đơn hàng
    private String transactionNo;  // Mã giao dịch VNPay
    private String transactionStatus; // Trạng thái giao dịch
    private String amount;         // Số tiền
    private String bankCode;       // Mã ngân hàng
    private String payDate;        // Ngày thanh toán
}
