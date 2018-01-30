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




	$("#goodsTypeId").change(function(){
		withdrawGoldOrMall();
	});

	var $baseTab = $("#baseTab");
	$baseTab.siblings().hide();




	$('.summernote').summernote({
		height:'370px',
		lang : 'zh-CN',
		onImageUpload: function(files, editor, $editable) {
			sendFile(files, editor, $editable);
		}
	});





	$("#baseBtn").click(function() {
		if (!$('#signupForm').valid()) {
			return;
		}
		save();
	});

});



function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/goods/spu/save",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 200) {
				parent.layer.msg("操作成功");
				$("#spuId").val(data.data);
				$("[name='spuId']").val(data.data);

				$("#baseTabBlock").find("[name='id']").val(data.data);
				withdrawGoldOrMall();
				$("#goodsTypeId").attr("disabled", true);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}


function returnList() {
	var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	parent.reLoad();
	parent.layer.close(index);
}

function submitImgs(){
	$("#test2").trigger('click');
}

var imgInitFlag = false;
function initImgUpload() {

	layui.use('upload', function(){
		var upload = layui.upload;
		//执行实例
		var uploadInst = upload.render({
			elem: '#test2' //绑定元素
			,url: '/goods/goodsImg/upload?spuId='+$("#spuId").val() //上传接口
			,size: 1000
			,accept:'file'
			,done: function(r){

				if (r.code == 200) {
					var imgHtml = '<div id="img'+r.data.id+'" class="file-box" >	<div class="file"><a href="#"><span class="corner"></span>' +
							'<div class="image"><img alt="image" class="img-responsive" src="'+ r.data.imgUrl+'">' +
							'</div>&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;<br/>' +
							'<button class="btn btn-danger btn-xs" onclick="removeImg('+ r.data.id +')">删除</button></a></div></div>';

					$("#imgListBlock").append(imgHtml);

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

	imgInitFlag = true;
}
function switchTab(tabId){
	/*if (tabId == 'skuTabBlock') {
		model.seen = true;
	} else {
		model.seen = false;
	}

	if (tabId == 'propertyTabBlock') {
		propertyModel.seen = true;
	} else {
		propertyModel.seen = false;
	}*/

	var $currentTab = $("#"+tabId);
	$currentTab.show();
	$currentTab.find(":input").removeAttr("disabled");

	$currentTab.siblings().hide();
	$currentTab.siblings().find(":input").attr("disabled", "true");
	withdrawGoldOrMall();

	if (tabId == 'imgTabBlock' &&　!imgInitFlag) {
		 initImgUpload();
	}


}

function withdrawGoldOrMall(){
	var goodsTypeId = $("#goodsTypeId").val();

	if (goodsTypeId=="1") {
		// 提金商品
		$(".withdrawGoldBlock").show();
		$(".mallBlock").hide();

	} else {
		$(".withdrawGoldBlock").hide();
		$(".mallBlock").show();
	}

	var spuId = $("#spuId").val();





	if (!spuId) {
		return;
	}

	$(".commonBlock").show();
	if (goodsTypeId=="2") {
		$(".mallBlockTab").show();
	}

}

function submitDetail() {
	var content_sn = $("#content_sn").code();
	$("#content").val(content_sn);
	$("#detailSpuId").val($("#spuId").val());
	$.ajax({
		cache : true,
		type : "POST",
		url : "/goods/goodsDetail/save",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 200) {
				parent.layer.msg("操作成功");
				$("#detailId").val(data.data);


			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}

function removeImg(id) {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/goods/goodsImg/remove?id="+id,
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 200) {
				$("#img" + id).remove();
				parent.layer.msg("操作成功");

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}






