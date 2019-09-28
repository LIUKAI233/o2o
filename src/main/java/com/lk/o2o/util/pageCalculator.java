package com.lk.o2o.util;

public class pageCalculator {
    public static int calculator(int pageIndex , int pageSize){
        return (pageIndex > 0)?(pageIndex-1)*pageSize:0;
    }
}
