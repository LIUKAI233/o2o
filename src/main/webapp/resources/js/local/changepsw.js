$(function () {
    var changepswUrl = "/myo2o/local/changelocalpwd";
    var usertype = getQueryString("usertype");

    $("#submit").click(function () {
        //获取相关数据
        var username = $("#username").val();
        var password = $("#password").val();
        var newpassword = $("#newpassword").val();
        var confirmpassword = $("#confirmpassword").val();
        if(newpassword != confirmpassword){
            $.toast("两次密码输入不一致");
            return;
        }
        var verifyCodeActul = $("#j-kaptcha").val();
        //判断验证码输入
        if (verifyCodeActul == null || verifyCodeActul == ""){
            $.toast("请输入验证码");
            return;
        }

        $.ajax({
            url:changepswUrl,
            type:'POST',
            data:{
                username:username,
                password:password,
                newpassword:newpassword,
                verifyCodeActul:verifyCodeActul
            },
            async: false,
            cache: false,
            success:function (data) {
                if(data.success){
                    window.location.href="/myo2o/local/login?usertype="+usertype;
                }else {
                    $.toast(data.errMsg);
                }
                $('#kaptcha_img').click();
                document.getElementById("j-kaptcha").value = "";
            }
        })
    })
});