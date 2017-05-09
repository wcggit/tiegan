package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import org.springframework.data.domain.Page;
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


  Page findAll(Specification<OffLineOrder> whereClause, Pageable pageRequest);

  Page findAllByMerchantAndState(Merchant merchant, Integer state, Pageable completeDate);

  @Query(value = "select sum(total_price) from off_line_order where `merchant_id` = ?1 and state = 1 and complete_date between ?2  and ?3", nativeQuery = true)
  Long countTotalPriceByMonth(Long id, Date start, Date end);

  List<OffLineOrder> findAllByMerchantAndCompleteDateBetween(Merchant merchant, Date start, Date end);



  @Query(value = "SELECT sum(total_price),sum(transfer_money) from off_line_order where  state=1 and merchant_id=?2 and complete_date like ?1", nativeQuery = true)
  List<Object[]> findTotalPriceAndTransferMoneySum(String today, Long id);




  @Query(value = "select * from off_line_order where merchant_id = ?1 and state = 1 and complete_date between ?2  and ?3 and rebate_way in (0,2,4,5,6)", nativeQuery = true)
  List<OffLineOrder> findWeiXinOrderByMerchantAndCompleteDateBetween(Merchant merchant, Date start, Date end);


  @Query(value = "select * from off_line_order where merchant_id = ?1 and state = 1 and complete_date between ?2  and ?3 and rebate_way in (1,3)", nativeQuery = true)
  List<OffLineOrder> findLePlusOrderByMerchantAndCompleteDateBetween(Merchant merchant, Date start, Date end);



}
