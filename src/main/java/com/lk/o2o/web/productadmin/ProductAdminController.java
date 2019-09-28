package com.lk.o2o.web.productadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "productadmin",method = RequestMethod.GET)
public class ProductAdminController {

    @RequestMapping("productcategorylist")
    public String productCategoryList(){
        return "product/productcategorylist";
    }

    @RequestMapping("productmanage")
    public String productManage(){
        return "product/productmanage";
    }

    @RequestMapping("productoperation")
    public String productOperation(){
        return "product/productoperation";
    }
}
