package com.jifenke.lepluslive.fuyou.repository;

import com.jifenke.lepluslive.fuyou.domain.entities.ScanCodeOrder;
import com.jifenke.lepluslive.fuyou.domain.entities.ScanCodeRefundOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * 富友扫码退款单 Created by zhangwen on 16/12/22.
 */
public interface ScanCodeRefundOrderRepository extends JpaRepository<ScanCodeRefundOrder, Long> {

  List<ScanCodeRefundOrder> findByScanCodeOrder(ScanCodeOrder scanCodeOrder);

  Page findAll(Specification<ScanCodeRefundOrder> whereClause, Pageable pageRequest);

  /**
   * 获取某一天的退款单列表  2016/12/29
   *
   * @param state 退款单状态
   */
  List<ScanCodeRefundOrder> findByStateAndCompleteDateBetween(Integer state, Date beginDate,
                                                              Date endDate);
}
