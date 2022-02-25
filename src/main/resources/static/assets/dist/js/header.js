$(document).ready(function() {

    $.ajax({
        type : "post",
        url : "/user/role",
        cache : false,
        success : function(data){
            if (data == 'ROLE_USER') {
                $('#manager').remove();
            } else {
                $('#user').after('<li><a id="manager" class="dropdown-item" href="/course/createview">강의 생성</a></li>');
            }
        },
        error:function(request, error) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert("세션오류");
        }
    });


});
