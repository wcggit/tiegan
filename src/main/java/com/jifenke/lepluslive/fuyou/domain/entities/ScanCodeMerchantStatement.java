package com.jifenke.lepluslive.fuyou.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import javax.persistence.*;
import java.util.Date;

/**
 * 富友扫码展示用结算单(以商户ID为单位) Created by zhangwen on 16/12/21.
 */
@Entity
@Table(name = "SCAN_CODE_MERCHANT_STATEMENT")
public class ScanCodeMerchantStatement {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String orderSid = MvUtil.getOrderNumber(); //结算单号

  private Date createdDate;

  private String tradeDate; //账单日期（对应的交易记录是哪一天完成的）（yyyyMMdd）

  @ManyToOne
  private Merchant merchant;  //结算门店

  private Long commonSettlementId;  //生成结算单时，该门店对应的普通商户号

  private Long allianceSettlementId;  //生成结算单时，该门店对应的佣金商户号

  private Long transferMoney = 0L; //商户实际入账(包括现金和红包)=transferMoneyFromTruePay+transferMoneyFromScore

  private Long transferMoneyFromTruePay = 0L; //每笔订单中现金支付转给商户的金额(富友结算)

  private Long transferMoneyFromScore = 0L;   //每笔订单中红包支付转给商户的金额

  private Long refundMoney = 0L;  //退款金额（当天结算金额=退款金额+实际入账）

  private Long merchantUserId;  //对应的商户ID

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(String tradeDate) {
    this.tradeDate = tradeDate;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  public Long getCommonSettlementId() {
    return commonSettlementId;
  }

  public void setCommonSettlementId(Long commonSettlementId) {
    this.commonSettlementId = commonSettlementId;
  }

  public Long getAllianceSettlementId() {
    return allianceSettlementId;
  }

  public void setAllianceSettlementId(Long allianceSettlementId) {
    this.allianceSettlementId = allianceSettlementId;
  }

  public Long getTransferMoney() {
    return transferMoney;
  }

  public void setTransferMoney(Long transferMoney) {
    this.transferMoney = transferMoney;
  }

  public Long getTransferMoneyFromTruePay() {
    return transferMoneyFromTruePay;
  }

  public void setTransferMoneyFromTruePay(Long transferMoneyFromTruePay) {
    this.transferMoneyFromTruePay = transferMoneyFromTruePay;
  }

  public Long getTransferMoneyFromScore() {
    return transferMoneyFromScore;
  }

  public void setTransferMoneyFromScore(Long transferMoneyFromScore) {
    this.transferMoneyFromScore = transferMoneyFromScore;
  }

  public Long getMerchantUserId() {
    return merchantUserId;
  }

  public void setMerchantUserId(Long merchantUserId) {
    this.merchantUserId = merchantUserId;
  }

  public Long getRefundMoney() {
    return refundMoney;
  }

  public void setRefundMoney(Long refundMoney) {
    this.refundMoney = refundMoney;
  }
}
