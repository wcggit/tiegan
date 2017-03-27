package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.weixin.domain.entities.InitialOrderRebateActivity;
import com.jifenke.lepluslive.weixin.repository.WeiXinAwardLogRepository;
import com.jifenke.lepluslive.weixin.repository.WeiXinAwardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by xf on 2016/9/26.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinAwardService {

  @Inject
  private WeiXinAwardRepository weiXinAwardRepository;

  @Inject
  private WeiXinAwardLogRepository weiXinAwardLogRepository;


  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Long findRebateCountByMerchantAndState(Merchant merchant, int state) {
    return weiXinAwardLogRepository.countRebateByMerchantAndState(merchant.getId(), state);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Long findAwardNumByMerchant(Merchant merchant) {
    return weiXinAwardLogRepository.countNumByMerchant(merchant.getId());
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page findAwardOrderByMerchantSortByCreateDate(Merchant merchant, int offset) {
    return weiXinAwardLogRepository.findAllByMerchant(merchant, new PageRequest(offset - 1, 10,
                                                                                new Sort(
                                                                                    Sort.Direction.DESC,
                                                                                    "createdDate")));
  }

  @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
  public List<InitialOrderRebateActivity> findActivityByMerchant(Merchant merchant) {
    return weiXinAwardRepository.findByMerchant(merchant);
  }

}
