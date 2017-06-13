package com.jifenke.lepluslive.printer.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.service.OffLineOrderService;
import com.jifenke.lepluslive.printer.domain.entities.Printer;
import com.jifenke.lepluslive.printer.service.PrinterService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by lss on 16-11-11.
 */
@RestController
@RequestMapping("/wx")
public class PrinterController {


    @Inject
    private PrinterService printerService;

    @Inject
    private OffLineOrderService offLineOrderService;

    /**
     * 重新打印 17/06/12
     *
     * @param orderSid       订单号
     */
    @RequestMapping(value = "/printer/print")
    public
    LejiaResult goPayPage(@RequestParam String orderSid) {

        OffLineOrder
            order =
            offLineOrderService.findOffLineOrderByOrderSid(orderSid);
        if(order != null){
            Printer printer = printerService.findByMerchant(order.getMerchant().getId());
            if(printer != null){
                if( printer.getState()==1){
                   printerService.addReceipt(orderSid);
                    return LejiaResult.ok();
                }
            }
        }

        return LejiaResult.build(1001,"暂无可用打印机");
    }


}
