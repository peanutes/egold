$(function () {

	//选中个人或者公司
	$("input.invoiceType").click(function () {
		$('input.invoiceType').prop('checked', false);
		$(this).prop('checked', true);
		if ($(this).attr("id") == 'personal') {
			$("#invoiceType").text("个人");
		} else {
			$("#invoiceType").text("单位");

		}
	});

});
