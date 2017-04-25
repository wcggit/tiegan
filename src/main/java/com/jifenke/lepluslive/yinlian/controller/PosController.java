package com.jifenke.lepluslive.yinlian.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.criteria.OLOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.*;
import com.jifenke.lepluslive.merchant.service.MerchantUserService;
import com.jifenke.lepluslive.merchant.service.TemporaryMerchantUserService;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.yinlian.domain.criteria.UnionPosOrderCriteria;
import com.jifenke.lepluslive.yinlian.domain.entities.UnionPosOrder;
import com.jifenke.lepluslive.yinlian.service.UnionPosOrderService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lss on 16/3/18.
 */
@RestController
public class PosController {

    @Inject
    private MerchantUserService merchantUserService;

    @Inject
    private TemporaryMerchantUserService temporaryMerchantUserService;

    @Inject
    private UnionPosOrderService unionPosOrderService;
    /**
     * pos订单 跳页
     *
     */

    @RequestMapping(value = "/wx/posOrderList", method = RequestMethod.GET)
    public ModelAndView posOrderList(Model model) {
        Merchant merchant= getMerchant();
        model.addAttribute("merchant",merchant);
        return MvUtil.go("/weixin/posOrder");
    }


    @RequestMapping(value = "/wx/getPosOrderByAjax", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult getPosOrderByAjax(@RequestBody UnionPosOrderCriteria unionPosOrderCriteria) {
        if (unionPosOrderCriteria.getOffset() == null) {
            unionPosOrderCriteria.setOffset(1);
        }
        Merchant merchant = getMerchant();
        unionPosOrderCriteria.setMerchant(merchant.getMerchantSid());
        unionPosOrderCriteria.setState(1);
        Page page=unionPosOrderService.findUnionPosOrderByPage(unionPosOrderCriteria,20);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("page",page);

        unionPosOrderCriteria.setOffset(1);
        Page page2 = unionPosOrderService.findUnionPosOrderByPage(unionPosOrderCriteria, 100000);
        List<UnionPosOrder> unionPosOrderList = page2.getContent();
        int totalElements = unionPosOrderList.size();
        int totalPrice = 0;
        for (UnionPosOrder unionPosOrder : unionPosOrderList) {
            totalPrice += unionPosOrder.getTransferMoney();
        }
        List<Integer> integerList = new ArrayList<Integer>();
        integerList.add(totalElements);
        integerList.add(totalPrice);
        map.put("integerList", integerList);
        return LejiaResult.ok(map);
    }

    public Merchant getMerchant() {
        Subject currentUser = SecurityUtils.getSubject();
        PrincipalCollection principals = currentUser.getPrincipals();
        String userName = (String) principals.getPrimaryPrincipal();
        MerchantUser merchantUser = merchantUserService.findByName(userName);
        TemporaryMerchantUserShop temporaryMerchantUserShop = temporaryMerchantUserService.findByMerchantUserId(merchantUser.getId());
        Merchant merchant = temporaryMerchantUserShop.getMerchant();
        return merchant;
    }
}


