//黄金订单
$(function () {

		$(".orderExpiredTime").text(getLocalStorageItem("orderExpiredTime", 90));//订单有效时间

		//当前金价
		var currentPrice = getLocalStorageItem("currentPrice", 280);

		$(".accordingToMoney,.accordingToWeight").click(function () {
			//先清空原来的值,然后计算出自身表单的值
			$("#buyGoldWeightHidden").val("");
			$("#shouldPayAmountHidden").val("");
			$("#goldWeight").text(0);//预计克重
			$(".totalAmount").text('0元');//订单总额
			$(".shouldPayAmount").text('0元');//订单总额

			if ($(this).hasClass("accordingToMoney")) {
				moneyCalculate($("#amount_money"));
			} else {
				weightCalculate($("#amount_weight"));
			}

		});

		//按金额来下单
		$("#amount_money").unbind("keyup").bind("keyup", function () {
			$("#buyGoldWeightHidden").val("");//清空用来判断是否输入了订单价钱的值

			clearErrorMessage();
			onlyAmountForMoney(this);//直接去掉非数字
			moneyCalculate($(this));
		});

		//按克重来下单
		$("#amount_weight").unbind("keyup").bind("keyup", function () {
			$("#buyGoldWeightHidden").val("");//清空用来判断是否输入了订单价钱的值

			clearErrorMessage();
			onlyAmountForWeight(this);//直接去掉非数字
			weightCalculate($(this));

		});

		//按输入金额计算相关信息
		function moneyCalculate(jqObject) {
			var inputMoneyAmount = jqObject.val(),
				financeProductType = $("#financeProductType").val(),
				estimateWeight = (inputMoneyAmount / currentPrice),//预计克重

				term = $("#term").val(),
				annualRate = $("#annualRate").val(),
				annualRateMax = $("#annualRateMax").val(),
				estimateProfitMin = (inputMoneyAmount) * annualRate * 0.01 * term / 365,//预计最低收益
				estimateProfitMax = (inputMoneyAmount) * annualRateMax * 0.01 * term / 365;//预计最高收益

			if (systemConfig.RISE_FALL == financeProductType) {

				$(".estimate_profit").text(formatMoney(estimateProfitMin) + "~" + formatMoney(estimateProfitMax));//预计收益

			} else {
				$("#estimate_weight_value").text(formatGold(estimateWeight));//预计克重

				$("#buyGoldWeightHidden").val(estimateWeight);//判断是否输入了数量用

			}

			setDetailPreviewValue(inputMoneyAmount,inputMoneyAmount, estimateWeight, false);

		}

		//按输入克重计算相关信息
		function weightCalculate(jqObject) {
			var inputWeightAmount = jqObject.val(),
				estimateMoney = inputWeightAmount * currentPrice;//预计金额

			$("#estimate_money_value").text(formatMoney(estimateMoney));//预计金额

			$("#buyGoldWeightHidden").val(inputWeightAmount);//判断是否输入了数量用

			setDetailPreviewValue(estimateMoney,estimateMoney, inputWeightAmount, false);
		}

		//设置页面订单预览信息
		function setDetailPreviewValue(money,shouldPayAmount, weight, isNewUserGold) {
			var financeProductType = $("#financeProductType").val();
			isNewUserGold = isNewUserGold || false;

			//如果是看涨跌
			if (systemConfig.RISE_FALL == financeProductType) {
				//$("#goldWeight").text(formatGoldWithUnit(weight));//预计克重
				$(".totalAmount").text(formatMoneyWithUnit(money));//订单总额
				$(".shouldPayAmount").text(formatMoneyWithUnit(shouldPayAmount));//订单总额
				$("#totalAmountHidden").val(money);//订单总额
				$("#shouldPayAmountHidden").val(shouldPayAmount);//在线支付金额
			} else {

				$("#goldWeight").text(formatGoldWithUnit(weight));//预计克重
				$(".totalAmount").text(formatMoneyWithUnit(money));//订单总额
				$(".shouldPayAmount").text(formatMoneyWithUnit(shouldPayAmount));//订单总额
				$("#totalAmountHidden").val(money);//订单总额
				$("#shouldPayAmountHidden").val(shouldPayAmount);//订单总额

				//如果新手金,需要直接设置如下信息以用来显示
				if (isNewUserGold) {
					$(".dealGoldPrice").text(formatMoneyWithUnit(money));//成交价格
					$(".dealGoldWeight").text(formatGoldWithUnit(weight));//成交重量
					$(".dealTotalAmount").text(formatMoneyWithUnit(money));//成交总额
				}
			}

			////默认选中余额支付
			$("#useEnableBalance").addClass("mui-active");
			calculateMoney4BalancePaySwitch();
		}

		$("#agree").click(function () {
			clearErrorMessage();
		})

		//去具体下单详情页
		$("#toGoldFormDetail").click(function () {
				var memberId = $("#memberId").val(),
					productId = $("#productId").val(),
					realNameValid = $("#realNameValid").val(),
					hasLogin = $("#hasLogin").val(),
					bankcardBind = $("#bankcardBind").val(),
					isNewUserGold = $("#isNewUserGold").val(),
					buyGoldWeightHidden = $("#buyGoldWeightHidden").val(),
					shouldPayAmountHidden = $("#shouldPayAmountHidden").val()
					;

				if (isBlank(memberId)) {
					alert("您还没有登录,请先登录");
					goToPage("/wap/mine");
					return;
				} else if (isBlank(bankcardBind)) {
					alert("您还没有绑定银行卡,请先绑定");
					goToPage("/user/toBankCardBinding");
					return;
				}

				if (!validateOrderForm()) {
					return false;
				}

				//显示下单详情页
				$("#goldFormDetail").show();
				$("#goldFormInput").hide();

				//显示红包及优惠劵信息
				$.post(getApiUrl("/invest/member/coupon/enableCoupons"), {
					productId: productId,
					orderAmount: shouldPayAmountHidden

				}, function (result) {
					if (isReturnSuccess(result)) {
						var cashCoupons = result.data.cashCoupons,
							discountCoupons = result.data.discountCoupons,
							couponAmont = 0,
							shouldPayAmountHidden = $("#shouldPayAmountHidden").val(),
							totalAmountHidden = $("#totalAmountHidden").val();
						if (cashCoupons.length > 0) {
							$("#cashCouponIdHidden").val(cashCoupons[0].id);
							couponAmont = cashCoupons[0].couponAmount || 0;
							$("#couponAmount").text(couponAmont);
							$("#couponAmountHidden").val(couponAmont);
							$("#cashCouponDesc").text(cashCoupons[0].couponAmountStr);
							$("#cashCouponDesc").addClass("orange");
						}

						//优惠劵处理
						if (discountCoupons.length > 0) {
							$("#discountCouponIdHidden").val(discountCoupons[0].id);
							$("#discountCouponDesc").text(discountCoupons[0].couponAmountStr);
							$("#discountCouponDesc").addClass("orange");
							$(".has-discount-coupon-display").show();
							$(".no-discount-coupon-display").hide();

						} else {
							$("#discountCouponIdHidden").val(null);
							$(".has-discount-coupon-display").hide();
							$(".no-discount-coupon-display").show();
						}

						//如果有红包,需要重新计算卡支付金额相关信息
						if (null != couponAmont) {
							$(".shouldPayAmount").text(formatMoneyWithUnit(shouldPayAmountHidden - couponAmont));
							$("#shouldPayAmountHidden").val(shouldPayAmountHidden - couponAmont);
						}


						//如果是新手金,在这里设置相关预览信息,这句需要注意顺序
						if ("Y" == isNewUserGold) {
							setDetailPreviewValue(shouldPayAmountHidden,shouldPayAmountHidden-couponAmont, buyGoldWeightHidden, true);
						}else{
							setDetailPreviewValue(totalAmountHidden,shouldPayAmountHidden-couponAmont, buyGoldWeightHidden, false);
						}
					}

				});

			}
		);

		//使用优惠劵选择按纽
		$("#useDiscountCoupon").click(function () {

			if ($(this).hasClass("mui-active")) {//使用优惠劵
				$("#discountCouponDesc").show();
				$("#dont-use-discount-coupon").hide();
			} else {
				$("#discountCouponDesc").hide();
				$("#dont-use-discount-coupon").show();

			}
		});

		//使用余额选择按纽
		$("#useEnableBalance").click(function () {
			calculateMoney4BalancePaySwitch();
		});

		//下单
		$("#submitOrder").click(function () {

			var isBankcardBind = $("#bankcardBind").val(),//是否绑定了银行卡
				productId = $("#productId").val(),
				productName = $("#productName").val(),
				productType = $("#productType").val(),//注意与产品类型的定期活期涨跌区别开
				memberId = $("#memberId").val(),
				isUseEnableBalance = $("#useEnableBalance").hasClass("mui-active"),//是否使用余额
				totalAmount = $("#totalAmountHidden").val(),
				cashCouponId = $("#cashCouponIdHidden").val(),//红包ID
				discountCouponId = $("#discountCouponIdHidden").val(),//优惠劵ID
				isUseDiscountCoupon = $("#useDiscountCoupon").hasClass("mui-active"),//是否使用优惠劵

				balancePayAmount = $("#balancePayAmountHidden").val();
			;

			if (!validatePayOrderForm()) {
				return;
			}

			//不使用优惠劵
			if (!isUseDiscountCoupon) {
				discountCouponId = null;
			}

			//提交订单到后台
			showLoadingAndDisableButton("下单中", $("#submitOrderAndToPay"));

			$.post(getApiUrl("/order/order/submitOrder"), {
				memberId: memberId,
				productId: productId,
				productType: productType,
				productName: productName,
				totalAmount: totalAmount,
				cashCouponId: cashCouponId,
				discountCouponId: discountCouponId,
				balancePayAmount: balancePayAmount
			}, function (result) {
				console.log(result)
				if (isUserNotLogin(result)) {
					alert("您还没登录,请先登录")
					goToPage("/wap/mine");
					return;
				} else if (isReturnSuccess(result)) {
					hideLoadingAndEnableButton($("#submitOrderAndToPay"));

					var orderSn = result.data.orderSn,
						shouldPayAmount = result.data.shouldPayAmount;

					//弹出输入密码框
					showPayPasswordForm(orderSn, shouldPayAmount);

				} else {
					//可能发生的其它错误
					alert(result.msg);
					hideLoadingAndEnableButton($("#submitOrderAndToPay"));
				}

			});

		});

		//弹出支付密码输入界面,按照是否还需要银行卡再支付金额来决定是否输入银行短信验证码
		function showPayPasswordForm(orderSn, shouldPayAmount) {
			$(".close_pay").show();
			$(".popUpMask").show();
			$("#requestPayPasswordWrapper").show();

			$("#closePay").click(function () {
				$(".popUpMask").hide();
				$("#requestPayPasswordWrapper").hide();
			});

			//支付
			$("#payOrder").click(function () {
					var payPwd = $("#payPwd").val();

					//todo 如果是测试环境且需要网上支付,修改金额再支付以减少测试成本
					if (shouldPayAmount > 0) {
						if (systemConfig.isTesting) {
							$.post(getApiUrl("/order/order/modifyOrderAmount"), {
								orderSn: orderSn,
								amount: 0.01,
							}, function (result) {
								payForTheOrder(orderSn, payPwd, shouldPayAmount);
							});
						}
					} else {
						payForTheOrder(orderSn, payPwd, shouldPayAmount);
					}

				}
			)

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

		//订单支付
		function payForTheOrder(orderSn, payPwd, shouldPayAmount) {

			if (isBlank(orderSn)) {
				alert("订单错误,请重新下单");
				return;
			} else if (isBlank(payPwd)) {
				alert("请输入支付密码");
				$("#payPwd").focus();
				return;
			}

			showLoadingAndDisableButton("提交支付请求中", $("#payOrder"));

			$.post(getApiUrl("/order/order/confirmPay"), {
				orderSn: orderSn,
				payPwd: payPwd,
			}, function (result) {
				console.log(result)
				if (isUserNotLogin(result)) {
					goToPage("/wap/mine");
					return;
				} else if (isReturnSuccess(result)) {

					hideLoadingAndEnableButton($("#payOrder"));

					var isNewUserGold = $("#isNewUserGold").val();

					//如果不是新手金,在这里设置相关预览信息
					//新手金的已经在前面设置过一次
					if ("N" == isNewUserGold) {
						var dealCashCouponAmount = result.data.cashCouponAmount,
							dealDiscountCouponAmount = result.data.discountCouponRate;
						$(".dealGoldPrice").text(formatMoneyWithUnit(result.data.currentGoldPrice));//成交价格
						$(".dealGoldWeight").text(formatGoldWithUnit(result.data.goldWeight));//成交重量
						$(".dealTotalAmount").text(formatMoneyWithUnit(result.data.totalAmount));//成交总额

						//红包金额
						if (null != dealCashCouponAmount && dealCashCouponAmount != "0.00") {
							$(".dealCashCouponAmount").text(formatMoneyWithUnit(dealCashCouponAmount));
							$(".dealCashCouponAmount").addClass("orange");
						}

						//优惠劵
						if (null != dealDiscountCouponAmount && dealDiscountCouponAmount != "0.00") {
							$(".dealDiscountCouponAmount").text("加息"+dealDiscountCouponAmount + "%");
							$(".dealDiscountCouponAmount").addClass("orange");

						}

					}

					//status (integer, optional): 状态，0：待支付，1：已发起支付，待短信验证，2：支付处理中，
					// 3：支付成功， 4：支付失败， 5：支付超时
					if (result.data.status == 1) {
						showCheckSmsCodeForm(result.data.payRequestNo);
					} else if (result.data.status == 3) {//表示支付成功
						paySuccess();
					}
				} else {
					alert(result.msg)
					hideLoadingAndEnableButton($("#payOrder"));
				}
			});
		}

		//完成(关闭购买成功窗口导航到"我的订单")
		$(".finish_info").click(function () {
			goToPage("/user/myOrderList");
			return;
		});

		//订单支付密码确认
		function validatePaySmsCode(payRequestNo, smsCode) {
			showLoadingAndDisableButton("支付中", $("#payOrderSmsCode"));
			$.post(getApiUrl("/order/order/validatePaySmsCode"), {
					payRequestNo: payRequestNo,
					smsCode: smsCode,
				}, function (result) {
					console.log(result)
					if (isReturnSuccess(result)) {
						paySuccess();
					} else {
						alert(result.msg)
						hideLoadingAndEnableButton($("#payOrderSmsCode"));
					}
				}
			)
			;
		}

		//支付成功转向
		function paySuccess() {
			hideLoading();
			$("#buyingSuccess").show();
			$("#goldFormDetail").hide();
			$("#goldFormInput").hide();
		}

		function validateOrderForm() {
			var buyGoldWeight = $("#buyGoldWeightHidden").val(),
				financeProductType = $("#financeProductType").val(),
				minAmount = $("#minAmount").val(),
				buyAmount = $("#amount_money").val(),
				agree = $("#agree").is(":checked"),
				result = true,
				errorMessage;

			if (financeProductType == systemConfig.RISE_FALL) {
				if (isBlank(buyAmount) || buyAmount <= 0) {
					errorMessage = "请输入需要购买的数量";
					$("#amount_money").focus();
				} else if (Number(buyAmount) < Number(minAmount)) {
					errorMessage = "请至少购买" + minAmount + "元";
					$("#amount_money").focus();
				} else if (!agree) {
					errorMessage = "请阅读并同意协议";
					$("#agree").focus();
				}
			} else {
				if (isBlank(buyGoldWeight) || buyGoldWeight <= 0) {
					errorMessage = "请输入需要购买的数量";
					$("#buyGoldWeight").focus();
				} else if (!agree) {
					errorMessage = "请阅读并同意协议";
					$("#agree").focus();
				}
			}

			if (isNotBlank(errorMessage)) {
				//$(".errorPlaceHolder").html(errorMessage);
				alert(errorMessage);
				result = false;
			}

			return result;
		}

		function validatePayOrderForm() {
			var productId = $("#productId").val(),
				productName = $("#productName").val(),
				financeProductType = $("#financeProductType").val(),
				memberId = $("#memberId").val(),
				isUseEnableBalance = $("#useEnableBalance").hasClass("mui-active"),//是否使用余额
				totalAmount = $("#shouldPayAmountHidden").val(),
				balancePayAmount = 0,
				result = true,
				errorMessage
				;

			if (isUseEnableBalance) {
				balancePayAmount = totalAmount;
			}

			if (isBlank(productId) || isBlank(productName) || isBlank(financeProductType)) {
				errorMessage = "对应的产品有误,请重新选择下单";
			} else if (isBlank(memberId)) {
				errorMessage = "您还没有登录,请先登录!";
			} else if (isBlank(totalAmount)) {
				errorMessage = "金额有就误,请重新选择下单";
			}

			if (isNotBlank(errorMessage)) {
				alert(errorMessage);
				result = false;
			}

			return result;
		}

		/**
		 * 是否使用余额计算
		 */
		function calculateMoney4BalancePaySwitch() {
			var enableBalance = $("#enableBalance").val(),//用户帐户余额
				shouldPayAmountHidden = $("#shouldPayAmountHidden").val(),//下单需要在线付款总额
				totalAmountHidden = $("#totalAmountHidden").val(),//下单需要付款总额
				couponAmount = $("#couponAmountHidden").val(),//红包支付额
				enableBalanceCanUse,//可以使用多少余额来支付
				cardPayAmount;//卡支付金额
			couponAmount = couponAmount || 0;
			if ($("#useEnableBalance").hasClass("mui-active")) {//使用余额支付
				if (Number(enableBalance) >= Number(shouldPayAmountHidden)) {
					enableBalanceCanUse = shouldPayAmountHidden;
					cardPayAmount = 0;
				} else {
					enableBalanceCanUse = enableBalance;
					cardPayAmount = shouldPayAmountHidden - enableBalance;
				}
			} else {//不使用余额支付
				enableBalanceCanUse = 0;
				cardPayAmount = shouldPayAmountHidden;
			}
			$("#balancePayAmount").text(formatMoneyWithUnit(enableBalanceCanUse));
			$("#balancePayAmountHidden").val((enableBalanceCanUse));
			$(".shouldPayAmount").text(formatMoneyWithUnit(cardPayAmount));
			$("#cardPayAmountHidden").val((cardPayAmount));
		}
	}
)
;


