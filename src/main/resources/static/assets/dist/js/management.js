var fileCount = 0;
// 해당 숫자를 수정하여 전체 업로드 갯수를 정한다.

// 파일 고유넘버
var fileNum = 0;
// 첨부파일 배열
var content_files = new Array();

let courseName;


$(document).on("change", "#file", function () {

    var files = $('#file')[0].files;
    var filesArr = Array.prototype.slice.call(files);
    if (fileCount + filesArr.length > 10) {
        alert('10개이상은 첨부하실수없습니다.');
        return;
    }


    filesArr.forEach(function (f, index) {
        if (f.size > 10 * 1024 * 1024) {
            alert(f.name + '해당 파일크기가 너무큽니다. 업로드에서 제외됩니다.');
            delete filesArr[index];
        }
    });


    fileRender(filesArr, '#fileChange');
    console.log(content_files);
})

function fileDelete(fileNum) {
    var no = fileNum.replace(/[^0-9]/g, "");
    content_files[no].is_delete = true;
    $('#' + fileNum).remove();
    fileCount--;
    console.log(content_files);
}

var viewSize=1;
$(document).ready(function () {
    var listeners = window.matchMedia("screen and (min-width: 768px)");
    if (!listeners.matches) {
        viewSize = 0;
    }
    listeners.addListener(function (e) {
        if (e.matches) {
            viewSize=1;//pc
        } else {
            viewSize=0;//모바일
        }
    });

    if (courseBoard.length > 0) {
        $('#viewBox').removeAttr("hidden");
        let index = courseBoard[0].id;
        navRender(courseBoard);
        mainRender(index, courseBoard);
        $(".courseboard-href").first().addClass('active');
    } else {

        alert("생성된 강의 목차가없습니다. 강의 회차를 추가해주세요");
        createBoxShow();
    }

})

