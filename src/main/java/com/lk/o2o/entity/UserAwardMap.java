package com.lk.o2o.entity;

import java.util.Date;

/**
 * 顾客以领取的奖品的映射
 */
public class UserAwardMap {
    //主键id
    private Long userAwardId;
    //创建时间，即使用时间
    private Date createTime;
    //奖品状态 0表示未使用，1表示以使用
    private Integer usedStatus;
    //领取消耗的积分
    private Integer point;
    //顾客信息实体类
    private PersonInfo usew;
    //奖品信息实体类
    private Award award;
    //店铺信息实体类
    private Shop shop;
    //操作员信息实体类
    private PersonInfo operator;

    public Long getUserAwardId() {
        return userAwardId;
    }

    public void setUserAwardMapId(Long userAwardId) {
        this.userAwardId = userAwardId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUsedStatus() {
        return usedStatus;
    }

    public void setUsedStatus(Integer usedStatus) {
        this.usedStatus = usedStatus;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public PersonInfo getUsew() {
        return usew;
    }

    public void setUsew(PersonInfo usew) {
        this.usew = usew;
    }

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public PersonInfo getOperator() {
        return operator;
    }

    public void setOperator(PersonInfo operator) {
        this.operator = operator;
    }
}
