package com.jifenke.lepluslive.printer.repository;

import com.jifenke.lepluslive.printer.domain.entities.Printer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by root on 16-12-22.
 */
public interface PrinterRepository extends JpaRepository<Printer,Long> {

    @Query(value = "SELECT * from printer where merchant_id=?1", nativeQuery = true)
    Printer findPrinterByMerchantId(Long merchantId);

}
