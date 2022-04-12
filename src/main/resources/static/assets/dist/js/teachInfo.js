const courseTemplate = '{{#data}}\n' +
    '                        <div class="col">\n' +
    '                            <div class="card shadow-sm">\n' +
    '                                {{#thumbnail}}\n' +
    '                                    <a href="/course/detailcourse?id={{id}}"><img\n' +
    '                                        class="bd-placeholder-img card-img-top"\n' +
    '                                        width="100%" height="300px"\n' +
    '                                        src="{{thumbnail}}"></a>\n' +
    '\n' +
    '                                {{/thumbnail}}\n' +
    '\n' +
    '                                {{^thumbnail}}\n' +
    '                                    <a href="/course/detailcourse?id={{id}}">\n' +
    '                                    <svg class="bd-placeholder-img card-img-top" width="100%" height="300px"\n' +
    '                                         xmlns="http://www.w3.org/2000/svg" role="img"\n' +
    '                                         aria-label="Placeholder: Thumbnail"\n' +
    '                                         preserveAspectRatio="xMidYMid slice" focusable="false"><title>\n' +
    '                                        썸네일없음</title>\n' +
    '                                        <rect width="100%" height="100%" fill="#55595c"></rect>\n' +
    '                                        <text x="50%" y="50%" fill="#eceeef" dy=".3em">썸네일없음</text>\n' +
    '                                    </svg>\n' +
    '                                    </a>\n' +
    '                                {{/thumbnail}}\n' +
    '                                <div class="card-body">\n' +
    '                                    <h5 class="card-text">{{courseName}}</h5>\n' +
    '                                    <p class="card-text">강사명 : {{user.nickname}}({{user.username}})</p>\n' +
    '                                    <div class="d-flex justify-content-between align-items-center">\n' +
    '                                        <div class="btn-group">\n' +
    '                                            <div class="star-ratings">\n' +
    '                                                <div id="star" style="width: {{starScope}}%"\n' +
    '                                                     class="star-ratings-fill space-x-2 text-lg">\n' +
    '                                                    <span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>\n' +
    '                                                </div>\n' +
    '                                                <div class="star-ratings-base space-x-2 text-lg">\n' +
    '                                                    <span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>\n' +
    '                                                </div>\n' +
    '                                            </div>\n' +
    '                                            <div class="star-ratings">\n' +
    '                                                <small id="scope" class="text-muted">({{scope}})</small>\n' +
    '                                            </div>\n' +
    '                                        </div>\n' +
    '                                        <small id="date" class="text-muted mt-1 ">\n' +
    '                                            강좌 생성일 : {{createDate}}\n' +
    '                                        </small>\n' +
    '                                    </div>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                    {{/data}}';
const addTemplate =
    '<div id="moreBtn" class="col my-4" style=" display: flex; justify-content: center;">\n' +
    '                    <nav aria-label="Page navigation example">\n' +
    '                        <ul class="pagination">\n' +
    '\n' +
    '                            <li class="page-item" id="next">\n' +
    '                                <a class="page-link"  id="next" aria-label="next">\n' +
    '                                    <span aria-hidden="true"><i class="bi bi-chevron-down"></i> 더보기</span>\n' +
    '                                </a>\n' +
    '                            </li>\n' +
    '                        </ul>\n' +
    '                    </nav>\n' +
    '                </div>';


$(document).on('click','#moreBtn', function(){
    $.ajax({
        type: "post",
        url: "/user/userDetailInfo/" + user,
        data: {
            page: nexNum
        },
        success: function (data) {
            $('#moreBtn').remove();
            let jsonData = {
                "data": data.content,
            };
            pageNumber=data.pageable.pageNumber+1;
            nexNum=parseInt(pageNumber) + 1;
            totalPages = data.totalPages;
            Mustache.parse(courseTemplate);
            var rendered = Mustache.render(courseTemplate, jsonData);
            $('#resultBox').append(rendered);
            Next = totalPages > pageNumber ? true : false;
            if (Next) {
                $('#addBtn').append(addTemplate);
            } else {
                $('#moreBtn').remove();
            }


        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });

})