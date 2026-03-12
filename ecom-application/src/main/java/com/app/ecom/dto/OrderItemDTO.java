package com.app.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long productid;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
