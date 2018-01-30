// 以下为官方示例
$().ready(function() {



	layui.use('upload', function(){
		var upload1 = layui.upload;
		//执行实例
		var uploadInst1 = upload1.render({
			elem: '#test1' //绑定元素
			,url: '/sys/sysfile/upload' //上传接口
			,size: 1000
			,accept:'file'
			,done: function(r){

				if (r.code == 200) {
					$("#imgUrl").val(r.data);
					$("#thumImg").attr("src", r.data+"?x-oss-process=image/resize,m_fixed,h_50,w_100");
					layer.msg("上传成功");
				} else {
					layer.msg(r.msg);
				}
			}
			,error: function(r){
				layer.msg(r.msg);
			}
		});
	});



	$("#baseBtn").click(function() {
		if (!$('#signupForm').valid()) {
			return false;
		}

		update();
	});


});


function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/goods/spu/update",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.code == 200) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				/*var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);*/

			} else {
				parent.layer.msg(data.msg);
			}

		}
	});

}

function returnList() {
	var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	parent.layer.close(index);
}

/*var model = new Vue({
	el : "#skuTabBlock",
	data : {
		list: [{id:null,barcode:"12",spec:"32",stock:null, fee:null,price:null,sort:null},{id:null,barcode:"12",spec:"32",stock:23, fee:45.34,price:23.45,sort:null}],
		seen:true
	}
});*/

function switchTab(tabId){

	if (tabId == 'skuTabBlock') {
		model.seen = true;
	} else {
		model.seen = false;
	}

	var $currentTab = $("#"+tabId);
	$currentTab.show();
	$currentTab.find(":input").removeAttr("disabled");

	$currentTab.siblings().hide();
	$currentTab.siblings().find(":input").attr("disabled", "true");



}





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



