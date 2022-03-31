$(function(){
    var cropper;
    // 사진 업로드 버튼
    $('#photoBtn').on('change', function(){
        $('.them_img').empty().append('<img id="image" src="">');
        var image = $('#image');
        var imgFile = $('#photoBtn').val();
        var fileForm = /(.*?)\.(jpg|jpeg|png)$/;

        // 이미지가 확장자 확인 후 노출
        if(imgFile.match(fileForm)) {
            var reader = new FileReader();
            reader.onload = function(event) {

                image.attr("src", event.target.result);
                cropper = image.cropper( {
                    dragMode: 'move',
                    viewMode:1,
                    aspectRatio: 1,
                });
            };
            reader.readAsDataURL(event.target.files[0]);
            $('#cropperModal').modal('show');
        } else{
            alert("이미지 파일(jpg, png형식의 파일)만 올려주세요");
            $('#photoBtn').focus();
            return;
        }
    });
    // 사진 다시 올리기 버튼
    $('#resetPhoto').on('click', function(){
        if($('input[type="file"]').val() != ""){
            $('#photoBtn').val('');
            $('.them_img img').attr('src','').remove();
            $('.btn_wrap a:last-child').removeClass('bg1');
            $('input[type="file"]').click();
        }else{
            //alert('업로드된 사진이 없습니다.');
        }
    });
    // 업로드 버튼
    $('#complete').on('click', function(){
        $('.them_img').append('<div class="result_box"><img id="result" src=""></div>');
        var image = $('#image');
        var result = $('#result');
        var canvas;
        if($('input[type="file"]').val() != ""){
            canvas = image.cropper('getCroppedCanvas',{
                width:200,
                height:200
            });
            result.attr('src',canvas.toDataURL("image/jpg"));

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
        }else{
            alert('사진을 업로드 해주세요');
            $('input[type="file"]').focus();
            return;
        }
    });
});
function onClickUpload() {
    let myInput = document.getElementById("photoBtn");
    myInput.click();
}