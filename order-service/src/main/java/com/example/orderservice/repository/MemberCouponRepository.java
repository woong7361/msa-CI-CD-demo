package com.example.orderservice.repository;

import com.example.orderservice.dto.coupon.CouponResponseDto;
import com.example.orderservice.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    Optional<MemberCoupon> findByCouponCouponIdAndMemberId(Long couponId, Long memberId);

    List<MemberCoupon> findByMemberId(Long memberId);

    @Query("SELECT mc " +
            "FROM MemberCoupon mc " +
            "JOIN FETCH Coupon c on mc.coupon.couponId = c.couponId " +
            "WHERE mc.memberId = :memberId " +
            "   AND (c.productId = :productId OR c.productId IS NULL) " +
            "   AND mc.isUsed = false")
    List<MemberCoupon> findAvailableCoupons(Long memberId, Long productId);

    @Modifying
    @Query("UPDATE MemberCoupon mc " +
            "SET mc.isUsed = true " +
            "WHERE mc.memberCouponId = :memberCouponId")
    void use(Long memberCouponId);

    @Query("SELECT COUNT(*) " +
            "FROM MemberCoupon mc " +
            "WHERE mc.coupon.couponId = :couponId")
    Long countByCouponId(Long couponId);

    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    void getLock(String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(String key);
}
