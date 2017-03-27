package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWeiXinUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Created by wcg on 16/5/17.
 */
public interface MerchantWeiXinUserRepository extends JpaRepository<MerchantWeiXinUser,Long> {

//  @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
@Query(value = "SELECT * from merchant_wx_user where open_id=?1 ", nativeQuery = true)
MerchantWeiXinUser findByOpenId(String openid);

  Optional<MerchantWeiXinUser> findByMerchantUser(MerchantUser merchantUser);
}
