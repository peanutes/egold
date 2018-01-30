// 以下为官方示例
$().ready(function() {

	$("[type='submit']").click(function() {
		if (!$('#signupForm').valid()) {
			return false;
		}
		if (!$(img).val()) {
			$.tips('请先上传图片！');
			return false;
		}
		update();
	});

	// 初始化Web Uploader
	var uploader = WebUploader.create({

		// 选完文件后，是否自动上传。
		auto: true,

		// swf文件路径
		swf: '/js/plugins/webuploader/Uploader.swf',

		// 文件接收服务端。
		server: '/sys/upload/singleFileUpload',

		// 选择文件的按钮。可选。
		// 内部根据当前运行是创建，可能是input元素，也可能是flash.
		// pick: '#filePicker',
		pick: {
			id: '#filePicker',
			label: ''
		},

		// 只允许选择图片文件。
		accept: {
			title: 'Images',
			extensions: 'gif,jpg,jpeg,bmp,png',
			mimeTypes: 'image/*'
		}
	});


	// 当有文件添加进来的时候
	uploader.on( 'fileQueued', function( file ) {
		var $li = $(
						'<div id="' + file.id + '"  style="float:left; width:110px; padding-left=15px">' +
						'<img>' +
						'<div class="info">' + file.name + '</div>' +
						'</div>'
				),
				$img = $li.find('img');


		// $list为容器jQuery实例
		var $list = $("#fileList");
		$list.html("");
		$(".originImgFlag").remove();
		$(".fileListFlag").html("");
		$list.append( $li );

		// 创建缩略图
		// 如果为非图片文件，可以不用调用此方法。
		// thumbnailWidth x thumbnailHeight 为 100 x 100
		uploader.makeThumb( file, function( error, src ) {
			if ( error ) {
				$img.replaceWith('<span>不能预览</span>');
				return;
			}

			$img.attr( 'src', src );
		}, 100, 100 );
		uploader.upload();
	});


	uploader.on( 'uploadBeforeSend', function( block, data ) {

		// 修改data可以控制发送哪些携带数据。
		data.folder = 'upload/banner/';
	});

	// 文件上传过程中创建进度条实时显示。
	uploader.on( 'uploadProgress', function( file, percentage ) {
		var $li = $( '#'+file.id ),
				$percent = $li.find('.progress span');

		// 避免重复创建
		if ( !$percent.length ) {
			$percent = $('<p class="progress"><span></span></p>')
					.appendTo( $li )
					.find('span');
		}

		$percent.css( 'width', percentage * 100 + '%' );
	});

// 文件上传成功，给item添加成功class, 用样式标记上传成功。
	uploader.on( 'uploadSuccess', function( file ) {

		$("#img").val("https://egold.oss-cn-shenzhen.aliyuncs.com/upload/banner/"+file.name);
		parent.layer.msg("上传成功");
	});

// 文件上传失败，显示上传出错。
	uploader.on( 'uploadError', function( file ) {
		var $li = $( '#'+file.id ),
				$error = $li.find('div.error');

		// 避免重复创建
		if ( !$error.length ) {
			$error = $('<div class="error"></div>').appendTo( $li );
		}

		$error.text('上传失败');
		parent.layer.msg("上传成功");
	});

// 完成上传完了，成功或者失败，先删除进度条。
	uploader.on( 'uploadComplete', function( file ) {
		$( '#'+file.id ).find('.progress').remove();
	});

});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});
function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/cms/banner/update",
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


function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			type : {
				required : true
			},
			name : {
				required : true
			},
			code : {
				required : true
			}
		},
		messages : {

			type : {
				required : icon + "请输入类型"
			},
			name : {
				required : icon + "请输入名称"
			},
			code : {
				required : icon + "请输入编码"
			}
		}
	})
}