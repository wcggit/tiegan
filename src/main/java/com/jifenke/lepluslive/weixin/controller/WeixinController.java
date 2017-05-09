package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.fuyou.domain.criteria.ScanCodeOrderCriteria;
import com.jifenke.lepluslive.fuyou.domain.entities.ScanCodeMerchantStatement;
import com.jifenke.lepluslive.fuyou.domain.entities.ScanCodeOrder;
import com.jifenke.lepluslive.fuyou.service.ScanCodeMerchantStatementService;
import com.jifenke.lepluslive.fuyou.service.ScanCodeOrderService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.domain.criteria.OLOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.*;
import com.jifenke.lepluslive.merchant.service.*;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.service.FinanicalStatisticService;
import com.jifenke.lepluslive.order.service.OffLineOrderService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wcg on 16/3/18.
 */
@RestController
public class WeixinController {

    private static Logger log = LoggerFactory.getLogger(WeixinController.class);

    private String appId = Constants.APPID;

    @Value("${weixin.weixinRootUrl}")
    private String weixinRootUrl;

    @Inject
    private WeiXinService weiXinService;

    @Inject
    private MerchantWeiXinUserService merchantWeiXinUserService;

    @Inject
    private OffLineOrderService offLineOrderService;

    @Inject
    private FinanicalStatisticService finanicalStatisticService;

    @Inject
    private MerchantUserShopService merchantUserShopService;

    @Inject
    private MerchantUserService merchantUserService;

    @Inject
    private TemporaryMerchantUserService temporaryMerchantUserService;

    @Inject
    private MerchantService merchantService;

    @Inject
    private MerchantScanPayWayService merchantScanPayWayService;

    @Inject
    private LeJiaUserService leJiaUserService;

    @Inject
    private ScanCodeMerchantStatementService scanCodeMerchantStatementService;

    @Inject
    private ScanCodeOrderService scanCodeOrderService;


