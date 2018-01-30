$(function () {

	//获取我的订单列表
	$.post(getApiUrl("/order/order/myOrderList"), {
		page: 1,
		size: systemConfig.defaultPageSize
	}, function (result) {
		console.log(result)
		if (isReturnSuccess(result)) {

			var resu = result.data,
				htmlAllStatusContent = "",
				htmlInProgressContent = '',
				htmlFinishedContent = '',
				htmlExpiredContent = ''
				;

			if (resu != null && resu.list != null) {
				var resultObject = resu.list;
				//PROCESSING(1, "进行中"),
				//FINISH(2, "已完成"),
				//	EXPIRED(3, "已过期"),;

				for (var i = 0; i < resultObject.length; i++) {
					var createDate = resultObject[i].creteDate,
						orderStatus = resultObject[i].myorderStatus,
						orderStatusDesc = getOrderStatus(orderStatus),
						myorderType = resultObject[i].myorderType,
						myorderTypeDesc = getOrderType(myorderType),
						action = resultObject[i].creteDate,
						amount = resultObject[i].creteDate,
						enabledAmount = resultObject[i].creteDate,
						productName = resultObject[i].productName
					orderTotalAmount = resultObject[i].orderTotalAmount;

					htmlAllStatusContent += ' <div class="detail_row"><span class="detail_caption color_929292">' + createDate + '</span>'
					htmlAllStatusContent += ' 	<span class="detail_desc orange">' + orderStatusDesc + '</span></div>'
					htmlAllStatusContent += ' 	<div class="detail_row"><span class="detail_caption">' + myorderTypeDesc + '</span> '
					htmlAllStatusContent += ' 	<span class="detail_desc">' + amount + '&gt;</span></div>  '
					htmlAllStatusContent += ' <div class="detail_row color_929292"><span class="detail_caption">' + productName + '</span>'
					htmlAllStatusContent += ' 	<span class="detail_desc">订单总额：¥' + orderTotalAmount + '&gt;</span></div>'
					htmlAllStatusContent += ' <div class="split_bar_f5f5f5 height20"></div>'

					if (orderStatus == 1) {
						htmlInProgressContent += ' <div class="detail_row"><span class="detail_caption color_929292">' + createDate + '</span>'
						htmlInProgressContent += ' 	<span class="detail_desc orange">' + orderStatusDesc + '</span></div>'
						htmlInProgressContent += ' 	<div class="detail_row"><span class="detail_caption">' + myorderTypeDesc + '</span> '
						htmlInProgressContent += ' 	<span class="detail_desc">' + amount + '&gt;</span></div>  '
						htmlInProgressContent += ' <div class="detail_row color_929292"><span class="detail_caption">' + productName + '</span>'
						htmlInProgressContent += ' 	<span class="detail_desc">订单总额：¥' + orderTotalAmount + '&gt;</span></div>'
						htmlInProgressContent += ' <div class="split_bar_f5f5f5 height20"></div>'
					} else if (orderStatus == 2) {
						htmlFinishedContent += ' <div class="detail_row"><span class="detail_caption color_929292">' + createDate + '</span>'
						htmlFinishedContent += ' 	<span class="detail_desc orange">' + orderStatusDesc + '</span></div>'
						htmlFinishedContent += ' 	<div class="detail_row"><span class="detail_caption">' + myorderTypeDesc + '</span> '
						htmlFinishedContent += ' 	<span class="detail_desc">' + amount + '&gt;</span></div>  '
						htmlFinishedContent += ' <div class="detail_row color_929292"><span class="detail_caption">' + productName + '</span>'
						htmlFinishedContent += ' 	<span class="detail_desc">订单总额：¥' + orderTotalAmount + '&gt;</span></div>'
						htmlFinishedContent += ' <div class="split_bar_f5f5f5 height20"></div>'
					}
					else if (orderStatus == 3) {
						htmlExpiredContent += ' <div class="detail_row"><span class="detail_caption color_929292">' + createDate + '</span>'
						htmlExpiredContent += ' 	<span class="detail_desc orange">' + orderStatusDesc + '</span></div>'
						htmlExpiredContent += ' 	<div class="detail_row"><span class="detail_caption">' + myorderTypeDesc + '</span> '
						htmlExpiredContent += ' 	<span class="detail_desc">' + amount + '&gt;</span></div>  '
						htmlExpiredContent += ' <div class="detail_row color_929292"><span class="detail_caption">' + productName + '</span>'
						htmlExpiredContent += ' 	<span class="detail_desc">订单总额：¥' + orderTotalAmount + '&gt;</span></div>'
						htmlExpiredContent += ' <div class="split_bar_f5f5f5 height20"></div>'
					}

				}

				//根据状态代码获取状态描述
				function getOrderStatus(statusNum) {
					var result = statusNum;
					if (1 == statusNum) {
						result = "进行中";
					} else if (2 == statusNum) {
						result = "已完成";
					} else if (3 == statusNum) {
						result = "已过期";
					}

					return result;
				}

				//根据类型代码获取产品类型描述
				function getOrderType(statusNum) {
					var result = statusNum;
					if (1 == statusNum) {
						result = "买金";
					} else if (2 == statusNum) {
						result = "卖金";
					} else if (3 == statusNum) {
						result = "存金";
					} else if (4 == statusNum) {
						result = "提金";
					} else if (5 == statusNum) {
						result = "充值";
					} else if (6 == statusNum) {
						result = "提现";
					} else if (7 == statusNum) {
						result = "黄金商城";
					} else if (8 == statusNum) {
						result = "看涨跌";
					} else if (9 == statusNum) {
						result = "定期金转活期金";
					}

					return result;
				}

				htmlAllStatusContent = htmlAllStatusContent || "<p class='ta_center fz_32 padding_20'>暂无数据</p>";
				htmlFinishedContent = htmlFinishedContent || "<p class='ta_center fz_32 padding_20'>暂无数据</p>";
				htmlInProgressContent = htmlInProgressContent || "<p class='ta_center fz_32 padding_20'>暂无数据</p>";
				htmlExpiredContent = htmlExpiredContent || "<p class='ta_center fz_32 padding_20'>暂无数据</p>";

				$("#tab_1_content").html(htmlAllStatusContent);
				$("#tab_2_content").html(htmlFinishedContent);
				$("#tab_3_content").html(htmlInProgressContent);
				$("#tab_4_content").html(htmlExpiredContent);
			}

		}
	});

});