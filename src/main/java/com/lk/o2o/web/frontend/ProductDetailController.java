package com.lk.o2o.web.frontend;

import com.lk.o2o.entity.Product;
import com.lk.o2o.service.ProductService;
import com.lk.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "/frontend")
@Controller
public class ProductDetailController {

    private ProductService productService;

    @Autowired
    public ProductDetailController(ProductService productService){
        this.productService = productService;
    }

    @RequestMapping(value = "/listproductdetailpageinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listProductDetailPageInfo(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //获取商品id
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        if(productId != -1L){
            Product product = productService.queryProductById(productId);
            modelMap.put("success",true);
            modelMap.put("product",product);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","product is empty");
        }
        return modelMap;
    }
}