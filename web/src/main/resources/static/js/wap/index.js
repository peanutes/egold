/**
 * Wap 首页
 */

getAppConfig();//加载配置

//高显示当前页
$(function () {
	$(".fix-bar-ul li").eq(0).addClass("on");
});

//焦点图显示
$(function () {
	$.post(getApiUrl("/cms/cms/banner"),
		{
			code: 'WAP_HOME_BANNER'
		},
		function (result) {
			if (isReturnSuccess(result)) {
				var i, resu = result.data, html = "", duplicateEnd = "", duplicateStart = "";

				if (resu.length > 0) {
					for (i = 0; i < resu.length; i++) {
						html += '<div class="mui-slider-item"><a href="' + resu[i].url + '"><img src="' + resu[i].img + '"/></a></div>';
					}

					//支持循环，需要重复图片节点
					duplicateStart += '<div class="mui-slider-item mui-slider-item-duplicate"><a href="' + resu[0].url + '"><img src="' + resu[0].img + '"/></a></div>';
					duplicateEnd += '<div class="mui-slider-item mui-slider-item-duplicate"><a href="' + resu[resu.length - 1].url + '"><img src="' + resu[resu.length - 1].img + '"/></a></div>';

					$("#focus-picture").html(duplicateEnd + html + duplicateStart);

					//焦点图设置
					var gallery = mui('.mui-slider');
					gallery.slider({
						interval: 5000 //自动轮播周期，若为0则不自动播放，默认为0；
					});
				}
			}
		}
	);
});

//滚动公告显示
$(function () {
	$(".close_notice").on("click", function () {
		hideNotice();
	});

	if (needToShowNotice()) {
		showNotice();
	}

	function needToShowNotice() {
		return localStorage.displayNotice === undefined || localStorage.displayNotice === "Y";
	}

	function hideNotice() {
		$("#notice").remove();
		localStorage.displayNotice = "N";
	}

	function showNotice() {
		//读取公告
		$.post(getApiUrl("/sys/notice/list"), {
				noticeStatus: 2,
				page: systemConfig.defaultPage,
				size: systemConfig.defaultPageSize
			},
			function (result) {
				if (isReturnSuccess(result)) {
					var i, resu, html = "";
					if (null != result.data && null != result.data.list) {
						resu = result.data.list;

						if (resu.length > 0) {
							for (i = 0; i < resu.length; i++) {
								html += '<li>';
								html += '	<a href="' + systemConfig.url + '/sys/notice/detail?id=' + resu[i].id + '">' + resu[i].noticeTitle + '</a>';
								html += '</li>';
							}

							$("#notice_list").html(html);

							//显示公告
							$("#notice").show();

							localStorage.displayNotice = "Y";

							//滚动播放公告
							setInterval(function () {
								if (needToShowNotice()) {
									var ul = $('.notice_titles ul'),
										first = ul.children().first();

									first.clone(true).appendTo(ul);
									first.remove();
								}

							}, "4500");
						}

					}
				}
			}
		);

	}

	//查看保单样本
	$(".check_bill_sample a").click(function () {
		$(this).attr("href", getLocalStorageItem("accountAgreementUrl", "/wap/index"));
	});
});
