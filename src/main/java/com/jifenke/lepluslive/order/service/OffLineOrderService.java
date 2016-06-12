package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.PayWay;
import com.jifenke.lepluslive.order.repository.OffLineOrderRepository;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

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
}
