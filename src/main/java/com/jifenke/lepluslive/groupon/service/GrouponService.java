package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponStatistic;
import com.jifenke.lepluslive.groupon.repository.GrouponCodeRepository;
import com.jifenke.lepluslive.groupon.repository.GrouponOrderRepository;
import com.jifenke.lepluslive.groupon.repository.GrouponStatisticRepository;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.criteria.OLOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.PayWay;
import com.jifenke.lepluslive.partner.service.PartnerService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by wcg on 2017/6/19.
 */
@Service
@Transactional(readOnly = true)
public class GrouponService {

  @Inject
  private GrouponCodeRepository grouponCodeRepository;

  @Inject
  private MerchantService merchantService;

  @Inject
  private PartnerService partnerService;

  @Inject
  private GrouponStatisticRepository grouponStatisticRepository;

  @Inject
  private EntityManager entityManager;

  @Inject
  private GrouponOrderRepository grouponOrderRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<GrouponCode> findGrouponCodeByMerchant(Merchant merchant) {
    return grouponCodeRepository.findByMerchantAndState(merchant, 1);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public GrouponCode findGrouponCodeBySid(String sid) {
    return grouponCodeRepository.findOneBySid(sid);
  }

  /**
   * 核销团购和分润
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void chargeOffGrouponCode(GrouponCode grouponCode, Merchant merchant, String name) {
    grouponCode.setState(1);
    grouponCode.setMerchant(merchant);
    grouponCode.setCheckDate(new Date());
    grouponCode.setMerchantUser(name);
    LeJiaUser leJiaUser = grouponCode.getLeJiaUser();
    GrouponOrder grouponOrder = grouponCode.getGrouponOrder();
    Integer orderstate = 1;
    for (GrouponCode code : grouponOrder.getGrouponCodes()) {
      if (code.getId() != grouponCode.getId()) {
        if (grouponCode.getState() == 0) {
          orderstate = 0;
          break;
        }
      }
    }
    grouponOrder.setOrderState(orderstate);
    if (leJiaUser.getBindMerchant() != null) {
      merchantService
          .shareToMerchant(grouponCode.getShareToLockMerchant(), leJiaUser.getBindMerchant(),
                           grouponCode.getSid(), 15006L);
    }
    if (leJiaUser.getBindPartner() != null) {
      partnerService.shareToPartner(grouponCode.getShareToLockPartner(), leJiaUser.getBindPartner(),
                                    grouponCode.getSid(), 15006L);
      partnerService.shareToPartnerManager(grouponCode.getShareToLockPartnerManager(),
                                           leJiaUser.getBindPartner().getPartnerManager(),
                                           grouponCode.getSid(), 15006L);
    }
    partnerService.shareToPartner(grouponCode.getShareToTradePartner(), merchant.getPartner(),
                                  grouponCode.getSid(), 15006L);
    partnerService.shareToPartnerManager(grouponCode.getShareToTradePartnerManager(),
                                         merchant.getPartner().getPartnerManager(),
                                         grouponCode.getSid(), 15006L);

  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page findGrouponCodeByOffset(Integer offset, Long merchantId) {
    Sort sort = new Sort(Sort.Direction.DESC, "checkDate");
    return grouponCodeRepository
        .findAll(getWhereClause(merchantId),
                 new PageRequest(offset - 1, 10, sort));
  }

  public static Specification<GrouponCode> getWhereClause(Long merchantId) {
    return new Specification<GrouponCode>() {
      @Override
      public Predicate toPredicate(Root<GrouponCode> r, CriteriaQuery<?> q,
                                   CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        predicate.getExpressions().add(
            cb.equal(r.get("merchant"),
                     new Merchant(merchantId)));
        predicate.getExpressions().add(
            cb.equal(r.get("state"),
                     1));
        return predicate;
      }
    };
  }

  public Page<GrouponStatistic> findGrouponStatisticByOffset(Integer offset, Long id) {
    Sort sort = new Sort(Sort.Direction.DESC, "balanceDate");
    return grouponStatisticRepository
        .findAll(new Specification<GrouponStatistic>() {
                   @Override
                   public Predicate toPredicate(Root<GrouponStatistic> r, CriteriaQuery<?> q,
                                                CriteriaBuilder cb) {
                     Predicate predicate = cb.conjunction();

                     predicate.getExpressions().add(
                         cb.equal(r.get("merchant"),
                                  new Merchant(id)));
                     return predicate;
                   }
                 },
                 new PageRequest(offset - 1, 10, sort));
  }

  public GrouponStatistic findGrouponStatisticBySid(String sid) {
    return grouponStatisticRepository.findBySid(sid);
  }

  /**
   * 统计商家核销单总价 佣金 实际到账
   *
   * @param i 卷码类型 0 代表 普通订单 1 会员订单
   */
  public Object[] statisticCode(Merchant merchant, Date start, Date end, int i) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String starts = sdf.format(start);
    String ends = sdf.format(end);
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select ifnull(sum(trasnfer_money),0),count(*),ifnull(sum(total_price),0),ifnull(sum(commission),0) from groupon_code where state = 1 and check_date between '");
    sql.append(starts);
    sql.append("' and '");
    sql.append(ends);
    sql.append("' and merchant_id = ");
    sql.append(merchant.getId());
    sql.append(" and code_type = ");
    sql.append(i);
    List<Object[]> resultList = entityManager.createNativeQuery(sql.toString()).getResultList();
    return resultList.get(0);
  }

  /**
   *
   * @param merchant
   * @param start
   * @param end
   * @param codeType
   * @return
   */
  public List<GrouponCode> findGrouponCodeByMerchantAndDateAndType(Merchant merchant, Date start, Date end,
                                                      int codeType) {
    return grouponCodeRepository.findByMerchantAndCodeTypeAndStateAndCheckDateBetween(merchant,codeType,1,start,end);
  }

  public GrouponOrder findGrouponOrderBySid(String sid) {
    return grouponOrderRepository.findOneByOrderSid(sid);
  }
}
