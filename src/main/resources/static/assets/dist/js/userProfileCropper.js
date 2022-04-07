var cropper;
$(function () {


    $('#saveProfileBtn').on('click', function () {

        var image = $('#image');

        var canvas;
        if ($('input[type="file"]').val() != "") {
            canvas = image.cropper('getCroppedCanvas', {
                width: 300,
                height: 300
            });

            canvas.toBlob(function (blob) {
                var formData = new FormData();
                formData.append('file', blob, 'profile.jpg');
                $.ajax('/profileUpdate', {
                    method: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        if (data != null) {
                            $('#profileImage').attr('src', data);
                            $('#cropperModal').modal("hide"); //닫기
                        } else {
                            alert('업로드 실패');
                        }
                    },
                    error: function () {
                        alert('업로드 실패');
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
    $('.them_img').empty().append('<img id="image" src="" style="height: auto; width: 100%">');
    var image = $('#image');
    var imgFile = $('#photoBtn').val();
    var fileForm = /(.*?)\.(jpg|jpeg|png)$/;
    var width =$( window ).width();
    var height;
    if (width < 530) {
        width = $(window).width() - 50;
        height = 400;
    }
    else{
        width =470;
        height =500;
    }

    if (imgFile.match(fileForm)) {
        var reader = new FileReader();
        reader.onload = function () {
            image.attr("src", event.target.result);
            cropper = image.cropper({
                dragMode: 'move',
                viewMode: 1,
                aspectRatio: 1,
                autoCropArea: 1,
                maxContainerWidth: width,
                maxContainerHeight: height,
                minContainerWidth: width,
                minContainerHeight: height
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

