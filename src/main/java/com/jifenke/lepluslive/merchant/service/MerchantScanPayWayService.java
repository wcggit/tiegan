package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScanPayWay;
import com.jifenke.lepluslive.merchant.repository.MerchantScanPayWayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;

/**
 * 富友结算规则、类型及结算账户 Created by zhangwen on 2017/1/9.
 */
@Service
@Transactional(readOnly = true)
public class MerchantScanPayWayService {

  @Inject
  private MerchantScanPayWayRepository repository;

  @Inject
  private  MerchantService merchantService;


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public MerchantScanPayWay findByMerchantId(Long merchantId) {
    MerchantScanPayWay scanPayWay = repository.findByMerchantId(merchantId);
    if (scanPayWay == null) {
      Merchant merchant = merchantService.findMerchantById(merchantId);
      Date date = new Date();
      scanPayWay = new MerchantScanPayWay();
      scanPayWay.setCreateDate(date);
      scanPayWay.setLastUpdate(date);
      scanPayWay.setMerchantId(merchantId);
      scanPayWay.setCommission(merchant.getLjCommission());
      repository.saveAndFlush(scanPayWay);
    }
    return scanPayWay;
  }

}
