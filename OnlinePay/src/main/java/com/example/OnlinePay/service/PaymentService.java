package com.example.OnlinePay.service;

import com.example.OnlinePay.dto.*;
import com.example.OnlinePay.exception.AppException;
import com.example.OnlinePay.exception.ErrorCode;
import com.example.OnlinePay.repository.PaymentRepository;
import com.example.OnlinePay.config.VNPAYConfig;
import com.example.OnlinePay.entity.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    public PaymentResResponse createOrder( VnpayCreateOrderRequest vnpayCreateOrderRequest)  {
        try {
            String vnp_TxnRef = VNPAYConfig.getRandomNumber(8);
            String vnp_TmnCode = VNPAYConfig.vnp_TmnCode;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", VNPAYConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VNPAYConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(vnpayCreateOrderRequest.getAmount() * 100));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_BankCode", "NCB");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", "order");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VNPAYConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", "127.0.0.1");
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VNPAYConfig.hmacSHA512(VNPAYConfig.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VNPAYConfig.vnp_PayUrl + "?" + queryUrl;

            PaymentResResponse paymentResResponse = new PaymentResResponse();
            paymentResResponse.setStatus("Ok");
            paymentResResponse.setMessage(("Successfully"));
            paymentResResponse.setURL(paymentUrl);

            Payment payment = Payment.builder()
                    .amount(vnpayCreateOrderRequest.getAmount())
                    .orderId(vnpayCreateOrderRequest.getOrderid())
                    .status(Payment.PaymentStatus.PENDING)
                    .paymentMethod("VNPAY")
                    .transaction(vnp_TxnRef)
                    .createdDate(vnp_CreateDate)
                    .build();
            paymentRepository.save(payment);
            return paymentResResponse;
        }catch (UnsupportedEncodingException e) {
            throw new AppException(ErrorCode.INVALID_PAYMENT);
        }catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }

    }
    public Map<String, String> handleIpn(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            // Lấy params từ request
            Map<String, String> vnpParams = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String name = paramNames.nextElement();
                String value = request.getParameter(name);
                if (value != null && !value.isEmpty()) {
                    vnpParams.put(name, value);
                }
            }

            // Verify chữ ký VNPay
            boolean validSignature = VNPAYConfig.verify(vnpParams);

            // Tìm payment trong DB
            String txnRef = vnpParams.get("vnp_TxnRef");
            Payment payment = paymentRepository.findByTransaction(txnRef);
            if (payment == null) {
                throw new AppException(ErrorCode.ORDER_NOT_FOUND);
            }
            // Kiểm tra trạng thái và số tiền
            boolean checkAmount = true;
            long amount = payment.getAmount()*100;
            String db_amount = String.valueOf(amount);
            String vnp_Amount = vnpParams.get("vnp_Amount");
            if (!vnp_Amount.equals(db_amount)) {
                checkAmount = false;
            }
            boolean checkOrderStatus = payment.getStatus() == Payment.PaymentStatus.PENDING;

            if (!checkAmount) {
                throw new AppException(ErrorCode.INVALID_AMOUNT);
            }

            if (!checkOrderStatus) {
                throw new AppException(ErrorCode.ORDER_EXPIRED);
            }
            if (!validSignature) {
                if (payment.getStatus() == Payment.PaymentStatus.PENDING) {
                    payment.setStatus(Payment.PaymentStatus.FAILED);
                    paymentRepository.save(payment);
                }
                throw new AppException(ErrorCode.INVALID_SIGNATURE);
            }


            String responseCode = vnpParams.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                payment.setStatus(Payment.PaymentStatus.SUCCESS);
            } else {
                payment.setStatus(Payment.PaymentStatus.FAILED);
            }
            paymentRepository.save(payment);

            return Map.of("RspCode", "00", "Message", "Confirm Success");

        } catch (AppException ex) {

            return Map.of(
                    "RspCode", String.valueOf(ex.getErrorCode().getCode()),
                    "Message", ex.getErrorCode().getMessage()
            );
        } catch (Exception ex) {

            return Map.of(
                    "RspCode", "99",
                    "Message", "Unknown error: " + ex.getMessage()
            );
        }
    }


    public Map<String, Object> orderReturn(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> vnp_Params = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String value = request.getParameter(name);
            vnp_Params.put(name, value);
        }

        boolean checkSignature = VNPAYConfig.verify(vnp_Params);

        Map<String, Object> result = new HashMap<>(vnp_Params);
        result.put("validSignature", checkSignature);
        result.putAll(vnp_Params);

        if (!checkSignature) {
            result.put("Message", "Dữ liệu không hợp lệ (chữ ký sai)");
            result.put("Code", "INVALID_SIGNATURE");
        } else {
            String responseCode = vnp_Params.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                result.put("Message", "Thanh toán thành công");
                result.put("Code", "SUCCESS");
            } else {
                result.put("Message", "Thanh toán thất bại (mã lỗi: " + responseCode + ")");
                result.put("Code", "FAILED");
            }
        }

        return result;
    }
    public VnpayQueryResponse queryTransaction(HttpServletRequest req, VnpayQueryRequest vnpayQueryRequest )  {

        try {
            String transaction= vnpayQueryRequest.getTransaction();
            String transDate= vnpayQueryRequest.getTransDate();
            Payment payment= paymentRepository.findByTransaction(transaction);
            if (payment == null) {
                throw new AppException(ErrorCode.ORDER_NOT_FOUND);
            }
            // 1. Tham số request
            String vnp_RequestId = VNPAYConfig.getRandomNumber(8);
            String vnp_Version = "2.1.0";
            String vnp_Command = "querydr";
            String vnp_TmnCode = VNPAYConfig.vnp_TmnCode;
            String vnp_TxnRef = transaction;
            String vnp_OrderInfo = "Kiem tra ket qua GD OrderId:" + vnp_TxnRef;

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            String vnp_IpAddr = VNPAYConfig.getIpAddress(req);

            // 2. JSON Params
            JsonObject vnp_Params = new JsonObject();
            vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
            vnp_Params.addProperty("vnp_Version", vnp_Version);
            vnp_Params.addProperty("vnp_Command", vnp_Command);
            vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.addProperty("vnp_TransactionDate", transDate);
            vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

            // 3. Secure Hash
            String hash_Data = String.join("|",
                    vnp_RequestId, vnp_Version, vnp_Command,
                    vnp_TmnCode, vnp_TxnRef, transDate,
                    vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);

            String vnp_SecureHash = VNPAYConfig.hmacSHA512(VNPAYConfig.secretKey, hash_Data);
            vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

            // 4. Gửi request
            URL url = new URL(VNPAYConfig.vnp_ApiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(vnp_Params.toString());
                wr.flush();
            }

            // 5. Đọc response từ VNPay
            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String output;
                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
            }

            // 6. Parse JSON response -> DTO
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);

            VnpayQueryResponse dto = new VnpayQueryResponse();
            dto.setResponseCode(jsonResponse.get("vnp_ResponseCode").getAsString());
            dto.setMessage(jsonResponse.get("vnp_Message").getAsString());
            dto.setTxnRef(jsonResponse.get("vnp_TxnRef").getAsString());
            dto.setTransactionNo(jsonResponse.has("vnp_TransactionNo") ? jsonResponse.get("vnp_TransactionNo").getAsString() : null);
            dto.setTransactionStatus(jsonResponse.get("vnp_TransactionStatus").getAsString());
            dto.setAmount(jsonResponse.get("vnp_Amount").getAsString());
            dto.setBankCode(jsonResponse.has("vnp_BankCode") ? jsonResponse.get("vnp_BankCode").getAsString() : null);
            dto.setPayDate(jsonResponse.has("vnp_PayDate") ? jsonResponse.get("vnp_PayDate").getAsString() : null);

            return dto;

        } catch (Exception e) {
            e.printStackTrace();
            return new VnpayQueryResponse("99", "System Error: " + e.getMessage(),
                    null, null, null, null, null, null);
        }
    }
    public VnpayRefundResponse refundTransaction(VnpayRefundRequest req, String ipAddr) throws Exception {
       Payment payment = paymentRepository.findByTransaction(req.getTransaction());
        // Tạo tham số
        String vnp_RequestId = VNPAYConfig.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TmnCode = VNPAYConfig.vnp_TmnCode;
        String vnp_TransactionType = req.getTransactionType();
        String vnp_TxnRef = payment.getTransaction();
        long amount = req.getAmount() * 100;
        String vnp_Amount = String.valueOf(amount);
        String vnp_OrderInfo = "Hoan tien GD OrderId:" + vnp_TxnRef;
        String vnp_TransactionNo = req.getTransactionNo() != null ? req.getTransactionNo() : "";
        String vnp_TransactionDate = req.getTransDate();
        String vnp_CreateBy = req.getUser();

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        // Build JSON request
        JsonObject vnp_Params = new JsonObject();
        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TransactionType", vnp_TransactionType);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_Amount", vnp_Amount);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);

        if (!vnp_TransactionNo.isEmpty()) {
            vnp_Params.addProperty("vnp_TransactionNo", vnp_TransactionNo);
        }

        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransactionDate);
        vnp_Params.addProperty("vnp_CreateBy", vnp_CreateBy);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", ipAddr);

        // Tạo dữ liệu hash
        String hash_Data = String.join("|",
                vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode,
                vnp_TransactionType, vnp_TxnRef, vnp_Amount, vnp_TransactionNo,
                vnp_TransactionDate, vnp_CreateBy, vnp_CreateDate, ipAddr, vnp_OrderInfo
        );
        String vnp_SecureHash = VNPAYConfig.hmacSHA512(VNPAYConfig.secretKey, hash_Data);
        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        // Gửi request POST
        URL url = new URL(VNPAYConfig.vnp_ApiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(vnp_Params.toString());
        wr.flush();
        wr.close();

        // Đọc response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String output;
        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        // TODO: parse JSON response -> VnpayRefundResponse
        System.out.println("VNPay refund response: " + response);


        ObjectMapper mapper = new ObjectMapper();
        VnpayRefundResponse res = mapper.readValue(response.toString(), VnpayRefundResponse.class);
       if(res.getVnp_ResponseCode()=="00") {
           payment.setStatus(Payment.PaymentStatus.REFUNDED);
       }
        return res;
    }

}
