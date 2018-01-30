//系统配置
var fullAddress = location.href, protocolPrefix = fullAddress.substring(0, fullAddress.indexOf("//") + 2);

var systemConfig = {
	isTesting: false,
	url: protocolPrefix + window.location.host,
	refreshPriceDuration: 1000 * 2,
	successCode: 200,
	BUS_NO_LOGIN: 55001,// 没有登陆
	BUS_NO_REAL_NAME: 55002,//没有实名认证
	BUS_NO_BIND_CARD: 55003,//没有绑卡
	BUS_NO_DEAL_PWD: 55004,//没有设置交易密码
	BUS_TOKEN_EXPIRED: 55005,//登陆过期，需要重新登陆
	defaultPage: 1,
	defaultPageSize: 999,
	PAGE_SIZE:10,
	smsDelay: 60,//短信验证码间隔时间
	CURRENT_DEPOSIT: "CURRENT_DEPOSIT",//活期金
	TERM_DEPOSIT: "TERM_DEPOSIT",//定期金
	RISE_FALL: "RISE_FALL",//看涨跌
	urlTesting: "http://192.168.50.124:8181",
	secretCode: "faldskrqewrqwpi231028",
	queryString4Pc: "a=1&terminalType=pc&terminalId=pc&callTimestamp=" + new Date().getTime() + "&sign=1&token=1",//生产环境设置成只有a=1
	queryString4Wap: "a=1&terminalType=wap&terminalId=wap&callTimestamp=" + new Date().getTime() + "&sign=1&token=1",//生产环境设置成只有a=1
	queryString4Mall: "a=1&terminalType=app&terminalId=app&callTimestamp=" + new Date().getTime() + "&sign=1"//生产环境设置成只有a=1
}

//返回完整的api地址(Common)
function getApiUrl4Common(serviceAddress) {
	var queryString = systemConfig.queryString4Wap;
	if (serviceAddress.indexOf("?") < 0) {
		queryString = "?" + queryString;
	} else {
		queryString = "&" + queryString;
	}

	return systemConfig.url + serviceAddress + queryString;
}

//加载系统字典配置,正常情况下仅加载一次
if (null == localStorage.getItem("hasLoadApplicationConfig")) {
	getAppConfig();
}

//获取系统字典配置
function getAppConfig() {
	$.ajax({
		type: "POST",
		cache: false,
		data: null,
		async: true,//使用异步加载
		url: getApiUrl4Common("/conf/conf/appConfig"),
		success: function (result) {
			if (isReturnSuccess(result)) {
				var resultData = result.data;

				for (var p in resultData) {
					localStorage.setItem(p, resultData[p]);
				}

				localStorage.setItem("hasLoadApplicationConfig", "true");

			}
		}, error: function (result) {
			console.log("获取配置失败.");
		}
	});
}

//获取LocalStorage的值,如果没有,则先调用接口加载(同步加载)
function getLocalStorageItem(itemKey, defaultValue) {
	var itemValue = localStorage.getItem(itemKey);
	if ("null" == itemValue || !itemValue) {
		$.ajax({
			type: "POST",
			cache: false,
			data: null,
			async: false,//使用同步返回
			url: getApiUrl("/conf/conf/appConfig"),
			success: function (result) {
				if (isReturnSuccess(result)) {
					var resultData = result.data;

					for (var p in resultData) {
						localStorage.setItem(p, resultData[p]);
					}

					localStorage.setItem("hasLoadApplicationConfig", "true");

				}
			}, error: function (result) {
				console.log("获取配置失败.");
			}
		});
	}

	if ("null" == itemValue || !itemValue) {
		itemValue = defaultValue;
	}

	return itemValue;
}

//如果有.current_price, 则显示当前金价
$(function () {
	if ($(".current_price").length > 0) {
		currentGoldPrice();

		//定时刷新最新价格
		/*setInterval(function () {
			currentGoldPrice();
		}, 1000 * getLocalStorageItem("refreshPriceInterval", 60));*/

		window.setTimeout(function () {
			currentGoldPrice();
			window.setTimeout(arguments.callee, 1000 * getLocalStorageItem("refreshPriceInterval", 60));
		}, 1000 * getLocalStorageItem("refreshPriceInterval", 60));

	}
});

