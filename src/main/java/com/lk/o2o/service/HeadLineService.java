package com.lk.o2o.service;

import com.lk.o2o.entity.HeadLine;

import java.util.List;

public interface HeadLineService {

    public static final String HeadLineListKEY = "headLineList";

    /**
     * 查询符合条件的头条集合
     * @param headLineCondition 查询条件
     * @return 头条集合
     */
    List<HeadLine> queryHeadLineList(HeadLine headLineCondition);
}
