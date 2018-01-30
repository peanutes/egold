$().ready(function() {
	$(".riseFall").hide();


	$("#productType").change(function(){
		//alert(JSON.stringify(val));

		var productType = $("#productType").val();

		if (productType=="RISE_FALL") {
			$(".riseFall").show();
			$(".nonRiseFall").hide();
			$("#incomeStructLabel").html("收益发放方式")

		} else {
			$(".riseFall").hide();
			$(".nonRiseFall").show();
			$("#incomeStructLabel").html("收益构成")
		}



	});

	laydate.render({
		elem: '#deadLineDate'
		,type: 'datetime'
	});

	laydate.render({
		elem: '#interestBeginDate'
		,type: 'date'
	});


	$("#submitAndPublish").click(function() {
		if (!$('#signupForm').valid()) {
			return;
		}
		save(1);
	});

	$("#saveDraft").click(function() {
		if (!$('#signupForm').valid()) {
			return;
		}
		save(2);
	});
});



function save(status) {
	$("#status").val(status);

	$.ajax({
		cache : true,
		type : "POST",
		url : "/invest/financialProduct/save",
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