//点击实时刷新金价
$(function () {

	$(".refresh_price_img").on("click", function () {
		currentGoldPrice();
	});
});

//格式化百分比
function formatPercentage(s, n) {
	return formatMoney(s, 2);
}

//格式化黄金克重不带单位
function formatGold(s, n) {
	return formatMoney(s, 3);
}

//格式化黄金克重带单位
function formatGoldWithUnit(s, n) {
	return formatMoney(s, 3) + '克';
}

//格式化金额不带单位
function formatMoney(s, n) {
	n = n > 0 && n <= 20 ? n : 2;
	s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
	var l = s.split(".")[0].split("").reverse(),
		r = s.split(".")[1];
	t = "";
	for (i = 0; i < l.length; i++) {
		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
	}
	return t.split("").reverse().join("") + "." + r;
}

//格式化金额带单位
function formatMoneyWithUnit(s, n) {
	n = n > 0 && n <= 20 ? n : 2;
	s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
	var l = s.split(".")[0].split("").reverse(),
		r = s.split(".")[1];
	t = "";
	for (i = 0; i < l.length; i++) {
		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
	}
	return t.split("").reverse().join("") + "." + r + "元";
}

//反格式化金额
function unFormatMoney(s) {
	return parseFloat(s.replace(/[^\d\.-]/g, ""));
}

//点击显示各个维度tab的详细
$("ul.detail_tab li").on("click", function () {
	var type = $(this).attr("id");

	$("ul.detail_tab li").removeClass("active");
	$("#" + type).addClass("active");

	$(".list_control").hide();

	$("#" + type + "_list").show();

});



//返回完整的api地址(商城用)
function getApiUrl4AppMall(serviceAddress) {
	var queryString = systemConfig.queryString4Mall;
	if (serviceAddress.indexOf("?") < 0) {
		queryString = "?" + queryString;
	} else {
		queryString = "&" + queryString;
	}

	return systemConfig.url + serviceAddress + queryString;
}

//返回上一页
$(function () {
	$(".top_title .return").on("click", function () {
		backToLastPage();
	})
});

//返回上一页专门为内容而设置
$(function () {
	$(".top_title_4_app .return").on("click", function () {
		backToLastPage();
	})
});

//判断后台调用接口是否返回了正确的code，否则就是有异常，需要进一步处理
function isReturnSuccess(result) {
	return null != result && result.code == systemConfig.successCode
}

//判断用户是否未登录
function isUserNotLogin(result) {
	return null != result && (result.code == systemConfig.BUS_NO_LOGIN || result.code == systemConfig.BUS_TOKEN_EXPIRED);
}

//判断用户是否未绑卡
function isUserNotBindBankcard(result) {
	return null != result && result.code == systemConfig.BUS_NO_BIND_CARD;
}

//判断用户是否未实名
function isUserNotSetRealName(result) {
	return null != result && result.code == systemConfig.BUS_NO_REAL_NAME;
}

//把需要清除功能的输入框加入输入清除功能
$(function () {
	$("input.need_input_clear").focus(function () {
		showOrHideClearButton($(this))
	});
	$("input.need_input_clear").blur(function () {
		showOrHideClearButton($(this))
	});

	$("input.need_input_clear").keyup(function () {
		showOrHideClearButton($(this))
	});

	$(".input_clear").click(function () {
		$(this).parent().find('input.need_input_clear').val('');
		$(this).hide();
	});

	function showOrHideClearButton(jqObject) {
		if (jqObject.val() == '') {
			jqObject.parent().children(".input_clear").hide();
		} else {
			jqObject.parent().children(".input_clear").show();
		}
	}

})

//点击tab配置
$("ul.display_tab li").on("click", function () {
	var type = $(this).attr("id");

	$("ul.display_tab li").removeClass("active");
	$(this).addClass("active");

	$(".list_control").hide();

	$("#" + type + "_content").show();

});

//列表显示页显示隐藏切换
$(function () {
	$(".list_title li").on('click', function () {
		$(this).find(".list_title_level2").toggle();
	})
});

//展示当前金价
function currentGoldPrice() {
	$.post(getApiUrl("/gold/price/realTimePrice"),
		{},
		function (result) {
			if (isReturnSuccess(result)) {
				if (null != result && result.code == systemConfig.successCode) {
					$(".current_price").hide();

					$(".current_price").html(formatMoney(result.data));

					$(".current_price").fadeIn(2000);

					localStorage.currentPrice = formatMoney(result.data);
				}
			}

		}
	)
}

