package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;


/**
 * Created by wcg on 16/5/9.
 */
public interface FinancialStatisticRepository extends JpaRepository<FinancialStatistic,Long>{


  Page findAll(Specification<FinancialStatistic> financialClause, Pageable pageRequest);

  Page findAllByMerchant(Merchant merchant, Pageable completeDate);

  @Query(value = "select  ifnull(sum(transfer_price+app_transfer+pos_transfer),0)  from financial_statistic where `merchant_id` = ?1 and balance_date between ?2  and ?3", nativeQuery = true)
  Long countTransferPriceByMonth(Long id, Date start, Date end);

  FinancialStatistic findByStatisticId(String id);
}
