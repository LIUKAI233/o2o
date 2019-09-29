$(function () {
    //获取url中的店铺id
    var productId = getQueryString("productId");
    //判断id是否存在，若存在则是修改商品，反正为添加商品
    var isEdit = productId ? true : false;
    //添加商品路径
    var addUrl = "/myo2o/productadmin/addproduct";
    //修改商品路径
    var modifyUrl = "/myo2o/productadmin/modifyproduct";
    //获取商品分类的url
    var categoryUrl = "/myo2o/productadmin/getproductcategorylist";
    //获取商品信息的url
    var infoUrl = "/myo2o/productadmin/getproductById?productId="+productId;

    if(isEdit){
        //获取商品信息
        getProductInfo(productId);
    }else{
        getCategoryInfo();
    }

    //返回所有的商品分类
    function getCategoryInfo() {
        $.getJSON(categoryUrl,function (data) {
            //从返回的JSON中获取商品分类信息，并赋值给表单
            if(data.success){
                var categoryHtml = "";
                data.productCategoryList.map(function (item, index) {
                    categoryHtml += "<option id = '" + item.productCategoryId + "'>" + item.productCategoryName + "</option>"
                });
                $('#product-category').html(categoryHtml);
            }
        })
    }

    //根据id查询商品信息
    function getProductInfo(Id){
        $.getJSON(infoUrl,function (data) {
            //从返回的JSON中获取product，并赋值给表单
            if(data.success){
                var product = data.product;
                $('#product-name').val(product.productName);
                $('#product-desc').val(product.productDesc);
                $('#point').val(product.point);
                $('#normal-price').val(product.normalPrice);
                $('#promotion-price').val(product.promotionPrice);
                //获取商品原本分类和商品所有分类
                var optionCategory = "";
                var categoryList = data.productCategoryList;
                var categoryId = product.productCategory.productCategoryId;
                //生成目录选择列表，并默认选择原先的分类
                categoryList.map(function (item, index) {
                    optionCategory += "<option id = '" + item.productCategoryId + "'>" + item.productCategoryName + "</option>"
                });
                $('#product-category').html(optionCategory);
                $("#product-category option[data-id='"+categoryId+"']").attr("selected","selected");
            }
        });
    }

    //针对商品详情图控件组，若该控件的最后一个元素发生变化(即上传了图片)
    //且控件总数未达到6个，则生成新的一个文件上传控件
    $('.detail-img-div').on('change','.detail-img:last-child',function() {
        if( $('.detail-img').length < 6){
            $('#detail-img').append('<input type="file" class="detail-img">');
        }
    });
    
    $('#submit').click(function () {
        var product = {};
        product.productName = $('#product-name').val();
        product.priority = $('#priority').val();
        product.point = $('#point').val();
        product.normalPrice = $('#normal-price').val();
        product.promotionPrice = $('#promotion-price').val();
        product.productDesc = $('#product-desc').val();
        //若是修改商品，传入商品ID，添加则为空
        product.productId = productId;
        //获取选定的类别id
        var selectType = document.getElementById("product-category");
        var productCategoryId = selectType.options[selectType.selectedIndex].id;
        var productCategory = {};
        productCategory.productCategoryId = productCategoryId;
        product.productCategory = productCategory;
        //获取缩略图文件流
        var thumbnail = $('#small-img')[0].files[0];
        var verifyCodeActul = $('#j-kaptcha').val();
        if (verifyCodeActul == null || verifyCodeActul == "") {
            $.toast('请输入验证码!');
            return;
        }
        //生成表单对象，用于接收参数并返回给后台
        var formData = new FormData();
        formData.append('verifyCodeActul', verifyCodeActul);
        formData.append('thumbnail', thumbnail);
        //遍历商品详情图控件，获取里面的文件流
        $('.detail-img').map(function (index ,item) {
            if($('.detail-img')[index].files.length > 0){
                //将第i个文件流，赋值给key为productImgi的键值对里
                formData.append("productImg"+index, $('.detail-img')[index].files[0])
            }
        })
        formData.append('productStr', JSON.stringify(product));
        formData.append('statusChange','true');
        $.ajax({
            url: (isEdit ? modifyUrl : addUrl),
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功！');
                    window.location.href="/myo2o/productadmin/productmanage";
                } else {
                    $.toast('提交失败！' + data.errMsg);
                }
                $('#kaptcha_img').click();
                document.getElementById("j-kaptcha").value = "";
            }
        });
    })
})