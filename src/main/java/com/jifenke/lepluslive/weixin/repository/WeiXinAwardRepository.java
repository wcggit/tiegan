package com.jifenke.lepluslive.weixin.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.weixin.domain.entities.InitialOrderRebateActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by xf on 2016/9/26.
 */
public interface WeiXinAwardRepository extends JpaRepository<InitialOrderRebateActivity,Long> {
  List<InitialOrderRebateActivity> findByMerchant(Merchant merchant);
}
