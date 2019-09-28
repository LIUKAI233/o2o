package com.lk.o2o.service.impl;

import com.lk.o2o.dao.ProductDao;
import com.lk.o2o.dao.ProductImgDao;
import com.lk.o2o.dto.ImageHolder;
import com.lk.o2o.dto.ProductExecution;
import com.lk.o2o.entity.Product;
import com.lk.o2o.entity.ProductImg;
import com.lk.o2o.enums.ProductStateEnum;
import com.lk.o2o.exceptions.ProductOperationException;
import com.lk.o2o.service.ProductService;
import com.lk.o2o.util.FileUtil;
import com.lk.o2o.util.ImageUtil;
import com.lk.o2o.util.pageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    /*添加商品并处理图片*/
    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder image, List<ImageHolder> imageList) {
        //1.判断空值
        if(product != null && product.getShop() != null && product.getShop().getShopId() != null){
            //2.给商品附上默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            //商品默认上架状态
            product.setEnableStatus(1);
            //若商品缩略图不为空则添加
            if(image != null && image.getImage() != null && image.getImageName() != null){
                addProductImage(product,image);
            }
            //若商品详情图不为空则添加
            if(imageList != null && imageList.size() > 0){
                addProductImageList(product,imageList);
            }
            try {
                int effectNum = productDao.insertProduct(product);
                if(effectNum <= 0) {
                    throw new ProductOperationException("创建商品失败");
                }
            }catch (Exception e){
                throw new ProductOperationException("addProduct error"+e.getMessage());
            }
            return new ProductExecution(ProductStateEnum.SUCCESS);
        }else{
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    /*根据传入的商品信息，修改商品*/
    public ProductExecution modifyProduct(Product product, ImageHolder image, List<ImageHolder> imageList) {
        //判断空值
        if(product != null && product.getShop().getShopId() != null && product.getProductId() != null){
            product.setLastEditTime(new Date());
            //若商品缩略图不为空，则删除原来的再添加
            try {
                if(image != null && image.getImage() != null && image.getImageName() != null){
                    //先获取原先缩列图的路径
                    Product productById = productDao.queryProductById(product.getProductId());
                    //删除图片
                    FileUtil.deleteFile(productById.getImgAddr());
                    //然后更新缩略图信息
                    addProductImage(product,image);
                }
                if (imageList != null && imageList.size() > 0){
                     //查询原先的详情图集合
                    List<ProductImg> productImgs = productImgDao.selectProductImgListByProductId(product.getProductId());
                    //删除详情图信息
                    productImgDao.deleteProductImagByProductId(product.getProductId());
                    for (ProductImg pi: productImgs) {
                        //循环删除详情图片
                        FileUtil.deleteFile(pi.getImgAddr());
                    }
                    //添加详情图
                    addProductImageList(product,imageList);
                }
                try {
                    int effectNum = productDao.updataProduct(product);
                    if(effectNum <= 0){
                        return new ProductExecution(ProductStateEnum.INNER_ERROR);
                    }
                    return new ProductExecution(ProductStateEnum.SUCCESS);
                }catch (Exception e){
                    throw new ProductOperationException("更新店铺失败"+e.getMessage());
                }
            } catch (Exception e) {
                throw new ProductOperationException("图片处理失败"+e.getMessage());
            }
        }else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    /*通过传入的商品id，查询商品信息*/
    public Product queryProductById(Long productId) {
        return productDao.queryProductById(productId);
    }

    /*根据输入条件，分页返回数据*/
    @Override
    public ProductExecution queryProductList(Product product, Integer pageIndex, Integer pageSize) {
        int rowIndex = pageCalculator.calculator(pageIndex, pageSize);
        try {
            ProductExecution pe = new ProductExecution();
            List<Product> products = productDao.selectProduct(product, rowIndex, pageSize);
            int count = productDao.selectCount(product);
            pe.setProductList(products);
            pe.setCount(count);
            return pe;
        }catch (Exception e){
            throw new ProductOperationException("queryProductList error"+e.getMessage());
        }
    }

    /*批量添加处理图片*/
    private void addProductImageList(Product product, List<ImageHolder> imageList){
        //传入店铺ID,获得图片的相对路径
        String dest = FileUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgs = new ArrayList<>();
        //遍历数组，并添加相关信息
        for(ImageHolder imageHolder:imageList) {
            ProductImg productImg = new ProductImg();
            //传入图片信息和相对路径，使用工具类保存图片，并返回图片的相对路径
            String shopImgAddr = ImageUtil.generateNormalImg(imageHolder, dest);
            productImg.setCreateTime(new Date());
            productImg.setProductId(product.getProductId());
            productImg.setImgAddr(shopImgAddr);
            productImgs.add(productImg);
        }
        if(productImgs.size() > 0){
            try{
                int effectNum = productImgDao.insertProductImgs(productImgs);
                if(effectNum <= 0){
                    throw new ProductOperationException("创建商品详情图失败");
                }
            }catch (Exception e){
                throw new ProductOperationException("addProductImageList error"+e.getMessage());
            }
        }
    }

    /*添加处理图片*/
    private void addProductImage(Product product , ImageHolder image) {
        //传入店铺ID,获得图片的相对路径
        String dest = FileUtil.getShopImagePath(product.getShop().getShopId());
        //传入图片信息和相对路径，使用工具类保存图片，并返回图片的相对路径
        String shopImgAddr = ImageUtil.generateThumbnail(image, dest);
        product.setImgAddr(shopImgAddr);
    }

}
