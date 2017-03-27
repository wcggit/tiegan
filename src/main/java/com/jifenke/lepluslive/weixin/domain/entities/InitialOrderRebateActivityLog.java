package com.jifenke.lepluslive.weixin.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wcg on 16/9/12.
 */
@Entity
@Table(name = "INITIAL_ORDER_REBATE_ACTIVITY_LOG")
public class InitialOrderRebateActivityLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;


  @OneToOne(cascade = CascadeType.ALL)
  private OffLineOrder offLineOrder;

  @ManyToOne
  private Merchant merchant;

  private String nickname; //防止店主微信号变化 每笔消费记录店主号

  private String headImageUrl;

  private Integer state;  // 完成状态

  private Long rebate; //返利金额

  private String exceptionLog; //如果未发放,说明原因

  private Date createdDate;

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OffLineOrder getOffLineOrder() {
    return offLineOrder;
  }

  public void setOffLineOrder(OffLineOrder offLineOrder) {
    this.offLineOrder = offLineOrder;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getHeadImageUrl() {
    return headImageUrl;
  }

  public void setHeadImageUrl(String headImageUrl) {
    this.headImageUrl = headImageUrl;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Long getRebate() {
    return rebate;
  }

  public void setRebate(Long rebate) {
    this.rebate = rebate;
  }

  public String getExceptionLog() {
    return exceptionLog;
  }

  public void setExceptionLog(String exceptionLog) {
    this.exceptionLog = exceptionLog;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }
}
