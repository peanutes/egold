$(function () {

	//添加地址
	$("#goToAddAddress").click(function () {
		goToPage("/myAddress/add");
	});

	//修改地址
	$("#goToEditAddress").click(function () {
		goToPage("/myAddress/edit");
	});

	$(".chooseAddress").on("tap", function () {
		var provinceId = $("#provinceId").val() || 110000,
			cityId = $("#cityId").val() || 110101,
			countyId = $("#countyId").val() || 110101
			;

		//选择省市区
		var city_picker = new mui.PopPicker({layer: 3});
		city_picker.setData(init_city_picker);
		//city_picker.pickers[0].setSelectedValue(provinceId);
		//city_picker.pickers[1].setSelectedValue(cityId);
		//city_picker.pickers[2].setSelectedValue(countyId);
		city_picker.pickers[0].setSelectedIndex((provinceId +"").substring(0,2) -11);
		city_picker.pickers[1].setSelectedIndex((cityId +"").substring(2,4) -1);
		city_picker.pickers[2].setSelectedIndex((countyId +"").substring(4,6) -0);
		//city_picker.pickers[0].setSelectedIndex("河北省");
		//city_picker.pickers[1].setSelectedIndex("唐山市");
		//city_picker.pickers[2].setSelectedIndex("丰南区");

		setTimeout(function () {
			city_picker.show(function (items) {
				var preAddressTemp;
				$("#provinceId").val((items[0] || {}).value);
				$("#cityId").val((items[1] || {}).value);
				$("#countyId").val((items[2] || {}).value);

				$("#province").val((items[0] || {}).text);
				$("#city").val((items[1] || {}).text);
				$("#county").val((items[2] || {}).text);

				preAddressTemp = (items[0] || {}).text + "" + (items[1] || {}).text + "" + (items[2] || {}).text;

				$("#preAddress").val(preAddressTemp);
				$("#detailAddress").val(preAddressTemp);
			});
		}, 200);
	});

	//保存地址
	$("#saveAddress").click(function () {
		var addressId = $("#addressId").val(),
			receiver = $("#receiver").val(),
			receiverTel = $("#receiverTel").val(),
			zipCode = $("#zipCode").val(),
			provinceId = $("#provinceId").val(),
			cityId = $("#cityId").val(),
			countyId = $("#countyId").val(),
			province = $("#province").val(),
			city = $("#city").val(),
			county = $("#county").val(),
			preAddress = $("#preAddress").val(),
			detailAddress = $("#detailAddress").val(),
			address = $("#address").val(),
			fullAddress = preAddress + detailAddress;

		if (isBlank(receiver)) {
			alertMessage("请输入收货人姓名!");
			$("#receiver").focus();
			return;
		}
		if (isBlank(receiverTel)) {
			alertMessage("请输入联系人电话!");
			$("#receiverTel").focus();
			return;
		}
		if (isBlank(preAddress)) {
			alertMessage("请选择省市区县!");
			$("#preAddress").focus();
			return;
		}
		if (isBlank(detailAddress)) {
			alertMessage("请输入镇街门牌号等详细信息!");
			$("#detailAddress").focus();
			return;
		}

		saveAddress();

	});
});

function saveAddress() {
	showLoadingAndDisableButton("保存中", $("#saveAddress"));

	var receiver = $("#receiver").val(),
		addressId = $("#addressId").val(),
		receiverTel = $("#receiverTel").val(),
		zipCode = $("#zipCode").val(),
		provinceId = $("#provinceId").val(),
		cityId = $("#cityId").val(),
		countyId = $("#countyId").val(),
		province = $("#province").val(),
		city = $("#city").val(),
		county = $("#county").val(),
		preAddress = $("#preAddress").val(),
		detailAddress = $("#detailAddress").val(),
		address = $("#address").val(),
		defaultAddress = $("#defaultAddress").val() || 0,
		//fullAddress = preAddress + detailAddress,
		updateUrl = "/member/address/update";

	if (isBlank(addressId)) {
		updateUrl = "/member/address/add";
	}

	$.post(getApiUrl(updateUrl), {
			id: addressId,
			receiver: receiver,
			receiverTel: receiverTel,
			zipCode: zipCode,
			provinceId: provinceId,
			cityId: cityId,
			countyId: countyId,
			province: province,
			city: city,
			county: county,
			address: detailAddress,
			defaultAddress: defaultAddress

		}, function (result) {
			hideLoadingAndEnableButton($("#saveAddress"));

			if (isReturnSuccess(result)) {
				alertMessage("保存成功!");
				goToPage("/myAddress/list");
			} else {
				alertMessage(result.msg);
			}
		}
	);
}