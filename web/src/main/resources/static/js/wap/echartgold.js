var ctxStatic = $("#ctxStatic").val();
var urlPerfix = $("#urlPerfix").val();
require.config({
	paths: {echarts: ctxStatic + '/js/plugins'}
});
require([
		'echarts',
		'echarts/chart/line'	//线状图表
	],
	function (ec) {

		var Gold24Time = ec.init(document.getElementById("gold24Time"));
		var todayDate=change_date(1);
		Gold24Time.showLoading({
			text : '正在努力的读取数据中...'
		});
		$.getJSON(urlPerfix+"/getEchartPrice.do?days=1", function(d) {
			if(d.resultCode!=200){
				return;
			}
			var price_day_x = d.content.x;
			var price_day_y = d.content.y;
			var price_day_y_max = d.content.max;
			var price_day_y_min = d.content.min;

			Gold24Time.setOption({

				//======
				tooltip : {
					trigger: 'axis',
					showDelay: 0,
					hideDelay: 100,
					formatter: function(params) {
						return params[0].value + '元/克' + '<p style="color:#FF9906;font-size: 12px">' + params[0].seriesName + params[0].name + '</p>';
					},
					backgroundColor:"#fff",
					borderColor:"#FFEACB;",
					borderRadius:"3",
					borderWidth:"1",
					textStyle:{color:'#F56E00'},
					axisPointer : {
						type : 'line',
						lineStyle: {
							color: '#ffcc9f',
							width: 1,
							type: 'solid'
						}
					}
				},
				grid:{
					x:'55',
					y:'15',
					x2:'20',
					y2:'35',
					borderWidth: 0
				},
				calculable:false,
				animation: {
					addDataAnimation:true,
					animationThreshold:true,
					animationDuration:true,
					animationDurationUpdate:true,
					animationEasing:true
				},
				xAxis : [
					{
						type : 'category',

						boundaryGap : false,

						axisLabel : { //X轴刻度配置
							textStyle:{
								fontFamily:'微软雅黑',
								color:'#999',
								fontSize : 14
							},
							margin:13,
							interval:50
						},
						axisLine: {
							onZero: false,
							lineStyle: {
								color: "#efefef",
								width: 1
							}
						},
						splitLine : {//隔列背景线
							show:false
						},
						axisTick : {    // 轴刻度标记
							show:true,
							length: 6,
							interval: 50,
							lineStyle: {
								color: "#efefef",
								width: 1
							}
						},
						data : price_day_x
					}
				],
				yAxis : [
					{
						type : 'value',
						scale: true,
						splitNumber:3,
						//max:price_day_y_max,
						//min:price_day_y_min,
						nameLocation: 'start',
						inverse: true,
						axisLabel : {
							textStyle:{
								fontFamily:'微软雅黑',
								color:'#999',
								fontSize : 14
							}
						},
						axisLine: {
							lineStyle: {
								color: "#fff",
								width: 1
							}
						},
						splitLine : {//隔行背景线
							show:true,
							lineStyle: {
								color: "#f4f4f4",
								type: 'solid',
								width: 1
							}
						}
					}
				],
				series : [
					{
						type:'line',
						symbol: 'circle',
						symbolSize: 4,
						itemStyle: {
							normal: {
								areaStyle: {
									color : (function (){
										if(window.navigator.userAgent.indexOf('MSIE 7') != -1 || window.navigator.userAgent.indexOf('MSIE 8') != -1)
										{
											return "rgba(255,237,210,0.5)";
										}
										var zrColor = require('zrender/tool/color');
										return zrColor.getLinearGradient(
											0, 100, 0, 240,//折线渐变色宽高比
											[[0, 'rgba(255,237,210,0.6)'],[0.8, 'rgba(255,237,210,0)']]
										)
									})()
								},
								lineStyle: {
									width: 1,
									color: "#ff730c"
								},
								color: 'rgba(0,0,0,0)',
								borderWidth:2,
								borderColor:"rgba(0,0,0,0)"

							},
							emphasis: {
								color: '#fd9728',
								borderWidth:2,
								borderColor:"#fff",
								label : {
									show: true,
									position: 'inside',
									textStyle : {
										fontSize : '0',
										color: 'rgba(0,0,0,0)'
									}
								}
							}
						},
						data: price_day_y
					}
				]
				//=========

			});
			Gold24Time.hideLoading();
		});


		var Gold7day = ec.init(document.getElementById("gold7Day"));
		Gold7day.showLoading({
			text : '正在努力的读取数据中...'
		});
		$.getJSON(urlPerfix+"/getEchartPrice.do?days=7", function(d) {

			if(d.resultCode!=200){
				return;
			}
			var price_week_x = d.content.x;
			var price_week_y = d.content.y;
			var price_week_y_max = d.content.max;
			var price_week_y_min = d.content.min;

			Gold7day.setOption({

				//====
				tooltip : {
					trigger: 'axis',
					formatter: function(params) {
						return params[0].value + '元/克' + '<p style="color:#FF9906;font-size: 12px">' + ' '+ params[0].seriesName + params[0].name + '</p>';
					},
					showDelay: 0,
					hideDelay: 100,
					backgroundColor:"#fff",
					borderColor:"#FFEACB;",
					borderRadius:"3",
					borderWidth:"1",
					textStyle:{color:'#F56E00'},
					axisPointer : {
						type : 'line',
						lineStyle: {
							color: '#ffcc9f',
							width: 1,
							type: 'solid'
						}
					}
				},
				grid:{
					x:'50',
					y:'15',
					x2:'20',
					y2:'35',
					borderWidth: 0
				},
				calculable:false,
				animation: {
					addDataAnimation:true,
					animationThreshold:true,
					animationDuration:true,
					animationDurationUpdate:true,
					animationEasing:true
				},
				xAxis : [
					{
						type : 'category',
						scale : true,
						splitNumber: 29,
						boundaryGap : false,
						axisLabel : {
							textStyle:{
								fontFamily:'微软雅黑',
								color:'#999',
								fontSize : 14
							},
							margin:13,
							interval:24,
							//轴标文字处理
							formatter: function(value){
								var date = value.split(' ')[0].split('-')[2];
								return date + "日";
							}
						},
						axisLine: {
							lineStyle: {
								color: "#efefef",
								width: 1
							},
							interval:24
						},
						splitLine : {//隔列背景线
							show:false
						},
						axisTick : {    // 轴标记
							show:true,
							length: 6,
							interval:24,
							lineStyle: {
								color: "#efefef",
								width: 1
							}
						},
						data : price_week_x
					}
				],
				yAxis : [
					{
						type : 'value',
						scale: true,
						splitNumber:4,
						//max:price_week_y_max,
						//min:price_week_y_min,
						nameLocation: 'start',
						inverse: true,
						axisLabel : {
							textStyle:{
								fontFamily:'微软雅黑',
								color:'#999',
								fontSize : 14
							}
						},
						axisLine: {
							lineStyle: {
								color: "#fff",
								width: 0
							}
						},
						splitLine : {//隔行背景线
							show:true,
							lineStyle: {
								color: "#f4f4f4",
								type: 'solid',
								width: 1
							}
						}
					}
				],
				series : [
					{
						type:'line',
						symbol: 'circle',
						symbolSize: 4,
						itemStyle: {
							normal: {
								areaStyle: {
									color : (function (){
										if(window.navigator.userAgent.indexOf('MSIE 7') != -1 || window.navigator.userAgent.indexOf('MSIE 8') != -1)
										{
											return "rgba(255,237,210,0.5)";
										}
										var zrColor = require('zrender/tool/color');
										return zrColor.getLinearGradient(
											0, 100, 0, 240,//折线渐变色宽高比
											[[0, 'rgba(255,237,210,0.6)'],[0.8, 'rgba(255,237,210,0)']]
										)
									})()
								},
								lineStyle: {
									width: 1,
									color: "#ff730c"
								},
								label : {
									show: false,
									position: 'top',
									formatter: '{c} '
								},
								color: 'rgba(0,0,0,0)',
								borderWidth:2,
								borderColor:"rgba(0,0,0,0)"
							},
							emphasis: {
								color: '#fd9728',
								borderWidth:2,
								borderColor:"#fff",
								label : {
									show: true,
									position: 'inside',
									textStyle : {
										fontSize : '0',
										color: 'rgba(0,0,0,0)'
									}
								}
							}
						},
						data: price_week_y
					}
				]
				//====

			});
			Gold7day.hideLoading();
		});


		var Gold30Day = ec.init(document.getElementById("gold30Day"));
		Gold30Day.showLoading({
			text : '正在努力的读取数据中...'
		});
		$.getJSON(urlPerfix+"/getEchartPrice.do?days=30", function(d) {
			if(d.resultCode!=200){
				return;
			}
			var price_month_x = d.content.x;
			var price_month_y = d.content.y;
			var price_month_y_max = d.content.max;
			var price_month_y_min = d.content.min;

			Gold30Day.setOption({
				tooltip : {
					trigger: 'axis',
					showDelay: 0,
					hideDelay: 100,
					formatter: function(params) {
						return params[0].value + '元/克' + '<p style="color:#FF9906;font-size: 12px">' + ' '+ params[0].seriesName + params[0].name + '</p>';
					},
					backgroundColor:"#fff",
					borderColor:"#FFEACB;",
					borderRadius:"3",
					borderWidth:"1",
					textStyle:{color:'#F56E00'},
					axisPointer : {
						type : 'line',
						lineStyle: {
							color: '#ffcc9f',
							width: 1,
							type: 'solid'
						}
					}
				},
				grid:{
					x:'50',
					y:'15',
					x2:'20',
					y2:'35',
					borderWidth: 0
				},
				calculable:false,
				animation: {
					addDataAnimation:true,
					animationThreshold:true,
					animationDuration:true,
					animationDurationUpdate:true,
					animationEasing:true
				},
				xAxis : [
					{
						type : 'category',
						boundaryGap : false,
						axisLabel : {
							textStyle:{
								fontFamily:'微软雅黑',
								color:'#999',
								fontSize : 14
							},
							margin:13,
							interval:26,
							//轴标文字处理
							formatter: function(value){
								var date = value.split(' ')[0].split('-')[2];
								return date + "日";
							}
						},
						axisLine: {
							lineStyle: {
								color: "#efefef",
								width: 1
							},
							interval:46
						},
						splitLine : {//隔列背景线
							show:false
						},
						axisTick : {    // 轴标记
							show:true,
							length: 6,
							interval:26,
							lineStyle: {
								color: "#efefef",
								width: 1
							}
						},
						data : price_month_x
					}
				],
				yAxis : [
					{
						type : 'value',
						scale: true,
						splitNumber:4,
						//max:price_month_y_max,
						//min:price_month_y_min,
						nameLocation: 'start',
						inverse: true,
						axisLabel : {
							textStyle:{
								fontFamily:'微软雅黑',
								color:'#999',
								fontSize : 14
							}
						},
						axisLine: {
							lineStyle: {
								color: "#fff",
								width: 0
							}
						},
						splitLine : {//隔行背景线
							show:true,
							lineStyle: {
								color: "#f4f4f4",
								type: 'solid',
								width: 1
							}
						}
					}
				],
				series : [
					{
						type:'line',
						symbol: 'circle',
						symbolSize: 4,
						itemStyle: {
							normal: {
								areaStyle: {
									color : (function (){
										if(window.navigator.userAgent.indexOf('MSIE 7') != -1 || window.navigator.userAgent.indexOf('MSIE 8') != -1)
										{
											return "rgba(255,237,210,0.5)";
										}
										var zrColor = require('zrender/tool/color');
										return zrColor.getLinearGradient(
											0, 100, 0, 240,//折线渐变色宽高比
											[[0, 'rgba(255,237,210,0.6)'],[0.8, 'rgba(255,237,210,0)']]
										)
									})()
								},
								lineStyle: {
									width: 1,
									color: "#ff730c"
								},
								color: 'rgba(0,0,0,0)',
								borderWidth:2,
								borderColor:"rgba(0,0,0,0)"
							},
							emphasis: {
								color: '#fd9728',
								borderWidth:2,
								borderColor:"#fff",
								label : {
									show: true,
									position: 'inside',
									textStyle : {
										fontSize : '0',
										color: 'rgba(0,0,0,0)'
									}
								}
							}
						},
						data: price_month_y
					}
				]
			});
			Gold30Day.hideLoading();
		});

	}
);


