var model = new Vue({
	el : "#skuTabBlock",
	data : {
		list: [{id:null,barcode:"",spec:"",stock:null, fee:null,price:null,sort:null}],
		seen:true
	}
});

addLine = function () {
	var dm = {id:null,barcode:"",spec:"",stock:null, fee:null,price:null,sort:null};
	model.list.push(dm);
};

delLine = function (index) {
	index = parseInt(index);
	model.list.splice(index, 1);
};

/*doWatch = function () {
	alert(JSON.stringify(model.$data.list));
	console.log(model.$data.list);
};*/

doSubmitSkus = function () {
	//model.data.spuId = $("#spuId").val();
	$.ajax({
		url: "/goods/sku/save?spuId="+$("#spuId").val(),
		contentType: "application/json",
		data : JSON.stringify(model.$data.list),
		type: "POST",
		dataType: 'json',
		success: function (data) {
			parent.layer.msg("操作成功");
			model.$data.list = data.data;
		}
	});
};