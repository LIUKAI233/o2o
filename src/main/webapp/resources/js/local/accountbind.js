$(function () {
    var bindUrl = "/myo2o/local/bindlocalauth";
    //从地址栏获取usertype
    //usertype=1为前端展示系统，其他为店家管理系统
    var usertype = getQueryString('usertype');
    $("#submit").click(function () {
        var username = $("#username").val();
        var password = $("#password").val();
        var verifyCodeActul = $('#j-kaptcha').val();
        if (verifyCodeActul == null || verifyCodeActul == "") {
            $.toast('请输入验证码!');
            return;
        }
        $.ajax({
            url: bindUrl,
            type: 'POST',
            data: {
                username : username,
                password : password,
                verifyCodeActul : verifyCodeActul
            },
            async: false,
            cache: false,
            success:function (data) {
                if(data.success){
                    $.toast("绑定成功！");
                    if (usertype == 1){
                        //若用户在前端展示系统，则自动返回前端只是系统首页
                        window.location.href="/myo2o/frontend/index";
                    }else{
                        //若用户在店家管理页面，则自动返回店家管理页面首页
                        window.location.href="/myo2o/shopadmin/shoplist";
                    }
                }else{
                    $.toast("绑定失败！"+data.errMsg);
                }
                $('#kaptcha_img').click();
                document.getElementById("j-kaptcha").value = "";
            }
        })
    })
})