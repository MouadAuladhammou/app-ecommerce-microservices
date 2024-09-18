package com.mouad.ecommerce.product;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductResponse(
    Integer id,
    String name,
    String description,
    double availableQuantity,
    BigDecimal price,
    Integer categoryId,
    String categoryName,
    String categoryDescription
) implements Serializable {
}
