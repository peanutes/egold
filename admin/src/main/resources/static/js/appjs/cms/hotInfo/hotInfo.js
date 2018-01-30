var prefix = "/cms/hotInfo"
$(function() {
	load();
});

function load() {
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/list", // 服务器数据的加载地址
						// showRefresh : true,
						// showToggle : true,
						// showColumns : true,
						iconSize : 'outline',
						toolbar : '#exampleToolbar',
						striped : true, // 设置为true会有隔行变色效果
						dataType : "json", // 服务器返回的数据类型
						pagination : true, // 设置为true会在底部显示分页条
						// queryParamsType : "limit",
						// //设置为limit则会发送符合RESTFull格式的参数
						singleSelect : false, // 设置为true将禁止多选
						// contentType : "application/x-www-form-urlencoded",
						// //发送到服务器的数据编码类型
						pageSize : 10, // 如果设置了分页，每页数据条数
						pageNumber : 1, // 如果设置了分布，首页页码
						// search : true, // 是否显示搜索框
						showColumns : false, // 是否显示内容下拉框（选择显示的列）
						sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者
													// "server"
						queryParams : function(params) {
							return {
								// 说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								size : params.limit,
								page : params.offset,
								title_RLIKE : $('#searchTitle').val(),
								articleType : $('#searchArticleType').val()
							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						columns : [
								{
									checkbox : true
								},
								{

									title : '序号', // 列标题
									formatter: function (value, row, index) {
										return index+1;
									}

								},
                                {
									field : 'title',
									title : '标题'
                                },

								{
									field : 'articleType',
									title : '文章类型',
									formatter : function (value) {
										if (value == 1) {
											return "热门资讯";
										} else if (value == 2) {
											return "帮助中心";
										} else if (value == 3) {
											return "了解我们";
										}
										//else if (value == 4) {
										//	return "安全保障";
										//}
										else if (value == 5) {
											return "保单样本";
										}else if (value == 6) {
											return "常见问题";
										}else if (value == 7) {
											return "产品介绍";
										}else if (value == 8) {
											return "注册协议";
										}else if (value == 9) {
											return "活期金协议";
										}else if (value == 10) {
											return "定期金协议";
										}else if (value == 11) {
											return "看涨跌协议";
										}else if (value == 12) {
											return "公告";
										}else if (value == 13) {
											return "特价金细则";
										}else if (value == 14) {
											return "实名认证协议";
										}else if (value == 15) {
											return "新手金协议";
										}else if (value == 16) {
											return "存金规则";
										}
									}
								},
							

                                {
									field : 'source',
									title : '来源'
                                },
								{
									field : 'listImg',
									title : '列表图',
									align : 'center',
									formatter : function (value, row, index) {
										if (value && value!="") {
											var imgSrc = value + "?x-oss-process=image/resize,m_fixed,h_80,w_100"

											return "<img src='" + imgSrc + "'>";
										}else {
											return "";
									}

									}

								},

								{
									field : 'createDate',
									title : '时间'
								},

                                {
									field : 'status',
									title : '状态',
									align : 'center',
									formatter : function(value, row, index) {
										if (value == '0') {
											return '<span class="label label-danger">草稿</span>';
										} else if (value == '1') {
											return '<span class="label label-primary">发布</span>';
										}
									}
                                },


                                {
									field : 'sort',
									title : '排序'
                                },
							

								{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
										var e = '<a  class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
												+ row.id
												+ '\')"><i class="fa fa-edit "></i></a> ';
										var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
												+ row.id
												+ '\')"><i class="fa fa-remove"></i></a> ';
										var f = '<a class="btn btn-success btn-sm '+s_publish_h+'" href="#" title="发布"  mce_href="#" onclick="publish(\''
												+ row.id
												+ '\')"><i class="fa fa-rocket"></i></a> ';

										return e + d + f;

									}
								} ]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}
function add() {
	// iframe层
	var addPage = layer.open({
		type : 2,
		title : '增加热门资讯',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '400px' ],
		content : prefix + '/add' // iframe的url
	});
	layer.full(addPage);
}
function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix + "/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code == 200) {
					layer.msg("操作成功");
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	})
}
function edit(id) {
	var editPage = layer.open({
		type : 2,
		title : '修改热门资讯',
		maxmin : true,
		shadeClose : true, // 点击遮罩关闭层
		area : [ '800px', '400px' ],
		content : prefix + '/edit/' + id // iframe的url
	});
	layer.full(editPage);
}
function batchRemove() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['id'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.code == 200) {
					layer.msg("操作成功");
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {

	});


}

function publish(id) {
	layer.confirm('确定要发布选中的文章？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix + "/publish",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code == 200) {
					layer.msg("操作成功");
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	})
}

