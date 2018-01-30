//退出登录
$(function () {
	$(".logout_btn").click(function () {
		$.post(getApiUrl("/member/member/logout"),
			{},
			function (result) {
				if (isReturnSuccess(result)) {
					mui.toast("退出登录成功!");
					goToPage("/wap/index");
				}

			}
		)
	});
});

//使用FastClick消除300m延迟
$(function () {
	if (typeof FastClick == 'function') {
		FastClick.attach(document.body);
	}
});

//展示金价走势图
function goldTrendChart(chartObj, type, result) {

	var dataDpr = $('html').attr("data-dpr"), times, prices, max, min, option, interval = 'auto', width = $(window).width();

	times = result.times;
	prices = result.prices;
	max = Math.ceil(result.maxPrice);
	min = result.minPrice - 1;

	interval = Math.round(times.length / 5);

	option = {
		tooltip: {
			trigger: 'axis',
			showDelay: 0,
			hideDelay: 100,

			formatter: function (params) {
				var times = params[0].name.substring(0, 16), prices = params[0].value;

				$("#date_and_price").html(times + '   |  ' + prices + '元/克');

				return '<div class="gold_price_dynamic"><p>价格:  ' + prices + '元/克' + '</p><p>' + params[0].seriesName + times + '</p></div>';
			},
			backgroundColor: "#FFFFFF",
			borderColor: "#FF8800;",
			borderWidth: 1 * dataDpr,
			borderRadius: "4",
			axisPointer: {
				type: 'line',
				lineStyle: {
					color: 'rgb(222,132,90)',
					width: 1 * dataDpr,
					type: 'solid'
				}
			}
		},
		//这里grid设置成百分比才能更好的适应浏览器
		grid: {
			x: '15%',
			y: '2%',
			x2: '8%',//这里需要设置好才能使最右那个刻度各种情况都能正确显示出来
			y2: '15%',
			borderWidth: 0
		},
		xAxis: {
			type: 'category',
			boundaryGap: false,

			axisLine: {
				onZero: false,
				lineStyle: {
					color: "#666666",
					width: 1
				}
			},

			axisTick: { // X轴刻度标记
				show: true,
				length: 6,//刻度线长度
				interval: 'auto',
				lineStyle: {
					color: "#666666",
					width: 1 * dataDpr
				}
			},
			axisLabel: { //X轴刻度配置
				textStyle: {
					fontFamily: '微软雅黑',
					color: '#666666',
					fontSize: 12 * dataDpr
				},
				margin: 12,
				interval: interval,
				//轴标文字处理
				formatter: function (value) {
					var dateStr;
					if (type == 'trend_one_day') {
						dateStr = value.split(' ')[1].substring(0, 5);
					}
					else if (type == 'trend_seven_day') {
						dateStr = value.substring(5, 10);
					} else if (type == 'trend_one_month') {
						dateStr = value.substring(5, 10);
					} else if (type == 'trend_one_year') {
						dateStr = value.substring(5, 10);
					} else if (type == 'trend_history') {
						dateStr = value.substring(5, 10);
					}

					return dateStr;
				}
				//rotate: 45//如果文字太长，倾斜显示
			},
			splitLine: {//隔列背景线
				show: false
			},
			data: times
		},
		yAxis: [{
			type: 'value',
			//boundaryGap: ['2%', '100%'],
			scale: true,
			nameLocation: 'start',
			inverse: false,

			max: max,
			min: min,
			splitNumber: 3,

			splitLine: { //隔行背景线
				show: true,
				lineStyle: {
					color: "#dddddd",
					type: 'dashed',
					width: 1 * dataDpr
				}
			},
			axisLabel: { //Y轴刻度配置
				textStyle: {
					fontFamily: '微软雅黑',
					color: '#666666',
					fontSize: 12 * dataDpr
				},
				margin: 12,//坐标轴文本标签与坐标轴的间距
				length: 6,//刻度线长度
				interval: 'auto'
			}
		}],

		series: [{
			name: '时间: ',
			type: 'line',
			smooth: true,
			symbol: 'circle',
			symbolSize: 4 * dataDpr,
			sampling: 'average',
			itemStyle: {
				normal: {
					lineStyle: {
						width: 1 * dataDpr,
						color: 'rgb(222,132,90)'
					},
					color: 'rgba(0,0,0,0)',
					borderWidth: 2,
					borderColor: "rgba(0,0,0,0)"

				},
				emphasis: {
					color: '#FFFFFF',
					borderWidth: 1,
					borderColor: "#FF8800",
					label: {
						show: true,
						position: 'inside',
						textStyle: {
							fontSize: '0',
							color: 'rgba(0,0,0,0)'
						}
					}
				}
			},
			areaStyle: {
				normal: {
					color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
						offset: 0,
						color: 'rgba(252, 171, 53,1)'
					}, {
						offset: 1,
						color: 'rgba(251, 248, 224,0.5)'
					}])
				}
			},

			data: prices
		}]
	};

	chartObj.hideLoading();//隐藏Loading提示

	//重新定义图表显示区域的大小
	chartObj.resize({
		width: width
	});

	chartObj.setOption(option);

}

//清除错误信息
function clearErrorMessage() {
	$(".errorPlaceHolder").html("");
}

//返回完整的api地址(Wap)
function getApiUrl(serviceAddress) {
	var queryString = systemConfig.queryString4Wap;
	if (serviceAddress.indexOf("?") < 0) {
		queryString = "?" + queryString;
	} else {
		queryString = "&" + queryString;
	}

	return systemConfig.url + serviceAddress + queryString;
}

function alertMessage(msg) {
	layer.open({
		title: [
			'提示信息',
			'background-color:#ff8800; color:#fff;font-size:0.5rem'
		],
		shadeClose: true,
		btn: ['确定'],
		content: '<p class="messageContent">' + msg + '</p>'
		//, time: 4
	});
}

function confirmMessage(msg, fn) {
	layer.open({
		title: [
			'提示信息',
			'background-color:#ff8800; color:#fff;font-size:0.5rem'
		],
		content: '<p class="messageContent">' + msg + '</p>'
		, btn: ['确定'],
		shadeClose: true
		, yes: function (index) {
			layer.close(index);
			if (null != fn) {
				fn();
			}

		}
	})
	;
}