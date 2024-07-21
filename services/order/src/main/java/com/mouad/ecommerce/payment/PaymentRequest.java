package com.mouad.ecommerce.payment;

import com.mouad.ecommerce.customer.CustomerResponse;
import com.mouad.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Integer orderId,
    String orderReference,
    CustomerResponse customer
) {
}