//返回上一页
function backToLastPage() {
	window.history.back();//如果要强行刷新的话就是：window.history.back();location.reload();
}

//跳转页面
function goToPage(page) {
	window.location = page;
}

//得到尾号
function getBankCardLastNumber(bankCard) {
	return bankCard.substr(bankCard.length - 4);
}

//置顶
//$(function () {
//	$(document).scroll(function () {
////		var a=getTransform("#index-top");
//		if ($(document).scrollTop() > 0) { //|| $("#index-top").
//			$('#totop').fadeIn(300);
//		} else {
//			$("#totop").fadeOut(300);
//		}
//	});
//	$('#totop').click(function () {
//		$('html,body').animate({scrollTop: '0px'}, 300);
//	});
//});

//表单验证
function onlyNum(t) {
	t.value = t.value.replace(/[^\d]/g, '');
}
function is_tel(v) {
	return /^1[3|4|5|7|8]\d{9}$/.test(v);
}
function is_pass(v) {
	return /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$/.test(v);//六到十二位数字字母
}

var domain = '//' + window.location.host; //默认域名
//从url中获取参数
function getParam(paramName) {
	var p = "";
	if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
		var arr = decodeURIComponent(this.location.search).substring(1, this.location.search.length).split("&");
		$.each(arr, function (k, v) {
			if (v.indexOf("=") > 0 && v.split("=")[0].toLowerCase() == paramName.toLowerCase()) {
				p = v.split("=")[1];
				return false;
			}
		});
	}
	return p;
}

function atLeast(t, num) {
	if (t.value < num) {
		t.value = num;
	}
}

function atMost(t, num) {
	if (t.value > num) {
		t.value = num;
	}
}

