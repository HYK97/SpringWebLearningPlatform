var fileCount = 0;
// 해당 숫자를 수정하여 전체 업로드 갯수를 정한다.
// 파일 고유넘버
var fileNum = 0;
// 첨부파일 배열
var content_files = new Array();




$(document).on("change", "#file", function () {
    $('#fileChange').empty();
    fileCount = 0;
// 해당 숫자를 수정하여 전체 업로드 갯수를 정한다.
// 파일 고유넘버
    fileNum = 0;
// 첨부파일 배열
    content_files = new Array();

    var files = $('#file')[0].files;

    // 파일 배열 담기
    var filesArr = Array.prototype.slice.call(files);

    // 파일 개수 확인 및 제한

    fileCount = fileCount + filesArr.length;


    // 각각의 파일 배열담기 및 기타
    filesArr.forEach(function (f) {
        var reader = new FileReader();
        reader.onload = function (e) {
            content_files.push(f);
            $('#fileChange').append(
                '<div id="file' + fileNum + '" onclick="fileDelete(\'file' + fileNum + '\')">'
                + '<font style="font-size:12px">' + f.name + '</font>'
                + '<svg style="width:20px; height:auto; vertical-align: middle; cursor: pointer;" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="mx-1 bi bi-dash-square" viewBox="0 0 16 16">\n' +
                '  <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"/>\n' +
                '  <path d="M4 8a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7A.5.5 0 0 1 4 8z"/>\n' +
                '</svg>'
                + '<div/>'
            );
            fileNum ++;
        };
        reader.readAsDataURL(f);
    });
    console.log(content_files);
})

// 파일 부분 삭제 함수
function fileDelete(fileNum){
    var no = fileNum.replace(/[^0-9]/g, "");
    content_files[no].is_delete = true;
    $('#' + fileNum).remove();
    fileCount --;
    console.log(content_files);
}

/*
 * 폼 submit 로직
 */







$(document).ready(function(){


    $(document).on("click", "#createBtn", function () {
        var formData = new FormData();



        formData.append('title',$("#title").val() );
        formData.append('contents',$("#contents").val() );


        for (var x = 0; x < content_files.length; x++) {
            // 삭제 안한것만 담아 준다.
            if(!content_files[x].is_delete){
                formData.append("file", content_files[x]);
            }
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




