// 以下为官方示例
$().ready(function() {

	var productType = $("#productType").val();

	$("#productType").change(function(){
		//alert(JSON.stringify(val));
		alert("不允许修改");
		return false;

	});

	if (productType=="RISE_FALL") {
		$(".riseFall").show();
		$(".nonRiseFall").hide();
		$("#incomeStructLabel").html("收益发放方式")

	} else {
		$(".riseFall").hide();
		$(".nonRiseFall").show();
		$("#incomeStructLabel").html("收益构成")
	}


	laydate.render({
		elem: '#deadLineDate'
		,type: 'datetime'
	});

	laydate.render({
		elem: '#interestBeginDate'
		,type: 'date'
	});


	$("#saveAndPublish").click(function() {
		if (!$('#signupForm').valid()) {
			return false;
		}

		update(1);
	});

	$("#saveDraft").click(function() {
		if (!$('#signupForm').valid()) {
			return false;
		}

		update(2);
	});


});


function update(status) {
	$("#status").val(status);
	$.ajax({
		cache : true,
		type : "POST",
		url : "/invest/financialProduct/update",
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
