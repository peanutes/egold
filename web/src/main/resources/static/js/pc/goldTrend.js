//金价走势
$(function () {
	var chartContainer = document.getElementById('trend-chart'),
		myChart = echarts.init(chartContainer);

	$(function () {
		myChart.showLoading({
			text: '', maskColor: '#FFFFFF', textColor: '#FF8800',
		});

		$.post(getApiUrl("/gold/price/hourGoldPrices"), {},
			function (data) {
				if (data.code == '200') {
					goldTrendChart(myChart, "trend_one_day", data.data);
				}
			}
		);
	});

	//点击显示各个维度的金价走势
	$(".trend-tab ul li").on("click", function () {
		var type = $(this).attr("id"), result = {}, goldService;

		$(".trend-tab ul li").removeClass("active");
		$("#" + type).addClass("active");
		if (type == 'trend_one_day') {
			goldService = "hourGoldPrices";
		} else if (type == 'trend_seven_day') {
			goldService = "weekGoldPrices";
		} else if (type == 'trend_one_month') {
			goldService = "monthGoldPrices";
		} else if (type == 'trend_one_year') {
			goldService = "dailyGoldPrices";
		} else if (type == 'trend_history') {
			goldService = "dailyGoldPrices";
		}

		myChart.showLoading({
			text: '', maskColor: '#FFFFFF', textColor: '#FF8800',
		});

		$.post(getApiUrl("/gold/price/" + goldService), {},
			function (data) {
				if (data.code == '200') {
					goldTrendChart(myChart, type, data.data);
				}
			}
		);
	});

})