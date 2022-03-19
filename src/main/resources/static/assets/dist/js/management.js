var fileCount = 0;
// 해당 숫자를 수정하여 전체 업로드 갯수를 정한다.

// 파일 고유넘버
var fileNum = 0;
// 첨부파일 배열
var content_files = new Array();


$(document).on("change", "#file", function () {

    var files = $('#file')[0].files;
    var filesArr = Array.prototype.slice.call(files);

    if (fileCount + filesArr.length > 10) {
        alert('10개이상은 첨부하실수없습니다.');
        return;
    }



    // 각각의 파일 배열담기 및 기타
    /*for (let i = 0; i < filesArr.length; i++) {
            if (filesArr[i].size > 10 * 1024 * 1024) {
                alert(filesArr[i].name+'해당 파일크기가 너무큽니다. 업로드에서 제외됩니다.');
                filesArr.splice(i);
                break ;
            }
        var reader = new FileReader();
        reader.onload = function (e) {
            content_files.push(filesArr[i]);
            $('#fileChange').append(
                '<div id="file' + fileNum + '" onclick="fileDelete(\'file' + fileNum + '\')">'
                + '<font style="font-size:12px">'+(i+1)+ filesArr[i].name + '</font>'
                + '<svg style="width:20px; height:auto; vertical-align: middle; cursor: pointer;" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="mx-1 bi bi-dash-square" viewBox="0 0 16 16">\n' +
                '  <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"/>\n' +
                '  <path d="M4 8a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7A.5.5 0 0 1 4 8z"/>\n' +
                '</svg>'
                + '<div/>'
            );
            fileNum ++;
        };
        reader.readAsDataURL(filesArr[i]);
    }*/


    filesArr.forEach(function (f, index) {
        if (f.size > 10 * 1024 * 1024) {
            alert(f.name + '해당 파일크기가 너무큽니다. 업로드에서 제외됩니다.');
            delete filesArr[index];
        }
    });


    filesArr.forEach(function (f) {
        var reader = new FileReader();
        reader.onload = function (e,) {
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
            fileNum++;
            fileCount++;
        };
        reader.readAsDataURL(f);
    });
    console.log(content_files);
})

function fileDelete(fileNum) {
    var no = fileNum.replace(/[^0-9]/g, "");
    content_files[no].is_delete = true;
    $('#' + fileNum).remove();
    fileCount--;
    console.log(content_files);
}


$(document).ready(function () {
    var courseId = getCourseId();
    $(document).on("click", "#createBtn", function () {
        var check = formBtn();
        if (check == 1) {
            $("#title").focus();
            return;
        }
        let title = $("#title").val();
        let contents = $("#contents").val();
        var formData = new FormData();


        formData.append('title', title);
        formData.append('contents', contents);


        for (var x = 0; x < content_files.length; x++) {
            if (!content_files[x].is_delete) {
                formData.append("file", content_files[x]);
            }
        }

        $.ajax({
            type: "post",
            enctype: 'multipart/form-data',
            url: "/courseboard/createBoard/" + courseId,
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                if (data == "1") {
                    alert('등록성공');
                    courseBoard = getData();
                    navRender(courseBoard);
                    $(".courseboard-href").removeClass("active");
                    $(".courseboard-href").last().trigger("click");
                    $('#createBox').attr("hidden", "hidden");
                    $('#viewBox').removeAttr("hidden");
                    $("form")[0].reset();
                    $("form").removeClass("was-validated");
                    $('#fileChange').empty();
                    $('#contents').summernote('reset');

                } else {
                    alert('오류');
                }
            },
            error: function (request, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                alert("오류");
            }
        });


    });


})




