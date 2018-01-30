/**
 * validator报错方式替换
 * 更换报错方式为tooltip，3秒后隐藏
 * 验证成功后隐藏提示
 */
$.extend($.validator.defaults, {
	errorPlacement : function(error, element) {
		element.tooltip('destroy');
		setTimeout(function() {
			element.tooltip({
				"title" : error.html(),
				"placement" : "top",
				"trigger" : "manual"
			});
			element.tooltip('show');
		}, 300);
		setTimeout(function() {
			element.tooltip('hide');
		}, 3000);
	},
	success : function(error, element) {
		$(element).tooltip('destroy');
	}
});
