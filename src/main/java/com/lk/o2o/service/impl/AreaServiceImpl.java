package com.lk.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lk.o2o.cache.JedisUtil;
import com.lk.o2o.dao.AreaDao;
import com.lk.o2o.entity.Area;
import com.lk.o2o.exceptions.AreaOperationException;
import com.lk.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;


    @Override
    @Transactional
    public List<Area> getAreaList() {
        String key = AREALISTKEY;
        List<Area> areaList = null;
        //创建JSON工具对象
        ObjectMapper mapper = new ObjectMapper();
        //判断redis中是否有这个key
        if(!jedisKeys.exists(key)){
            //不存在
            areaList = areaDao.queryArea();
            try {
                //把返回的集合转换成JSON字符串
                String jsonString = mapper.writeValueAsString(areaList);
                //存到redis中
                jedisStrings.set(key,jsonString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new AreaOperationException(e.getMessage());
            }
        }else {
            //存在
            //从redis中取key对应的字符串
            String jsonString = jedisStrings.get(key);
            //jsonString对应的java类型
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
            try {
                areaList = mapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                throw new AreaOperationException(e.getMessage());
            }
        }
        return areaList;
    }
}
