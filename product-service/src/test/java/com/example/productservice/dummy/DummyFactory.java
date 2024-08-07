package com.example.productservice.dummy;

import com.example.productservice.Entity.Product;
import com.example.productservice.Entity.ProductImage;
import com.example.productservice.Entity.ProductType;
import com.example.productservice.dto.product.ProductCreateRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public class DummyFactory {

    public static Product getDummyProduct(Long productId) {
        Product product = Product.builder()
                .productId(productId)
                .name("name")
                .description("desc")
                .unitPrice(100)
                .quantity(20)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .productType(ProductType.builder()
                        .productTypeId(4534L)
                        .type("hat")
                        .build())
                .productImages(List.of(
                        ProductImage.builder()
                                .productImageId(54634L)
                                .originalName("original_name")
                                .physicalName("physicalName")
                                .path("/c/a/x/c/x/")
                                .build()
                ))
                .build();

        product.addStyle("s1");
        product.addStyle("s2");
        product.addStyle("s3");

        return product;
    }

    public static ProductCreateRequestDto getDummyProductCreateRequestDto() {
        ProductCreateRequestDto requestDto = ProductCreateRequestDto.builder()
                .name("name")
                .description("desc")
                .productTypeId(45634L)
                .productStyles(List.of("s1", "s2", "s3"))
                .unitPrice(4563)
                .quantity(13)
                .build();

        return requestDto;
    }

    public static ProductImage getDummyProductImage() {
        return ProductImage.builder()
                .originalName("originalName")
                .physicalName("physicalName")
                .path("path")
                .productImageId(546451L)
                .product(getDummyProduct(45341L))
                .isDelete(false)
                .build();
    }
}
