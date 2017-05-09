package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.TemporaryMerchantUserShop;
import com.jifenke.lepluslive.merchant.repository.TemporaryMerchantUserShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by lss on 2017/2/15.
 */
@Service
@Transactional(readOnly = false)
public class TemporaryMerchantUserService {
    @Inject
    private TemporaryMerchantUserShopRepository temporaryMerchantUserShopRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public TemporaryMerchantUserShop findByMerchantUserId(Long id) {
        return temporaryMerchantUserShopRepository.findByMerchantUserId(id);
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addOne(TemporaryMerchantUserShop temporaryMerchantUserShop) {
        temporaryMerchantUserShopRepository.save(temporaryMerchantUserShop);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteOne(TemporaryMerchantUserShop temporaryMerchantUserShop) {
        temporaryMerchantUserShopRepository.delete(temporaryMerchantUserShop);
    }



}
