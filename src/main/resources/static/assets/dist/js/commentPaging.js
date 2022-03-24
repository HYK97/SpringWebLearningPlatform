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
    '            <hr class=" mt-3 mb-2">\n' +
    '            <div >\n' +
    '                <div class="text-break">\n' +
    '                    <div >\n' +
    '                        <div class="user-field">\n' +
    '                            <div class="mx-2">\n' +
    '                                <img src="https://github.com/mdo.png" alt="mdo" width="32" height="32"\n' +
    '                                     class="rounded-circle">\n' +
    '                            </div>\n' +
    '                            <div class="user-field-name">\n' +
    '                                <div> <p class=" my-3 ">{{username}}</p></div>\n' +
    '               {{^myCommentsFlag}} ' +
    '                <div class="dropdown dropdown-user text-end" data-user="{{username}}">\n' +
    '                <a href="#" class="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">\n' +
    '                </a>\n' +
    '                <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">\n' +
    '                    <li><a class="dropdown-item createReply">답글쓰기</a></li>\n' +
    '                </ul>{{/myCommentsFlag}}\n' +
    '               {{#myCommentsFlag}}' +
    '                <div class="dropdown dropdown-user text-end"  data-user="{{username}}">\n' +
    '                <a href="#" class="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">\n' +
    '                </a>\n' +
    '                      <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">\n' +
    '                    <li><a class="dropdown-item update"  >수정</a></li>\n' +
    '                    <li><a class="dropdown-item delete" data-id="{{id}}">삭제</a></li>\n' +
    '                </ul>{{/myCommentsFlag}}\n' +
    '            </div>\n' +
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
    '            <hr class=" mt-3 mb-2">\n' +
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

function getReplyData(commentsId,element) {

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
                $('#collapse' + commentsId).find('a').remove();
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
                    $('#collapse' + commentsId).append('<a class="page-link" data-page="2"  onclick="replyAjax('+commentsId+',this)">▼댓글 더보기</a>')
                }
            },
            error: function (request, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                alert("오류");
            }
        })
    }

}

function replyAjax(commentsId,element) {
        let commentsPage = element.dataset.page;
        $.ajax({
            type: "get",
            url: '/comments/getReply/' + commentsId,
            data: {
                page: commentsPage
            },
            success: function (data) {
                $('#collapse' + commentsId).find('a').remove();
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
                    $('#collapse' + commentsId).append('<a class="page-link" data-page="'+(commentsPage+1)+'" onclick="replyAjax('+commentsId+',this)">▼더보기</a>');
                }
            },
            error: function (request, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                alert("오류");
            }
        })
}
