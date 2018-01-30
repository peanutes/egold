$(function () {
	var mobile = "";
	//实名认证按纽
	$("#realNameAuthBtn").click(function () {

		clearErrorMessage();

		var realName = $("#realName").val(),
			idCard = $("#idCard").val();

		if (!validateRealNameAuthForm()) {
			return false;
		}

		showLoading("验证中","#realNameAuthBtn");

		$.post(getApiUrl("/member/member/realNameAuth"), {
				realName: realName,
				idCard: idCard
			}, function (result) {
				console.log(result)
				if (isReturnSuccess(result)) {
					console.log("success real name auth");
					$(".for_real_name_success").show();
					$(".for_real_name").hide();
					hideLoading("#realNameAuthBtn");
				} else {
					$(".errorPlaceHolder").html(result.msg);
					hideLoading("#realNameAuthBtn");
				}
			}
		)

	});

	$("#toBankCardBindingBtn").click(function () {
		goToPage("/user/toBankCardBinding");
	});

	$(".realNameSkip").click(function () {
		goToPage("/wap/mine");
	});


});

function validateRealNameAuthForm() {
	var realName = $("#realName").val(),
		idCard = $("#idCard").val(),
		agree = $("#agree").is(":checked"),
		errorMessage,
		result = true;

	if (isBlank(realName)) {
		errorMessage = "请输入真实姓名";
		$("#realName").focus();
	} else if (isBlank(idCard)) {
		errorMessage = "请输入身份证号";
		$("#idCard").focus();
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
