$(function () {
	var token = $("#token").val(),
		addressId = $("#addressId").val();

	//去下单
	$("#toGoldOrder").click(function () {
		var skuId = $("#skuId").val();

		goToPage("/goldMall/toGoldOrder4App?token=" + token + "&skuId=" + skuId);
	});

	//选择地址
	$("#chooseAddress").click(function () {
		//todo
		//goToPage("/goldMall/toGoldOrder?token=" + token + "&skuId=" + skuId);
	});

	//选择地址(跳转至App)
	$("#goToChooseAddress").click(function () {
		window.location = "egold://user/addressManage?token =" + token + "&addressId="+addressId;
	});

	//购买件数
	$(".number-count").unbind("keyup").bind("keyup", function () {
		onlyNum(this);//直接去掉非数字
		atLeast(this, 1);
		atMost(this, 999);
		calculateMoney($(".number-count").val());
	});

	//加
	$(".numbox-plus").click(function () {
		var numberCount = $(".number-count").val() || 0;

		if (numberCount < 999) {
			numberCount++;
		} else {
			alertMessage("至多购买999件");
			return;
		}
		calculateMoney(numberCount);

		$(".number-count").val(numberCount);

	});

	//加
	$(".numbox-minus").click(function () {
		var numberCount = $(".number-count").val() || 0;

		if (numberCount > 1) {
			numberCount--;
		} else {
			alertMessage("至少购买一件");
			return;
		}

		calculateMoney(numberCount);

		$(".number-count").val(numberCount);

	});

	//使用余额选择按纽
	$("#useEnableBalance").click(function () {
		calculateMoney4BalancePaySwitch();
	});

	//编辑发票
	$("#edit_invoice").click(function () {
		$(".order_form").hide();
		$(".invoice_info").show();
	});

	$(".checkbox").click(function () {
		var id = $(this).attr("id");

		if (id == "personal") {
			$(".invoiceType").text("个人");
			$("#invoiceTypeHidden").val("personal");
		} else {
			$(".invoiceType").text("单位");
			$("#invoiceTypeHidden").val("company");

		}
	});

	//确认发票信息
	$("#conformInvoice").click(function () {
		var
			invoiceType = $("#invoiceTypeHidden").val(),
			taxNo = $("#taxNo").val(),
			companyFullName = $("#companyFullName").val();
		if (invoiceType == "company" && isBlank(companyFullName)) {
			alertMessage("请输入单位全称");
			$("#companyFullName").focus();
			return;
		}

		$(".order_form").show();
		$(".invoice_info").hide();
	});

	//下单弹出交易密码
	$("#submitOrder").click(function () {
		var num = $(".number-count").val(),
			stock = $("#stock").val() || 0;
		if (Number(num) > Number(stock)) {
			alertMessage("订单数量不能超过库存数量("+stock+"),请修改!");
			$(".number-count").focus();
			return;
		}
		showPayPasswordForm();
	});

	//弹出支付密码输入界面,按照是否还需要银行卡再支付金额来决定是否输入银行短信验证码
	function showPayPasswordForm() {
		$(".close_pay").show();
		$(".popUpMask").show();
		$("#requestPayPasswordWrapper").show();

		$("#closePay").click(function () {
			$(".popUpMask").hide();
			$("#requestPayPasswordWrapper").hide();
		});

		//保存发票并支付
		$("#payOrder").click(function () {
				var
					invoiceItem = $("#invoiceItem").val(),
					invoiceType = $("#invoiceTypeHidden").val(),
					invoiceTitle = $("#invoiceTitle").val(),
					taxNo = $("#taxNo").val(),
					token = $("#token").val(),
					companyFullName = $("#companyFullName").val(),
					dealPwd = $("#payPwd").val(),//交易密码;
					num = $(".number-count").val(),
					stock = $("#stock").val() || 0;

				var num = $(".number-count").val(),
					stock = $("#stock").val() || 0;

				if (Number(num) > Number(stock)) {
					alertMessage("订单数量不能超过库存数量("+stock+"),请修改!");
					$(".number-count").focus();
					return;
				}

				if (invoiceType == "company" && isBlank(companyFullName)) {
					alertMessage("请输入单位全称");
					$("#companyFullName").focus();
					return;
				}

				if (isBlank(dealPwd)) {
					alertMessage("请输入交易密码");
					$("#payPwd").focus();
					return;
				}
				if (invoiceType == "company") {
					invoiceTitle = companyFullName;
				}

				showLoadingAndDisableButton("提交支付请求中", $("#payOrder"));
				$.post(getApiUrl4AppMall("/mall/submitInvoice4Mall"), {
					invoiceItem: invoiceItem,
					invoiceType: invoiceType,
					invoiceTitle: invoiceTitle,
					taxNo: taxNo,
					token: token
				}, function (result) {
					if (isUserNotLogin(result)) {
						hideLoadingAndEnableButton($("#payOrder"));

						goToPage("egold://user/login");
						return;
					} else if (isReturnSuccess(result)) {
						payForTheOrder();
					} else {
						alertMessage(result.msg)
						hideLoadingAndEnableButton($("#payOrder"));
					}
				});

			}
		)

	};

	//订单支付
	function payForTheOrder() {

		var isBankcardBind = $("#bankcardBind").val(),//是否绑定了银行卡
			skuId = $("#skuId").val(),
			num = $(".number-count").val(),
			balanceAmount = $("#balanceAmount").val(),
			addressId = $("#addressId").val(),
			isUseEnableBalance = $("#useEnableBalance").hasClass("mui-active"),//是否使用余额
			totalAmount = $("#totalAmountHidden").val(),
			dealPwd = $("#payPwd").val(),//交易密码
			invoiceId = $("#invoiceId").val(),//发票Id
			token = $("#token").val(),
			stock = $("#stock").val();

		if (Number(num) > Number(stock)) {
			alertMessage("订单数量不能超过库存数量("+stock+"),请修改!");
			$(".number-count").focus();
			return;
		}
		if (isBlank(dealPwd)) {
			alertMessage("请输入交易密码");
			$("#payPwd").focus();
			return;
		}

		$.post(getApiUrl4AppMall("/mall/submitOrder4App"), {
			skuId: skuId,
			num: num,
			balanceAmount: balanceAmount,
			addressId: addressId,
			dealPwd: dealPwd,
			invoiceId: invoiceId,
			token: token
		}, function (result) {
			if (isUserNotLogin(result)) {
				//alert("您还没登录,请先登录")
				goToPage("egold://user/login");
				return;
			} else if (isReturnSuccess(result)) {

				hideLoadingAndEnableButton($("#payOrder"));

				//status (integer, optional): 状态，0：待支付，1：已发起支付，待短信验证，2：支付处理中，
				// 3：支付成功， 4：支付失败， 5：支付超时
				if (result.data.status == 1) {
					showCheckSmsCodeForm(result.data.payRequestNo);
				} else if (result.data.status == 3) {//表示支付成功
					paySuccess();
				}
			} else {
				hideLoadingAndEnableButton($("#payOrder"));

				alertMessage(result.msg)
			}
		});
	};

	//弹出支付短信密码输入界面
	function showCheckSmsCodeForm(payRequestNo) {
		$("#remindInfo").show();

		$("#payPwd").hide();
		$("#payOrder").hide();
		$("#smsCode").show();
		$("#payOrderSmsCode").show();

		//短信验证支付
		$("#payOrderSmsCode").click(function () {
				var smsCode = $("#smsCode").val();
				validatePaySmsCode(payRequestNo, smsCode);
			}
		)

	};

	//订单支付密码确认
	function validatePaySmsCode(payRequestNo, smsCode) {
		showLoadingAndDisableButton("支付中", $("#payOrderSmsCode"));
		var token = $("#token").val();

		$.post(getApiUrl("/order/order/validatePaySmsCode4Mall"), {
				payRequestNo: payRequestNo,
				smsCode: smsCode,
				token4Mall: token
			}, function (result) {
				if (isReturnSuccess(result)) {
					paySuccess();
				} else {
					hideLoadingAndEnableButton($("#payOrderSmsCode"));
					alertMessage(result.msg);
				}
			}
		);
	}

	//支付成功转向
	function paySuccess() {
		hideLoading();

		$(".market_order_detail").hide();

		confirmMessage("支付成功",function(){
			goToPage("egold://user/orderList?token=" + token);
		});

	}

});

