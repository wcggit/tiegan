package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by wcg on 2017/6/19.
 */
public interface GrouponCodeRepository extends JpaRepository<GrouponCode,Long> {

  List<GrouponCode> findByMerchantAndState(Merchant merchant, int i);

  GrouponCode findOneBySid(String sid);

  Page<GrouponCode> findAll(Specification<GrouponCode> whereClause, Pageable pageRequest);

  List<GrouponCode> findByMerchantAndCodeTypeAndStateAndCheckDateBetween(Merchant merchant, int codeType, int i,
                                                            Date start, Date end);
}
