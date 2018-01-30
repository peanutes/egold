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
		height:'370px',
		lang : 'zh-CN',
		onImageUpload: function(files, editor, $editable) {
			sendFile(files, editor, $editable);
		}
	});
	$("#saveAndPublishBtn").click(function() {
		if (!$('#signupForm').valid()) {
			return;
		}


		var articleType = $("#articleType").val();
		var listImg = $("#listImg").val();

		if (articleType==1 && (!listImg || listImg=="" )) {
			$.tips('热门资讯请输入列表图！');
			return false;
		}
		save(1);
	});
	$("#saveBtn").click(function() {
		if (!$('#signupForm').valid()) {
			return;
		}


		var articleType = $("#articleType").val();
		var listImg = $("#listImg").val();

		if (articleType==1 && (!listImg || listImg=="" )) {
			$.tips('热门资讯请输入列表图！');
			return false;
		}
		save(0);
	});
});


function save(status) {

	$("#status").val(status);
	var content_sn = $("#content_sn").code();
	$("#content").val(content_sn);

	/*console.info($("#content").val());



	console.info($('#signupForm').serialize());*/


	$.ajax({
		cache : true,
		type : "POST",
		url : "/cms/hotInfo/save",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(r) {
			if (r.code == 200) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				$("#cid").val(r.cid);
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(r.msg)
			}
		}
	});
}
function returnList() {
	var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	parent.layer.close(index);
}
