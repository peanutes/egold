$(function () {

	$(".sysMobile").html(getLocalStorageItem("sysMobile","")+"&nbsp;");
	$(".sysQQ").html(getLocalStorageItem("sysQQ","")+"&nbsp;");
	$(".sysWechat").html(getLocalStorageItem("sysWechat","")+"&nbsp;");
	$(".sysEmail").html(getLocalStorageItem("sysEmail","")+"&nbsp;");
	$(".sysWebSite").html(getLocalStorageItem("sysWebSite","")+"&nbsp;&nbsp;&gt;");
	$(".sysWebSiteUrl").attr("href",getLocalStorageItem("sysWebSiteUrl",""));
	//去绑卡
	$("#my-bank-card").click(function () {
		goToPage("/user/toBankCardBinding");
	});

	//查看用户协议
	$("#userAgreement").click(function () {
		goToPage("/news/article?articleType=AGREEMENT_REGISTER");
	});

	//跳转关于我们
	$("#toAboutUs").click(function () {
		goToPage("/user/aboutUs");
	});

	//跳转到个人信息
	$("#toMyInfo").click(function () {
		goToPage("/user/myInfo");
	});

	//跳转到我的地址
	$("#toMyAddress").click(function () {
		goToPage("/myAddress/list");
	});


});


