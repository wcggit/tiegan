package com.jifenke.lepluslive.fuyou.repository;

import com.jifenke.lepluslive.fuyou.domain.entities.ScanCodeSettleOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 富友扫码结算单 Created by zhangwen on 16/12/19.
 */
public interface ScanCodeSettleOrderRepository extends JpaRepository<ScanCodeSettleOrder, Long> {

  /**
   * 根据商户号和交易日期获取结算单 2016/12/30
   *
   * @param merchantNum 商户号
   * @param tradeDate   交易日期
   */
  ScanCodeSettleOrder findByMerchantNumAndTradeDate(String merchantNum, String tradeDate);

  Page findAll(Specification<ScanCodeSettleOrder> whereClause, Pageable pageRequest);

  /**
   * 获取所有的某种状态的结算单  2017/01/04
   *
   * @param state 结算单状态 0=待转账|1=已转账|2=挂帐
   */
  List<ScanCodeSettleOrder> findByState(int state);

  /**
   * 统计某个商户号的累计流水信息  2017/01/09
   *
   * @param merchantNum 商户号
   * @param state       结算单状态
   */
  @Query(value = "SELECT SUM(transfer_money),SUM(transfer_money_from_true_pay),SUM(transfer_money_from_score) FROM scan_code_settle_order WHERE merchant_num = ?1 AND state = ?2", nativeQuery = true)
  List<Object[]> countByMerchantNumAndState(String merchantNum, int state);
}
