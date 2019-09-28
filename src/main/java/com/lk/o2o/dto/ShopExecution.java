package com.lk.o2o.dto;

import com.lk.o2o.entity.Shop;
import com.lk.o2o.enums.ShopStateEnum;

import java.util.List;

public class ShopExecution {
    // 状态标识
    private int state;
    //状态描述
    private String stateInfo;
    //店铺数量
    private int count;
    //操作的shop(曾删改的时候用到)
    private Shop shop;
    //shop列表(查询店铺列表的时候用到)
    private List<Shop> shopList;

    public ShopExecution(){

    }

    //店铺操作失败使用的构造器
    public ShopExecution(ShopStateEnum shopStateEnump){
        this.state = shopStateEnump.getStatue();
        this.stateInfo = shopStateEnump.getStateInfo();
    }
    //店铺操作成功使用的构造器
    public ShopExecution(ShopStateEnum shopStateEnump, Shop shop){
        this.state = shopStateEnump.getStatue();
        this.stateInfo = shopStateEnump.getStateInfo();
        this.shop = shop;
    }
    //店铺操作成功使用的构造器
    public ShopExecution(ShopStateEnum shopStateEnump,List<Shop> shopList){
        this.state = shopStateEnump.getStatue();
        this.stateInfo = shopStateEnump.getStateInfo();
        this.shopList = shopList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
