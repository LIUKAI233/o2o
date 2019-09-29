$(function () {
    var loginUrl = "/myo2o/local/logincheck";
    var i = 0;
    var usertype = getQueryString("usertype");

    $("#submit").click(function () {
        //默认不需要输入验证码
        var needVerify = false;
        var username = $("#username").val();
        var password = $("#password").val();
        var verifyCodeActul = null;
        /*若密码输入错误超过3次，调用此方法，显示验证码*/
        if(i >= 3){
            document.getElementById("VerifyCode").style.display="";
            needVerify = true;
            verifyCodeActul = $("#j-kaptcha").val();
            if(verifyCodeActul == null){
                $.toast("请输入验证码");
                return;
            }
        }
        $.ajax({
            url:loginUrl,
            type: 'POST',
            data: {
                username : username,
                password : password,
                needVerify : needVerify,
                verifyCodeActul : verifyCodeActul
            },
            async: false,
            cache: false,
            success:function (data) {
                if(data.success){
                    $.toast("登录成功");
                    if (usertype == 1){
                        //若用户在前端展示系统，则自动返回前端只是系统首页
                        window.location.href="/myo2o/frontend/index";
                    }else{
                        //若用户在店家管理页面，则自动返回店家管理页面首页
                        window.location.href="/myo2o/shopadmin/shoplist";
                    }
                }else{
                    $.toast("登录失败！"+data.errMsg);
                    i++;
                }
                $('#kaptcha_img').click();
                document.getElementById("j-kaptcha").value = "";
            }
        })
    })
});