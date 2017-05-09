package com.jifenke.lepluslive.merchant.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wcg on 16/5/9.
 */
@Entity
@Table(name = "MERCHANT_USER")
public class MerchantUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;//登录名称

    private String password;

    @ManyToOne
    @JsonIgnore
    private Merchant merchant;   // 禁止循环引用

    private Integer type; //9-系统管理员  8-管理员(商户)  2-子账号  【之前：0收营员 1 店主 一个商户只有一个店主】

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    // ---  新版本扩展属性 ---
    @OneToOne
    private MerchantBank merchantBank;  //  银行卡号及名称
    private String merchantName;        // 商户名称
    private String linkMan;             // 商户负责人 （联系人）
    private String phoneNum;            // 联系方式
    private Long lockLimit;             // 锁定上限
    private Date createdDate;           // 创建时间

    @ManyToOne
    private City city;                  // 所在城市

    @ManyToOne
    private Partner partner;            // 合伙人

    private Long createUserId;                                                                  // 所属商户（管理员） ID

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Long getLockLimit() {
        return lockLimit;
    }

    public void setLockLimit(Long lockLimit) {
        this.lockLimit = lockLimit;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public MerchantBank getMerchantBank() {
        return merchantBank;
    }

    public void setMerchantBank(MerchantBank merchantBank) {
        this.merchantBank = merchantBank;
    }

    public MerchantUser() {
    }

    public MerchantUser(Long id, String merchantName) {
        this.id = id;
        this.merchantName = merchantName;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }
}
