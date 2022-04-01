var cropper;
$(function () {


    $('#complete').on('click', function () {
        $('.them_img').append('<div class="result_box"><img id="result" src=""></div>');
        var image = $('#image');
        var result = $('#result');
        var canvas;
        if ($('input[type="file"]').val() != "") {
            canvas = image.cropper('getCroppedCanvas', {
                width: 500,
                height: 500
            });
            result.attr('src', canvas.toDataURL("image/jpg"));

            canvas.toBlob(function (blob) {
                var formData = new FormData();

                $.ajax('보낼곳 url', {
                    method: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function () {
                        alert('업로드 성공');
                    },
                    error: function () {
                        alert('업로드 실패');
                        $('.result_box').remove()
                    },
                });
            })
        } else {
            alert('사진을 업로드 해주세요');
            $('input[type="file"]').focus();
            return;
        }
    });
});

function onClickUpload() {
    let myInput = $("#photoBtn");
    $("#photoBtn").val('');
    myInput.trigger('click');

}

$('.modal-close').on('click', function () {
    $('#cropperModal').modal("hide"); //닫기
});

$('#photoBtn').on('change', function () {
    $('.them_img').empty().append('<img id="image" src="">');
    var image = $('#image');
    var imgFile = $('#photoBtn').val();
    var fileForm = /(.*?)\.(jpg|jpeg|png)$/;

    if (imgFile.match(fileForm)) {
        var reader = new FileReader();
        reader.onload = function () {

            image.attr("src", event.target.result);

            cropper = image.cropper({
                dragMode: 'move',
                viewMode: 1,
                aspectRatio: 1,
                minContainerWidth: 470,
                minContainerHeight: 500
            });


        };
        reader.readAsDataURL(event.target.files[0]);
        $('#cropperModal').modal("show");
    } else {
        alert("이미지 파일(jpg, png형식의 파일)만 올려주세요");
        $('#photoBtn').focus();
        return;
    }
});

