var propertyModel = new Vue({
	el : "#propertyTabBlock",
	data : {
		list: [{id:null,propertyName:"",propertyValue:""}],
		seen:true
	}
});

addPropertyLine = function () {
	var dm = {id:null, propertyName: "", propertyValue: ""};
	propertyModel.list.push(dm);
};

delPropertyLine = function (index) {
	index = parseInt(index);
	propertyModel.list.splice(index, 1);
};

/*doWatch = function () {
 alert(JSON.stringify(propertyModel.$data.list));
 console.log(propertyModel.$data.list);
 };*/

doSubmitItems = function () {
	//propertyModel.data.spuId = $("#spuId").val();
	$.ajax({
		url: "/goods/goodsProperty/save?spuId="+$("#spuId").val(),
		contentType: "application/json",
		data : JSON.stringify(propertyModel.$data.list),
		type: "POST",
		dataType: 'json',
		success: function (data) {
			parent.layer.msg("操作成功");
			propertyModel.$data.list = data.data;
		}
	});
};