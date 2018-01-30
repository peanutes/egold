$().ready(function() {

	layui.use('upload', function(){
		var upload = layui.upload;
		//执行实例
		var uploadInst = upload.render({
			elem: '#test1' //绑定元素
			,url: '/sys/sysfile/upload' //上传接口
			,size: 1000
			,accept:'file'
			,done: function(r){

				if (r.code == 200) {
					$("#img").val(r.data);
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

	$("[type='submit']").click(function() {
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
		url : "/sys/store/save",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 200) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}


function returnList() {
	var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	parent.layer.close(index);
}



function doUpload() {
	if ($("#file").val() == "") {
		layer.msg("请选择要上传的文件");
		return;
	}
	var formData = new FormData($("#uploadForm")[0]);
	$.ajax({
		url : '/sys/sysfile/upload',
		type : 'POST',
		data : formData,
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(r) {
			layer.msg("上传成功");
		},
		error : function(r) {
			layer.alert("文件大小超限,最大支持30MB");
		}
	});
}