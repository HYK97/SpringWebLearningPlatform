let pageNumber = 0;
let totalPages = 0;
let totalElements = 0;
let preNum = 0;
let nexNum = 0;
let Previous = null;
let Next = null;


const replys = '' +
    ' {{#data}}' +
    '        <div class="row my-1 " style="margin-left:3rem">\n' +
    '            <div >\n' +
    '            <hr class=" mt-3 mb-2">\n' +
    '                <div class="text-break">\n' +
    '                    <div >\n' +
    '                        <div class="user-field">\n' +
    '                            <div class="mx-2">\n' +
    '                                <img src="https://github.com/mdo.png" alt="mdo" width="32" height="32"\n' +
    '                                     class="rounded-circle">\n' +
    '                            </div>\n' +
    '                            <div class="user-field-name">\n' +
    '                                <div> <p class=" my-3 username">{{username}}</p></div>\n' +
    '               {{^myCommentsFlag}} ' +
    '                <div class="dropdown dropdown-user text-end" data-user="{{username}}">\n' +
    '                <a href="#" class="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">\n' +
    '                </a>\n' +
    '                <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">\n' +
    '                    <li><a class="dropdown-item  createReply" data-use="disabled" data-id="{{replyId}}">답글쓰기</a></li>\n' +
    '                </ul>{{/myCommentsFlag}}\n' +
    '               {{#myCommentsFlag}}' +
    '                <div class="dropdown dropdown-user text-end"  data-user="{{username}}">\n' +
    '                <a href="#" class="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">\n' +
    '                </a>\n' +
    '                      <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">\n' +
    '                    <li><a class="dropdown-item createReply" data-use="disabled" data-id="{{replyId}}">답글쓰기</a></li>\n' +
    '                    <li><a class="dropdown-item update"  data-id="{{replyId}}">수정</a></li>\n' +
    '                    <li><a class="dropdown-item delete" data-id="{{replyId}}">삭제</a></li>\n' +
    '                </ul>{{/myCommentsFlag}}\n' +
    '            </div>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                        <p class="text-muted my-2 comments">\n' +
    '                            {{{comments}}}\n' +
    '                        </p>\n' +
    '\n' +
    '                        <small class=" my-3 create-date">\n' +
    '                            {{createDate}}\n' +
    '                        </small>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            <hr class=" mt-3 mb-2 end">\n' +
    '            </div>\n' +
    '            </div>\n' +
    '          </div>\n' +
    ' {{/data}}'

