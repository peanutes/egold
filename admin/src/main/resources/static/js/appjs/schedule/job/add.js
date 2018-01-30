$().ready(function() {
	validateRule();
	// $("#signupForm").validate();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/schedule/job/save",
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
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			beanName : {
				required : true
			},
			methodName : {
				required : true
			},
			cronExpression : {
				required : true
			},
			remark : {
				required : true
			}
		},
		messages : {

			beanName : {
				required : icon + "请输入Bean名"
			},
			methodName : {
				required : icon + "请输入方法名"
			},
			cronExpression : {
				required : icon + "请输入Cron表达式"
			},
			remark : {
				required : icon + "请输入job说明"
			}
		}
	})
}