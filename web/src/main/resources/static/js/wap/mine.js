//getAppConfig();//加载配置

//高亮显示当前页
$(function () {
	$(".fix-bar-ul li").eq(3).addClass("on");
});

//获取用户数据
$(function () {
	$.post(getApiUrl("/member/member/accountOverview"), {}, function (result) {
			if (isReturnSuccess(result)) {
				$(".total_balance .number").text(formatMoney(result.data.totalAssets));//资产总额
				$(".yesterday .number").text(formatMoney(result.data.yesterdayProfit));//昨日收益
				$(".increment .number").text(formatMoney(result.data.totalProfit));//累计收益
				$(".all_assets .number").text(formatGold(result.data.goldBalance) + '克');//黄金资产(克重)
				$(".total_balance_number .number").text(formatMoney(result.data.goldBalanceAmount) + "元");//黄金资产(元)
				$(".available_balance .number").text(formatMoney(result.data.enableBalance));//可用余额

			} else {
			}

		}
	);

	$(".display_or_hide_number").click(function () {

		//掩藏数据
		if ($(this).attr("data-to_action") == "mask") {
			$(this).attr("src", "../images/mine/eye_open.png");
			$(this).attr("data-to_action", "unMask");
			$(this).attr("title", "隐藏数据");
			$(".number").hide();
			$(".start").show();
		} else {
			$(this).attr("src", "../images/mine/eye_close.png");
			$(this).attr("data-to_action", "mask");
			$(this).attr("title", "显示数据");

			$(".start").hide();
			$(".number").show();
		}
	});


	//点击红包
	$(".red_package").click(function () {
		goToPage("/user/myCashCoupon");
	})

	//点击优惠劵
	$(".coupon").click(function () {
		goToPage("/user/myDiscountCoupon");
	})

});
