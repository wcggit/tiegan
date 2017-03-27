package com.jifenke.lepluslive.weixin.domain.entities;


import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wcg on 16/3/18.
 */
@Entity
@Table(name = "WEI_XIN_USER")
@Cacheable
public class WeiXinUser {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String openId;

  private String appOpenId;  //app登录的openId

  private String unionId;

  private String nickname;
  private Long sex;
  private String language;
  private String city;
  private String province;
  private String country;


  private String headImageUrl;

  private String accessToken;

  private String refreshToken;

  private Integer hongBaoState = 0; //红包状态 0 未开红包, 1 已开红包;

  @OneToOne(fetch = FetchType.LAZY)
  private LeJiaUser leJiaUser; //条形码

  Date dateCreated;
  Date lastUpdated;
  Date lastUserInfoDate;   //上次从微信服务器抓取用户信息的时间

  private Integer state = 0;   //是否是会员 0=不是   1=是

  private Integer subState = 0;   //关注状态 0=从未关注过   1=关注   2=曾经关注现取消关注

  private Date subDate;       //关注时间

  //  类型_活动ID_来源ID
  // 1_2_0表示送红包活动,活动id为2,0无意义
  // 2_3_456表示裂变活动,活动id为3,邀请人weiXinUser的id为456
  // 0_0_0表示表示默认关注
  // 3_4_1表示其他活动,活动id为4,1无意义
  // 4_0_123表示商家邀请码，商家Id为123
  // 5_0_0表示充值送话费来源
  private String subSource;   //关注来源

  private Integer massRemain = 4;  //本月群发余数

  private Date sendMassDate;   //最近一次发送时间

  public Integer getHongBaoState() {
    return hongBaoState;
  }

  public void setHongBaoState(Integer hongBaoState) {
    this.hongBaoState = hongBaoState;
  }

  public LeJiaUser getLeJiaUser() {
    return leJiaUser;
  }

  public void setLeJiaUser(LeJiaUser leJiaUser) {
    this.leJiaUser = leJiaUser;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNickname() {
    return nickname;
  }

  public Date getSubDate() {
    return subDate;
  }

  public void setSubDate(Date subDate) {
    this.subDate = subDate;
  }

  public String getSubSource() {
    return subSource;
  }

  public void setSubSource(String subSource) {
    this.subSource = subSource;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public Long getSex() {
    return sex;
  }

  public void setSex(Long sex) {
    this.sex = sex;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getHeadImageUrl() {
    return headImageUrl;
  }

  public void setHeadImageUrl(String headImageUrl) {
    this.headImageUrl = headImageUrl;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public Date getLastUserInfoDate() {
    return lastUserInfoDate;
  }

  public void setLastUserInfoDate(Date lastUserInfoDate) {
    this.lastUserInfoDate = lastUserInfoDate;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getAppOpenId() {
    return appOpenId;
  }

  public void setAppOpenId(String appOpenId) {
    this.appOpenId = appOpenId;
  }

  public String getUnionId() {
    return unionId;
  }

  public void setUnionId(String unionId) {
    this.unionId = unionId;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getMassRemain() {
    return massRemain;
  }

  public void setMassRemain(Integer massRemain) {
    this.massRemain = massRemain;
  }

  public Date getSendMassDate() {
    return sendMassDate;
  }

  public Integer getSubState() {
    return subState;
  }

  public void setSubState(Integer subState) {
    this.subState = subState;
  }

  public void setSendMassDate(Date sendMassDate) {
    this.sendMassDate = sendMassDate;
  }
}
