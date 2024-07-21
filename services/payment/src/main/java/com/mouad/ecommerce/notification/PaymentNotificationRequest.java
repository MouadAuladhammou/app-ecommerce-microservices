package com.mouad.ecommerce.notification;

import com.mouad.ecommerce.payment.PaymentMethod;

import java.math.BigDecimal;

public record PaymentNotificationRequest(
    String orderReference,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    String customerFirstname,
    String customerLastname,
    String customerEmail
) {
}
