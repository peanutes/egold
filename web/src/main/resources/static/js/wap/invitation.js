$(function () {

	//获取我的邀请记录
	$.post(getApiUrl("/member/member/myInvitation"), {
		page: 1,
		size: systemConfig.defaultPageSize
	}, function (result) {
		console.log(result)
		if (isReturnSuccess(result)) {
			var resu = result.data,
				commission = resu.commission,
				htmlContent = '<div class="row"> <p>好友</p> <p>是否投资</p> <p>投资时间</p> </div>';

			$("#commission").text(isNotBlank(commission) ? formatMoney(commission) : formatMoney(0));//佣金

			if (resu != null && resu.myInvitationMemberPage != null && resu.myInvitationMemberPage.list != null) {
				var resultObject = resu.myInvitationMemberPage.list;

				if (resultObject.length < 1) {
					htmlContent += "<p class='ta_center fz_32 padding_20' >暂无数据</p>";
				}

				for (var i = 0; i < resultObject.length; i++) {
					htmlContent += '<div class="row"> <p>' + resultObject[i].mobile + '</p><p>' + resultObject[i].invisted + '</p><p>' + resultObject[i].registerDate + '</p></div>';
				}

				$(".invitation_detail_list").html(htmlContent);
			}

		}
	});

//邀请好友按纽
	$("#inviteFriend").click(function () {
		goToPage("/user/invitation");
	});

})
;