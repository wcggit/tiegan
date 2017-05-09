package com.jifenke.lepluslive.fuyou.repository;

import com.jifenke.lepluslive.fuyou.domain.entities.ScanCodeRefundLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 富友扫码订单退款记录 Created by zhangwen on 16/12/19.
 */
public interface ScanCodeRefundLogRepository extends JpaRepository<ScanCodeRefundLog, Long> {


}
