package com.jifenke.lepluslive.weixin.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.weixin.domain.entities.InitialOrderRebateActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by xf on 2016/9/27.
 */
public interface WeiXinAwardLogRepository extends JpaRepository<InitialOrderRebateActivityLog,Long> {


  Page findAllByMerchant(Merchant merchant, Pageable createdDate);

  @Query(value = "select sum(rebate) from initial_order_rebate_activity_log where merchant_id=?1 and state = ?2 ",nativeQuery = true)
  Long countRebateByMerchantAndState(Long merchantId, Integer state);

  @Query(value = "select count(1) from initial_order_rebate_activity_log where merchant_id=?1 ",nativeQuery = true)
  Long countNumByMerchant(Long merchantId);


}
