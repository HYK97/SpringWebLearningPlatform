$(document).ready(function(){
    $(document).on("click", "#createBtn", function () {
        var formData = new FormData();



        formData.append('title',$("#title").val() );
        formData.append('contents',$("#contents").val() );


        var len = $('#file')[0].files.length;
        for (var i = 0; i < len; i++) {
            formData.append("file", $('#file')[0].files[i]);
        }


        $.ajax({
            type: "post",
            enctype: 'multipart/form-data',
            url: "/courseboard/createBoard",
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                alert(data);
            },
            error: function (request, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                alert("오류");
            }
        });


    });


})

