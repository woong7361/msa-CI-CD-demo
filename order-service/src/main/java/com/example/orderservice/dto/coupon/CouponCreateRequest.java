package com.example.orderservice.dto.coupon;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CouponCreateRequest {
    private String name;
    private String description;

    private Integer discount;

    private Long productId;
}
