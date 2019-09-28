package com.lk.o2o.web.frontend;

import com.lk.o2o.dto.ProductExecution;
import com.lk.o2o.entity.Product;
import com.lk.o2o.entity.ProductCategory;
import com.lk.o2o.entity.Shop;
import com.lk.o2o.service.ProductCategoryService;
import com.lk.o2o.service.ProductService;
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

@RequestMapping(value = "/frontend",method = RequestMethod.GET)
@Controller
public class ShopDetailController {
    private final ShopService shopService;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    @Autowired
    public ShopDetailController(ShopService shopService, ProductService productService, ProductCategoryService productCategoryService) {
        this.shopService = shopService;
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    @RequestMapping(value = "/listshopdetailpageinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShopDetailPageInfo(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //从前端获取shopId
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if(shopId == -1L){
            modelMap.put("success",false);
            modelMap.put("errMsg","shopId is empty");
            return modelMap;
        }
        try {
            //获取店铺信息
            Shop shop = shopService.getShopById(shopId);
            //把商铺信息返回给前端
            modelMap.put("shop",shop);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        try {
            //查询该店铺下的所有商品类别
            List<ProductCategory> productCategoryList = productCategoryService.queryProductCategory(shopId);
            //把该商铺下的所有商品类别集合，返回给前端
            modelMap.put("productCategoryList",productCategoryList);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        modelMap.put("success",true);
        return modelMap;
    }

    /*依据查询条件，分页列出该店铺下的所有商品*/
    @RequestMapping(value = "productlist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> productList(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //从前端获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //从前端获取一页需要显示的条数
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从前端获取shopId
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        //判空
        if(pageIndex > -1 && pageSize > -1 && shopId > -1){
            //从前端获取商品类别id
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            //从前端获取模糊查找的商品名
            String productName = HttpServletRequestUtil.getString(request, "productName");
            //组合查询条件
            Product productCondition = comportProductCondition4Search(shopId,productCategoryId,productName);
            try {
                ProductExecution pe = productService.queryProductList(productCondition, pageIndex, pageSize);
                modelMap.put("productList",pe.getProductList());
                modelMap.put("count",pe.getCount());
                modelMap.put("success",true);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","pageIndex or pageSize or shopId is empty");
            return modelMap;
        }
        return modelMap;
    }

    //组合查询条件，将条件封装到productCondition中返回
    private Product comportProductCondition4Search(long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if(productCategoryId != -1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if(productName != null && !productName.equals("")){
            productCondition.setProductName(productName);
        }
        //返回前端展示的商品都是审核成功的商品
        productCondition.setEnableStatus(1);
        return productCondition;
    }
}