$(document).on("click", "#createBtn", function () {
    var courseId = getCourseId();
    var check = formBtn('#courseBoardForm');
    if (check == 1) {
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
                courseBoard = getData();
                navRender(courseBoard);
                $(".courseboard-href").last().trigger("click");
                viewBoxShow();
                $("form")[0].reset();
                $("form").removeClass("was-validated");
                $('#fileChange').empty();
                $('#contents').summernote('reset');
                fileReset();
                alert('등록성공');
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


$(document).on("click", "#deleteBtn", function () {
    let deleteText = $("#deleteText").val();
    if (deleteText !== ("삭제하기")) {
        alert("다시입력해주세요");
        return;
    }

    $.ajax({
        type: "post",
        url: "/courseboard/deleteCourseBoard/" + courseBoardId,
        success: function (data) {
            if (data == "1") {
                alert("삭제성공");
                courseBoard = getData();
                navRender(courseBoard);
                $(".courseboard-href").removeClass("active");
                $(".courseboard-href").first().trigger("click");
                viewBoxShow();
                $('#deleteText').val('');
            } else {
                alert("error 삭제실패");
            }
        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });
});


$(document).on("click", "#courseUpdateBtn", function () {
    var courseId = getCourseId();
    var check = formBtn('#courseUpdateForm');
    if (check == 1) {
        return;
    }
    let courseName = $("#courseName").val();
    let courseExplanation = $("#courseExplanation").val();
    var formData = new FormData();

    formData.append('courseName', courseName);
    formData.append('courseExplanation', courseExplanation);
    formData.append("thumbnail", $("#thumbnail")[0].files[0]);

    $.ajax({
        type: "post",
        url: "/course/update/" + courseId,
        processData: false,
        contentType: false,
        async: false,
        data: formData,
        success: function (data) {
            if (data == "1") {
                $(".courseboard-href").first().trigger("click");
                viewBoxShow();
                $("form")[0].reset();
                $("form").removeClass("was-validated");
                $('#contents').summernote('reset');
                viewBoxShow();
                alert("업데이트 성공");
            } else {
                alert("업데이트 실패");
            }
        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });

});

/*
* contents: "courseBoardContents0"
courseName: "test5"
createDate: "2022-03-21"
files: []
id: 26
teachName: "tuser5"
title: null
views: 3
* */

$(document).on("click", "#courseInfoUpdateBtn", function () {
    var courseId = getCourseId();
    $(".nav-link").removeClass("active");
    $(this).addClass('active');

    $.ajax({
        type: "get",
        url: "/course/getCourse/" + courseId,
        async: false,
        success: function (data) {
            $("#courseExplanation").summernote('code', data.courseExplanation);
            $("#courseName").val(data.courseName);
            courseName=data.courseName;

            $("#thumbnailImg").attr("src", data.thumbnail);
        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });
    updateBoxShow();
    toggleBtn();
});


$(document).on("click", "#update-tab", function () {


    $.ajax({
        type: "get",
        url: "/courseboard/getCourseBoard/" + courseBoardId,
        success: function (data) {
            $('#updateFileChange').empty();
            fileReset();
            $("#updateTitle").val(data.title);
            $("#updateContents").summernote('code', data.contents);
            var array = new Array();
            data.files.forEach(function (f) {
                var size = "1".repeat(f.fileSize)
                var file = new File([size], f.origFileName, {type: "image/png", lastModified: new Date().getTime()});
                array.push(file);
            });
            fileRender(array, '#updateFileChange');

        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });


});

function fileRender(filesArr, target) {
    filesArr.forEach(function (f) {
        var reader = new FileReader();
        reader.onload = function (e,) {
            content_files.push(f);
            $(target).append(
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
}

$(document).on("change", "#updateFile", function () {

    var files = $('#updateFile')[0].files;
    var filesArr = Array.prototype.slice.call(files);

    if (fileCount + filesArr.length > 10) {
        alert('10개이상은 첨부하실수없습니다.');
        return;
    }

    filesArr.forEach(function (f, index) {
        if (f.size > 10 * 1024 * 1024) {
            alert(f.name + '해당 파일크기가 너무큽니다. 업로드에서 제외됩니다.');
            delete filesArr[index];
        }
    });

    fileRender(filesArr, '#updateFileChange');
    console.log(content_files);
})

function fileReset() {
    fileCount = 0;
    fileNum = 0;
    content_files = new Array();
}


$(document).on("click", "#addBtn", function () {

    $(".nav-link").removeClass("active");
    $(this).addClass('active');
    fileReset();
    $("#courseBoardForm")[0].reset();
    $("#courseBoardForm").removeClass("was-validated");
    $("#fileChange").empty();
    $('#contents').summernote('reset');

    createBoxShow();
    toggleBtn();
});


$(document).on("click", "#updateCourseBoardBtn", function () {
    var check = formBtn('#courseBoardUpdateForm');
    if (check == 1) {

        return;
    }
    let title = $("#updateTitle").val();
    let contents = $("#updateContents").val();
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
        url: "/courseboard/updateCourseBoard/" + courseBoardId,
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            if (data == "1") {
                courseBoard = getData();
                navRender(courseBoard);
                mainRender(courseBoardId, courseBoard)
                let index = courseBoard.findIndex(f => f.id == courseBoardId);
                $(".courseboard-href").eq(index).trigger("click");
                viewBoxShow();
                $("#courseBoardUpdateForm")[0].reset();
                $("#courseBoardUpdateForm").removeClass("was-validated");
                $('#updateFileChange').empty();
                $('#updateContents').summernote('reset');
                fileReset();
                alert('수정 성공');
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




$(document).on("click", '#statisticsBtn', function () {
    $(".nav-link").removeClass("active");
    $(this).addClass('active');
    statisticsBoxShow();
    toggleBtn();
});
$(document).on("click", '#deleteModalB', function () {
    $("#confirm").text(courseName+"삭제하기");
    $("#deleteConfirm").attr("placeholder",courseName+"삭제하기");


    $("#deleteModal").modal('show');
});

function toggleBtn() {
    if (viewSize == 0) {
        $('.navbar-toggler').trigger('click');
    }
}



$(document).on("click", '.deleteUserCourseBtn', function () {
    let text = $("#deleteConfirm").val();
    if (text == courseName + "삭제하기") {
        $.ajax({
            url: '/course/deleteCourse/'+getCourseId(),
            type: 'post',
            async: false,
            success: function (data) {
                if (data == "1") {
                    alert("삭제성공");
                    location.href = '/main/index';
                } else {
                    $('#feedback').css('display', 'none');
                    $('#deleteConfirm').removeClass('fail-input');
                    $("#deleteModal").modal('hide');
                    alert("삭제실패");
                }

            },
            error: function (request, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                alert("오류");
            }
        })

    } else {

        $('#feedback').css('display', 'block');
        $('#deleteConfirm').addClass('fail-input');
    }
});
