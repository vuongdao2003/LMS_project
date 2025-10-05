package com.example.onlinepay.controller;


import com.example.onlinepay.dto.*;
import com.example.onlinepay.config.VNPAYConfig;
import com.example.onlinepay.service.PaymentService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService vnPayService;

    @PostMapping("create")
    public ResponseEntity<?> createOrder(@RequestBody VnpayCreateOrderRequest request) throws UnsupportedEncodingException {
        return ResponseEntity.ok(vnPayService.createOrder(request));
    }
    @GetMapping("/return")
    public ResponseEntity<?>  getPaymentInfo(@Parameter(hidden = true)HttpServletRequest request) throws UnsupportedEncodingException {
        return ResponseEntity.ok(vnPayService.orderReturn(request));
    }
    @PostMapping("/query")
    public VnpayQueryResponse queryTransaction(@Parameter(hidden = true)HttpServletRequest req,
                                               @RequestBody VnpayQueryRequest vnpayQueryRequest) throws UnsupportedEncodingException {
        return vnPayService.queryTransaction(req, vnpayQueryRequest );
    }
    @PostMapping("/refund")
    public VnpayRefundResponse refundTransaction(@RequestBody VnpayRefundRequest request,
                                                 @Parameter(hidden = true)HttpServletRequest httpServletRequest) throws Exception {
        String ipAddr = VNPAYConfig.getIpAddress(httpServletRequest);
        return vnPayService.refundTransaction(request, ipAddr);
    }
    @RequestMapping(value = "/webhook", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, String>> webhook(@Parameter(hidden = true)HttpServletRequest request) throws UnsupportedEncodingException {
        // Gọi service xử lý IPN
        Map<String, String> response = vnPayService.handleIpn(request);

        // Trả về JSON với các RspCode chuẩn
        return ResponseEntity.ok(response);
    }

}