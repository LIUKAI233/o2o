$(function() {
	var loading = false;
	var maxItems = 999;
	var pageSize = 10;
	var listUrl = '/myo2o/frontend/listshops';
	var searchDivUrl = '/myo2o/frontend/listshopspageinfo';
	var pageNum = 1;
	var parentId = getQueryString('parentId');
	var areaId = '';
	var shopCategoryId = '';
	var shopName = '';

	function getSearchDivData() {
		var url = searchDivUrl + '?' + 'parentId=' + parentId;
		$.getJSON(url,function(data) {
			if (data.success) {
				//遍历添加商铺类别
				var shopCategoryList = data.shopCategoryList;
				var html = '';
				html += '<a href="#" class="button" data-category-id=""> 全部类别  </a>';
				shopCategoryList
						.map(function(item, index) {
							html += '<a href="#" class="button" data-category-id='
									+ item.shopCategoryId
									+ '>'
									+ item.shopCategoryName
									+ '</a>';
						});
				$('#shoplist-search-div').html(html);
				//遍历添加区域类别
				var selectOptions = '<option value="">全部街道</option>';
				var areaList = data.areaList;
				areaList.map(function(item, index) {
					selectOptions += '<option value="'
							+ item.areaId + '">'
							+ item.areaName + '</option>';
				});
				$('#area-search').html(selectOptions);
			}
		});
	}
	getSearchDivData();

	//传入分页信息，添加商铺列表
	function addItems(pageSize, pageIndex) {
		// 生成新条目的HTML
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&parentId=' + parentId + '&areaId=' + areaId
				+ '&shopCategoryId=' + shopCategoryId + '&shopName=' + shopName;
		loading = true;
		$.getJSON(url, function(data) {
			if (data.success) {
				//获取返回商铺总数
				maxItems = data.count;
				var html = '';
				//遍历添加商铺列表
				data.shopList.map(function(item, index) {
					html += '' + '<div class="card" data-shop-id="'
							+ item.shopId + '">' + '<div class="card-header">'
							+ item.shopName + '</div>'
							+ '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'
							+ item.shopImg + '" width="44">' + '</div>'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">' + item.shopDesc
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
							+ '更新</p>' + '<span>点击查看</span>' + '</div>'
							+ '</div>';
				});
				$('.list-div').append(html);
				//获取以添加的商铺信息数量
				var total = $('.list-div .card').length;
				if (total >= maxItems) {
					// 隐藏加载提示符
					$('.infinite-scroll-preloader').hide();
				}else {
					$('.infinite-scroll-preloader').show();
				}
				pageNum += 1;
				loading = false;
				$.refreshScroller();
			}
		});
	}

	// 预先加载10条
	addItems(pageSize, pageNum);

	//无限滚动加载
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		//若loading为true，则退出无限滚动加载
		if (loading)
			return;
		//一页10条，页码+1
		addItems(pageSize, pageNum);
	});

	$('.shop-list').on('click', '.card', function(e) {
		//e.currentTarget 注册了监听点击事件的组件
		var shopId = e.currentTarget.dataset.shopId;
		//在当前页面打开URL，展示商品页面
		window.location.href = '/myo2o/frontend/shopdetail?shopId=' + shopId;
	});

	$('#shoplist-search-div').on('click','.button',function(e) {
		if (parentId) {// 如果传递过来的是一个父类下的子类
			//e.target 实际触发了点击事件的组件
			shopCategoryId = e.target.dataset.categoryId;
			//判断当前点击的事件是否被点击过
			if ($(e.target).hasClass('button-fill')) {
				//点击过，移除button-fill这个class属性，改变css样式
				$(e.target).removeClass('button-fill');
				shopCategoryId = '';
			} else {
				//未点击过，添加button-fill这个class属性，改变css样式，并移除同级事件的class属性改变css样式
				$(e.target).addClass('button-fill').siblings()
						.removeClass('button-fill');
			}
			$('.list-div').empty();
			pageNum = 1;
			addItems(pageSize, pageNum);
		} else {// 如果传递过来的父类为空，则按照父类查询
			parentId = e.target.dataset.categoryId;
			if ($(e.target).hasClass('button-fill')) {
				$(e.target).removeClass('button-fill');
				parentId = '';
			} else {
				$(e.target).addClass('button-fill').siblings()
						.removeClass('button-fill');
			}
			$('.list-div').empty();
			pageNum = 1;
			addItems(pageSize, pageNum);
			parentId = '';
		}

	});

	//点击搜索框，按模糊查询
	$('#search').on('change', function(e) {
		//为shopName赋值，把查询到的商铺列表置为空，重新查询
		shopName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	//更改区域选项
	$('#area-search').on('change', function() {
		//为areaId赋值，把查询到的商铺列表置为空，重新查询
		areaId = $('#area-search').val();
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	//点击开启弹窗
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});

	$.init();
});
