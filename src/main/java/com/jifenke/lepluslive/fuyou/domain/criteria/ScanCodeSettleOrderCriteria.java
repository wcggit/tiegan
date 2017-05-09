package com.jifenke.lepluslive.fuyou.domain.criteria;

/**
 * 富友扫码结算单查询条件 Created by zhangwen on 16/12/30.
 */
public class ScanCodeSettleOrderCriteria {

  private Integer state;  //转账状态
  //账单日期
  private String startDate;

  private String endDate;

  private String merchantName;  //门店名称

  private String merchantUserName; //商户名称

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

  public String getMerchantName() {
    return merchantName;
  }

  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  public String getMerchantUserName() {
    return merchantUserName;
  }

  public void setMerchantUserName(String merchantUserName) {
    this.merchantUserName = merchantUserName;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }
}
