const template = '<div>\n' +
    '    {{#data.content}}\n' +
    '        <div class="row my-1 ">\n' +
    '            <div >\n' +
    '                <div class="text-break">\n' +
    '                    <div >\n' +
    '                        <div class="user-field">\n' +
    '                            <div class="mx-2">\n' +
    '                            {{#user.profileImage}}\n' +
    '                                <img src="{{user.profileImage}}" alt="mdo" width="32" height="32"\n' +
    '                                     class="rounded-circle">\n' +
    '                            {{/user.profileImage}}\n' +
    '                            {{^user.profileImage}}\n' +
    '                                <img src="https://github.com/mdo.png" alt="mdo" width="32" height="32"\n' +
    '                                     class="rounded-circle">\n' +
    '                            {{/user.profileImage}}\n' +
    '                            </div>\n' +
    '                            <div class="user-field-name">\n' +
    '                                <div> <p class=" my-3 ">{{user.nickname}}</p></div>\n' +

    '               {{#teachUser}}{{^reply}} ' +
    '                <div class="dropdown dropdown-user text-end" hidden data-user="{{user.username}}">\n' +
    '                <a href="#" class="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">\n' +
    '                </a>\n' +
    '                <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">\n' +
    '                    <li><a class="dropdown-item createReply" data-bs-toggle="modal" data-id="{{id}}" data-bs-target="#exampleModal3">답글쓰기</a></li>\n' +
    '                </ul>{{/reply}}{{/teachUser}}\n' +

    '               {{^teachUser}}' +
    '                <div class="dropdown dropdown-user text-end" hidden data-user="{{user.username}}">\n' +
    '                <a href="#" class="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">\n' +
    '                </a>\n' +
    '                      <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">\n' +
    '                    <li><a class="dropdown-item update" data-bs-toggle="modal" data-scope="{{scope}}" data-comments="{{comments}}" data-id="{{id}}"data-bs-target="#exampleModal" >수정</a></li>\n' +
    '                    <li><a class="dropdown-item delete" data-id="{{id}}">삭제</a></li>\n' +
    '                </ul>{{/teachUser}}\n' +
    '            </div>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                        <div class="d-flex justify-content-between align-items-center">\n' +
    '                            <div class="btn-group">\n' +
    '                                <div class="star-ratings">\n' +
    '                                    <div id="star" style="width: {{starScope}}%"\n' +
    '                                         class="star-ratings-fill space-x-2 text-lg">\n' +
    '                                        <span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>\n' +
    '                                    </div>\n' +
    '                                    <div class="star-ratings-base space-x-2 text-lg">\n' +
    '                                        <span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>\n' +
    '                                    </div>\n' +
    '                                </div>\n' +
    '                                <div class="star-ratings my-1">\n' +
    '                                    <small id="scope" class="text-muted"> ({{scope}})</small>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                        <p class="text-muted my-2 comments">\n' +
    '                            {{comments}}\n' +
    '                        </p>\n' +
    '\n' +
    '                        <small class=" my-3 create-date">\n' +
    '                            {{createDate}}\n' +
    '                        </small>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '            {{#reply}}\n' +
    '                <div class="reply-box  text-break">\n' +
    '                            <div class="user-field-name">\n' +
    '                                <div> <p class=" my-3"> 강사 ' + getId().teachNickname + '님의 답글</p></div>\n' +
    '                   {{#teachUser}}\n' +
    '                                <div class="dropdown text-end">\n' +
    '                <a href="#" class="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">\n' +
    '                </a>\n' +
    '                <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">\n' +
    '                    <li><a class="dropdown-item update" data-id="{{replyId}}" data-comments="{{reply}}" data-bs-toggle="modal" data-bs-target="#exampleModal2">수정</a></li>\n' +
    '                    <li><a class="dropdown-item delete" data-id="{{replyId}}" >삭제</a></li>\n' +
    '                </ul>\n' +
    '                            </div>\n' +
    '                        {{/teachUser}}\n' +
    '                        </div>\n' +
    '                    <p class="text-muted my-2 ">\n' +
    '                        {{reply}}\n' +
    '                    </p>\n' +
    '                     <div>  \n' +
    '                    <small class=" my-3 create-date">\n' +
    '                        {{createDate}}\n' +
    '                    </small>\n' +
    '                    </div> \n' +
    '                </div>\n' +
    '            {{/reply}}\n' +
    '            <hr class=" mt-3 mb-2">\n' +
    '        </div>\n' +
    '    {{/data.content}}\n' +
    '</div>';

function aspect(maxWidth, maxHeight) {
    var ratio = 0; // Used for aspect ratio
    var width = $(this).width(); // Current image width
    var height = $(this).height(); // Current image height // Check if the current width is larger than the max
    if (width > maxWidth) {
        ratio = maxWidth / width; // get ratio for scaling image
        $(this).attr("width", maxWidth); // Set new width
        $(this).attr("height", height * ratio); // Scale height based on ratio
        height = height * ratio; // Reset height to match scaled image
    }
    var width = $(this).width(); // Current image width
    var height = $(this).height(); // Current image height // Check if current height is larger than max
    if (height > maxHeight) {
        ratio = maxHeight / height; // get ratio for scaling
        $(this).attr("height", maxHeight); // Set new height
        $(this).attr("width", width * ratio); // Scale width based on ratio
        width = width * ratio; // Reset width to match scaled image
    }
}
$(window).resize(function () {
    resizeIframe();
})
function resizeIframe() {
    var width = $('#courseExplanationBox').innerWidth();

    $('#courseExplanationBox').find("div,img,iframe").each(function () {
        aspect.call(this, width, 500);
    })
}
$(document).ready(function () {

    resizeIframe();
    joinPopOver();


    var Previous;
    var Next;
    var pageNumber;
    var totalElements;
    var preNum;
    var nexNum;
    var totalPages;
    var startPage;
    var endPage;

    if (getId().teachUser == true) {
        $('.review-container').remove();

    }

    $(document).on("click", "#joinBtn", function () {
        $("#applicationCheck").submit();
    });

    paging(0);

    //삭제
    $(document).on("click", ".delete", function () {
        $.ajax({
            type: "post",
            url: "/course/deleteevaluation/" + $(this).data("id") + "/" + getId().id,
            success: function (data) {
                if (data == "1") {
                    paging(0);
                    alert("삭제성공");
                } else if (data == "2") {
                    alert("허용되지않는 접근자");
                } else {
                    alert("삭제실패")
                }
            },
            error: function (request, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                alert("오류");
            }
        });

    });

    $('.modal').on('hidden.bs.modal', function (e) {
        $(this).find('form')[0].reset()
    });


// 수정
    let modalId;
    $(document).on("click", ".update", function (e) {
        let scope = String($(this).data('scope'));
        let id = $(this).data('id');
        let comments = $(this).data('comments');
        let target = $(this).data('bs-target');
        modalId = target;
        if (scope != null) {
            if (scope.indexOf('.') == -1) {
                scope = scope + '.0';
            }
            $(target).find("input:radio[name='star']:radio[value='" + scope + "']").prop('checked', true); // 선택하기
        }
        $(target).find('#modal-message-text').text(comments);
        $(target).find('#evaluationId').val(id);
    });

    $(document).on("click", ".createReply", function (e) {
        let id = $(this).data('id');
        let target = $(this).data('bs-target');
        modalId = target;
        $(target).find('#replyId').val(id);
    });


    $(document).on("click", ".modalBtn", function () {
        if (!starFormCheck(modalId + " form")) {
            if (modalId == "replyUpdate") {
                alert("답글을 입력해주세요");
            } else {
                alert("별점과 수강평을 입력해주세요");
            }
            return;
        }

        var queryString = $(modalId + " form").serialize();
        $.ajax({
            type: "post",
            url: "/course/updateevaluation",
            data: queryString,
            success: function (data) {
                if (data == "1") {
                    $(modalId).modal('hide');
                    paging();
                    alert("수정성공");
                } else {
                    alert("실패");
                }

            },
            error: function (request, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                alert("오류");
            }
        });
    });


    //페이징 및 댓글데이터 불러오기
    function paging(page = 0, sort = "createDate-Desc") {
        $.ajax({
            type: "get",
            url: "/course/commentsview",
            data: {
                courseId: getId().id,
                page: page,
                sortBy: sort
            },
            success: function (data) {
                //댓글데이터
                $('#result').empty();
                $('.page').remove();

                Mustache.parse(template);
                var jsonData = {
                    "data": data.CourseEvaluationDto,
                    "applicationCheck": getId().applicationCheck,
                    "teachUser": getId().teachUser
                }
                var rendered = Mustache.render(template, jsonData);
                $('#result').html(rendered);

                $('.dropdown-user').each(function () {
                    let data1 = $(this).data("user");
                    if (data1 == getId().loginUser || jsonData.teachUser == true) {
                        $(this).removeAttr('hidden');
                    }
                })


                //페이징
                pageNumber = data.CourseEvaluationDto.pageable.pageNumber;
                totalPages = data.CourseEvaluationDto.totalPages;
                startPage = Math.floor(pageNumber / 10) * 10 + 1;
                endPage = startPage + 9 < totalPages ? startPage + 9 : totalPages;
                pageNumber++;
                totalElements = data.totalElements
                preNum = parseInt(pageNumber) - 1;
                nexNum = parseInt(pageNumber) + 1;
                Previous = pageNumber != 1 ? true : false;
                Next = totalPages != pageNumber ? true : false;
                var html = "";

                for (var num = startPage; num <= endPage; num++) {
                    if (num == pageNumber) {
                        html += '<li class="page-item active"><a class="page-link page">' + num + '</a></li>';
                    } else {
                        html += '<li class="page-item"><a class="page-link page" >' + num + '</a></li>';
                    }
                }
                $("#previous").after(html);
                if (Previous) {
                    $("#previous").removeClass("disabled");
                } else {
                    $("#previous").addClass("disabled");
                }
                if (Next) {
                    $("#next").removeClass("disabled");
                } else {
                    $("#next").addClass("disabled");
                }
            },
            error: function (request, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                alert("세션오류");
            }
        });
    }


    //페이징 클릭 이벤트
    $(document).on("click", ".page", function () {
        let page = $(this).text();
        let sort = $('.text-success').data("sort");
        paging(page, sort);
    });
    $(document).on("click", "#previousAtag", function () {

        let sort = $('.text-success').data("sort");
        paging(preNum, sort);
    });
    $(document).on("click", "#nextAtag", function () {
        let sort = $('.text-success').data("sort");
        paging(nexNum, sort);
    });
    $(document).on("click", ".review-sort", function () {
        let sort = $(this).data("sort");
        $('.review-sort').removeClass("text-success");
        $(this).addClass("text-success");
        paging(0, sort);
    });


    //강사답변쓰기 클릭
    $(document).on("click", "#replyCreateBtn", function () {
        if (!starFormCheck("#replyCreate")) {
            alert("답글을 입력해주세요");
            return;
        }

        var queryString = $("form[name=replyCreate]").serialize();
        $.ajax({
            type: "post",
            url: "/course/createevaluation",
            data: queryString,
            success: function (data) {
                if (data == "1") {
                    $(modalId).modal('hide');
                    paging();
                } else if (data == "2") {
                    alert("잘못된 접근입니다.");
                } else {
                    paging();
                    alert("이미 작성하셨습니다.");
                }
            },
            error: function (request, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                alert("오류");
            }
        });
    });


    //수강평쓰기 버튼클릭
    $(document).on("click", "#evaluationBtn", function () {
        if (!starFormCheck("#evaluation")) {
            alert("별점과 수강평을 입력해주세요");
            return;
        }
        var queryString = $("form[name=evaluation]").serialize();
        $.ajax({
            type: "post",
            url: "/course/createevaluation",
            data: queryString,
            success: function (data) {
                if (data == "1") {
                    paging();
                } else if (data == "2") {
                    alert("잘못된 접근입니다.");
                } else {
                    history.go(0);
                    alert("이미 작성하신 수강평이있습니다.");
                }
            },
            error: function (request, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                alert("오류");
            }
        });
    });

});

function joinPopOver() { //팝오버
    let trigger = true;
    const URLSearch = new URLSearchParams(location.search);
    let checkBtn = URLSearch.get('joinBtn');
    var popover;
    if (checkBtn == 'true') {
        $("#joinBtn").toggleClass("emphasized");
        var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
        var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
            return new bootstrap.Popover(popoverTriggerEl)
        })
        var btn = document.getElementById('joinBtn');
        if (btn != null) {

            popover = new bootstrap.Popover(btn);
            popover.show();
            trigger = false;
        }
    }
    $(document).on("click", "body", function () {
        if (checkBtn == 'true' && trigger == false) {
            $("#joinBtn").removeClass("emphasized");
            popover.hide();
        }
    });
}