/**
 * 计算金额
 * @param numberCount
 */
function calculateMoney(numberCount) {
	var
		num = $(".number-count").val() || 1,//购买数量
		enableBalance = $("#enableBalance").val() || 0,//用户帐户余额
		onlinePayAmount = $("#onlinePayAmount").val() || 0,//下单需要在线付款总额
		otherTotalMoney = $("#otherTotalMoney").val() || 0,//其它费用总额
		price = $("#price").val() || 0,//单价
		orderTotalMoney = $("#totalAmount").val() || 0,//下单需要付款总额
		enableBalanceCanUse = 0 //可以使用多少余额来支付
		;

	if (isBlank(numberCount)) {
		alertMessage("请填写购买数量!");
		return;
	}

	//设置购买数量
	$(".number-count").val(numberCount);

	calculateMoney4BalancePaySwitch();

}

/**
 * 是否使用余额计算
 */
function calculateMoney4BalancePaySwitch() {
	var
		num = $(".number-count").val() || 1,//购买数量
		enabledBalance = $("#enabledBalance").val() || 0,//用户帐户余额
		onlinePayAmount = $("#onlinePayAmount").val() || 0,//需在线付款额
		otherTotalMoney = $("#otherTotalMoney").val() || 0,//其它费用总额
		price = $("#price").val() || 0,//单价
		enableBalanceCanUse = 0, //可以使用多少余额来支付,
		totalAmount //订单总额
		;

	//总额需要计算出来
	totalAmount = Number(num) * Number(price) + Number(otherTotalMoney);

	if ($("#useEnableBalance").hasClass("mui-active")) {//使用余额支付
		if (Number(enabledBalance) >= Number(totalAmount)) {
			enableBalanceCanUse = totalAmount;
		} else {
			enableBalanceCanUse = enabledBalance;
		}
	} else {//不使用余额支付
		enableBalanceCanUse = 0;
	}

	//计算需要支付的总额 = 总 - 余额
	onlinePayAmount = totalAmount - enableBalanceCanUse;

	$("#orderTotalMoney").text(formatMoneyWithUnit(totalAmount));

	$(".balancePayAmount").text(formatMoneyWithUnit(enableBalanceCanUse));
	$("#balanceAmount").val(enableBalanceCanUse);

	$(".onlinePayAmount").text(formatMoneyWithUnit(onlinePayAmount));
	$("#onlinePayAmount").val(onlinePayAmount);
}

function checkBox(obj) {
// 只有当选中的时候才会去掉其他已经勾选的checkbox，所以这里只判断选中的情况
	if (obj.is(":checked")) {
		// 先把所有的checkbox 都设置为不选种
		$('input.mybox').prop('checked', false);
		// 把自己设置为选中
		obj.prop('checked', true);
	}
}
