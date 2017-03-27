package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * Created by wcg on 16/5/9.
 */
public interface FinancialStatisticRepository extends JpaRepository<FinancialStatistic,Long>{


  Page findAll(Specification<FinancialStatistic> financialClause, Pageable pageRequest);

  Page findAllByMerchant(Merchant merchant, Pageable completeDate);

  @Query(value = "SELECT ifnull(sum(transfer_price+app_transfer+pos_transfer),0)  from financial_statistic  where  merchant_id=?1 and balance_date like ?2 ", nativeQuery = true)
  Long countTransferPriceByMonth(Long id, String dateStr);

  FinancialStatistic findByStatisticId(String id);

  @Query(value = "SELECT SUM(transfer_price),count(*) from financial_statistic where merchant_id=?1 and state=1", nativeQuery = true)
  List<Object[]> findTotalAndNumberFromDailyOrderlePLus(Long id);




}
