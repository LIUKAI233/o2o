package com.lk.o2o.web.local;

import com.lk.o2o.dto.LocalAuthExecution;
import com.lk.o2o.entity.LocalAuth;
import com.lk.o2o.entity.PersonInfo;
import com.lk.o2o.enums.LocalAuthStateEnum;
import com.lk.o2o.service.LocalAuthService;
import com.lk.o2o.util.CodeUtil;
import com.lk.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/local",method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {

    private LocalAuthService localAuthService;

    @Autowired
    private LocalAuthController(LocalAuthService localAuthService){
        this.localAuthService = localAuthService;
    }

    /**
     * 将用户信息和平台账号绑定
     * @param request 请求
     * @return 处理结果
     */
    @RequestMapping(value = "/bindlocalauth",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> bindLocalAuth(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //校验验证码
        if(CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入正确的验证码");
            return modelMap;
        }
        //从前端获取用户名
        String username = HttpServletRequestUtil.getString(request, "username");
        //获取密码
        String password = HttpServletRequestUtil.getString(request, "password");
        //从session中获取用户登录信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //空值判断
        if(username != null && password != null && user != null && user.getUserId() != null){
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUsername(username);
            localAuth.setPassword(password);
            localAuth.setPersonInfo(user);
            LocalAuthExecution localAuthExecution = localAuthService.addLocalAuth(localAuth);
            if(localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()){
                modelMap.put("success",true);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg",localAuthExecution.getStateInfo());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","用户名和密码均不能为空");
        }
        return modelMap;
    }

    /**
     * 更改账号密码
     * @param request 请求信息
     * @return 处理结果
     */
    @RequestMapping(value = "changelocalpwd",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> changeLocalpwd(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //校验验证码
        if(CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入正确的验证码");
            return modelMap;
        }
        //从前端获取用户名
        String username = HttpServletRequestUtil.getString(request, "username");
        //获取密码
        String password = HttpServletRequestUtil.getString(request, "password");
        //获取新密码
        String newpassword = HttpServletRequestUtil.getString(request, "newpassword");
        //从session中获取用户登录信息(用户使用微信登录，就可以获取到用户信息)
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if(user == null || user.getUserId() == null){
            modelMap.put("success",false);
            modelMap.put("errMsg","登录超时，请重新登录");
            return modelMap;
        }
        //判空，session不为空，新旧密码不一样
        if(username != null && password != null && newpassword != null  && !password.equals(newpassword)){
            //先取出该登录信息绑定的账号，进行对比
            LocalAuth localAuthByUserId = localAuthService.getLocalAuthByUserId(user.getUserId());
            if (localAuthByUserId == null || !localAuthByUserId.getUsername().equals(username)){
                modelMap.put("success",false);
                modelMap.put("errMsg","请输入正确的账号信息");
                return modelMap;
            }
            //更改密码
            LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(user.getUserId(), username, password, newpassword);
            if(localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()){
                modelMap.put("success",true);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg",localAuthExecution.getStateInfo());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入正确的信息");
        }
        return modelMap;
    }

    /**
     * 登录验证
     * @param request 请求信息
     * @return 处理结果
     */
    @RequestMapping(value = "/logincheck",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> loginCheck(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //判断是否需要输入验证码 若为true，则需要验证码，为false，则不需要
        Boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
        //校验验证码
        if(needVerify && CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入正确的验证码");
            return modelMap;
        }
        //从前端获取用户名
        String username = HttpServletRequestUtil.getString(request, "username");
        //获取密码
        String password = HttpServletRequestUtil.getString(request, "password");
        //非空判断
        if(username != null && password != null){
            //传入用户名和密码进行查找
            LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPassword(username, password);
            if(localAuth != null){
                //若能取到账号信息，说明登录成功
                modelMap.put("success",true);
                //把用户信息设置到session中
                request.getSession().setAttribute("user",localAuth.getPersonInfo());
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","用户名或密码错误");
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入用户名和密码");
            return modelMap;
        }
        return modelMap;
    }

    /**
     * 当用户点击登出按钮，就注销user这个session
     * @param request 请求
     * @return 处理结果
     */
    @RequestMapping(value = "loginout",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> loginOut(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //将用户的session置为空
        request.getSession().setAttribute("user",null);
        modelMap.put("success",true);
        return modelMap;
    }
}
