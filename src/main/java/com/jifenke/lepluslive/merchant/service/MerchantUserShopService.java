package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUserShop;
import com.jifenke.lepluslive.merchant.repository.MerchantUserShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by lss on 2017/2/14.
 */
@Service
@Transactional(readOnly = true)
public class MerchantUserShopService {
    @Inject
    private MerchantUserShopRepository merchantUserShopRepository;



    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<MerchantUserShop> findMerchantByMerchantUserId(Long id) {
        return merchantUserShopRepository.findMerchantByMerchantUserId(id);
    }



}