function commentsRender(page) {
    $.ajax({
        type: "get",
        url: '/comments/getComments/' + courseBoardId,
        data: {
            page: page
        },
        success: function (data) {
            $('#commentsList').empty();
            $('.page').remove();
            let jsonData = {
                "data": data.content,
            };
            //페이징
            pageNumber = data.pageable.pageNumber + 1;
            totalPages = data.totalPages;
            totalElements = data.totalElements;
            preNum = parseInt(pageNumber) - 1;
            nexNum = parseInt(pageNumber) + 1;
            Previous = pageNumber != 1 ? true : false;
            Next = totalPages > pageNumber ? true : false;
            var html = "";

            for (var num = 1; num <= totalPages; num++) {
                var onclick = 'commentsRender(' + num + ')';
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
            Mustache.parse(comments);
            var rendered = Mustache.render(comments, jsonData);
            $('#commentsList').html(rendered);

        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    })
};


//페이징 클릭 이벤트
$(document).on("click", ".page", function () {
    let page = $(this).text();
    commentsRender(page);
});
$(document).on("click", "#previousAtag", function () {
    commentsRender(preNum);
});
$(document).on("click", "#nextAtag", function () {
    commentsRender(nexNum);
});

function getReplyData(commentsId, element) {

    let commentsPage = element.dataset.page;
    if (commentsPage == "1") {
        $.ajax({
            type: "get",
            url: '/comments/getReply/' + commentsId,
            data: {
                page: commentsPage
            },
            success: function (data) {
                element.dataset.page = parseInt(commentsPage) + 1;
                $('#collapse' + commentsId).find('a.more').remove();
                let jsonData = {
                    "data": data.content,
                };
                let pageNumber = data.pageable.pageNumber + 1;
                let totalPages = data.totalPages;
                ;
                let Next = totalPages > pageNumber ? true : false;

                Mustache.parse(replys);
                var rendered = Mustache.render(replys, jsonData);
                $('#collapse' + commentsId).append(rendered);
                if (Next) {
                    $('#collapse' + commentsId).append('<a class="page-link more" data-page="2"  onclick="replyAjax(' + commentsId + ',this)">▼댓글 더보기</a>')
                }
            },
            error: function (request, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                alert("오류");
            }
        })
    }
}

function replyAjax(commentsId, element) {
    let commentsPage = parseInt(element.dataset.page);
    $.ajax({
        type: "get",
        url: '/comments/getReply/' + commentsId,
        data: {
            page: commentsPage
        },
        success: function (data) {
            $('#collapse' + commentsId).find('a.more').remove();
            let jsonData = {
                "data": data.content,
            };
            let pageNumber = data.pageable.pageNumber + 1;
            let totalPages = data.totalPages;
            ;
            let Next = totalPages > pageNumber ? true : false;

            Mustache.parse(replys);
            var rendered = Mustache.render(replys, jsonData);
            $('#collapse' + commentsId).append(rendered);
            if (Next) {
                $('#collapse' + commentsId).append('<a class="page-link more" data-page="' + (commentsPage + 1) + '" onclick="replyAjax(' + commentsId + ',this)">▼댓글 더보기</a>');
            } else {
                $('#collapse' + commentsId).find('a.more').remove();
            }
        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    })
}

$(document).on('click', '.createReply', function () {
    let id = $(this).data('id');
    let attr = $(this).data('use');
    let input = $(this).closest('div.row.my-1').find('.reply');
    let targetUser = $(this).closest('.text-break').find('.username').text();
    if ($(this).data('use') == 'able') {
        input.focus();
    } else {
        $(this).data('use', 'able');
        let html = '<div class="my-3 col-12 border p-3 border-1">\n' +
            '                <form class="createReplyForm" onsubmit="return false">\n' +
            '                <input type="text" class="form-control reply" placeholder="댓글을 입력하세요" name="comments">\n' +
            '                <input type="hidden" class="form-control" name="targetUser" value ="' + targetUser + '">\n' +
            '                </form>\n' +
            '                           <button class="btn btn-secondary mt-3 replyCreate" type="button"  data-id="' + id + '">\n' +
            '                                     대댓글쓰기\n' +
            '                            </button>\n' +
            '                           <button class="btn btn-secondary mt-3 replyCreateCancel" type="button" >\n' +
            '                                     취소\n' +
            '                            </button>\n' +
            '                </div>';
        $(this).closest('div.text-break').siblings('.end').after(html);
    }

})

$(document).on('click', '.replyCreate', function () {

    let id = $(this).data('id');
    let element= $(this);
    let comments = $(this).siblings('form').find('input[name=comments]').val();
    let targetUser = $(this).siblings('form').find('input[name=targetUser]').val();
    let data = '<p style="font-weight: 900;">@' + targetUser + '<p> ' + comments;
    let collapse = $(this).closest('div.row.my-1.mainComments').find('.collapse');
    let parent = $(this).closest('div.row.my-1.mainComments');
    if (comments.length == 0) {
        alert("댓글을 작성후 눌러주세요");
        return;
    }
    $.ajax({
        type: "post",
        url: "/comments/createReply/" + id + "/" + courseBoardId,
        data: {
            comments: data
        },
        success: function (data) {
            if (data !=null) {

                    let jsonData = {
                        "data": data,
                    };
                    Mustache.parse(replys);
                    var rendered = Mustache.render(replys, jsonData);

                if (collapse.length == 0) { //리플없을때
                    parent.append(rendered);
                } else { //리플 있을때
                    if (collapse.hasClass('show') == false) { //리플 안펼쳤을때
                        parent.append(rendered);
                    } else { //리플펼쳤을때
                        let children= collapse.children('.page-link');
                        if (children.length >0) { //5개 이하일때
                            let last = collapse.children().last();
                            last.before (rendered);
                        }else { //5개 이상일때
                            collapse.append(rendered);
                        }
                    }
                }
                deleteReplyBox(element);
            } else {
                alert("실패");
            }
        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });

})
$(document).on('click', '.replyCreateCancel', function () {

    deleteReplyBox(this);

})

function deleteReplyBox(element) {
    //let closest = $(element).closest('div.row.my-1').find('.createReply');
    let closest = $(element).parent().siblings('.text-break').find('.createReply');
    closest.data('use', 'disabled');
    $(element).parent().remove();
}

