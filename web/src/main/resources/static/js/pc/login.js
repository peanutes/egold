$(function () {



    $("#pcLogin").unbind("click").click(function () {
        var password = $("#password").val(),
            mobile = $("#mobile").val();

        if (!mobile) {
            showMessage("登陆账号不允许为空", "3000", true);
            return;
        }

        if (!password) {
            showMessage("密码不允许为空", "3000", true);

            return;
        }


        disableElementAndSetText($("#pcLogin"), "登陆中...");


        $.post(getApiUrl("/member/member/login"), {
                userName: mobile,
                password: password
            }, function (result) {
                if (isReturnSuccess(result)) {
                    goToPage("/pc/myAccount");
                } else {
                    showMessage(result.msg, "3000", true);
                    enableElementAndSetText($("#pcLogin"), "登陆");
                }
            }
        );
    });
});