var dataDpr = $('html').attr("data-dpr"), chartContainer = document.getElementById('trend-chart'),
	myChart = echarts.init(chartContainer), oneDayresult;

oneDayresult = {
	"resultCode": "200",
	"resultMsg": "成功",
	"content": {
		"retCode": "0000",
		"retMsg": "操作成功",
		"x": ["00:00", "00:05", "00:10", "00:15", "00:20", "00:25", "00:30", "00:35", "00:40", "00:45", "00:50", "00:55", "01:00", "01:05", "01:10", "01:15", "01:20", "01:25", "01:30", "01:35", "01:40", "01:45", "01:50", "01:55", "02:00", "02:05", "02:10", "02:15", "02:20", "02:25", "02:30", "02:35", "02:40", "02:45", "02:50", "02:55", "03:00", "03:05", "03:10", "03:15", "03:20", "03:25", "03:30", "03:35", "03:40", "03:45", "03:50", "03:55", "04:00", "04:05", "04:10", "04:15", "04:20", "04:25", "04:30", "04:35", "04:40", "04:45", "04:50", "04:55", "05:00", "05:05", "05:10", "05:15", "05:20", "05:25", "05:30", "05:35", "05:40", "05:45", "05:50", "05:55", "06:00", "06:05", "06:10", "06:15", "06:20", "06:25", "06:30", "06:35", "06:40", "06:45", "06:50", "06:55", "07:00", "07:05", "07:10", "07:15", "07:20", "07:25", "07:30", "07:35", "07:40", "07:45", "07:50", "07:55", "08:00", "08:05", "08:10", "08:15", "08:20", "08:25", "08:30", "08:35", "08:40", "08:45", "08:50", "08:55", "09:00", "09:05", "09:10", "09:15", "09:20", "09:25", "09:30", "09:35", "09:40", "09:45", "09:50", "09:55", "10:00", "10:05", "10:10", "10:15", "10:20", "10:25", "10:30", "10:35", "10:40", "10:45", "10:50", "10:55", "11:00", "11:05", "11:10", "11:15", "11:20", "11:25", "11:30", "11:35", "11:40", "11:45", "11:50", "11:55", "12:00", "12:05", "12:10", "12:15", "12:20", "12:25", "12:30", "12:35", "12:40", "12:45", "12:50", "12:55", "13:00", "13:05", "13:10", "13:15", "13:20", "13:25", "13:30", "13:35", "13:40", "13:45", "13:50", "13:55", "14:00", "14:05", "14:10", "14:15", "14:20", "14:25", "14:30", "14:35", "14:40", "14:45", "14:50", "14:55", "15:00", "15:05", "15:10", "15:15", "15:20", "15:25", "15:30", "15:35", "15:40", "15:45", "15:50", "15:55", "16:00", "16:05", "16:10", "16:15", "16:20", "16:25", "16:30", "16:35", "16:40", "16:45", "16:50", "16:55", "17:00", "17:05", "17:10", "17:15", "17:20", "17:25", "17:30", "17:35", "17:40", "17:45", "17:50", "17:55", "18:00", "18:05", "18:10", "18:15", "18:20", "18:25", "18:30", "18:35", "18:40", "18:45", "18:50", "18:55", "19:00", "19:05", "19:10", "19:15", "19:20", "19:25", "19:30", "19:35", "19:40", "19:45", "19:50", "19:55", "20:00", "20:05", "20:10", "20:15", "20:20", "20:25", "20:30", "20:35", "20:40", "20:45", "20:50", "20:55", "21:00", "21:05", "21:10", "21:15", "21:20", "21:25", "21:30", "21:35", "21:40", "21:45", "21:50", "21:55", "22:00", "22:05", "22:10", "22:15", "22:20", "22:25", "22:30", "22:35", "22:40", "22:45", "22:50", "22:55", "23:00", "23:05", "23:10", "23:15", "23:20", "23:25", "23:30", "23:35", "23:40", "23:45", "23:50", "23:55"],
		"x2": null,
		"y": [278.53, 278.54, 278.40, 278.48, 278.35, 278.27, 278.29, 278.39, 278.38, 278.34, 278.11, 278.25, 278.27, 278.24, 278.40, 278.38, 278.46, 278.44, 278.38, 278.23, 278.19, 278.25, 278.21, 278.19, 278.06, 278.04, 278.02, 277.97, 278.15, 278.25, 278.27, 278.31, 278.59, 278.53, 278.46, 278.45, 278.35, 278.36, 278.42, 278.56, 278.52, 278.39, 278.30, 278.31, 278.28, 278.39, 278.51, 278.64, 278.61, 278.62, 278.53, 278.50, 278.49, 278.49, 278.49, 278.53, 278.50, 278.47, 278.45, 278.41, 278.42, 278.42, 278.42, 278.42, 278.42, 278.42, 278.42, 278.42, 278.42, 278.42, 278.42, 278.42, 278.42, 278.53, 278.58, 278.50, 278.60, 278.55, 278.54, 278.57, 278.56, 278.62, 278.53, 278.51, 278.53, 278.58, 278.68, 278.74, 278.73, 278.68, 278.74, 278.67, 278.59, 278.68, 278.65, 278.68, 278.75, 278.79, 278.72, 278.75, 278.79, 278.76, 278.80, 278.83, 278.81, 278.78, 278.74, 278.78, 278.72, 278.79, 278.75, 278.74, 278.81, 278.79, 278.80, 278.80, 278.96, 279.05, 279.09, 279.22, 279.20, 279.20, 279.30, 279.12, 279.00, 278.98, 279.02, 278.98, 279.04, 279.00, 279.13, 279.14, 279.16, 279.21, 279.32, 279.44, 279.42, 279.42, 279.31, 279.41, 279.42, 279.38, 279.38, 279.33, 279.43, 279.36, 279.30, 279.25, 279.29, 279.28, 279.23, 279.27, 279.29, 279.16, 279.02, 279.01, 279.06, 279.06, 279.13, 279.13, 278.99, 278.90, 278.93, 278.95, 279.09, 279.03, 279.02, 279.08, 279.11, 279.09, 279.18, 279.20, 279.15, 279.23, 279.25, 279.13, 279.11, 278.98, 279.22, 279.19, 279.10, 279.00, 278.99, 279.02, 278.94, 278.99, 278.99, 278.84, 278.90, 279.01, 279.06, 279.16, 279.30, 279.07, 279.08, 278.98, 278.98, 279.09, 279.22, 279.05, 278.86, 279.03, 279.01, 279.07, 279.09, 279.02, 279.01, 279.01, 279.00, 279.07, 279.07, 279.08, 278.96, 278.91, 278.97, 278.96, 278.94, 279.01, 279.01, 279.07, 279.06, 279.11, 279.08, 279.06, 278.92, 278.88, 278.95, 278.90, 279.03, 279.03, 279.00, 278.94, 278.94, 278.86, 278.82, 278.82, 279.03, 279.05, 278.92, 278.97, 278.90, 278.83, 279.08, 278.95, 279.03, 279.00, 278.91, 278.96, 279.12, 278.89, 278.89, 278.89, 278.88, 278.89, 278.90, 278.96, 279.08, 279.16, 279.18, 279.16, 279.17, 279.28, 279.25, 279.17, 279.26, 279.00, 278.96, 278.96, 279.01, 279.21, 279.04, 279.15, 278.96, 279.15, 279.13, 279.12, 279.22, 279.30, 279.05, 279.00, 279.04, 279.01, 278.95, 278.93, 278.87, 279.09, 279.00, 279.08],
		"max": 279.44,
		"min": 277.97,
		"success": true
	}
};

