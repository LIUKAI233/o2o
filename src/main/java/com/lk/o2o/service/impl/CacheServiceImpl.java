package com.lk.o2o.service.impl;

import com.lk.o2o.cache.JedisUtil;
import com.lk.o2o.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Override
    public void removeKey(String keyPrefix) {
        //匹配所有以keyPrefis开头的key
        Set<String> keys = jedisKeys.keys(keyPrefix + '*');
        //遍历删除对应的key—value
        for (String key: keys) {
            jedisKeys.del(key);
        }
    }
}
