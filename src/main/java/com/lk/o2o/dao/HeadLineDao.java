package com.lk.o2o.dao;

import com.lk.o2o.entity.HeadLine;

import java.util.List;

public interface HeadLineDao {

    /**
     * 查询头条的相关信息
     * @param headLineCondition 查询条件
     * @return 头条信息
     */
    List<HeadLine> selectHeadLineList(HeadLine headLineCondition);
}
