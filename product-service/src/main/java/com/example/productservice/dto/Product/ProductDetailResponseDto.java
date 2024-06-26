package com.example.productservice.dto.Product;

import com.example.productservice.Entity.ProductStyle;
import com.example.productservice.Entity.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductDetailResponseDto {
    private Long productId;

    private String name;
    private String description;
    private ProductType productType;
    private List<String> productStyles;

    private Integer unitPrice;
    private Integer quantity;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public  void setProductStyles(List<ProductStyle> styles) {
        this.productStyles = styles.stream()
                .map(sty -> sty.getStyle())
                .collect(Collectors.toList());
    }
}