//金额
function onlyAmountForMoney(obj) {
	var a = [
		['^0(\\d+)$', '$1'], //禁止录入整数部分两位以上，但首位为0
		['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
		['\\.(\\d?)\\.+', '.$1'], //禁止录入两个以上的点
		['^(\\d+\\.\\d{2}).+', '$1'] //禁止录入小数点后两位以上
	];
	for (i = 0; i < a.length; i++) {
		var reg = new RegExp(a[i][0]);
		obj.value = obj.value.replace(reg, a[i][1]);
	}
}

//克重
function onlyAmountForWeight(obj) {
	var a = [
		['^0(\\d+)$', '$1'], //禁止录入整数部分两位以上，但首位为0
		['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
		['\\.(\\d?)\\.+', '.$1'], //禁止录入两个以上的点
		['^(\\d+\\.\\d{3}).+', '$1'] //禁止录入小数点后3位以上
	];
	for (i = 0; i < a.length; i++) {
		var reg = new RegExp(a[i][0]);
		obj.value = obj.value.replace(reg, a[i][1]);
	}
}

//英文字母
function is_en(v) {
	return /^[A-Za-z]+$/.test(v);
}

//英文字母和数字 6到20位
function is_enAndnum(v) {
	return /^[A-Za-z0-9]{6,20}$/.test(v);
}

//手机号码
function is_mobile(v) {
	return /^(13|14|15|17|18)[0-9]{9}$/.test(v);
}

//email
function is_email(v) {
	return /^(\w-*\.*)+@(\w-?)+(\.\w{2,10})+$/.test(v);
}

//固定电话
function is_home_tel(v) {
	return /^[0-9]{1,4}(-|[0-9])\d{5,15}$/.test(v);
}

//合法密码
function is_valid_password(v) {
	//return /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$/.test(v);//六至三十二位数字字母
	return /^[0-9A-Za-z]{6,32}$/.test(v);//六至三十二位或者数字字母
}

//中文
function is_chinese(v) {
	return /^([\u4e00-\u9fa5])([\u4e00-\u9fa5·]){0,23}([\u4e00-\u9fa5])$/i.test(v);
}

function is_consignee(v) {
	return /^(([\u4e00-\u9fa5])([\u4e00-\u9fa5·]){0,10}([\u4e00-\u9fa5]))|([a-zA-Z]([a-zA-Z]|\s){2,20})$/i.test(v);
}

//字符串长度
function getStringLength(str) {
	if (!str) {
		return;
	}
	var bytesCount = 0;
	for (var i = 0; i < str.length; i++) {
		var c = str.charAt(i);
		if (/^[\u0000-\u00ff]$/.test(c)) {
			bytesCount += 1;
		} else {
			bytesCount += 2;
		}
	}
	return bytesCount;
}

//测试
$(function () {

	//var queryString = "terminalType=1&terminalId=1";
	//queryString = queryString+"&callTimestamp=" + new Date().getTime()
	//var sign = getSha1Text(queryString);
	//queryString = queryString + "&sign="+sign;
	//console.log(queryString)
	//
	//$.post(systemConfig.urlTesting + "/invest/financial/product/homeProducts?" +queryString,
	//	{
	//
	//	},
	//	function (data) {
	//		var i, html = "";
	//		console.log(data)
	//
	//	}
	//)
	;
})

function getSha1Text(queryString) {
	//queryString = queryString + "&callTimestamp=" + new Date().getTime();
	//为了测试，加个这些参数统一调用
	//queryString =  queryString + "&terminalType=poTesting1&terminalId=poTesting2"
	var map = queryString.split("&");
	var parameterArr = map.sort();
	var text = "";
	for (var i = 0; i < map.length; i++) {
		if (i != 0) {
			text = text + "&";
		}
		text = text + map[i];
	}

	return _toSha1(text + systemConfig.secretCode);
}

function _toSha1(text) {
	var data = new Uint8Array(_encodeUTF8(text));
	var result = _sha1(data);
	var hex = Array.prototype.map.call(result, function (e) {
		return (e < 16 ? "0" : "") + e.toString(16);
	}).join("");

	return hex;
}

function _encodeUTF8(s) {
	var i, r = [], c, x;
	for (i = 0; i < s.length; i++)
		if ((c = s.charCodeAt(i)) < 0x80) r.push(c);
		else if (c < 0x800) r.push(0xC0 + (c >> 6 & 0x1F), 0x80 + (c & 0x3F));
		else {
			if ((x = c ^ 0xD800) >> 10 == 0) //对四字节UTF-16转换为Unicode
				c = (x << 10) + (s.charCodeAt(++i) ^ 0xDC00) + 0x10000,
					r.push(0xF0 + (c >> 18 & 0x7), 0x80 + (c >> 12 & 0x3F));
			else r.push(0xE0 + (c >> 12 & 0xF));
			r.push(0x80 + (c >> 6 & 0x3F), 0x80 + (c & 0x3F));
		}
	;
	return r;
};

function _sha1(data) {
	/**************************************************
	 Author：次碳酸钴（admin@web-tinker.com）
	 Input：Uint8Array
	 Output：Uint8Array
	 **************************************************/
	var i, j, t;
	var l = ((data.length + 8) >>> 6 << 4) + 16, s = new Uint8Array(l << 2);
	s.set(new Uint8Array(data.buffer)), s = new Uint32Array(s.buffer);
	for (t = new DataView(s.buffer), i = 0; i < l; i++)s[i] = t.getUint32(i << 2);
	s[data.length >> 2] |= 0x80 << (24 - (data.length & 3) * 8);
	s[l - 1] = data.length << 3;
	var w = [], f = [
			function () {
				return m[1] & m[2] | ~m[1] & m[3];
			},
			function () {
				return m[1] ^ m[2] ^ m[3];
			},
			function () {
				return m[1] & m[2] | m[1] & m[3] | m[2] & m[3];
			},
			function () {
				return m[1] ^ m[2] ^ m[3];
			}
		], rol = function (n, c) {
			return n << c | n >>> (32 - c);
		},
		k = [1518500249, 1859775393, -1894007588, -899497514],
		m = [1732584193, -271733879, null, null, -1009589776];
	m[2] = ~m[0], m[3] = ~m[1];
	for (i = 0; i < s.length; i += 16) {
		var o = m.slice(0);
		for (j = 0; j < 80; j++)
			w[j] = j < 16 ? s[i + j] : rol(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1),
				t = rol(m[0], 5) + f[j / 20 | 0]() + m[4] + w[j] + k[j / 20 | 0] | 0,
				m[1] = rol(m[1], 30), m.pop(), m.unshift(t);
		for (j = 0; j < 5; j++)m[j] = m[j] + o[j] | 0;
	}
	;
	t = new DataView(new Uint32Array(m).buffer);
	for (var i = 0; i < 5; i++)m[i] = t.getUint32(i << 2);
	return new Uint8Array(new Uint32Array(m).buffer);
};

function _encodeUTF8(s) {
	var i, r = [], c, x;
	for (i = 0; i < s.length; i++)
		if ((c = s.charCodeAt(i)) < 0x80) r.push(c);
		else if (c < 0x800) r.push(0xC0 + (c >> 6 & 0x1F), 0x80 + (c & 0x3F));
		else {
			if ((x = c ^ 0xD800) >> 10 == 0) //对四字节UTF-16转换为Unicode
				c = (x << 10) + (s.charCodeAt(++i) ^ 0xDC00) + 0x10000,
					r.push(0xF0 + (c >> 18 & 0x7), 0x80 + (c >> 12 & 0x3F));
			else r.push(0xE0 + (c >> 12 & 0xF));
			r.push(0x80 + (c >> 6 & 0x3F), 0x80 + (c & 0x3F));
		}
	;
	return r;
};

function isBlank(obj) {
	return (!obj || $.trim(obj) === "");
}

function isNotBlank(obj) {
	return !isBlank(obj);
}

function disableElement(jqObject) {
	if (null != jqObject) {
		jqObject.attr("disabled", true);
	}
}

function enableElement(jqObject) {
	if (null != jqObject) {
		jqObject.removeAttr("disabled");
	}
}

function disableElementAndSetText(jqObject, text2set) {
	if (null != jqObject) {
		jqObject.attr("disabled", true);
	}
	if (isNotBlank(text2set)) {
		jqObject.text(text2set);
	}
}

function enableElementAndSetText(jqObject, text2set) {
	if (null != jqObject) {
		jqObject.removeAttr("disabled");
	}

	if (isNotBlank(text2set)) {
		jqObject.text(text2set);
	}
}

function clearErrorMessage() {
	$(".errorPlaceHolder").html("");
}

//扩展jquery方法
//(function($){
//	$.isBlank = function(obj){
//		return(!obj || $.trim(obj) === "");
//	};
//
//	$.isNotBlank = function(obj){
//		return !$.isBlank(obj);
//	};
//})(jQuery);

function showLoading(displayContent) {
	displayContent = displayContent || "加载中";
	$(".loading").show();
	$(".loadingContent").text(displayContent + ", 请稍候...");
}

function hideLoading() {
	$(".loading").hide();
}

function showLoadingAndDisableButton(displayContent, jqBtnObject) {
	showLoading(displayContent);

	disableElement(jqBtnObject);
}

function hideLoadingAndEnableButton(jqBtnObject) {
	hideLoading();

	enableElement(jqBtnObject);
}

//滚动显示顶部
/*$(function () {
 $(".wap-header").posfixed({
 distance: 0,
 pos: "top",
 type: "while",
 hide: false
 })
 });*/

////回到顶部
//$('.go-to-top').posfixed({
//	distance : 200,
//	direction : 'bottom',
//	type : 'always',
//	tag : {
//		obj : $('body'),
//		direction : 'right',
//		distance : 20
//	},
//	hide : true
//});



//获取终端类型
function getTerminalType() {
	var terminalType = "pc";

	if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
		terminalType = "ios";
	} else if (/(Android)/i.test(navigator.userAgent)) {
		terminalType = "android";
	} else {
		terminalType = "pc";
	}

	return terminalType;
}

//根据不同的终端类型下载app
$(function () {
	var terminalType = getTerminalType(), apkUrl;

	$(".download-app").click(function () {
		//if ("iphone" == terminalType) {
		//	window.location = getLocalStorageItem("iphoneAppDownloadUrl", "/");
		//} else {
			$.post(getApiUrl("/conf/conf/appVersion"), {
					appType: terminalType
				},
				function (result) {
					if (isReturnSuccess(result)) {
					if (null != result.data) {
						apkUrl = result.data.apkUrl;
					}

					apkUrl = apkUrl || "about:blank";

						window.location = apkUrl;
					}

		}
			)
		//}

	})

})