package com.jifenke.lepluslive.merchant.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by wcg on 16/5/13.
 */
@Entity
@Table(name = "MERCHANT_WALLET_LOG")
public class MerchantWalletLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;


  private String orderSid;

  private Long merchantId;

  private Long beforeChangeMoney; //商户钱包改变前金额

  private Long afterChangeMoney; //改变后的金额

  private Long type; //如果为1代表线下支付订单

  private Date createDate = new Date();

  public Long getType() {
    return type;
  }

  public void setType(Long type) {
    this.type = type;
  }

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

  public Long getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(Long merchantId) {
    this.merchantId = merchantId;
  }

  public Long getBeforeChangeMoney() {
    return beforeChangeMoney;
  }

  public void setBeforeChangeMoney(Long beforeChangeMoney) {
    this.beforeChangeMoney = beforeChangeMoney;
  }

  public Long getAfterChangeMoney() {
    return afterChangeMoney;
  }

  public void setAfterChangeMoney(Long afterChangeMoney) {
    this.afterChangeMoney = afterChangeMoney;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
