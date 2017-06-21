package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWalletLog;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletLogRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletRepository;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by wcg on 16/3/17.
 */
@Service
@Transactional(readOnly = true)
public class MerchantService {

  @Inject
  private MerchantRepository merchantRepository;



  @Inject
  private MerchantWalletRepository merchantWalletRepository;

  @Inject
  private MerchantUserRepository merchantUserRepository;

  @Inject
  private MerchantWalletLogRepository merchantWalletLogRepository;



  /**
   * 获取商家详情
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Merchant findMerchantById(Long id) {
    return merchantRepository.findOne(id);
  }



  public MerchantWallet findMerchantWalletByMerchant(Merchant merchant) {
    return merchantWalletRepository.findByMerchant(merchant);
  }


  public MerchantUser findMerchantUserByName(String name) {
    return merchantUserRepository.findByName(name);
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void shareToMerchant(long shareMoney, Merchant merchant, String orderSid, Long type) {
    if (shareMoney > 0) {
      MerchantWalletLog log = new MerchantWalletLog();

      MerchantWallet merchantWallet = findMerchantWalletByMerchant(merchant);

      Long availableBalance = merchantWallet.getAvailableBalance();

      log.setBeforeChangeMoney(availableBalance);
      long afterShareMoney = availableBalance + shareMoney;

      log.setAfterChangeMoney(afterShareMoney);

      log.setMerchantId(merchant.getId());

      log.setOrderSid(orderSid);

      log.setType(type);

      merchantWallet.setTotalMoney(merchantWallet.getTotalMoney() + shareMoney);

      merchantWallet.setAvailableBalance(afterShareMoney);

      merchantWalletLogRepository.save(log);

      merchantWalletRepository.save(merchantWallet);
    }


  }


}