$(".trend-tab ul li").on("click", function () {
	var type = $(this).attr("id"), result = {};

	$(".trend-tab ul li").removeClass("active");
	$("#" + type).addClass("active");

	if (type == 'trend_one_day') {
		result = oneDayresult;

	} else if (type == 'trend_seven_day') {
		result = {
			"resultCode": "200",
			"resultMsg": "成功",
			"content": {
				"retCode": "0000",
				"retMsg": "操作成功",
				"x": ["00:00", "00:05", "00:10", "00:15", "00:20", "00:25", "00:30", "00:35", "00:40", "00:45", "00:50", "00:55"],
				"x2": null,
				"y": [278.53, 278.54, 278.40, 278.48, 278.35, 278.27, 278.29, 278.39, 278.38, 278.34, 278.11, 278.25, 278.27, 278.24, 278.40, 278.38, 278.46, 278.44, 278.38, 278.23, 278.19, 278.25,],
				"max": 279.44,
				"min": 277.97,
				"success": true
			}
		};
	} else if (type == 'trend_one_month') {
		result = oneDayresult;

	} else if (type == 'trend_one_year') {
		result = {
			"resultCode": "200",
			"resultMsg": "成功",
			"content": {
				"retCode": "0000",
				"retMsg": "操作成功",
				"x": ["00:00", "00:05", "00:10", "00:15", "00:20", "00:25", "00:30", "00:35", "00:40", "00:45", "00:50", "00:55"],
				"x2": null,
				"y": [278.53, 278.54, 278.40, 278.48, 278.35, 278.27, 278.29, 278.39, 278.38, 278.34, 278.11, 278.25, 278.27, 278.24, 278.40, 278.38, 278.46, 278.44, 278.38, 278.23, 278.19, 278.25,],
				"max": 279.44,
				"min": 277.97,
				"success": true
			}
		};
	} else if (type == 'trend_history') {
		result = {
			"resultCode": "200",
			"resultMsg": "成功",
			"content": {
				"retCode": "0000",
				"retMsg": "操作成功",
				"x": ["00:00", "00:05", "00:10", "00:15", "00:20", "00:25", "00:30", "00:35", "00:40", "00:45", "00:50", "00:55"],
				"x2": null,
				"y": [278.53, 278.54, 278.40, 278.48, 278.35, 278.27, 278.29, 278.39, 278.38, 278.34, 278.11, 278.25, 278.27, 278.24, 278.40, 278.38, 278.46, 278.44, 278.38, 278.23, 278.19, 278.25,],
				"max": 279.44,
				"min": 277.97,
				"success": true
			}
		};
	}

	goldTrendChart(result);
});

goldTrendChart(oneDayresult);

function goldTrendChart(result) {

	var date, data, option;

	date = result.content.x;
	data = result.content.y;

	option = {
		tooltip: {
			trigger: 'axis',
			showDelay: 0,
			hideDelay: 100,
			formatter: function (params) {
				$("#date_and_price").html(params[0].name + '   |  ' + params[0].value + '元/克');
				return '<div class="gold_price_dynamic"><p>价格:  ' + params[0].value + '元/克' + '</p><p>' + params[0].seriesName + params[0].name + '</p></div>';
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

		xAxis: {
			type: 'category',
			axisLine: {
				onZero: false,
				lineStyle: {
					color: "#666666",
					width: 1
				}
			},

			axisTick: { // 轴刻度标记
				show: true,
				length: 1,
				interval: 1,
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
				margin: 0,
				interval: 60
			},
			boundaryGap: false,
			data: date
		},
		yAxis: {
			type: 'value',
			boundaryGap: [0, '100%'],
			scale: true,
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
				margin: 0,
				interval: 60
			}
		},

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

			data: data
		}]
	};

// 使用刚指定的配置项和数据显示图表。
	myChart.setOption(option);
}