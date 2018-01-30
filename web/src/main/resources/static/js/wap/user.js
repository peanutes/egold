//注册或者登录
$(function () {
	var mobile = "";

	// 输入手机号码时候校验
	$("#mobile").keyup(function () {
		onlyNum(this);//直接去掉非数字

		mobile = $("#mobile").val();

		//如果手机号码合法，enable下一步
		if (isNotBlank(mobile) && is_mobile(mobile)) {
			enableElement($("#toRegisterOrLoginBtn"));
		} else {
			disableElement($("#toRegisterOrLoginBtn"));
		}
	});

	//点击下一步决定是进入登录还是发送验证码来注册
	$("#toRegisterOrLoginBtn").click(function () {

		disableElementAndSetText($("#toRegisterOrLoginBtn"), "提交中...");

		mobile = $("#mobile").val();

		//用户是否已经注册过
		$.post(getApiUrl("/member/member/isRegister"), {
				mobile: mobile
			}, function (result) {
				if (isReturnSuccess(result)) {
					//如果已注册则显示输入密码框
					//否则发送注册验证码，跳转输入验证码
					if (result.data == true) {
						$(".for_login").show();
						$(".for_checkcode").hide();
						$(".for_register_login").hide();
						$(".for_login_success").hide();

						console.log("已经注册，输入密码登录")
					} else {
						console.log("未注册，发送验证码")
						$(".for_checkcode").show();
						$(".for_register_login").hide();
						$(".for_login_success").hide();

						getCheckCode(mobile, 'REGISTER');
					}
				} else {
					mui.toast(result.msg);
					$(".errorPlaceHolder").html(result.msg);
					consolg.log("未知错误");
				}

				enableElement($("#toRegisterOrLoginBtn"));

			}
		)

	});

	//输入验证码时候校验
	$("#checkCode").keyup(function () {
		var checkCode = $("#checkCode").val();

		onlyNum(this);//直接去掉非数字

		if (isNotBlank(mobile) && is_mobile(mobile)) {
			enableElement($("#toRegisterOrLoginBtn"));
		} else {
			disableElement($("#toRegisterOrLoginBtn"));
		}
	});

	//输入密码时候校验
	$("#loginPassword").keyup(function () {
		var loginPassword = $("#loginPassword").val();

		if (isNotBlank(loginPassword)) {
			enableElement($("#toLoginBtn"));
		} else {
			disableElement($("#toLoginBtn"));
		}
	});

	//注册按纽
	$("#toRegisterBtn").click(function () {
		var password = $("#password").val(),
			rePassword = $("#repassword").val(),
			refereeMobile = $("#refereeMobile").val(),
			agree = $("#agree").is(":checked"),
			checkCode = $("#checkCode").val();

		clearErrorMessage();

		if (!validateRegisterForm()) {
			return false;
		}

		disableElementAndSetText($("#toRegisterBtn"), "注册中...");

		$.post(getApiUrl("/member/member/register"), {
				mobile: mobile,
				password: password,
				smsCode: checkCode,
				refereeMobile: refereeMobile
			}, function (result) {
				console.log(result)
				if (isReturnSuccess(result)) {
					console.log("注册成功");
					$(".for_checkcode").show();
					$(".for_register_login").hide();
					$(".for_login_success").hide();

					goToPage("/wap/mine");

				} else {
					$(".errorPlaceHolder").html(result.msg);
					enableElementAndSetText($("#toRegisterBtn"), "注册");
				}
			}
		)

	});

	//登录按纽
	$("#toLoginBtn").click(function () {
		clearErrorMessage();
		var mobile = $("#mobile").val(),
			loginPassword = $("#loginPassword").val();

		if (!validateLoginForm()) {
			return false;
		}

		disableElementAndSetText($("#toLoginBtn"), "登录中...");

		$.post(getApiUrl("/member/member/login"), {
				userName: mobile,
				password: loginPassword
			}, function (result) {
				if (isReturnSuccess(result)) {
					goToPage("/wap/mine");
				} else {
					$(".errorPlaceHolder").html(result.msg);
					enableElementAndSetText($("#toLoginBtn"), "登录");

				}

			}
		)

	});

	//验证注册表单
	function validateRegisterForm() {
		var password = $("#password").val(),
			rePassword = $("#repassword").val(),
			agree = $("#agree").is(":checked"),
			checkCode = $("#checkCode").val(),
			result = true, errorMessage;

		if (isBlank(checkCode)) {
			errorMessage = "请输入手机验证码";
			$("#checkCode").focus();
		} else if (isBlank(password)) {
			errorMessage = "请输入密码";
			$("#password").focus();
		} else if (!is_valid_password(password)) {
			errorMessage = "密码必须6~32位数字字母";
			$("#password").focus();
		} else if (isBlank(rePassword)) {
			errorMessage = "请再次输入密码";
			$("#repassword").focus();
		} else if (password != rePassword) {
			errorMessage = "两次输入密码不一致";
			$("#repassword").focus();
		} else if (!agree) {
			errorMessage = "请阅读并同意协议";
			$("#agree").focus();
		}

		if (isNotBlank(errorMessage)) {
			$(".errorPlaceHolder").html(errorMessage);
			result = false;
		}

		return result;
	}

	//验证登录表单
	function validateLoginForm() {
		var loginPassword = $("#loginPassword").val(),
			result = true, errorMessage;

		if (isBlank(loginPassword)) {
			//mui.toast("请输入密码");
			$("#loginPassword").focus();
			return false;
		}

		if (isBlank(loginPassword)) {
			errorMessage = "请输入密码";
			$("#loginPassword").focus();
		}

		if (isNotBlank(errorMessage)) {
			$(".errorPlaceHolder").html(errorMessage);
			result = false;
		}

		return result;
	}

});
