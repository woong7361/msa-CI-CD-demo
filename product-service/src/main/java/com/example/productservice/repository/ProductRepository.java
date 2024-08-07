package com.example.productservice.repository;

import com.example.productservice.Entity.Product;
import com.example.productservice.dto.product.ProductFavoriteDto;
import com.example.productservice.dto.product.FavoriteProductResponseDto;
import com.example.productservice.repository.dsl.QueryDslProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QueryDslProductRepository {

    @Query("SELECT pr " +
            "FROM Product pr " +
            "JOIN FETCH pr.productImages pimage " +
            "JOIN FETCH pr.productType ptype " +
            "WHERE pr.productId = :productId " +
            "AND pimage.originalName LIKE concat(:startsWith, '%') ")
    Optional<Product> findByIdWithThumbnail(Long productId, String startsWith);

    @Query("SELECT NEW com.example.productservice.dto.product.ProductFavoriteDto( " +
            "   pr.productId, " +
            "   (SELECT count(pf.productFavoriteId) " +
            "       FROM ProductFavorite pf " +
            "       WHERE pf.product.productId = :productId), " +
            "   (SELECT count(pf.productFavoriteId) > 0 " +
            "       FROM ProductFavorite pf " +
            "       WHERE pf.product.productId = :productId AND pf.memberId = :memberId)) " +
            "FROM Product pr " +
            "WHERE pr.productId = :productId ")
    ProductFavoriteDto findFavoriteCountById(Long productId, Long memberId);

    @Query("SELECT" +
            "   pr.productId AS productId, pr.name AS name, pr.description AS description, " +
            "   pr.unitPrice AS unitPrice, pr.quantity AS quantity, " +
            "   ptype AS productType, " +
            "   pf, " +
            "   (SELECT GROUP_CONCAT(ps.style) " +
            "       FROM ProductStyle ps " +
            "       WHERE ps.product.productId = pr.productId) AS productStylesString, " +
            "   (SELECT pi.productImageId " +
            "       FROM ProductImage pi" +
            "       WHERE pi.product.productId = pr.productId AND pi.originalName LIKE 'thumbnail_%') AS thumbnailImageId, " +
            "   (SELECT count(pf.productFavoriteId) " +
            "       FROM ProductFavorite pf " +
            "       WHERE pf.product.productId = pr.productId) AS favorCount, " +
            "   (SELECT count(pf.productFavoriteId) > 0 " +
            "       FROM ProductFavorite pf " +
            "       WHERE pf.product.productId = pr.productId AND pf.memberId = :memberId) AS isFavor " +
            "FROM ProductFavorite pf " +
            "   JOIN FETCH pf.product pr " +
            "   JOIN FETCH pr.productType ptype " +
            "WHERE pf.memberId = :memberId ")
    List<FavoriteProductResponseDto> findProductByMemberFavorite(Long memberId, Pageable pageable);

    @Modifying()
    @Query("UPDATE Product p " +
            "SET p.quantity = p.quantity - :quantity " +
            "WHERE p.productId = :productId")
    void reduceQuantity(Long productId, Long quantity);
}
