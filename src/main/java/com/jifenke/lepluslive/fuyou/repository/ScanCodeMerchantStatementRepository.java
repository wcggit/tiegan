package com.jifenke.lepluslive.fuyou.repository;

import com.jifenke.lepluslive.fuyou.domain.entities.ScanCodeMerchantStatement;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 富友扫码结算单 Created by zhangwen on 16/12/19.
 */
public interface ScanCodeMerchantStatementRepository
    extends JpaRepository<ScanCodeMerchantStatement, Long> {


  Page findAllByMerchant(Merchant merchant, Pageable completeDate);


  ScanCodeMerchantStatement findByOrderSid(String orderSid);

  @Query(value = "SELECT sum(transfer_money+refund_money) from scan_code_merchant_statement  where date_format(trade_date,'%Y-%c-%d') BETWEEN date_format(?1,'%Y-%c-%d') and date_format(?2,'%Y-%c-%d') and merchant_id=?3", nativeQuery = true)
  Long sumTotalPriceByMonth(String startStr, String endStr, Long merchantId);




    @Query(value = "SELECT sum(transfer_money+refund_money),count(*) from scan_code_merchant_statement WHERE merchant_id=?1", nativeQuery = true)
    List<Object[]> findTotalAndNumberFromDailyOrderFuyou(Long id);







}
