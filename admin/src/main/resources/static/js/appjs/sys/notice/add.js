$().ready(function() {
	laydate.render({
		elem: '#invalidTime'
		,type: 'datetime'
	});

	$('.summernote').summernote({
		height:'370px',
		lang : 'zh-CN',
		onImageUpload: function(files, editor, $editable) {
			sendFile(files, editor, $editable);
		}
	});

	$("[type='submit']").click(function() {
		if (!$('#signupForm').valid()) {
			return;
		}
		save();
	});
});



function save() {
	var content_sn = $("#content_sn").code();
	$("#articleContent").val(content_sn);
	$.ajax({
		cache : true,
		type : "POST",
		url : "/sys/notice/save",
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
