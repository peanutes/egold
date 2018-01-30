//高亮显示当前页
$(function () {
	$(".fix-bar-ul li").eq(2).addClass("on");
})

//最新资讯显示
$(function () {
	//showHotInfoList(1, 2);
});

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

});

$(function () {
	// 页数
	var page = 0;

	$('.hot_news_list_wrap').dropload({
		scrollArea: window,
		loadDownFn: function (me) {
			page++;
			showHotInfoList(me, page, systemConfig.PAGE_SIZE);
		}
	});
});

function showHotInfoList(me, page, size) {
	$.post(getApiUrl("/cms/cms/hotInfoList"), {
			page: page,
			size: size
		},
		function (data) {
			console.log(data)
			if (data.code == '200') {
				var i, result = data.data.list, html = "";

				if (result.length > 0) {

					for (i = 0; i < result.length; i++) {
						html += '<li class="news_item"> ';
						html += '<div class="new_section">                                                 ';
						html += '	<div class="news_title">                                               ';
						html += '	<p><a href="' + systemConfig.url + '/news/newsDetail?id=' + result[i].id + '" title="">' + result[i].title + '</a></p>  ';
						html += '</div>                                                                    ';
						html += '<div>                                                                     ';
						html += '<span class="publish_time">' + result[i].publishTime + '</span>                        ';
						html += '<span class="news_source">' + result[i].source + '</span>                                 ';
						html += '	</div>                                                                 ';
						html += '	</div>                                                                 ';
						html += '	<a class="new_section"                                                 ';
						html += 'alt=""><img src="' + result[i].listImg + '"/></a>                         ';
						html += '</li>';
					}
					$("#hot_news_list").append(html);
					//$(".load_more").text("已全部加载");
				}
				console.log(data.data.nextPage)

				if (data.data.nextPage <= 0) {
					// 锁定
					me.lock();
					// 无数据
					me.noData();
				}

			}

			me.resetload();
		}
	);
}
