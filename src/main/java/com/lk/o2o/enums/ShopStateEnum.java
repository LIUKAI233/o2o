package com.lk.o2o.enums;

public enum ShopStateEnum {
    CHECK(0, "审核中"), OFFLINE(-1, "非法店铺"), SUCCESS(1, "操作成功"), PASS(2,"通过认证"),
    INNER_ERROR(-1001,"内部系统错误"),NULL_SHOPID(-1002,"shopId为空"),NULL_SHOP(-1003,"shop为空");
    private int statue;
    private String stateInfo;

    private ShopStateEnum(int statue, String stateInfo) {
        this.statue = statue;
        this.stateInfo = stateInfo;
    }

    /**
     * 根据传入的state的值，返回相应的stateInfo
     */
    public static ShopStateEnum stateOf(int state) {
        for (ShopStateEnum stateEnum : values()){
            if (stateEnum.getStatue() == state){
                return stateEnum;
            }
        }
        return null;
    }

    public int getStatue() {
        return statue;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
