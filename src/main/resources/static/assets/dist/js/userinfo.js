$(document).on('click', '.user', function () {

    location.href ="/user/info";
});
$(document).on('click', '.security', function () {

    location.href ="/user/security";
});



$(document).on('click', '#userInfoChangeBtn', function () {
        var check = checkForm('.needs-validation');
    if (check == 1) {

        return;
    }
    var data = $(".needs-validation").serialize();
    $.ajax({
        type : "post",
        data : data,
        url : "/user/update",
        success : function(data){
            if (data != null) {
                $('#email').val(data.email);
                $('#nickname').val(data.nickname);
                $('#selfIntroduction').val(data.selfIntroduction);
                alert("변경 완료");
            } else {
                alert("실패");
            }
        },
        error:function(request, error) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert("세션오류");
        }
    });

});

$(document).on('click', '#passwordChangeBtn', function () {
    var check = checkForm('.needs-validation');
    if (check == 1) {
        return;
    }
    var newPassword = $('#newPassword').val();
    var newPasswordAgain = $('#newPasswordAgain').val();
    if (newPassword != newPasswordAgain) {
        alert("비밀번호가 서로 다릅니다");
        return;
    }
    var data = $(".needs-validation").serialize();
    $.ajax({
        type : "post",
        data : data,
        url : "/user/passwordChange",
        success : function(data){
            if (data =="1") {
                $('.needs-validation')[0].reset();
                alert("변경 완료");
            } else if (data == "2") {
                $('.needs-validation')[0].reset();
                alert("비밀번호 다름");
            } else {
                alert("오류");

            }

        },
        error:function(request, error) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert("세션오류");
        }
    });

});