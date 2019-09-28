package com.lk.o2o.enums;

public enum ProductStateEnum {
    SUCCESS(1, "创建成功"), INNER_ERROR(-1001, "操作失败"), EMPTY(-1002, "请输入完整商品信息");

    private int statue;
    private String stateInfo;

    private ProductStateEnum(int statue, String stateInfo) {
        this.statue = statue;
        this.stateInfo = stateInfo;
    }

    /**
     * 根据传入的state的值，返回相应的stateInfo
     */
    public static ProductStateEnum stateOf(int state) {
        for (ProductStateEnum stateEnum : values()){
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
