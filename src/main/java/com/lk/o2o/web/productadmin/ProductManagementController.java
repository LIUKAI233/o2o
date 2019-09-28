package com.lk.o2o.web.productadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lk.o2o.dto.ImageHolder;
import com.lk.o2o.dto.ProductCategoryExecution;
import com.lk.o2o.dto.ProductExecution;
import com.lk.o2o.entity.Product;
import com.lk.o2o.entity.ProductCategory;
import com.lk.o2o.entity.Shop;
import com.lk.o2o.enums.ProductCategoryStateEnum;
import com.lk.o2o.service.ProductCategoryService;
import com.lk.o2o.service.ProductService;
import com.lk.o2o.util.CodeUtil;
import com.lk.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/productadmin")
public class ProductManagementController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;

    private final int IMAGEMAXCOUNT = 6;

    /*根据自定义信息，获取商品信息*/
    @RequestMapping(value = "getproductlist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getProductList(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //获取前台传过来的页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //获取前台要求一页的商品数量
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从session获取currentShop，主要为了获取shopId
        Shop currentShop =(Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)){
            //获取需要检索的条件，包括是否需要以类别，或者名字模糊查询
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product product = compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
            ProductExecution pe = productService.queryProductList(product, pageIndex, pageSize);
            modelMap.put("success", true);
            modelMap.put("productList", pe.getProductList());
            modelMap.put("count", pe.getCount());
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageIndex or pageSize or shopId");
        }
        return modelMap;
    }

    private Product compactProductCondition(Long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        //如果指定商品类别
        if(productCategoryId != -1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        //如果按照名字模糊查询
        if(productName != null && productName.equals("")){
            productCondition.setProductName(productName);
        }
        return productCondition;
    }

    /*根据商品ID，获取商品信息*/
    @RequestMapping(value = "getproductById",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getProductById(@RequestParam Long productId){
        Map<String, Object> modelMap = new HashMap<>();
        if(productId < 0){
            modelMap.put("success",false);
            modelMap.put("errMsg","productId empty");
        }
        try {
            Product product = productService.queryProductById(productId);
            List<ProductCategory> productCategoryList = productCategoryService.queryProductCategory(product.getShop().getShopId());
            modelMap.put("success",true);
            modelMap.put("product",product);
            modelMap.put("productCategoryList",productCategoryList);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    /*修改商品信息*/
    @RequestMapping(value = "modifyproduct",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> modifyproduct(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //验证码校验
        //判读是商品添加，还是上下架操作，若是后者，则不需要校验验证码
        boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
        if(!statusChange){
            if(CodeUtil.checkVerifyCode(request)){
                modelMap.put("success",false);
                modelMap.put("errMsg","请输入正确的验证码");
                return modelMap;
            }
        }
        //商品缩略图
        ImageHolder thumbnailHolder = null;
        //商品详情图集合
        List<ImageHolder> imageHolderList = new ArrayList<>();
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            //若存在中存在文件流，取出其中的文件，包括缩略图和详情图
            if (commonsMultipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                //获得缩略图信息
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
                if(thumbnailFile != null) {
                    thumbnailHolder = new ImageHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());
                }
                //取出详情图列表，并构建List<ImageHolder>,最大支持6张图片上传
                for (int i = 0; i < IMAGEMAXCOUNT; i++) {
                    CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
                    //如果取出的文件流不为空，就添加进List<ImageHolder>
                    if (productImgFile != null) {
                        ImageHolder productImg = new ImageHolder(productImgFile.getInputStream(), productImgFile.getOriginalFilename());
                        imageHolderList.add(productImg);
                    } else {
                        //为空就跳出循环
                        break;
                    }
                }
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            // 使用jackson,把前端传过来的json数据封装到pojo中
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(productStr, Product.class);
            //判读从前端获取的信息是否为空
            if(product != null) {
                try{
                    //从session获得店铺信息
                    Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
                    if(currentShop == null && currentShop.getShopId() == null){
                        modelMap.put("success",false);
                        modelMap.put("errMsg","登录超时，请重新登录");
                        return modelMap;
                    }
                    product.setShop(currentShop);
                    ProductExecution productExecution = productService.modifyProduct(product, thumbnailHolder, imageHolderList);
                    if(productExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
                        modelMap.put("success",true);
                    }else{
                        modelMap.put("success",false);
                        modelMap.put("errMsg",productExecution.getStateInfo());
                    }
                }catch(Exception e){
                    modelMap.put("success",false);
                    modelMap.put("errMsg",e.getMessage());
                }
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    /*添加商品*/
    @RequestMapping(value = "addproduct",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addProduct(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //验证码校验
        if(CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入正确的验证码");
            return modelMap;
        }
        //商品缩略图
        ImageHolder thumbnailHolder = null;
        //商品详情图集合
        List<ImageHolder> imageHolderList = new ArrayList<>();
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try{
            //若存在中存在文件流，取出其中的文件，包括缩略图和详情图
            if(commonsMultipartResolver.isMultipart(request)){
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
                //获得缩略图信息
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
                if(thumbnailFile != null) {
                    thumbnailHolder = new ImageHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());
                }
                //取出详情图列表，并构建List<ImageHolder>,最大支持6张图片上传
                for(int i=0 ; i < IMAGEMAXCOUNT ; i++){
                    CommonsMultipartFile productImgFile =  (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg"+i);
                    //如果取出的文件流不为空，就添加进List<ImageHolder>
                    if(productImgFile != null){
                        ImageHolder productImg = new ImageHolder(productImgFile.getInputStream(), productImgFile.getOriginalFilename());
                        imageHolderList.add(productImg);
                    }else{
                        //为空就跳出循环
                        break;
                    }
                }
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","上传图片为空");
                return modelMap;
            }
        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            // 使用jackson,把前端传过来的json数据封装到pojo中
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(productStr, Product.class);
            //判读从前端获取的信息是否为空
            if(product != null && thumbnailHolder != null && imageHolderList.size() > 0) {
                try{
                    //从session获得店铺信息
                    Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
                    if(currentShop == null && currentShop.getShopId() == null){
                        modelMap.put("success",false);
                        modelMap.put("errMsg","登录超时，请重新登录");
                        return modelMap;
                    }
                    product.setShop(currentShop);
                    ProductExecution productExecution = productService.addProduct(product, thumbnailHolder, imageHolderList);
                    if(productExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
                        modelMap.put("success",true);
                    }else{
                        modelMap.put("success",false);
                        modelMap.put("errMsg",productExecution.getStateInfo());
                    }
                }catch(Exception e){
                    modelMap.put("success",false);
                    modelMap.put("errMsg",e.getMessage());
                }
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg","请输入商品信息");
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    /*查询店铺商品类别*/
    @RequestMapping(value = "getproductcategorylist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getProductCategoryList(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        //判断店铺信息是否存在
        if (currentShop == null && currentShop.getShopId() <= 0){
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            modelMap.put("success",false);
            modelMap.put("errMsg",ps.getStateInfo());
        }else {
            List<ProductCategory> productCategoryList = productCategoryService.queryProductCategory(currentShop.getShopId());
            modelMap.put("success",true);
            modelMap.put("productCategoryList",productCategoryList);
        }
        return modelMap;
    }

    /*批量添加店铺类别*/
    @RequestMapping(value = "addproductcategories",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addProductCategories(@RequestBody List<ProductCategory> productCategories, HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        /*从session获取shopId，并添加进分类里面*/
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if(currentShop == null && currentShop.getShopId() == null){
            modelMap.put("success",false);
            modelMap.put("errMsg","登录超时，请重新登录");
        }
         for(ProductCategory pc: productCategories) {
            pc.setShopId(currentShop.getShopId());
        }
        try {
            if(productCategories == null && productCategories.size() <= 0){
                modelMap.put("success",false);
                modelMap.put("errMsg","请至少输入一个类别");
            }else {
                ProductCategoryExecution pe = productCategoryService.addProductCategorys(productCategories);
                if (ProductCategoryStateEnum.SUCCESS.getState() == pe.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", ProductCategoryStateEnum.INNER_ERROR.getStateInfo());
                }
            }
        } catch (Exception e) {
           modelMap.put("success",false);
           modelMap.put("errMsg",e.getMessage());
           return modelMap;
        }
        return modelMap;
    }


    /*删除店铺类别*/
    @RequestMapping(value = "removeproductcategory",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> removeProductCategory(Long productCategoryId , HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        /*从session获取shopId，并添加进分类里面*/
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if(currentShop == null && currentShop.getShopId() == null){
            modelMap.put("success",false);
            modelMap.put("errMsg","登录超时，请重新登录");
        }
        if(productCategoryId != null && productCategoryId > 0) {
            try {
                ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
                if (pe.getState() != ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg",pe.getStateInfo());
                } else {
                    modelMap.put("success", true);
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg",e.getMessage());
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请至少选择一个类别");
        }
        return modelMap;
    }
}
