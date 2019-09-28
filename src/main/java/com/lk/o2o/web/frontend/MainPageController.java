package com.lk.o2o.web.frontend;

import com.lk.o2o.entity.HeadLine;
import com.lk.o2o.entity.ShopCategory;
import com.lk.o2o.service.HeadLineService;
import com.lk.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*关于首页的相关操作*/
@Controller
@RequestMapping("/frontend")
public class MainPageController {
    private final ShopCategoryService shopCategoryService;
    private final HeadLineService headLineService;

    @Autowired
    private MainPageController(ShopCategoryService shopCategoryService, HeadLineService headLineService) {
        this.shopCategoryService = shopCategoryService;
        this.headLineService = headLineService;
    }

    @RequestMapping(value = "/listmainpageinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listMainPageInfo(){
        Map<String, Object> modelMap = new HashMap<>();
        List<HeadLine> headLineList;
        List<ShopCategory> shopCategoryList;
        try {
            //查询一级店铺分类
            shopCategoryList = shopCategoryService.getShopCategory(null);
            modelMap.put("shopCategoryList",shopCategoryList);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        try {
            //查询头条列表
            HeadLine headLineCondition = new HeadLine();
            headLineCondition.setEnableStatus(1);
            headLineList = headLineService.queryHeadLineList(headLineCondition);
            modelMap.put("headLineList",headLineList);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        modelMap.put("success",true);
        return modelMap;
    }
}
