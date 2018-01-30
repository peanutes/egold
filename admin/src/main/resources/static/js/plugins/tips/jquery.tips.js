/**
 * 窗口提示插件
 * 
 * @author ray02.liang
 * @param $
 * @use： 
 * 1、$.tips(message); 
 * 
 * 2、$.tips(message, options);
 * 例子：
 * options={
 *    time:3000,  // 动画时间
 *	  type:'success' // 提示类型  warning、success、info、danger ，默认是 warning
 * }
 * 
 * 3、$.tips(message, type);
 * 例子：
 * $.tips(message, 'success');
 * 
 */
(function($) {

	$.tips = function(message, options) {
		if (typeof options != "object") {
			options = {
				type : options
			}
		}
		return jtips(message, options);
	};

	jtips = function(message, options) {
		jtips.defaults.message = message;
		options = $.extend({}, jtips.defaults, options);
		return new Jtips(options);
	};

	Jtips = function(options) {
		$.extend(this, options);
		// 向dom添加元素
		var alertClass = this._alertClass();
		this.$el = $(this.template(alertClass)).appendTo(this.container);
		this.$css = this._center();
		this.$el.css(this.$css);
		// 增加动画
		this._animate();
	};

	Jtips.prototype = {

		_center : function() {
			var windowHeight = $(window).height();
			var windowWidth = $(window).width();
			var boxHeight = this.$el.outerHeight();
			var boxWidth = this.$el.find("#message").width();
			var topMargin = (windowHeight - boxHeight) / 2;
			var leftMargin = (windowWidth - boxWidth) / 2;
			var scrollTop = $(window).scrollTop();
			scrollTop = parseInt(scrollTop) + 100;
			var minMargin = 100;
			var style = {
				'top' : scrollTop,
				'left' : leftMargin
			};
			var defaultStyle = {
				'position' : 'absolute',
				'z-index' : '9999',
				'margin-left' : 'auto',
				'margin-right' : 'auto'
			};
			return $.extend(defaultStyle, style);
		},
		_animate : function() {
			var that = this;
			setTimeout(function() {
				that.$el.animate({
					left : 0,
					opacity : "hide"
				}, "slow", function() {
					that.$el.alert('close');
					that.$el.remove();
				});
			}, that.time);
		},
		_alertClass : function() {
			var type = this.type;
			if (type == "warning") {
				return "alert-warning";
			} else if (type == "success") {
				return "alert-success";
			} else if (type == "info") {
				return "alert-info";
			} else if (type == "danger") {
				return "alert-danger";
			}
			return "alert-warning";
		},
	}

	jtips.defaults = {
		message : 'Warning!',
		time : 3000,
		type : 'warning',
		template : function(alertClass) {
			return '<div class="alert ' + alertClass + ' alert-dismissible" role="alert">'
					+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close">'
					+ '<span aria-hidden="true">&times;</span></button><span id="message">'
					+ '<strong>' + jtips.defaults.message + '</strong></span>' + '</div>';
		},
		container : 'body'
	};

})(jQuery);