$(function () {
    var shopId = getQueryString("shopId");
    var isEdit = shopId ? true : false;
    var initUrl = "/myo2o/shopadmin/getshopinitinfo";
    var registerShopUrl = "/myo2o/shopadmin/registershop";
    var shopInfoUrl = "/myo2o/shopadmin/getshopbyid?shopId=" + shopId;
    var editShopUrl = "/myo2o/shopadmin/modifyshop"

    if(isEdit){
        getShopInfo();
    }else{
        getShopinitinfo();
    }

    function getShopInfo() {
        $.getJSON(shopInfoUrl, function (data) {
            if (data.success) {
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);
                var shopCategory = "<option id = '" + shop.shopCategory.shopCategoryId + "'>" + shop.shopCategory.shopCategoryName + "</option>"
                var tempAreaHtml = "";
                data.areaList.map(function (item, index) {
                    tempAreaHtml += "<option id = '" + item.areaId + "'>" + item.areaName + "</option>"
                });
                $('#shop-category').html(shopCategory);
                $('#shop-category').attr('disable','disable');
                $('#area').html(tempAreaHtml);
                $("#area option[data-id='"+shop.area.areaId+"']").attr("selected","selected");
            }
        });
    }

    /*获取店铺类别和区域信息*/
    function getShopinitinfo() {
        $.getJSON(initUrl, function (data) {
            if (data.success) {
                var tempHtml = "";
                var tempAreaHtml = "";
                data.shopCategoryList.map(function (item, index) {
                    tempHtml += "<option id = '" + item.shopCategoryId + "'>" + item.shopCategoryName + "</option>"
                });
                data.areaList.map(function (item, index) {
                    tempAreaHtml += "<option id = '" + item.areaId + "'>" + item.areaName + "</option>"
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });
    }

    $('#submit').click(function () {
        /*创建店铺对象，并获取对应的店铺信息*/
        var shop = {};
        if(isEdit){
            shop.shopId = shopId;
        }
        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();
        /*获取选择的店铺类别和区域ID*/
        var shopCategoryType = document.getElementById("shop-category");
        var shopCategoryId = shopCategoryType.options [shopCategoryType.selectedIndex].id;
        var areaType = document.getElementById("area");
        var areaId = areaType.options [areaType.selectedIndex].id;
        var area = {};
        var shopCategory = {};
        area.areaId = areaId;
        shopCategory.shopCategoryId = shopCategoryId;
        shop.area = area;
        shop.shopCategory = shopCategory;
        var shopImg = $('#shop-img')[0].files[0];
        var verifyCodeActul = $('#j-kaptcha').val();
        if (verifyCodeActul == null || verifyCodeActul == "") {
            $.toast('请输入验证码!');
            return;
        }
        /*打包相关数据*/
        var formData = new FormData();
        formData.append('verifyCodeActul', verifyCodeActul);
        formData.append('shopImg', shopImg);
        formData.append('shopStr', JSON.stringify(shop));
        $.ajax({
            url: (isEdit ? editShopUrl : registerShopUrl),
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功！');
                } else {
                    $.toast('提交失败！' + data.errMsg);
                }
                $('#kaptcha_img').click();
                document.getElementById("kaptcha_img").value = "";
            }
        });
    });
});
