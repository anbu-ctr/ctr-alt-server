package org.example.controller;

import org.example.dto.ApiResponse;
import org.example.dto.PaymentRequest;
import org.example.dto.GetPaymentsRequest;
import org.example.dto.PaymentDto;
import org.example.entity.Payment;
import org.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Creates a new payment record
     * Request body example:
     * {
     *   "type": "create",
     *   "data": {
     *     "memberId": 101,
     *     "amount": 999.00,
     *     "paymentMode": "CASH",
     *     "paymentDate": "2026-04-03",
     *     "notes": "Monthly fee - April"
     *   }
     * }
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createPayment(@RequestBody final PaymentRequest request) {
        try {
            if (!"create".equals(request.getType())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid request type. Expected 'create'"));
            }

            final Payment payment = paymentService.createPayment(request);
            final Map<String, Object> data = new HashMap<>();

            data.put("message", "Payment created successfully");
            data.put("paymentId", payment.getId());
            data.put("amount", payment.getAmount());
            data.put("paymentMode", payment.getPaymentMode());
            data.put("paymentDate", payment.getPaymentDate());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(data));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Retrieves payments for a specific member
     * Request body example:
     * {
     *   "type": "get",
     *   "data": {
     *     "memberId": 101,
     *     "start": 0,
     *     "limit": 10
     *   }
     * }
     */
    @PostMapping("/list")
    public ResponseEntity<ApiResponse> getPayments(@RequestBody final GetPaymentsRequest request) {
        try {
            if (!"get".equals(request.getType())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid request type. Expected 'get'"));
            }

            final GetPaymentsRequest.FilterData data = request.getData();
            final int start = data.getStart() != null ? data.getStart() : 0;
            final int limit = data.getLimit() != null ? data.getLimit() : 10;

            List<PaymentDto> payments;

            if (data.getMemberId() != null) {
                payments = paymentService.getPaymentsByMember(data.getMemberId(), start, limit);
            } else {
                payments = paymentService.getAllPayments(start, limit);
            }

            final Map<String, Object> responseData = new HashMap<>();
            responseData.put("payments", payments);
            responseData.put("count", payments.size());

            return ResponseEntity.ok(ApiResponse.success(responseData));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Retrieves a specific payment by ID
     */
    @GetMapping("/get")
    public ResponseEntity<ApiResponse> getPaymentById(@RequestParam final Long paymentId) {
        try {
            final PaymentDto payment = paymentService.getPaymentById(paymentId);
            return ResponseEntity.ok(ApiResponse.success(payment));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Updates an existing payment
     */
    @PutMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> updatePayment(
            @PathVariable final Long paymentId,
            @RequestBody final PaymentRequest request) {
        try {
            final Payment payment = paymentService.updatePayment(paymentId, request);
            final Map<String, Object> data = new HashMap<>();

            data.put("message", "Payment updated successfully");
            data.put("paymentId", payment.getId());
            data.put("amount", payment.getAmount());

            return ResponseEntity.ok(ApiResponse.success(data));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Deletes a payment record
     */
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> deletePayment(@PathVariable final Long paymentId) {
        try {
            paymentService.deletePayment(paymentId);
            final Map<String, Object> data = new HashMap<>();

            data.put("message", "Payment deleted successfully");

            return ResponseEntity.ok(ApiResponse.success(data));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Gets total payments for a member
     */
    @GetMapping("/member/{memberId}/total")
    public ResponseEntity<ApiResponse> getTotalPaymentsByMember(@PathVariable final Long memberId) {
        try {
            final Double totalAmount = paymentService.getTotalPaymentsByMember(memberId);
            final Map<String, Object> data = new HashMap<>();

            data.put("memberId", memberId);
            data.put("totalAmount", totalAmount);

            return ResponseEntity.ok(ApiResponse.success(data));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

