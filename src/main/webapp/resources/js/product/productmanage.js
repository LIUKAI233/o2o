$(function () {
    //获取此店铺下的商品列表的URL
    var listUrl = "/myo2o/productadmin/getproductlist?pageIndex=1&pageSize=999";
    //商品下架URL
    var statusUrl ='/myo2o/productadmin/modifyproduct';
    getList();

    //获取此店铺下的商品列表
    function getList() {
        $.getJSON(listUrl,function (data) {
            if(data.success){
                var productList = data.productList;
                var tempHtml = '';
                //遍历每条商品信息，拼接成一行显示，列信息包括
                //商品名称，优先级，上/下架(含productId),编辑按钮(含productId),预览(含productId)
                productList.map(function (item, index) {
                    var textOp = '下架';
                    var contrayStatus = 0;
                    if(item.enableStatus == 0){
                        //若状态值为0，表明是已下架的商品，操作变为上架
                        textOp = '上架';
                        contrayStatus = 1;
                    }
                    tempHtml += '' + '<div class="row row-product">'
                        + '<div class="col-33">'
                        + item.productName
                        + '</div>'
                        + '<div class="col-20">'
                        + item.point
                        + '</div>'
                        + '<div class="col-40">'
                        + '<a href="#" class="edit" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">编辑</a>'
                        + '<a href="#" class="status" data-id="'
                        + item.productId
                        + '" data-status="'
                        + contrayStatus
                        + '">'
                        + textOp
                        + '</a>'
                        + '<a href="#" class="preview" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">预览</a>'
                        + '</div>'
                        + '</div>';
                });
                $('.product-wrap').html(tempHtml);
            }
        })
    }

    $('.product-wrap').on('click','a',
            function(e) {
                var target = $(e.currentTarget);
                if (target.hasClass('edit')) {
                    //如果有class  edit则点击就进入店铺编辑页面，并带有productId
                    window.location.href = '/myo2o/productadmin/productoperation?productId='
                        + e.currentTarget.dataset.id;
                } else if (target.hasClass('status')) {
                    //如果有class status，则调用后台上下架相关功能，并带有productId
                    changeItemStatus(e.currentTarget.dataset.id,
                        e.currentTarget.dataset.status);
                } else if (target.hasClass('preview')) {
                    //执行预览商品功能
                    window.location.href = '/myo2o/frontend/productdetail?productId='
                        + e.currentTarget.dataset.id;
                }
            });
    
    function changeItemStatus(id,enableStatus) {
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;
        $.confirm('确定么?', function() {
            $.ajax({
                url : statusUrl,
                type : 'POST',
                data : {
                    productStr : JSON.stringify(product),
                    statusChange : true
                },
                dataType : 'json',
                success : function(data) {
                    if (data.success) {
                        $.toast('操作成功！');
                        getList();
                    } else {
                        $.toast(data.errMsg);
                    }
                }
            });
        });
    }
})