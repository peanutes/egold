$(function () {
	var mobile, bankCardNumber, checkCode;

	/**
	 * 输入银行卡号码时候校验
	 */
	$("#bankCardNumber").keyup(function () {
		onlyNum(this);

		bankCardNumber = $("#bankCardNumber").val();

		if (isNotBlank(bankCardNumber)) {
			enableElement($("#toBankCardTelephoneBtn"));
		} else {
			disableElement($("#toBankCardTelephoneBtn"));
		}
	});

	/**
	 * 输入手机号码时候校验
	 */
	$("#mobile").keyup(function () {
		onlyNum(this);

		mobile = $("#mobile").val();

		if (isNotBlank(mobile) && is_mobile(mobile)) {
			enableElement($("#toBankCardCheckCodeBtn"));
		} else {
			disableElement($("#toBankCardCheckCodeBtn"));
		}
	});

	/**
	 * 输入验证码时候校验
	 */
	$("#checkCode").keyup(function () {
		onlyNum(this);

		checkCode = $("#checkCode").val();

		if (isNotBlank(checkCode)) {
			enableElement($("#toBankCardBindingBtn"));
		} else {
			disableElement($("#toBankCardBindingBtn"));
		}
	});

	//Step1 :添加银行卡第一步按纽
	$("#toBankCardTelephoneBtn").click(function () {

		clearErrorMessage();

		var bankCardNumber = $("#bankCardNumber").val(),
			errorMessage;

		if (!validateBankCardNumberForm()) {
			return false;
		}

		showLoadingAndDisableButton("验证中", $("#toBankCardTelephoneBtn"));
		$.post(getApiUrl("/member/bankcard/bankCardCheck"), {
				bankCardNo: bankCardNumber
			}, function (result) {
				hideLoadingAndEnableButton($("#toBankCardTelephoneBtn"));

				if (isReturnSuccess(result)) {

					var htmlResult = "";
					$(".for_bank_card_number").hide();
					$(".for_bank_card_telephone").show();
					$(".for_bank_card_check_code").hide();
					$(".for_bank_card_binding_success").hide();
					htmlResult += '<img src="' + result.data.bankLogo + '"/>' + result.data.bankName + ':<span>尾号' + getBankCardLastNumber(bankCardNumber) + '</span>';
					$(".bank-card-info").html(htmlResult);
				} else {
					$(".errorPlaceHolder").html(result.msg);
				}

			}
		)

	});

	//获取验证码
	$("#toBankCardCheckCodeBtn").click(function () {
		clearErrorMessage();

		var mobile = $("#mobile").val(),
			bankCardNumber = $("#bankCardNumber").val(),
			errorMessage;

		if (!validateBankCardTelephoneForm()) {
			return false;
		} else {
			$(".for_bank_card_number").hide();
			$(".for_bank_card_telephone").hide();
			$(".for_bank_card_check_code").show();
			$(".for_bank_card_binding_success").hide();

			getBankCheckCode(mobile, bankCardNumber);

		}
	});

	//最后绑定按钮
	$("#toBankCardBindingBtn").click(function () {
		clearErrorMessage();

		var bankCardNumber = $("#bankCardNumber").val(),
			bindCardRequestNo = $("#bindCardRequestNo").val(),
			checkCode = $("#checkCode").val(),
			errorMessage;

		if (!validateBankCardCheckCodeForm()) {
			return false;
		}

		showLoadingAndDisableButton("绑定中", $("#toBankCardBindingBtn"));

		$.post(getApiUrl("/member/bankcard/bindCardConfirm"), {
				smsCode: checkCode,
				bindCardRequestNo: bindCardRequestNo
			}, function (result) {
				hideLoadingAndEnableButton($("#toBankCardBindingBtn"));

				if (isReturnSuccess(result)) {
					console.log("success setting bank card 2");
					$(".for_bank_card_number").hide();
					$(".for_bank_card_telephone").hide();
					$(".for_bank_card_check_code").hide();
					$(".for_bank_card_binding_success").show();

				} else {
					$(".errorPlaceHolder").html(result.msg);
				}
			}
		)

	});

	$("#toBuyGoldBtn").click(function () {
		goToPage("/wap/buyGold");
	});
})
;

//如果未实名,先让用户到实名认证
$("#toRealNameBtn").click(function () {
	goToPage("/user/toRealName")
});

function validateBankCardNumberForm() {
	var bankCardNumber = $("#bankCardNumber").val(),
		errorMessage,
		result = true;

	if (isBlank(bankCardNumber)) {
		$("#bankCardNumber").focus();
		errorMessage = "请输入银行卡号";
	}

	if (isNotBlank(errorMessage)) {
		$(".errorPlaceHolder").html(errorMessage);
		result = false;
	}

	return result;
}

function validateBankCardTelephoneForm() {
	var mobile = $("#mobile").val(),
		errorMessage,
		result = true;

	if (isBlank(mobile)) {
		$("#mobile").focus();
		errorMessage = "请输入预留手机号";
	} else if (!is_mobile(mobile)) {
		$("#mobile").focus();
		errorMessage = "请输入正确手机号";
	}

	if (isNotBlank(errorMessage)) {
		$(".errorPlaceHolder").html(errorMessage);
		result = false;
	}

	return result;
}

function validateBankCardCheckCodeForm() {
	var checkCode = $("#checkCode").val(),
		errorMessage,
		result = true;

	if (isBlank(checkCode)) {
		$("#checkCode").focus();
		errorMessage = "请输入验证码";
	}

	if (isNotBlank(errorMessage)) {
		$(".errorPlaceHolder").html(errorMessage);
		result = false;
	}

	return result;
}


