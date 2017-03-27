package com.jifenke.lepluslive.fuyou.service;

import com.jifenke.lepluslive.fuyou.domain.entities.ScanCodeMerchantStatement;
import com.jifenke.lepluslive.fuyou.repository.ScanCodeMerchantStatementRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by lss on 2017/3/16.
 */
@Service
public class ScanCodeMerchantStatementService {
    @Inject
    private ScanCodeMerchantStatementRepository scanCodeMerchantStatementRepository;


    public Page findFuyouFinanicalByMerchantAndPage(Merchant merchant, Integer offset) {

        return scanCodeMerchantStatementRepository.findAllByMerchant(merchant, new PageRequest(offset - 1, 20,
                new Sort(
                        Sort.Direction.DESC,
                        "createdDate")));
    }

    public  Long sumTotalPriceByMonth( String startStr,String endStr,Long merchantId){
        return scanCodeMerchantStatementRepository.sumTotalPriceByMonth(startStr,endStr,merchantId);
    }


    public List<Object[]> findTotalAndNumberFromDailyOrderFuyou(Long id){
        return scanCodeMerchantStatementRepository.findTotalAndNumberFromDailyOrderFuyou(id);
    }



    public ScanCodeMerchantStatement findByOrderSid(String orderSid){
        return scanCodeMerchantStatementRepository.findByOrderSid(orderSid);
    }






}
