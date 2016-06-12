package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by wcg on 16/5/5.
 */
public interface OffLineOrderRepository extends JpaRepository<OffLineOrder, Long> {

  OffLineOrder findByOrderSid(String orderSid);

  Page findAllByMerchantAndState(Merchant merchant,Integer state, Pageable completeDate);

  @Query(value = "select sum(total_price) from off_line_order where `merchant_id` = ?1 and state = 1 and complete_date between ?2  and ?3", nativeQuery = true)
  Long countTotalPriceByMonth(Long id, Date start, Date end);

  List<OffLineOrder> findAllByMerchantAndCompleteDateBetween(Merchant merchant, Date start, Date end);
}
