package com.lk.o2o.service.impl;

import com.lk.o2o.dao.LocalAuthDao;
import com.lk.o2o.dto.LocalAuthExecution;
import com.lk.o2o.entity.LocalAuth;
import com.lk.o2o.enums.LocalAuthStateEnum;
import com.lk.o2o.exceptions.LocalAuthOperationException;
import com.lk.o2o.service.LocalAuthService;
import com.lk.o2o.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    private LocalAuthDao localAuthDao;

    @Autowired
    private void setLocalAuthDao(LocalAuthDao localAuthDao){
        this.localAuthDao = localAuthDao;
    }

    //通过用户名和密码，获取用户信息
    public LocalAuth getLocalAuthByUsernameAndPassword(String username, String password) {
        return localAuthDao.queryLocalByUsernameAndPassword(username, MD5.getMd5(password));
    }

    //通过用户id，获取用户信息
    public LocalAuth getLocalAuthByUserId(Long userId) {
       return localAuthDao.queryLocalByUserId(userId);
    }

    //注册用户
    @Transactional
    public LocalAuthExecution addLocalAuth(LocalAuth localAuth) {
        //空值判断
        if(localAuth == null || localAuth.getUsername() == null || localAuth.getPassword() == null ||
        localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null){
            return new LocalAuthExecution(LocalAuthStateEnum.INFO_EMPTY);
        }
        //查询此用户是否绑定过平台账号
        LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
        if(tempAuth != null){
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE);
        }
        try {
            //若没绑定过平台账号，附上初始值
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            //加密密码
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            int effectNum = localAuthDao.insertLocalAuth(localAuth);
            if (effectNum <= 0) {
                throw new LocalAuthOperationException("平台账号绑定失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }
        }catch (Exception e){
            throw new LocalAuthOperationException("insertLocalAuth error"+e.getMessage());
        }
    }

    //修改用户登录密码
    @Transactional
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newpassword) {
        if(userId != -1L && username != null && password != null && newpassword != null){
            try {
                int effectNum = localAuthDao.updataLocalAuth(userId, username, MD5.getMd5(password), MD5.getMd5(newpassword), new Date());
                if (effectNum > 0){
                    return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
                }else{
                    throw new LocalAuthOperationException("更改密码失败");
                }
            }catch (Exception e){
                throw new LocalAuthOperationException("updataLocalAuth error"+e.getMessage());
            }
        }else{
            return new LocalAuthExecution(LocalAuthStateEnum.INFO_EMPTY);
        }
    }
}
