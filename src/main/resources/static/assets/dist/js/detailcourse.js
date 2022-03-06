const template = '<div>\n' +
    '    {{#data.content}}\n' +
    '        <div class="row my-1 ">\n' +
    '            <div >\n' +
    '                <div class="text-break">\n' +
    '                    <div >\n' +
    '                        <div class="user-field">\n' +
    '                            <div class="mx-2">\n' +
    '                                <img src="https://github.com/mdo.png" alt="mdo" width="32" height="32"\n' +
    '                                     class="rounded-circle">\n' +
    '                            </div>\n' +
    '                            <div class="user-field-name">\n' +
    '                                <div> <p class=" my-3">{{username}}</p></div>\n' +
    '                                <div class="dropdown text-end">\n' +
    '                <a href="#" class="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">\n' +
    '                </a>\n' +
    '                <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">\n' +
    '                    <li><a class="dropdown-item update" >수정</a></li>\n' +
    '                    <li><a class="dropdown-item delete" >삭제</a></li>\n' +
    '                </ul>\n' +
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
    '                        <p class="text-muted my-2 ">\n' +
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
    '                    <p class=" my-2 user-field-name ">\n' +
    '                        강사 ' + getId().teachName + '님의 답글\n' +
    '                    </p>\n' +
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


$(document).ready(function () {

    var Previous;
    var Next;
    var pageNumber;
    var totalElements;
    var preNum ;
    var nexNum ;
    var totalPages;


    pageing(0);

    //수정 삭제
    $(document).on("click", ".delete", function () {
        alert("sss");
    });



    //페이징 및 댓글데이터 불러오기
    function pageing(page=0,sort="createDate-Desc"){
        $.ajax({
            type: "get",
            url: "/course/commentsview",
            data: {
                courseId: getId().id,
                page : page,
                sortBy : sort
            },
            success: function (data) {
                //댓글데이터
                $('#result').empty();
                $('.page').remove();

                Mustache.parse(template);
                var jsonData = {
                    "data": data
                }
                var rendered = Mustache.render(template, jsonData);
                $('#result').html(rendered);

                //페이징
                pageNumber =data.pageable.pageNumber+1;
                totalPages =data.totalPages;
                totalElements = data.totalElements
                preNum = parseInt(pageNumber) - 1;
                nexNum = parseInt(pageNumber) + 1;
                Previous = pageNumber!=1 ? true :false;
                Next = totalPages>pageNumber ? true :false;
                var html = "";

                for (var num = 1; num <= totalPages; num++) {
                    var onclick ='page('+num+')';
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
    //페이징 클릭 이벤트
    $(document).on("click",".page",function(){
        let page = $(this).text();
        let sort = $('.text-success').data("sort");
        pageing(page,sort);
    });
    $(document).on("click","#previousAtag",function(){

        let sort = $('.text-success').data("sort");
        pageing(preNum,sort);
    });
    $(document).on("click","#nextAtag",function(){
        let sort = $('.text-success').data("sort");
        pageing(nexNum,sort);
    });
    $(document).on("click",".review-sort",function(){
        let sort = $(this).data("sort");
        $('.review-sort').removeClass("text-success");
        $(this).addClass("text-success");
        pageing(0,sort);
    });





});



