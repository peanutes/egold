// 以下为官方示例
$().ready(function() {

	$("#pass").click(function() {
		if (!$('#signupForm').valid()) {
			return false;
		}

		update("2");
	});
	$("#deny").click(function() {
		if (!$('#signupForm').valid()) {
			return false;
		}

		update(3);
	});


});


function update(status) {
	$("#status").val(status);


	$.ajax({
		cache : true,
		type : "POST",
		url : "/fund/withdraw/update",
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

