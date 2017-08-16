package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 2017/6/19.
 */
public interface GrouponOrderRepository extends JpaRepository<GrouponOrder,Long> {

  GrouponOrder findOneByOrderSid(String sid);
}
