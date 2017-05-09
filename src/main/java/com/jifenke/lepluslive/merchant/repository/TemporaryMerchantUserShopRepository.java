package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.TemporaryMerchantUserShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by sunxingfei on 2017/2/15.
 */
public interface TemporaryMerchantUserShopRepository extends JpaRepository<TemporaryMerchantUserShop,Long> {

    @Query(value = "SELECT * from temporary_merchant_user_shop where merchant_user_id=?1", nativeQuery = true)
    TemporaryMerchantUserShop findByMerchantUserId(Long id);



}
