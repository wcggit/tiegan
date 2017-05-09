package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by sunxingfei on 2017/2/14.
 */
@Service
@Transactional(readOnly = false)
public class MerchantUserService  {
    @Inject
    private MerchantUserRepository merchantUserRepository;


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public MerchantUser findByName(String userName) {
        return merchantUserRepository.findByName(userName);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public MerchantUser findById(Long id) {
        return merchantUserRepository.findOne(id);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveOne(MerchantUser merchantUser) {
         merchantUserRepository.save(merchantUser);
    }


}
