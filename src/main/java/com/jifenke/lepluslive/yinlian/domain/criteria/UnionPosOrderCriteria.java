package com.jifenke.lepluslive.yinlian.domain.criteria;

/**
 * Created by lss on 2017/3/30.
 */
public class UnionPosOrderCriteria {
    private String startDate;

    private String endDate;

    private Integer offset;

    private Integer orderState;//订单状态 0=未支付|1=已支付(未对账)|2=已支付(已对账)|3=未支付(已冲正)|4=未支付(已撤销)

    private String userSid;

    private String merchant;

    private Integer state;//1已支付 0未支付

    private Integer rebateWay;

    public Integer getRebateWay() {
        return rebateWay;
    }

    public void setRebateWay(Integer rebateWay) {
        this.rebateWay = rebateWay;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

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

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public String getUserSid() {
        return userSid;
    }

    public void setUserSid(String userSid) {
        this.userSid = userSid;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

}