    /**
     * 微信授权回调
     */
    @RequestMapping("/wx/userRegister")
    public String userRegister(@RequestParam(required = false) String action,
                               @RequestParam String code,
                               HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (code == null) {
            return "禁止授权无法访问app";
        }
        Map<String, Object> map = weiXinService.getSnsAccessToken(code);
        String openid = map.get("openid").toString();
        //获取accessToken与openid
        if (map.get("errcode") != null) {
            log.error(map.get("errcode").toString() + map.get("errmsg").toString());
        }
        MerchantWeiXinUser weiXinUser = merchantWeiXinUserService.findWeiXinUserByOpenId(openid);
        String accessToken = map.get("access_token").toString();
        //2种情况 当用户不存在时,当上次登录距离此次已经经过了3天
        if (weiXinUser == null || new Date(
                weiXinUser.getLastUserInfoDate().getTime() + 3 * 24 * 60 * 60 * 1000)
                .before(new Date())) {
            Map<String, Object> userDetail = weiXinService.getDetailWeiXinUser(accessToken, openid);
            if (userDetail.get("errcode") != null) {
                log.error(userDetail.get("errcode").toString() + userDetail.get("errmsg").toString());
            }
            try {
                merchantWeiXinUserService.saveWeiXinUser(userDetail, map);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MerchantUser merchantUser = weiXinUser.getMerchantUser();
            if (merchantUser != null) {
                UsernamePasswordToken
                        token =
                        new UsernamePasswordToken(merchantUser.getName(), merchantUser.getPassword(),
                                "UTF-8");
                //获取当前的Subject
                //  token.setRememberMe(true);
                Subject currentUser = SecurityUtils.getSubject();
                try {
                    //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
                    //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
                    //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
                    currentUser.login(token);
                } catch (Exception uae) {

                }
            }
        }
        try {
            CookieUtils.setCookie(request, response, appId + "-user-open-id", openid,
                    Constants.COOKIE_DISABLE_TIME);
            // request.getRequestDispatcher(action).forward(request, response);
            response.sendRedirect(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 交易记录 乐加 富有 跳页0
     */
    @RequestMapping(value = "/wx/tradeList", method = RequestMethod.GET)
    public ModelAndView tradeList(Model model) {


        Merchant merchant = getMerchant();
        MerchantScanPayWay merchantScanPayWay = merchantScanPayWayService.findByMerchantId(merchant.getId());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date) + "%";
        int totalPrice = 0;
        int transferMoney = 0;
        if (merchantScanPayWay.getType() != null && merchantScanPayWay.getType() == 0) {

            List<Object[]> returnValue = scanCodeOrderService.findTotalPriceAndTransferMoneySum(today, merchant.getId());

            if (returnValue.get(0)[0] != null) {
                totalPrice = Integer.valueOf(returnValue.get(0)[0].toString());
            }

            if (returnValue.get(0)[1] != null) {
                transferMoney = Integer.valueOf(returnValue.get(0)[1].toString());
            }
            List<Integer> integerList = new ArrayList<Integer>();
            integerList.add(totalPrice);
            integerList.add(transferMoney);
            model.addAttribute("integerList", integerList);
            return MvUtil.go("/weixin/tradeRecordFuyou");
        } else {
            List<Object[]> returnValue = offLineOrderService.findTotalPriceAndTransferMoneySum(today, merchant.getId());
            if (returnValue.get(0)[0] != null) {
                totalPrice = Integer.valueOf(returnValue.get(0)[0].toString());
            }
            if (returnValue.get(0)[1] != null) {
                transferMoney = Integer.valueOf(returnValue.get(0)[1].toString());
            }
            List<Integer> integerList = new ArrayList<Integer>();
            integerList.add(totalPrice);
            integerList.add(transferMoney);
            model.addAttribute("integerList", integerList);
            return MvUtil.go("/weixin/tradeRecord");
        }
    }


    /**
     * 交易记录 富有订单ajax请求0
     */
    @RequestMapping(value = "/wx/scanCodeOrder", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult findScanCodeOrderByPage(@RequestBody ScanCodeOrderCriteria scanCodeOrderCriteria) {
        if (scanCodeOrderCriteria.getOffset() == null) {
            scanCodeOrderCriteria.setOffset(1);
        }
        scanCodeOrderCriteria.setState(1);
        Merchant merchant = getMerchant();
        scanCodeOrderCriteria.setMerchantName(merchant.getName());

        Page page = scanCodeOrderService.findOrderByPage(scanCodeOrderCriteria, 10);
        scanCodeOrderCriteria.setOffset(1);
        Page page2 = scanCodeOrderService.findOrderByPage(scanCodeOrderCriteria, 100000);
        List<ScanCodeOrder> scanCodeOrderList = page2.getContent();
        int totalElements = scanCodeOrderList.size();
        int totalPrice = 0;
        int transferMoney = 0;
        int Commission = 0;
        List<Integer> integerList = new ArrayList<Integer>();
        for (ScanCodeOrder scanCodeOrder : scanCodeOrderList) {
            totalPrice += scanCodeOrder.getTotalPrice();
            transferMoney += scanCodeOrder.getTransferMoney();
            Commission += scanCodeOrder.getCommission();
        }
        integerList.add(totalElements);
        integerList.add(totalPrice);
        integerList.add(transferMoney);
        integerList.add(Commission);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("content", page.getContent());
        map.put("integerList", integerList);
        map.put("merchantName", merchant.getName());
        return LejiaResult.ok(map);
    }


    /**
     * 交易记录每笔订单详情  富有0
     */
    @RequestMapping(value = "/wx/scanCodeOrder/{sid}", method = RequestMethod.GET)
    public ModelAndView goScanCodeOrderInfo(
            @PathVariable String sid,
            Model model) {
        ScanCodeOrder scanCodeOrder = scanCodeOrderService.findByOrderSid(sid);
        model.addAttribute("scanCodeOrder", scanCodeOrder);
        Merchant merchant = scanCodeOrder.getMerchant();
        Long total=scanCodeOrder.getTotalPrice();
        Long trans=scanCodeOrder.getTransferMoney();
        long discount=Math.round((trans/100.0)/(total/100.0)*1000);
        model.addAttribute("discount", discount);
        Long orderType = scanCodeOrder.getOrderType().getId();
        if (orderType == 12004) {
            return MvUtil.go("/weixin/deversionScanCodeOrder");
        } else if (orderType == 12002 || orderType == 12005 || orderType == 12006) {
            return MvUtil.go("/weixin/memberScanCodeOrder");
        } else {
            return MvUtil.go("/weixin/commonScanCodeOrder");
        }
    }


    /**
     * 交易记录 乐加 订单ajax请求0
     */
    @RequestMapping(value = "/wx/offLineOrder", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult findOffLineOrderByPage(@RequestBody OLOrderCriteria olOrderCriteria) {
        if (olOrderCriteria.getOffset() == null) {
            olOrderCriteria.setOffset(1);
        }
        olOrderCriteria.setState(1);
        Merchant merchant = getMerchant();
        olOrderCriteria.setMerchant(merchant.getMerchantSid());

        Page page = offLineOrderService.findOrderByPage(olOrderCriteria, 10);
        olOrderCriteria.setOffset(1);
        Page page2 = offLineOrderService.findOrderByPage(olOrderCriteria, 100000);
        List<OffLineOrder> offLineOrderList = page2.getContent();
        int totalElements = offLineOrderList.size();
        int totalPrice = 0;
        int transferMoney = 0;
        int Commission = 0;
        List<Integer> integerList = new ArrayList<Integer>();
        for (OffLineOrder offLineOrder : offLineOrderList) {
            totalPrice += offLineOrder.getTotalPrice();
            transferMoney += offLineOrder.getTransferMoney();
            Commission += offLineOrder.getLjCommission();
        }
        integerList.add(totalElements);
        integerList.add(totalPrice);
        integerList.add(transferMoney);
        integerList.add(Commission);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("content", page.getContent());
        map.put("integerList", integerList);
        map.put("merchantName", merchant.getName());
        return LejiaResult.ok(map);
    }


    /**
     * 交易记录每笔订单详情  乐加0
     */
    @RequestMapping(value = "/wx/offLineOrder/{sid}", method = RequestMethod.GET)
    public ModelAndView goOrderInfo(
            @PathVariable String sid,
            Model model) {
        OffLineOrder offLineOrder = offLineOrderService.findOffLineOrderByOrderSid(sid);
        model.addAttribute("order", offLineOrder);
        Long total=offLineOrder.getTotalPrice();
        Long trans=offLineOrder.getTransferMoney();
        long discount=Math.round((trans/100.0)/(total/100.0)*1000);
        model.addAttribute("discount", discount);

        Merchant merchant = offLineOrder.getMerchant();
        int rebateWay = offLineOrder.getRebateWay();
        if (rebateWay == 1) {
            return MvUtil.go("/weixin/deversionOrder");
        } else if (rebateWay == 2 || rebateWay == 3 || rebateWay == 5 || rebateWay == 6) {
            return MvUtil.go("/weixin/memberOrder");
        } else {
            return MvUtil.go("/weixin/commonOrder");
        }
    }


    /**
     * 门店切换0
     */
    @RequestMapping(value = "/wx/merchantChange", method = RequestMethod.GET)
    public ModelAndView merchantChange(String data, Model model) {
        List<Merchant> merchantList = new ArrayList<Merchant>();
        Subject currentUser = SecurityUtils.getSubject();
        PrincipalCollection principals = currentUser.getPrincipals();
        String userName = (String) principals.getPrimaryPrincipal();
        MerchantUser merchantUser = merchantUserService.findByName(userName);
        if (merchantUser != null) {
            List<MerchantUserShop> MerchantUserShopList = merchantUserShopService.findMerchantByMerchantUserId(merchantUser.getId());
            for (MerchantUserShop merchantUserShop : MerchantUserShopList) {
                merchantList.add(merchantUserShop.getMerchant());
            }
            model.addAttribute("merchantList", merchantList);
        }
        if (merchantUser != null) {
            TemporaryMerchantUserShop temporaryMerchantUserShop = temporaryMerchantUserService.findByMerchantUserId(merchantUser.getId());
            if (temporaryMerchantUserShop == null) {

                model.addAttribute("temporaryMerchantUserShop", "");

            } else {
                model.addAttribute("temporaryMerchantUserShop", temporaryMerchantUserShop);
            }
        }
        model.addAttribute("data", data);
        return MvUtil.go("/weixin/changeStore");
    }

    /**
     * 确认门店切换0
     */
    @RequestMapping(value = "/wx/confirmMerchantChange", method = RequestMethod.GET)
    public ModelAndView confirmMerchantChange(String data, Model model, Long merchantId) {
        Subject currentUser = SecurityUtils.getSubject();
        PrincipalCollection principals = currentUser.getPrincipals();
        String userName = (String) principals.getPrimaryPrincipal();
        MerchantUser merchantUser = merchantUserService.findByName(userName);
        Merchant merchant = merchantService.findMerchantById(merchantId);
        if (merchantUser != null) {
            TemporaryMerchantUserShop temporaryMerchantUserShop = temporaryMerchantUserService.findByMerchantUserId(merchantUser.getId());
            if (temporaryMerchantUserShop == null) {
                TemporaryMerchantUserShop temporaryMerchantUserShop2 = new TemporaryMerchantUserShop();
                temporaryMerchantUserShop2.setMerchant(merchant);
                temporaryMerchantUserShop2.setMerchantUser(merchantUser);
                temporaryMerchantUserService.addOne(temporaryMerchantUserShop2);
            } else {
                temporaryMerchantUserService.deleteOne(temporaryMerchantUserShop);
                TemporaryMerchantUserShop temporaryMerchantUserShop2 = new TemporaryMerchantUserShop();
                temporaryMerchantUserShop2.setMerchant(merchant);
                temporaryMerchantUserShop2.setMerchantUser(merchantUser);
                temporaryMerchantUserService.addOne(temporaryMerchantUserShop2);
            }
        }
        if (data == null || data == "") {
            data = "/wx/tradeList";
        }
        return new ModelAndView("redirect:" + data);
    }


    /**
     * 门店选择0
     */
    @RequestMapping(value = "/wx/merchantChoose", method = RequestMethod.GET)
    public ModelAndView merchantChoose(Model model) {
        List<Merchant> merchantList = new ArrayList<Merchant>();
        Subject currentUser = SecurityUtils.getSubject();
        PrincipalCollection principals = currentUser.getPrincipals();
        String userName = (String) principals.getPrimaryPrincipal();
        MerchantUser merchantUser = merchantUserService.findByName(userName);
        if (merchantUser != null) {
            List<MerchantUserShop> MerchantUserShopList = merchantUserShopService.findMerchantByMerchantUserId(merchantUser.getId());
            for (MerchantUserShop merchantUserShop : MerchantUserShopList) {
                merchantList.add(merchantUserShop.getMerchant());
            }
            Long merchantUserCreateUserId = merchantUser.getCreateUserId();
            MerchantUser merchantUserCreateUser = null;
            if (merchantUserCreateUserId != null) {
                merchantUserCreateUser = merchantUserService.findById(merchantUserCreateUserId);
            }
            model.addAttribute("merchantList", merchantList);
            model.addAttribute("merchantUser", merchantUser);
            model.addAttribute("merchantUserCreateUser", merchantUserCreateUser);
        }
        return MvUtil.go("/weixin/loginSuccess");
    }


    /**
     * 确认门店选择0
     */
    @RequestMapping(value = "/wx/confirmMerchantChoose", method = RequestMethod.GET)
    public ModelAndView confirmMerchantChoose(Model model, Long merchantId) {
        Subject currentUser = SecurityUtils.getSubject();
        PrincipalCollection principals = currentUser.getPrincipals();
        String userName = (String) principals.getPrimaryPrincipal();
        MerchantUser merchantUser = merchantUserService.findByName(userName);
        Merchant merchant = merchantService.findMerchantById(merchantId);
        if (merchantUser != null) {
            TemporaryMerchantUserShop temporaryMerchantUserShop = temporaryMerchantUserService.findByMerchantUserId(merchantUser.getId());
            if (temporaryMerchantUserShop == null) {
                TemporaryMerchantUserShop temporaryMerchantUserShop2 = new TemporaryMerchantUserShop();
                temporaryMerchantUserShop2.setMerchant(merchant);
                temporaryMerchantUserShop2.setMerchantUser(merchantUser);
                temporaryMerchantUserService.addOne(temporaryMerchantUserShop2);
            } else {
                temporaryMerchantUserService.deleteOne(temporaryMerchantUserShop);
                TemporaryMerchantUserShop temporaryMerchantUserShop2 = new TemporaryMerchantUserShop();
                temporaryMerchantUserShop2.setMerchant(merchant);
                temporaryMerchantUserShop2.setMerchantUser(merchantUser);
                temporaryMerchantUserService.addOne(temporaryMerchantUserShop2);
            }
        }
        return new ModelAndView("redirect:/wx/merchantCenter");
    }


    /**
     * 跳商户中心页面1
     */
    @RequestMapping(value = "/wx/merchantCenter", method = RequestMethod.GET)
    public ModelAndView merchantCenter(Model model) {
        Subject currentUser = SecurityUtils.getSubject();
        PrincipalCollection principals = currentUser.getPrincipals();
        String userName = (String) principals.getPrimaryPrincipal();
        MerchantUser merchantUser = merchantUserService.findByName(userName);
        Long merchantUserCreateUserId = merchantUser.getCreateUserId();
        MerchantUser merchantUserCreateUser = null;
        if (merchantUserCreateUserId != null) {
            merchantUserCreateUser = merchantUserService.findById(merchantUserCreateUserId);
        }
        TemporaryMerchantUserShop temporaryMerchantUserShop = temporaryMerchantUserService.findByMerchantUserId(merchantUser.getId());
        model.addAttribute("merchant", temporaryMerchantUserShop.getMerchant());
        List<MerchantUserShop> merchantUserShopList = merchantUserShopService.findMerchantByMerchantUserId(merchantUser.getId());
        List<Merchant> merchantList = new ArrayList<Merchant>();
        for (MerchantUserShop merchantUserShop : merchantUserShopList) {
            merchantList.add(merchantUserShop.getMerchant());
        }
        Long merchantUserLockUsers = 0l;
        Long merchantLockUsers = 0l;
        Long lockLimit = 0l;
        Long userLimit = 0l;
        for (Merchant merchant : merchantList) {
            merchantUserLockUsers += leJiaUserService.findNumberUserByBindMerchant(merchant.getId());
        }
        merchantLockUsers = leJiaUserService.findNumberUserByBindMerchant(temporaryMerchantUserShop.getMerchant().getId());
        if (merchantUserCreateUser.getLockLimit() != null) {
            lockLimit = merchantUserCreateUser.getLockLimit();
        }
        if (temporaryMerchantUserShop.getMerchant().getUserLimit() != null) {
            userLimit = temporaryMerchantUserShop.getMerchant().getUserLimit();
        }
        List<Long> lockList = new ArrayList<>();
        lockList.add(merchantUserLockUsers);
        lockList.add(merchantLockUsers);
        lockList.add(lockLimit);
        lockList.add(userLimit);
        model.addAttribute("lockList", lockList);
        return MvUtil.go("/weixin/merchantCenter");
    }

    /**
     * 商户二维码0
     */
    @RequestMapping(value = "/wx/qrcode", method = RequestMethod.GET)
    public ModelAndView wxQrCode(HttpServletRequest request, Model model) {
        Merchant merchant = getMerchant();
        model.addAttribute("merchant", merchant);
        return MvUtil.go("/weixin/storeCode");
    }

    /**
     * 商户会员邀请码0
     */
    @RequestMapping(value = "/wx/invitationCode", method = RequestMethod.GET)
    public ModelAndView invitationCode(HttpServletRequest request, Model model) {
        Merchant merchant = getMerchant();
        Integer qrCode = 0;
        MerchantInfo merchantInfo = merchant.getMerchantInfo();
        if (merchantInfo != null && merchantInfo.getQrCode() != null) {
            qrCode = merchantInfo.getQrCode();
        }
        if (qrCode == 1) {
            model.addAttribute("ticket", "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + merchant.getMerchantInfo().getTicket());
            return MvUtil.go("/weixin/memberCode");
        } else {
            return null;
        }
    }


    /**
     * 每日账单 乐加 富有跳页0
     */
    @RequestMapping(value = "/wx/financialList", method = RequestMethod.GET)
    public ModelAndView goFinanicalListPage(Model model) {
        Merchant merchant = getMerchant();
        MerchantScanPayWay merchantScanPayWay = merchantScanPayWayService.findByMerchantId(merchant.getId());
        if (merchantScanPayWay.getType() != null && merchantScanPayWay.getType() == 0) {
            List<Object[]> list = scanCodeMerchantStatementService.findTotalAndNumberFromDailyOrderFuyou(merchant.getId());
            Long total = 0l;
            Long number = 0l;
            if (list.get(0)[0] != null) {
                total = Long.valueOf(list.get(0)[0].toString());
            }
            if (list.get(0)[1] != null) {
                number = Long.valueOf(list.get(0)[1].toString());
            }
            if (number == 0) {
                number = 1l;
            }
            Double average = (Math.round(total / number) * 100.0) / 100.0;
            model.addAttribute("total", total);
            model.addAttribute("average", average);
            return MvUtil.go("/weixin/dailyOrder_fuyou");
        } else {
            List<Object[]> list = finanicalStatisticService.findTotalAndNumberFromDailyOrderlePLus(merchant.getId());
            Long total = 0l;
            Long number = 1l;
            if (list.get(0)[0] != null) {
                total = Long.valueOf(list.get(0)[0].toString());
            }
            if (list.get(0)[1] != null&&list.get(0)[1].toString()!="0") {
                number = Long.valueOf(list.get(0)[1].toString());
            }
            Double average = (Math.round(total / number) * 100.0) / 100.0;
            model.addAttribute("total", total);
            model.addAttribute("average", average);
            return MvUtil.go("/weixin/dailyOrder_lePLus");
        }
    }

    /**
     * 每日账单ajax 乐加0
     */
    @RequestMapping(value = "/wx/financial", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult findFinancialByPage(
            @RequestParam(value = "page", required = false) Integer offset,
            HttpServletRequest request) {
        if (offset == null) {
            offset = 1;
        }
        Merchant merchant = getMerchant();
        Page page = finanicalStatisticService.findFinanicalByMerchantAndPage(merchant, offset);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("content", page.getContent());
        map.put("merchantName", merchant.getName());
        return LejiaResult.ok(map);
    }


    /**
     * 每日账单每月统计 乐加0
     */
    @RequestMapping(value = "/wx/sumMonthlyFinancialIncome", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult getMonthlyFinancialIncome(
            @RequestParam Long date) {
        Merchant merchant = getMerchant();
        return LejiaResult.ok(finanicalStatisticService.sumTotalPriceByMonth(date, merchant));
    }

    /**
     * 每日账单ajax 富有0
     */
    @RequestMapping(value = "/wx/fuyouFinancial", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult findFuyouFinancialByPage(
            @RequestParam(value = "page", required = false) Integer offset,
            HttpServletRequest request) {
        if (offset == null) {
            offset = 1;
        }
        Merchant merchant = getMerchant();
        Page page = scanCodeMerchantStatementService.findFuyouFinanicalByMerchantAndPage(merchant, offset);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("content", page.getContent());
        map.put("merchantName", merchant.getName());
        return LejiaResult.ok(map);
    }

    /**
     * 每日账单每月统计 富有0
     */
    @RequestMapping(value = "/wx/sumMonthlyFuyouFinancialIncome", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult getMonthlyFuyouFinancialIncome(
            @RequestParam String date) {
        Merchant merchant = getMerchant();
        Long totalPriceByMonth = 0l;
        try {
            String str = date;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(str);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            Date end = MvUtil.getMonthEndDate(year, month, calendar);
            Date start = MvUtil.getMonthStartDate(year, month, calendar);
            String startStr = sdf.format(start);
            String endStr = sdf.format(end);
            totalPriceByMonth = scanCodeMerchantStatementService.sumTotalPriceByMonth(startStr, endStr, merchant.getId());

        } catch (Exception e) {

        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("totalPriceByMonth", totalPriceByMonth);
        return LejiaResult.ok(map);
    }


    /**
     * 每日账单详情 乐加0
     */
    @RequestMapping(value = "/wx/financial/{id}", method = RequestMethod.GET)
    public ModelAndView goFinancialInfoPage(@PathVariable String id, Model model) {
        FinancialStatistic
                financialStatistic =
                finanicalStatisticService.findFinancialByStatisticId(id);
        Merchant merchant = financialStatistic.getMerchant();
        Date balanceDate = financialStatistic.getBalanceDate();
        Long weiXinOrderWeiXinTransferMoney = 0L;
        Long lePlusOrderWeiXinTransferMoney = 0L;
        Long lePlusOrderScoreaTransferMoney = 0L;
        Long transferMoney = 0L;
        List<OffLineOrder>
                offLineOrders =
                offLineOrderService
                        .findOffLineOrderByMerchantAndDate(merchant, balanceDate);
        for (OffLineOrder offLineOrder : offLineOrders) {
            transferMoney += offLineOrder.getTransferMoney();
        }


        List<OffLineOrder>
                weiXinoffLineOrders =
                offLineOrderService
                        .findWeiXinOrderByMerchantAndCompleteDateBetween(merchant, balanceDate);

        for (OffLineOrder weiXinoffLineOrder : weiXinoffLineOrders) {
            weiXinOrderWeiXinTransferMoney += weiXinoffLineOrder.getTransferMoneyFromTruePay();
        }


        List<OffLineOrder>
                lePlusoffLineOrders =
                offLineOrderService
                        .findLePlusOrderByMerchantAndCompleteDateBetween(merchant, balanceDate);

        for (OffLineOrder lePlusoffLineOrder : lePlusoffLineOrders) {
            lePlusOrderWeiXinTransferMoney += lePlusoffLineOrder.getTransferMoneyFromTruePay();
        }

        for (OffLineOrder lePlusoffLineOrder : lePlusoffLineOrders) {
            lePlusOrderScoreaTransferMoney += lePlusoffLineOrder.getTrueScore();
        }

        String bankNumber = merchant.getMerchantBank().getBankNumber();
        model.addAttribute("weiXinoffLineOrders", weiXinoffLineOrders);
        model.addAttribute("lePlusoffLineOrders", lePlusoffLineOrders);
        model.addAttribute("financial", financialStatistic);
        model.addAttribute("merchant", merchant);
        if(bankNumber.length()>4){
            model.addAttribute("bank1", bankNumber.substring(0, 4));
            model.addAttribute("bank2", bankNumber.substring(bankNumber.length() - 4, bankNumber.length()));
        }
        model.addAttribute("weiXinOrderWeiXinTransferMoney", weiXinOrderWeiXinTransferMoney);
        model.addAttribute("lePlusOrderWeiXinTransferMoney", lePlusOrderWeiXinTransferMoney);
        model.addAttribute("lePlusOrderScoreaTransferMoney", lePlusOrderScoreaTransferMoney);
        model.addAttribute("transferMoney", transferMoney);
        model.addAttribute("balanceDate", balanceDate);
        return MvUtil.go("/weixin/orderDetail_lePlus");
    }


    /**
     * 每日账单详情 富有0
     */
    @RequestMapping(value = "/wx/ScanCodeMerchantStatement/{id}", method = RequestMethod.GET)
    public ModelAndView goScanCodeMerchantStatementInfoPage(@PathVariable String id, Model model) {
        ScanCodeMerchantStatement scanCodeMerchantStatement = scanCodeMerchantStatementService.findByOrderSid(id);
        Merchant merchant = scanCodeMerchantStatement.getMerchant();
        String tradeDate = scanCodeMerchantStatement.getTradeDate();
        List<ScanCodeOrder> commonScanCodeOrderList = scanCodeOrderService.findCommonOrderMerchantIdAndCompleteDate(merchant.getId(), tradeDate);
        Long commonTotal = 0l;
        Long commonScorea = 0l;
        Long commonWeixin = 0l;
        Long leplusTotal = 0l;
        Long leplusScorea = 0l;
        Long leplusWeixin = 0l;
        Date date = null;

        for (ScanCodeOrder commonScanCodeOrder : commonScanCodeOrderList) {
            commonTotal += commonScanCodeOrder.getTransferMoney();
            commonScorea += commonScanCodeOrder.getTransferMoneyFromScore();
            commonWeixin += commonScanCodeOrder.getTransferMoneyFromTruePay();
        }
        List<ScanCodeOrder> leplusScanCodeOrderList = scanCodeOrderService.findLeplusOrderMerchantIdAndCompleteDate(merchant.getId(), tradeDate);
        for (ScanCodeOrder leplusScanCodeOrder : leplusScanCodeOrderList) {
            leplusTotal += leplusScanCodeOrder.getTransferMoney();
            leplusScorea += leplusScanCodeOrder.getTransferMoneyFromScore();
            leplusWeixin += leplusScanCodeOrder.getTransferMoneyFromTruePay();
        }
        if (commonScanCodeOrderList.size() != 0) {
            date = commonScanCodeOrderList.get(0).getCompleteDate();
        }
        if (leplusScanCodeOrderList.size() != 0) {
            date = leplusScanCodeOrderList.get(0).getCompleteDate();
        }
        String bankNumber = merchant.getMerchantBank().getBankNumber();
        model.addAttribute("date", date);
        model.addAttribute("merchant", merchant);
        model.addAttribute("commonTotal", commonTotal);
        model.addAttribute("commonScorea", commonScorea);
        model.addAttribute("commonWeixin", commonWeixin);
        model.addAttribute("leplusTotal", leplusTotal);
        model.addAttribute("leplusScorea", leplusScorea);
        model.addAttribute("leplusWeixin", leplusWeixin);
        model.addAttribute("commonScanCodeOrderList", commonScanCodeOrderList);
        model.addAttribute("leplusScanCodeOrderList", leplusScanCodeOrderList);
        model.addAttribute("bank1", bankNumber.substring(0, 4));
        model.addAttribute("bank2", bankNumber.substring(bankNumber.length() - 4, bankNumber.length()));
        model.addAttribute("scanCodeMerchantStatement", scanCodeMerchantStatement);
        return MvUtil.go("/weixin/orderDetail_fuyou");
    }


    /**
     * 商户信息 1
     */
    @RequestMapping(value = "/wx/merchantInfo", method = RequestMethod.GET)
    public ModelAndView merchantInfo(Model model) {
        Merchant merchant = getMerchant();
        String bankNumber = merchant.getMerchantBank().getBankNumber();
        model.addAttribute("merchant", merchant);
        model.addAttribute("bank2", bankNumber.substring(bankNumber.length() - 4, bankNumber.length()));
        model.addAttribute("bank1", bankNumber.substring(0, 4));
        if (merchant.getPartnership() == 1) {
            Double commission = 1 - merchant.getLjCommission().doubleValue() / 100.0;
            String s = (long) (commission * 1000) + "";
            char[] chars = s.toCharArray();
            if (chars[2] == '0') {
                s = s.substring(0, 2);
                if (chars[1] == '0') {
                    System.out.println(s);
                    s = s.substring(0, 1);
                }
            } else {
                s = new BigDecimal(s).divide(new BigDecimal(10)).toString();
            }
            model.addAttribute("commission", s);
        }
        return MvUtil.go("/weixin/signInfo");
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


