//获取支付公司的动态密码
function getBankCheckCode(mobile, bankCardNo) {
	var getCheckCodeBtn = $("#getCheckCodeBtn");

	getCheckCodeBtn.on("click", function () {
		//调用监听
		monitor(getCheckCodeBtn);
		//倒计时效果  getCode回调函数  获取验证码api
		countDown(getCheckCodeBtn, getCheckCode);
	});

	//调用监听
	monitor(getCheckCodeBtn);
	//倒计时效果  getCode回调函数  获取验证码api
	countDown(getCheckCodeBtn, getCheckCode);

	function getCheckCode() {
		$.post(getApiUrl("/member/bankcard/bindCardRequest"), {
				mobile: mobile,
				bankCardNo: bankCardNo
			}, function (result) {
				if (isReturnSuccess(result)) {
					console.log("验证码已发送，倒计时");
					mui.toast("验证码已发送至您的手机，请注意查收并填入");
					console.log(result.data);
					$("#bindCardRequestNo").val(result.data);
				} else {
					mui.toast(result.msg);
					$(".errorPlaceHolder").html(result.msg);
				}

			}
		)
	}
}

function getCheckCode(mobile, smsTplType) {
	var getCheckCodeBtn = $("#getCheckCodeBtn");

	getCheckCodeBtn.on("click", function () {
		//调用监听
		monitor(getCheckCodeBtn);
		//倒计时效果  getCode回调函数  获取验证码api
		countDown(getCheckCodeBtn, getCheckCode);
	});

	//调用监听
	monitor(getCheckCodeBtn);
	//倒计时效果  getCode回调函数  获取验证码api
	countDown(getCheckCodeBtn, getCheckCode);

	function getCheckCode() {
		$.post(getApiUrl("/sys/sms/send"), {
				mobile: mobile,
				smsTplType: smsTplType
			}, function (result) {
				if (isReturnSuccess(result)) {
					console.log("验证码已发送，倒计时");
					mui.toast("验证码已发送至您的手机，请注意查收并填入");
				} else {
					mui.toast(result.msg);
					$(".errorPlaceHolder").html(result.msg);
				}

			}
		)
	}
}

//防止页面刷新倒计时失效
/**
 *
 * @param {Object} obj  获取验证码按钮
 */
function monitor(obj) {
	var LocalDelay = getLocalDelay();
	if (LocalDelay.time != null) {
		var timeLine = parseInt((new Date().getTime() - LocalDelay.time) / 1000);
		if (timeLine > LocalDelay.delay) {
			console.log("过期");
		} else {
			_delay = LocalDelay.delay - timeLine;
			obj.text(_delay + "秒后重新发送");
			obj[0].disabled = true;
			var timer = setInterval(function () {
				if (_delay > 1) {
					_delay--;
					obj.text(_delay + "秒后重新发送");
					setLocalDelay(_delay);
				} else {
					clearInterval(timer);
					obj.text("获取验证码");
					obj[0].disabled = false;
				}
			}, 1000);
		}
	}
};

//倒计时效果
/**
 *
 * @param {Object} obj 获取验证码按钮
 * @param {Function} callback  获取验证码接口函数
 */
function countDown(obj, callback) {
	console.log(obj)
	if (obj.text() == "获取验证码") {
		var _delay = systemConfig.smsDelay;
		var delay = _delay;
		obj.text(_delay + "秒后重新发送");
		obj[0].disabled = true;
		var timer = setInterval(function () {
			if (delay > 1) {
				delay--;
				obj.html(delay + "秒后重新发送");
				setLocalDelay(delay);
			} else {
				clearInterval(timer);
				obj.text("获取验证码");
				obj[0].disabled = false;
			}
		}, 1000);

		callback();
	} else {
		return false;
	}
}

//设置setLocalDelay
function setLocalDelay(delay) {
	//location.href作为页面的唯一标识，可能一个项目中会有很多页面需要获取验证码。
	sessionStorage.setItem("delay_" + location.href, delay);
	sessionStorage.setItem("time_" + location.href, new Date().getTime());
}

//getLocalDelay()
function getLocalDelay() {
	var LocalDelay = {};
	LocalDelay.delay = sessionStorage.getItem("delay_" + location.href);
	LocalDelay.time = sessionStorage.getItem("time_" + location.href);
	return LocalDelay;
}