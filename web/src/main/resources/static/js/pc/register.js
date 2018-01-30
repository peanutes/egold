$(function () {

    $(".register_get_checkcode_btn").unbind('click').click(function () {


        var mobile = $("#mobile").val();


        if (!mobile) {
            showMessage("请输入手机号码", "3000", true);
            return;
        }



        disableElementAndSetText($(".register_get_checkcode_btn"), "发送中...");



        $.post(getApiUrl("/sys/sms/send"), {
            mobile: mobile,
            smsTplType:'REGISTER'

            }, function (result) {
                if (isReturnSuccess(result)) {
                    enableElementAndSetText($(".register_get_checkcode_btn"), "短信验证码已发送");
                } else {
                     showMessage(result.msg, "3000", true);
                     enableElementAndSetText($(".register_get_checkcode_btn"), "获取短信验证码");
                }
            }
        );



    });


    $("#registerBtn").unbind('click').click(function(){

        var mobile = $("#mobile").val(),
            password = $("#password").val(),
            repassword = $("#repassword").val(),
            smsCode = $("#smsCode").val(),
            referee = $("#referee").val();


        if (!mobile) {
            showMessage("请输入手机号码", "3000", true);
            return;
        }
        if (!password) {
            showMessage("请输入密码", "3000", true);
            return;
        }

        if (!repassword) {
            showMessage("请输入确认密码", "3000", true);
            return;
        }
        if (!smsCode) {
            showMessage("请输入手机验证码", "3000", true);
            return;
        }

        if (!$("#agree").prop('checked')) {
            showMessage("请同意协议", "3000", true);
            return;
        }

        if (password != repassword) {
            showMessage("两次输入密码不一致，请重新输入", "3000", true);
            return;
        }


        disableElementAndSetText($("#registerBtn"), "注册中...");

        $.post(getApiUrl("/member/member/register"), {
            mobile: mobile,
            password:password,
            smsCode:smsCode,
            refereeMobile:referee
        }, function (result) {
            if (isReturnSuccess(result)) {
                top.window.location = "/pc/myAccount";
            } else {
                showMessage(result.msg, "3000", true);
                enableElementAndSetText($("#registerBtn"), "注册领黄金");
            }
        });


    });



});