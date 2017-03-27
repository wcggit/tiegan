package com.jifenke.lepluslive.fuyou.service;


import com.jifenke.lepluslive.fuyou.domain.criteria.ScanCodeOrderCriteria;
import com.jifenke.lepluslive.fuyou.domain.entities.ScanCodeOrder;
import com.jifenke.lepluslive.fuyou.repository.ScanCodeOrderRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.domain.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 富友扫码订单 Created by zhangwen on 2016/12/19.
 */
@Service
public class ScanCodeOrderService {


  @Inject
  private ScanCodeOrderRepository repository;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScoreBService scoreBService;



  @Inject
  private EntityManager entityManager;


  @Inject
  private MerchantService merchantService;



  @Inject
  private PartnerService partnerService;

  /**
   * 根据订单号获取订单信息  2016/12/27
   *
   * @param orderSid 订单号
   */
  public ScanCodeOrder findByOrderSid(String orderSid) {
    return repository.findByOrderSid(orderSid);
  }



  /**
   * 富友扫码订单分页条件查询  16/12/20
   *
   * @param criteria 查询条件
   * @param limit    查询条数
   */
  public Page findOrderByPage(ScanCodeOrderCriteria criteria, Integer limit) {
    Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
    return repository
        .findAll(getWhereClause(criteria),
                 new PageRequest(criteria.getOffset() - 1, limit, sort));
  }

  private static Specification<ScanCodeOrder> getWhereClause(ScanCodeOrderCriteria orderCriteria) {
    return new Specification<ScanCodeOrder>() {
      @Override
      public Predicate toPredicate(Root<ScanCodeOrder> r, CriteriaQuery<?> q,
                                   CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        if (orderCriteria.getState() != null) { //订单状态
          predicate.getExpressions().add(cb.equal(r.get("state"), orderCriteria.getState()));
        }

        if (orderCriteria.getStartDate() != null && !""
            .equals(orderCriteria.getStartDate())) {//交易完成时间
          predicate.getExpressions().add(
              cb.between(r.get("completeDate"), new Date(orderCriteria.getStartDate()),
                         new Date(orderCriteria.getEndDate())));
        }

        if (orderCriteria.getMerchantName() != null && !""
            .equals(orderCriteria.getMerchantName())) { //门店名称
          predicate.getExpressions().add(
              cb.like(r.<Merchant>get("merchant").get("name"),
                      "%" + orderCriteria.getMerchantName() + "%"));
        }
        if (orderCriteria.getOrderType() != null) { //订单类型
          if(orderCriteria.getOrderType()==2){
            predicate.getExpressions().add(
                    cb.or(cb.equal(r.<Category>get("orderType").get("id"), "12004"),
                            cb.equal(r.<Category>get("orderType").get("id"), "12005"))
                    );
          }
          if(orderCriteria.getOrderType()==1){
            predicate.getExpressions().add(
                    cb.or(cb.equal(r.<Category>get("orderType").get("id"), "12001"),
                            cb.equal(r.<Category>get("orderType").get("id"), "12002"),
                            cb.equal(r.<Category>get("orderType").get("id"), "12003"),
                            cb.equal(r.<Category>get("orderType").get("id"), "12006"))
            );
          }
        }
        return predicate;
      }
    };
  }

  /**
   * 每日富友已完成订单统计(group by merchantNum)  2016/12/29
   *
   * @param currDay 订单支付时间yyyyMMdd%
   */
  public List countTransferGroupByMerchantNum(String currDay) {
    String
        sql =
        "SELECT merchant_num,SUM(transfer_money),SUM(transfer_money_from_true_pay),SUM(transfer_money_from_score) FROM scan_code_order WHERE state = 1 AND settle_date LIKE '"
        + currDay + "' GROUP BY merchant_num";

    System.out.println(sql);
    return entityManager.createNativeQuery(sql).getResultList();
  }

  /**
   * 每日富友已完成订单统计(group by Merchant)  2016/12/29
   *
   * @param currDay 订单支付时间yyyyMMdd%
   */
  public List countTransferGroupByMerchant(String currDay) {
    String
        sql =
        "SELECT merchant_id,SUM(transfer_money),SUM(transfer_money_from_true_pay),SUM(transfer_money_from_score) FROM scan_code_order WHERE state = 1 AND settle_date LIKE '"
        + currDay + "' GROUP BY merchant_id";

    System.out.println(sql);
    return entityManager.createNativeQuery(sql).getResultList();
  }

  /**
   * 统计某个门店的累计流水和累计收取红包  2017/02/10
   *
   * @param merchantId 门店ID
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Map<String, Long> countPriceByMerchant(Long merchantId) {
    List<Object[]> list = repository.countPriceByMerchant(merchantId);
    Map<String, Long> map = new HashMap<>();
    Long totalPrice = 0L;
    Long trueScore = 0L;

    if (list != null && list.size() > 0) {
      Object[] o = list.get(0);
      totalPrice = Long.valueOf(String.valueOf(o[0] != null ? o[0] : 0));
      trueScore = Long.valueOf(String.valueOf(o[1] != null ? o[1] : 0));
    }
    map.put("totalPrice_fy", totalPrice);
    map.put("trueScore_fy", trueScore);
    return map;
  }



  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ScanCodeOrder> findCommonOrderMerchantIdAndCompleteDate(Long merchantId, String date) {
    return repository.findCommonOrderMerchantIdAndCompleteDate(merchantId,date);
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ScanCodeOrder> findLeplusOrderMerchantIdAndCompleteDate(Long merchantId, String date) {
    return repository.findLeplusOrderMerchantIdAndCompleteDate(merchantId,date);
  }





  public List<Object[]> findTotalPriceAndTransferMoneySum(String today,Long id) {

    return repository.findTotalPriceAndTransferMoneySum(today,id);
  }




}
