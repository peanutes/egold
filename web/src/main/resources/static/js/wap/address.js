$(function () {
	//选择省市区
	var city_picker = new mui.PopPicker({layer: 3});
	city_picker.setData(init_city_picker);
	$("#city_text").on("tap", function () {
		setTimeout(function () {
			city_picker.show(function (items) {
				$("#city_id").val((items[0] || {}).value + "," + (items[1] || {}).value + "," + (items[2] || {}).value);//该ID为接收城市ID字段
				$("#city_text").html((items[0] || {}).text + " " + (items[1] || {}).text + " " + (items[2] || {}).text);
			});
		}, 200);
	});
});