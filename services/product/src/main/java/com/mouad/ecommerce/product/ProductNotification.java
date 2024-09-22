package com.mouad.ecommerce.product;

public class ProductNotification {
    private String message;
    private ProductResponse product;

    public ProductNotification() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }
}
