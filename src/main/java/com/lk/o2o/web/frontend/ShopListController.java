package com.lk.o2o.web.frontend;

import com.lk.o2o.dto.ShopExecution;
import com.lk.o2o.entity.Area;
import com.lk.o2o.entity.Shop;
import com.lk.o2o.entity.ShopCategory;
import com.lk.o2o.service.AreaService;
import com.lk.o2o.service.ShopCategoryService;
import com.lk.o2o.service.ShopService;
import com.lk.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*关于店铺的相关查询操作*/
@RequestMapping(value = "/frontend",method = RequestMethod.GET)
@Controller
public class ShopListController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    /*返回商品列表页里的shopCategory列表(二级或者一级),以及区域信息列表*/
    @RequestMapping(value = "listshopspageinfo")
    @ResponseBody
    private Map<String,Object> listShopsPageInfo(HttpServletRequest request){
        HashMap<String, Object> modelMap = new HashMap<>();
        //先声明返回的集合
        List<ShopCategory> shopCategoryList;
        List<Area> areaList;
        //从前端获取parentId
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        //返回shopCategory列表
        try {
            if (parentId != -1){
                //表示选择了一级列表，查看该类别的所有店铺信息
                ShopCategory shopCategory = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategory.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategory(shopCategory);
            }else{
                //表示选择全部商品
                shopCategoryList = shopCategoryService.getShopCategory(null);
            }
            modelMap.put("shopCategoryList",shopCategoryList);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        //返回区域信息列表
        try {
            areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        modelMap.put("success",true);
        return modelMap;
    }

    /*返回符合查询条件的店铺列表*/
    @RequestMapping(value = "/listshops",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShops(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //从前端获取页数
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //从前端获取一页的容量
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //非空判断
        if(pageIndex != -1 && pageSize != -1){
            //从前端获取一级类别id
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            //从前端获取二级类别id
            long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            //从前端获取区域id
            long areaId = HttpServletRequestUtil.getLong(request, "areaId");
            //从前端获取模糊查询的店铺名
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            Shop shopCondition = comportShopCondition4Search(parentId,shopCategoryId,areaId,shopName);
            ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
            modelMap.put("shopList",se.getShopList());
            modelMap.put("count",se.getCount());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","pageIndex or pageSize is empty");
        }
        return modelMap;
    }

    /*整合前端传过来的查询信息*/
    private Shop comportShopCondition4Search(long parentId, long shopCategoryId, long areaId, String shopName) {
        Shop shopCondition = new Shop();
        if(parentId != -1L){
            //查询一级列表下的所有店铺信息
            ShopCategory shopCategory = new ShopCategory();
            ShopCategory parent = new ShopCategory();
            parent.setShopCategoryId(parentId);
            shopCategory.setParent(parent);
            shopCondition.setShopCategory(shopCategory);
        }
        if(shopCategoryId != -1L){
            //查询二级列表下的所有店铺信息
            ShopCategory category = new ShopCategory();
            category.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(category);
        }
        if(areaId != -1L){
            //查询该区域列表下的店铺信息
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if(shopName != null && !shopName.equals("")){
            //查询名字包含shopName的店铺信息
            shopCondition.setShopName(shopName);
        }
        //返回前端展示的店铺都是审核成功的店铺
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
