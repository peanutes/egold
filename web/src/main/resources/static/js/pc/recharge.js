$(function () {

    $("#recharge").unbind("click").click(function () {
        var rechargeAmount = $("#rechargeAmount").val();
        var bankChannelId = $("#bankChannelId").val();

        if (!bankChannelId) {
            showMessage("请先选择银行", "3000");
            /*$('#rechargeAmount').toast({
             content:'请填写充值金额',
             duration:1000
             });*/
            return;
        }


        if (!rechargeAmount) {
            showMessage("请填写充值金额", "3000");
            /*$('#rechargeAmount').toast({
                content:'请填写充值金额',
                duration:1000
            });*/
            return;
        }

        disableElementAndSetText($("#recharge"), "充值中...");

        var newTab=top.window.open('/pc/gotoLoading');
        $.post(getApiUrl("/fund/pcRecharge"), {
            bankChannelId:bankChannelId,
            rechargeAmount:rechargeAmount

        }, function (result) {
         if (isReturnSuccess(result)) {
             newTab.location.href = result.data;
         } else {
             newTab.close();

             if (result && (result.code==systemConfig.BUS_NO_LOGIN || result.code==systemConfig.BUS_TOKEN_EXPIRED)) {
                 top.location="/pc/gotoLogin"
             } else {
                 showMessage(result.msg, "3000", true);
                 enableElementAndSetText($("#recharge"), "充值");
             }

         }

        });
     });

});