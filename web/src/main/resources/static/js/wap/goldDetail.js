$(function () {

	$("#buy_now").click(function () {
		var id = $("#productId").val(),
			financeProductType = $("#financeProductType").val(),
			beginDate = $("#beginDate").val();
		endDate = $("#endDate").val();

		if (systemConfig.TERM_DEPOSIT == financeProductType) {
			goToPage("/gold/goldOrder?id=" + id + "&beginDate=" + beginDate + "&endDate=" + endDate);
		} else if (systemConfig.CURRENT_DEPOSIT == financeProductType) {
			goToPage("/gold/goldOrder?id=" + id + "&beginDate=" + beginDate);
		} else if (systemConfig.RISE_FALL == financeProductType) {
			goToPage("/gold/riseNfallGoldOrder?id=" + id + "&beginDate=" + beginDate + "&endDate=" + endDate);
		}

	});

	//活期金买涨
	$("#buy_now_rise").click(function () {
		currentGoldBuyingTypeSetting("RISE");
	});

	//活期金买跌
	$("#buy_now_fall").click(function () {
		currentGoldBuyingTypeSetting("FALL");
	});

	$("#buying_record").click(function () {
		$.post(getApiUrl("/invest/financial/product/buyHistory"),
			{
				id: $("#detailId").val(),
				page: 1,
				size: systemConfig.defaultPageSize
			},
			function (result) {
				if (isReturnSuccess(result)) {
					console.log(result)
					var resu = result.data.list;
					if (resu.length > 0) {
						var htmlContent = "";
						for (var i = 0; i < resu.length; i++) {
							htmlContent += '<div class="detail_row"><span class="telephone"><img src="../images/from_' + resu[i].terminalType + '.png"/>&nbsp;'
								+ resu[i].mobile + '</span><span class="buying_weight">' + resu[i].weight + '克</span><span class="buying_time">' + resu[i].buyTime + '</span></div>'

						}
					}
					$("#buying_record_list").html(htmlContent);
				}

			}
		)
	});

	/**
	 * 设置活期金的购买类型
	 * 买涨:RISE
	 * 买跌:FALL
	 * @param buyingType
	 */
	function currentGoldBuyingTypeSetting(buyingType) {
		var id = $("#productId").val(),
			financeProductType = $("#financeProductType").val(),
			beginDate = $("#beginDate").val(),
			endDate = $("#endDate").val();

		goToPage("/gold/goldOrder?id=" + id + "&beginDate=" + beginDate + "&buyingType=" + buyingType);
	}

});
