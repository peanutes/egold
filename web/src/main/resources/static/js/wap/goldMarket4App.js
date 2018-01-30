$(function () {
	//显示商城列表

	listGoods();

	function listGoods(skuQueryOrderBy) {
		//默认按新品排序
		skuQueryOrderBy = skuQueryOrderBy || 'NEW_GOOD';

		$.post(getApiUrl("/mall/skuList"), {
			page: 1,
			size: systemConfig.defaultPageSize,
			skuQueryOrderBy: skuQueryOrderBy

		}, function (result) {
			if (isReturnSuccess(result)) {
				var resu = result.data,
					htmlContent = ""
					;

				if (resu != null && resu.list != null) {
					var resultObject = resu.list;
					for (var i = 0; i < resultObject.length; i++) {
						htmlContent += '<div class="gold_detail_section" data-id="' + resultObject[i].id + '">';
						htmlContent += '	<div class="gold_pic_wrapper">';
						htmlContent += '		<img src="' + resultObject[i].listImgUrl + '">';
						htmlContent += '	</div>';
						htmlContent += '	<p class="gold_name">' + resultObject[i].goodName + '</p>';
						htmlContent += '	<p class="gold_price">￥' + resultObject[i].price + '</p>';
						htmlContent += '</div>';
					}
				}

				$(".gold_list").html(htmlContent);

				//点击进入详情
				$(".gold_detail_section").click(function () {
					var skuId = $(this).data("id"),
						token = $("#token").val();
					goToPage("/goldMall/skuDetail4App?skuId=" + skuId + "&token=" + token);
				});
			}

		});
	}

	//排序
	$(".orderBy").click(function () {
		var orderBy = $(this).data("orderBy"),
			orderField = $(this).attr("id")//排序使用的字段,新品, 价格
			;
		$(".orderBy").removeClass("active");
		$(this).addClass("active");

		//如果是降序,设置为升
		//反之亦然
		if (orderBy == "_DESC") {
			$(this).attr("data-order-by", "_ASC")
			$(this).find('img').attr("src", "../images/market/order_asc.png");
		} else if (orderBy == "_ASC") {
			$(this).attr("data-order-by", "_DESC")
			$(this).find('img').attr("src", "../images/market/order_desc.png");
		} else if (orderBy == "NEW_GOOD") {

		}

		if (orderField == "PRICE") {
			//按价格排序
			listGoods(orderField + orderBy);
		} else if (orderField == "NEW_GOOD") {
			//按新品排序
			listGoods("NEW_GOOD");
		}

	});

	//去首页
	$("#toIndex").click(function () {
		goToPage("/wap/index");
	});

	//处理详情页的图片自适应问题
	$(".product_desc_pic_list img").css({
		"width": "100%",
		"height": "100%"
	});

	//焦点图显示
	$(function () {
		//焦点图设置
		var gallery = mui('.mui-slider');
		gallery.slider({
			interval: 5000 //自动轮播周期，若为0则不自动播放，默认为0；
		});
	});

});
