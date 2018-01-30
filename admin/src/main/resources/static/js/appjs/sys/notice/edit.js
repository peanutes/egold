// 以下为官方示例
$().ready(function() {

	$('.summernote').summernote({
		height : '370px',
		lang : 'zh-CN',
		onImageUpload: function(files, editor, $editable) {
			sendFile(files, editor, $editable);
		}
	});

	var content = $("#articleContent").val();

	$('#content_sn').code(content);

	laydate.render({
		elem: '#invalidTime'
		,type: 'datetime'
	});

	$("[type='submit']").click(function() {
		if (!$('#signupForm').valid()) {
			return false;
		}

		update();
	});


});


function update() {
	var content_sn = $("#content_sn").code();
	$("#articleContent").val(content_sn);
	$.ajax({
		cache : true,
		type : "POST",
		url : "/sys/notice/update",
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
