package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.criteria.OLOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.PayWay;
import com.jifenke.lepluslive.order.repository.OffLineOrderRepository;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wcg on 16/5/5.
 */
@Service
@Transactional(readOnly = true)
public class OffLineOrderService {

  private static final Logger log = LoggerFactory.getLogger(OffLineOrderService.class);

  @Inject
  private OffLineOrderRepository offLineOrderRepository;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private MerchantService merchantService;




  public Page findOrderByPage(OLOrderCriteria orderCriteria, Integer limit) {
    Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
    return offLineOrderRepository
            .findAll(getWhereClause(orderCriteria),
                    new PageRequest(orderCriteria.getOffset() - 1, limit, sort));
  }

  public static Specification<OffLineOrder> getWhereClause(OLOrderCriteria orderCriteria) {
    return new Specification<OffLineOrder>() {
      @Override
      public Predicate toPredicate(Root<OffLineOrder> r, CriteriaQuery<?> q,
                                   CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        if (orderCriteria.getState() != null) {
          predicate.getExpressions().add(
                  cb.equal(r.get("state"),
                          orderCriteria.getState()));
        }


        if (orderCriteria.getRebateWay() != null) {
          if (orderCriteria.getRebateWay() == 2) {
            predicate.getExpressions().add(
                    cb.or(cb.equal(r.<PayWay>get("rebateWay"), 1),
                            cb.equal(r.<PayWay>get("rebateWay"), 3)));
          }
          if(orderCriteria.getRebateWay() == 1){
            predicate.getExpressions().add(
                    cb.or(cb.equal(r.<PayWay>get("rebateWay"), 0),
                            cb.equal(r.<PayWay>get("rebateWay"), 4),
                            cb.equal(r.<PayWay>get("rebateWay"), 5),
                            cb.equal(r.<PayWay>get("rebateWay"), 6),
                            cb.equal(r.<PayWay>get("rebateWay"), 2)));
          }
        }





        if (orderCriteria.getStartDate() != null && orderCriteria.getStartDate() != "") {
          predicate.getExpressions().add(
                  cb.between(r.get("completeDate"), new Date(orderCriteria.getStartDate()),
                          new Date(orderCriteria.getEndDate())));
        }

        if (orderCriteria.getMerchant() != null) {
          predicate.getExpressions().add(
                  cb.equal(r.get("merchant").get("merchantSid"),
                          orderCriteria.getMerchant()));
        }


        return predicate;
      }
    };
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public OffLineOrder findOffLineOrderByOrderSid(String orderSid) {
    return offLineOrderRepository.findByOrderSid(orderSid);
  }


  public Page findOffLineOrderByMerchantAndPage(Merchant merchant, Integer offset) {
    return offLineOrderRepository.findAllByMerchantAndState(merchant, 1,
                                                            new PageRequest(offset - 1, 20,
                                                                            new Sort(
                                                                                Sort.Direction.DESC,
                                                                                "completeDate")));
  }

  public Long sumTotalPriceByMonth(Long dateTime, Merchant merchant) {
    Calendar cal = Calendar.getInstance();
    Date date = new Date(dateTime);
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    Date end = MvUtil.getMonthEndDate(year, month, cal);
    Date start = MvUtil.getMonthStartDate(year, month, cal);

    return offLineOrderRepository.countTotalPriceByMonth(merchant.getId(), start, end);
  }



  public List<OffLineOrder> findOffLineOrderByMerchantAndDate(Merchant merchant, Date balanceDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(balanceDate);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    Date start = calendar.getTime();
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    Date end = calendar.getTime();

    return offLineOrderRepository.findAllByMerchantAndCompleteDateBetween(merchant,start,end);
  }

  public List<Object[]> findTotalPriceAndTransferMoneySum(String today,Long id) {

    return offLineOrderRepository.findTotalPriceAndTransferMoneySum(today,id);
  }



  public List<OffLineOrder> findWeiXinOrderByMerchantAndCompleteDateBetween(Merchant merchant, Date balanceDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(balanceDate);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    Date start = calendar.getTime();
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    Date end = calendar.getTime();
    return offLineOrderRepository.findWeiXinOrderByMerchantAndCompleteDateBetween(merchant,start,end);
  }



  public List<OffLineOrder> findLePlusOrderByMerchantAndCompleteDateBetween(Merchant merchant, Date balanceDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(balanceDate);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    Date start = calendar.getTime();
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    Date end = calendar.getTime();

    return offLineOrderRepository.findLePlusOrderByMerchantAndCompleteDateBetween(merchant,start,end);
  }


}
