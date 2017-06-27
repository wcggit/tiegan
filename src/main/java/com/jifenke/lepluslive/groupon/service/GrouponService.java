package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;
import com.jifenke.lepluslive.groupon.repository.GrouponCodeRepository;
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

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
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
        return predicate;
      }
    };
  }
}
