$(document).ready(function() {

    $.ajax({
        type : "post",
        url : "/user/getUser",
        cache : false,
        success : function(data){
            if (data != null) {
                if (data.role == 'ROLE_USER') {
                    $('#manager').remove();
                } else {
                    $('#user').after('<li><a id="manager" class="dropdown-item" href="/course/createview">강의 생성</a></li>');
                    $('#user').after('<li><a id="manager" class="dropdown-item" href="/course/info/mycourselist">강의 관리</a></li>');
                }
                if (data.profileImage != null) {
                    $("#header-profileImage").attr('src',data.profileImage);
                }

            }else {
                alert("세션종료");
            }
        },
        error:function(request, error) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert("세션오류");
        }
    });


});
