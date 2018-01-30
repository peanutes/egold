$(function () {

});

/**
 * 获取我的红包列表
 * @param couponType
 * @param couponStatus
 * @param displayTabId
 */
function getCashCouponList(couponType, couponStatus, displayTabId) {

	displayTabId = displayTabId || "tab_1_content";

	$.post(getApiUrl("/invest/member/coupon/queryCoupon"), {
		couponType: couponType,
		couponStatus: couponStatus
	}, function (result) {

		if (isReturnSuccess(result)) {

			var resu = result.data,
				htmlContent = '',
				couponCategory = 'discount'//劵类型
				;

			if (resu != null) {
				for (var i = 0; i < resu.length; i++) {
					var couponAmountLabel = resu[i].couponAmountLabel || "",
						couponTypeLabel = resu[i].couponTypeLabel || "",
						endTimeStr = resu[i].endTimeStr || "",
						investAmountMinDesc = resu[i].investAmountMinDesc || "",
						investDeadlineMinDesc = resu[i].investDeadlineMinDesc || ""
						;

					htmlContent += '<div class="detail_table border_20 no_border_bottom background_white coupon_content">';
					htmlContent += '	<div class="detail_tr ">';
					htmlContent += '	    <div class="both-center color_ff8800 fz_32 detail_td left">';
					htmlContent += '	    ' + couponAmountLabel + '';
					htmlContent += '	    </div>';

					if (couponStatus == 'HAD_USED') {
						htmlContent += '	    <div class="detail_td middle coupon_content_4_already_used">';
					} else {
						htmlContent += '	    <div class="detail_td middle">';
					}
					htmlContent += '		    <p class="title2">' + investAmountMinDesc + '</p>';
					htmlContent += '		    <p class="title2">' + investDeadlineMinDesc + '</p>';
					htmlContent += '		    <p class="title3">有效期至' + endTimeStr + '</p>';
					htmlContent += '		</div>';

					if (couponType == 'CASH_COUPON') {
						couponCategory = 'cash';
					}
					//如果是已使用
					if (couponStatus == 'NON_USE') {
						htmlContent += '			<div class="detail_td  coupon-icon ' + couponCategory + '-not-use right">';
					} else if (couponStatus == 'EXPIRED') {
						htmlContent += '			<div class="detail_td  coupon-icon ' + couponCategory + '-expired-use right">';
					} else if (couponStatus == 'HAD_USED') {
						htmlContent += '			<div class="detail_td  coupon-icon ' + couponCategory + '-already-use right">';
					}
					htmlContent += '			<div class="label">' + couponTypeLabel + '</div>';
					htmlContent += '		</div>';
					htmlContent += '	</div>';
					htmlContent += '</div>';

				}

				$("#" + displayTabId).html(htmlContent);

				if (htmlContent.length < 1) {
					//$("#" + displayTabId).html('<div class=" border_20 no_border_bottom background_white ta_center fz_28">暂无数据.</div>');
					$("#" + displayTabId).html("<p class='ta_center fz_28 padding_20 border_20 background_white'>暂无数据</p>");

				}
			}

		}
	});
}