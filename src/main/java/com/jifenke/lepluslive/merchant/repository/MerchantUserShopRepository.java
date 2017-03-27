package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUserShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by lss on 2017/2/14.
 */
public interface MerchantUserShopRepository extends JpaRepository<MerchantUserShop,Long> {

    @Query(value = "SELECT * from merchant_user_shop  where merchant_user_id=?1", nativeQuery = true)
    List<MerchantUserShop> findMerchantByMerchantUserId(Long id);




}
