package com.lk.o2o.service;

import com.lk.o2o.entity.Area;

import java.util.List;

public interface AreaService {

    public static final String AREALISTKEY = "areaList";

    List<Area> getAreaList();
}
