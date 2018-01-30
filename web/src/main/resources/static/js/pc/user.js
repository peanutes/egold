//充值选择银行
$(function () {
	$(".bank_list img").click(function () {
		//删除所有选中的样式

		var src = $(this).attr("src"),
			normalSrc = src.replace("_on", ""),
			bankName = $(this).attr("data-bank_name"),
			bankChannel = $(this).attr("data-bank_channel"),
			bankId = $(this).attr("data-bank_id");
		logoFileExtensionIndex = normalSrc.lastIndexOf("."),
			logoFileExtension = normalSrc.substring(logoFileExtensionIndex);
		selectedSrc = normalSrc.replace(logoFileExtension, "_on" + logoFileExtension);

		//清除已选中的银行
		$(".bank_list img").each(
			function () {
				var src = $(this).attr("src"),
					normalSrc = src.replace("_on", "");
				$(this).attr("src", normalSrc);
			}
		);

		//选中当前点击银行
		$(this).attr("src", selectedSrc);

		$("#bankChannelId").val(bankChannel);

		//alert("您选中的银行是:" + bankName + "(" + bankId + ")");

	})
})