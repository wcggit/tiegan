package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponStatistic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 2017/6/27.
 */
public interface GrouponStatisticRepository extends JpaRepository<GrouponStatistic,Long>{

  Page findAll(Specification<GrouponStatistic> whereClause, Pageable pageRequest);

  GrouponStatistic findBySid(String sid);
}
