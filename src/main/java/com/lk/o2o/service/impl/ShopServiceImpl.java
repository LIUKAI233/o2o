package com.lk.o2o.service.impl;

import com.lk.o2o.dao.ShopDao;
import com.lk.o2o.dto.ImageHolder;
import com.lk.o2o.dto.ShopExecution;
import com.lk.o2o.entity.Shop;
import com.lk.o2o.enums.ShopStateEnum;
import com.lk.o2o.exceptions.ShopOperationException;
import com.lk.o2o.service.ShopService;
import com.lk.o2o.util.FileUtil;
import com.lk.o2o.util.ImageUtil;
import com.lk.o2o.util.pageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
@Transactional  //开启事务
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;

    /**
     * 获取符合条件的店铺列表
     * @param shopCondition 查询条件
     * @param pageIndex 查询页数
     * @param pageSize 一页多少条数据
     * @return 处理结果
     */
    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = pageCalculator.calculator(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int shopCount = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if(shopList != null){
            se.setShopList(shopList);
            se.setCount(shopCount);
        }else{
            se.setState(ShopStateEnum.INNER_ERROR.getStatue());
        }
        return se;
    }

    /**
     * 根据id获取店铺信息
     * @param shopId 店铺ID
     * @return 店铺信息
     */
    @Override
    public Shop getShopById(Long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    /**
     * 更新店铺
     * @param shop 店铺信息
     * @param thunbnail 图片相关信息
     * @return 处理结果
     */
    @Override
    public ShopExecution updataShop(Shop shop, ImageHolder thunbnail) {
        InputStream shopImgInputStream = null;
        String fileName = null;
        if(thunbnail != null) {
            shopImgInputStream = thunbnail.getImage();
            fileName = thunbnail.getImageName();
        }
        if(shop == null || shop.getShopId() == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            if(shopImgInputStream != null && fileName != null && !"".equals(fileName)){
                //1.处理图片文件
                Shop shopTemp = shopDao.queryByShopId(shop.getShopId());
                if(shopTemp.getShopImg() != null) {
                    FileUtil.deleteFile(shopTemp.getShopImg());
                }
                addShopImg(shop,thunbnail);
            }
            //2.修改店铺信息
            shop.setLastEditTime(new Date());
            int effectedNum = shopDao.updateShop(shop);
            if(effectedNum <= 0){
                return new ShopExecution(ShopStateEnum.INNER_ERROR);
            }else {
                shop = shopDao.queryByShopId(shop.getShopId());
                return new ShopExecution(ShopStateEnum.SUCCESS,shop);
            }
        }catch (Exception e){
            throw new ShopOperationException("updataShop err:"+e.getMessage());
        }
    }

    /**
     * 添加店铺
     * @param shop 店铺信息
     * @param thunbnail 图片相关信息
     * @return 处理结果
     */
    @Override
    public ShopExecution addShop(Shop shop, ImageHolder thunbnail) {

        if(shop == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        //给店铺附加初始值
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        shop.setEnableStatus(0);
        //调用方法，添加店铺
        int effectedNum = shopDao.insertShop(shop);
        if (effectedNum <= 0) {
            throw new ShopOperationException("店铺创建失败");
        }else{
            try {
                //创建图片保存位置，并保存图片路径
                addShopImg(shop,thunbnail);
            }catch (Exception e){
                throw new ShopOperationException("addShopImg ERROR"+e.getMessage());
            }
            effectedNum = shopDao.updateShop(shop);
            if (effectedNum <= 0){
                throw new ShopOperationException("店铺图片路径更新失败");
            }
        }
        return new ShopExecution(ShopStateEnum.SUCCESS,shop);
    }

    /**
     * 添加图片
     * @param shop 店铺信息
     * @param thunbnail 图片信息
     */
    private void addShopImg(Shop shop, ImageHolder thunbnail) {
        //获取shop图片目录的相对路径
        String dest = FileUtil.getShopImagePath(shop.getShopId());
        //传入图片文件和图片所在相对路径，返回图片完整的相对路径
        String shopImgAddr = ImageUtil.generateThumbnail(thunbnail,dest);
        shop.setShopImg(shopImgAddr);
    }
}
