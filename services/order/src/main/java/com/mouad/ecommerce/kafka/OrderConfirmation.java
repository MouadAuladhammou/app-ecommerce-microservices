package com.mouad.ecommerce.kafka;

import com.mouad.ecommerce.customer.CustomerResponse;
import com.mouad.ecommerce.order.PaymentMethod;
import com.mouad.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
    String orderReference,
    BigDecimal totalAmount,
    PaymentMethod paymentMethod,
    CustomerResponse customer,
    List<PurchaseResponse> products
) {
}
