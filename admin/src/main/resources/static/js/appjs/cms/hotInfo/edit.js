// 以下为官方示例
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
					$("#listImg").val(r.data);
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

	$('.summernote').summernote({
		height : '370px',
		lang : 'zh-CN',
		onImageUpload: function(files, editor, $editable) {
			sendFile(files, editor, $editable);
		}
	});

	var content = $("#content").val();

	$('#content_sn').code(content);

	$("[type='submit']").click(function() {
		if (!$('#signupForm').valid()) {
			return false;
		}

		var articleType = $("#articleType").val();
		var listImg = $("#listImg").val();

		if (articleType==1 && (!listImg || listImg=="" )) {
			$.tips('热门资讯请输入列表图！');
			return false;
		}
		update(1);
	});


});


function update(status) {
	$("#status").val(status);
	var content_sn = $("#content_sn").code();
	$("#content").val(content_sn);
	//console.dir($("#content").val());
	//console.dir($('#signupForm').serialize());
	$.ajax({
		cache : true,
		type : "POST",
		url : "/cms/hotInfo/update",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.code == 200) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

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
