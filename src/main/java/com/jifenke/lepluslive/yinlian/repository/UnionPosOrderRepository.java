package com.jifenke.lepluslive.yinlian.repository;

import com.jifenke.lepluslive.yinlian.domain.entities.UnionPosOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lss on 2017/3/30.
 */
public interface UnionPosOrderRepository extends JpaRepository<UnionPosOrder, Long> {
    Page findAll(Specification<UnionPosOrder> whereClause, Pageable pageRequest);
}
