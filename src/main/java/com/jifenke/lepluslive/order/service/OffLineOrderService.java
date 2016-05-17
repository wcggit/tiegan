package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.PayWay;
import com.jifenke.lepluslive.order.repository.OffLineOrderRepository;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.wxpay.service.WeiXinUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScoreBService scoreBService;


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public OffLineOrder createOffLineOrderForNoNMember(String truePrice, Long merchantId,
                                                     String openid) {
    OffLineOrder offLineOrder = new OffLineOrder();
    long truePirce = (long) (Float.parseFloat(truePrice) * 100);
    offLineOrder.setLeJiaUser(weiXinUserService.findWeiXinUserByOpenId(openid).getLeJiaUser());
    offLineOrder.setTotalPrice(truePirce);
    offLineOrder.setTruePay(truePirce);
    offLineOrder.setCreatedDate(new Date());
    offLineOrder.setRebateWay(0);
    Merchant merchant = merchantService.findMerchantById(merchantId);
    offLineOrder.setMerchant(merchant);
    offLineOrder.setWxCommission((long) Math.ceil(truePirce * 6 / 1000.0));
    offLineOrder.setPayWay(new PayWay(1L));
    offLineOrderRepository.save(offLineOrder);
    offLineOrder.setTransferMoney(offLineOrder.getTotalPrice() - offLineOrder.getWxCommission());
    long scoreB = (long) Math.ceil(truePirce * Constants.SCOREB / 10000.0);
    offLineOrder.setScoreB(scoreB);
    return offLineOrder;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public OffLineOrder createOffLineOrderForMember(String truePrice, Long merchantId,
                                                  String trueScore,
                                                  String totalPrice,
                                                  String userSid
  ) {
    OffLineOrder offLineOrder = new OffLineOrder();
    long truePirce = Long.parseLong(truePrice);
    long total = Long.parseLong(totalPrice);
    long scoreA = Long.parseLong(trueScore);
    offLineOrder.setLeJiaUser(leJiaUserService.findUserByUserSid(userSid));
    offLineOrder.setTotalPrice(total);
    offLineOrder.setTrueScore(scoreA);
    offLineOrder.setTruePay(truePirce);
    offLineOrder.setCreatedDate(new Date());
    Merchant merchant = merchantService.findMerchantById(merchantId);
    offLineOrder.setMerchant(merchant);
    offLineOrder.setRebateWay(0);
    offLineOrder.setWxCommission((long) Math.ceil(truePirce * 6 / 1000.0));
    offLineOrder.setPayWay(new PayWay(1L));
    offLineOrder.setTransferMoney(offLineOrder.getTotalPrice() - offLineOrder.getWxCommission());
    if (merchant.getPartnership() != 0) {
      offLineOrder.setRebateWay(1);
      long ljCommission = (long) Math.ceil(truePirce * merchant.getLjCommission() / 100.0);
      offLineOrder.setLjCommission(ljCommission);
      long rebate = (long) Math.ceil((ljCommission - offLineOrder.getWxCommission()) * 50 / 100.0);
      offLineOrder.setRebate(rebate);
      long scoreB = (long) Math.ceil(truePirce * Constants.SCOREB / 10000.0);
      offLineOrder.setScoreB(scoreB);
      offLineOrder.setTransferMoney(offLineOrder.getTotalPrice() - offLineOrder.getLjCommission());
    }
    offLineOrderRepository.save(offLineOrder);
    return offLineOrder;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void paySuccess(String orderSid) {
    OffLineOrder offLineOrder = offLineOrderRepository.findByOrderSid(orderSid);
    if (offLineOrder.getState() == 0) {
      if (offLineOrder.getRebateWay() == 0) {
        //对于非会员 消费后只增加b积分
        scoreBService.paySuccess(offLineOrder);
      } else {
        //对于乐加会员,消费成功后a,b积分均改变,
        try {
          scoreAService.paySuccessForMember(offLineOrder);
        } catch (Exception e) {
          log.error("该笔订单出现问题===========" + orderSid);
        }
        scoreBService.paySuccess(offLineOrder);
      }
      //不管会员还说非会员,消费完成对商家待转账金额增加
      merchantService.paySuccess(offLineOrder);
      offLineOrder.setState(1);
      offLineOrder.setCompleteDate(new Date());
      offLineOrderRepository.save(offLineOrder);
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public OffLineOrder findOffLineOrderByOrderSid(String orderSid) {
    return offLineOrderRepository.findByOrderSid(orderSid);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public OffLineOrder payByScoreA(String userSid, String merchantId, String totalPrice) {
    OffLineOrder offLineOrder = new OffLineOrder();
    long scoreA = Long.parseLong(totalPrice);
    offLineOrder.setLeJiaUser(leJiaUserService.findUserByUserSid(userSid));
    offLineOrder.setTotalPrice(scoreA);
    offLineOrder.setTrueScore(scoreA);
    offLineOrder.setTruePay(0L);
    offLineOrder.setCreatedDate(new Date());
    Merchant merchant = merchantService.findMerchantById(Long.parseLong(merchantId));
    offLineOrder.setMerchant(merchant);
    offLineOrder.setWxCommission(0L);
    offLineOrder.setState(1);
    offLineOrder.setPayWay(new PayWay(2L));
    if (merchant.getPartnership() != 0) { //代表乐加签约商家,会给消费者返现
      offLineOrder.setRebateWay(1);
      long ljCommission = (long) Math.ceil(scoreA * merchant.getLjCommission() / 100.0);
      offLineOrder.setLjCommission(ljCommission);
      long rebate = (long) Math.ceil(ljCommission * 50 / 100.0);
      offLineOrder.setRebate(rebate);
      long scoreB = (long) Math.ceil(scoreA * Constants.SCOREB / 10000.0);
      offLineOrder.setScoreB(scoreB);
      offLineOrder.setTransferMoney(offLineOrder.getTotalPrice() - offLineOrder.getLjCommission());
      scoreAService.paySuccessForMember(offLineOrder);
      scoreBService.paySuccess(offLineOrder);
    } else {
      offLineOrder.setRebateWay(0);
      offLineOrder.setTransferMoney(offLineOrder.getTotalPrice());
      scoreBService.paySuccess(offLineOrder);
    }
    offLineOrder.setCompleteDate(new Date());
    merchantService.paySuccess(offLineOrder);
    offLineOrderRepository.save(offLineOrder);
    return offLineOrder;
  }
}
