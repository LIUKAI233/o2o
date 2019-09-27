package com.lk.o2o.entity;

import java.util.Date;

public class ShopAuthMap {
    private Long shopAuthId;
    //职称名
    private String title;
    //职称符号，可用于权限控制
    private Integer titleFlag;
    //授权的状态，0表示不可用，1表示可用
    private Integer enableStatus;
    //创建时间
    private Date createTime;
    //最近一次更新时间
    private Date laseEditTime;
    //商店实体类
    private Shop shop;
    //员工实体类
    private PersonInfo employee;

    public Long getShopAuthId() {
        return shopAuthId;
    }

    public void setShopAuthId(Long shopAuthId) {
        this.shopAuthId = shopAuthId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTitleFlag() {
        return titleFlag;
    }

    public void setTitleFlag(Integer titleFlag) {
        this.titleFlag = titleFlag;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLaseEditTime() {
        return laseEditTime;
    }

    public void setLaseEditTime(Date laseEditTime) {
        this.laseEditTime = laseEditTime;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public PersonInfo getEmployee() {
        return employee;
    }

    public void setEmployee(PersonInfo employee) {
        this.employee = employee;
    }
}
