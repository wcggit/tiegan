package com.jifenke.lepluslive.fuyou.domain.criteria;

/**
 * 富友扫码退款单查询条件 Created by zhangwen on 16/12/28.
 */
public class ScanCodeRefundOrderCriteria {

  private String startDate; //交易完成时间

  private String endDate;

  private String refundStartDate;  //退款时间

  private String refundEndDate;

  private String userSid; //消费者TOKEN

  private String phoneNumber; //消费者手机号

  private String merchantName;  //门店名称=Merchant.name

  private Long merchantUserId;  //商户ID=MerchantUser.id

  private Integer orderType; //订单类型6种=Category.id(12001~12006)

  private String orderSid;  //订单编号

  private String merchantNum; //移动商户号

  private Integer offset; //页码

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getRefundStartDate() {
    return refundStartDate;
  }

  public void setRefundStartDate(String refundStartDate) {
    this.refundStartDate = refundStartDate;
  }

  public String getRefundEndDate() {
    return refundEndDate;
  }

  public void setRefundEndDate(String refundEndDate) {
    this.refundEndDate = refundEndDate;
  }

  public String getUserSid() {
    return userSid;
  }

  public void setUserSid(String userSid) {
    this.userSid = userSid;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getMerchantName() {
    return merchantName;
  }

  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  public Long getMerchantUserId() {
    return merchantUserId;
  }

  public void setMerchantUserId(Long merchantUserId) {
    this.merchantUserId = merchantUserId;
  }

  public Integer getOrderType() {
    return orderType;
  }

  public void setOrderType(Integer orderType) {
    this.orderType = orderType;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public String getMerchantNum() {
    return merchantNum;
  }

  public void setMerchantNum(String merchantNum) {
    this.merchantNum = merchantNum;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }
}
