let pageNumberCommunity = 0;
let totalPagesCommunity = 0;
let totalElementsCommunity = 0;
let preNumCommunity = 0;
let nexNumCommunity = 0;
let PreviousCommunity = null;
let NextCommunity = null;
let startPageCommunity;
let endPageCommunity;


const table = ' {{#data}}\n' +
    '                                <tr data-id="{{id}}">\n' +
    '                                    <td>\n' +
    '                                        {{id}}\n' +
    '                                    </td>\n' +
    '                                    <td>\n' +
    '                                        {{title}}\n' +
    '                                    </td>\n' +
    '                                    <td>\n' +
    '                                        {{user.nickname}}\n' +
    '                                    </td>\n' +
    '                                    <td>\n' +
    '                                        {{createDate}}\n' +
    '                                    </td>\n' +
    '                                  \n' +
    '                                </tr>\n' +
    '       {{/data}}'




$(document).on('click', '#communityTab', function () {
    renderCommunityList(0);

});
$(document).on('click', '#previousCommunityAtag', function () {
    renderCommunityList(preNumCommunity);

});
$(document).on('click', '#nextCommunityAtag', function () {
    if(this)
    renderCommunityList(nexNumCommunity);
});




function renderCommunityList(page) {
    let courseId = getCourseId();
    $.ajax({
        type: "post",
        url: "/community/getCommunityList/" + courseId,
        data: {
            page: page,
            search: ""
        },
        success: function (data) {
            if (data != null) {
                $('#tableResult').empty();
                $('.pages').remove();
                let jsonData = {
                    "data": data.content,
                };
                //페이징
                pageNumberCommunity = data.pageable.pageNumber;
                totalPagesCommunity = data.totalPages;
                startPageCommunity = Math.floor(pageNumberCommunity / 10) * 10 + 1;
                endPageCommunity = startPageCommunity + 9 < totalPagesCommunity ? startPageCommunity + 9 : totalPagesCommunity;
                pageNumberCommunity++;
                totalElementsCommunity = data.totalElements;
                preNumCommunity = parseInt(pageNumberCommunity) - 1;
                nexNumCommunity = parseInt(pageNumberCommunity) + 1;
                PreviousCommunity = pageNumberCommunity != 1 ? true : false;
                NextCommunity = totalPagesCommunity > pageNumberCommunity ? true : false;
                var html = "";

                for (var num = startPageCommunity; num <= endPageCommunity; num++) {
                    if (num == pageNumberCommunity) {
                        html += '<li class="page-item active"><a class="page-link pages" onclick="renderCommunityList(' + num + ')">' + num + '</a></li>';
                    } else {
                        html += '<li class="page-item"><a class="page-link pages" onclick="renderCommunityList(' + num + ')">' + num + '</a></li>';
                    }
                }
                $("#previousCommunity").after(html);
                if (PreviousCommunity) {
                    $("#previousCommunity").removeClass("disabled");
                } else {
                    $("#previousCommunity").addClass("disabled");
                }
                if (NextCommunity) {
                    $("#nextCommunity").removeClass("disabled");
                } else {
                    $("#nextCommunity").addClass("disabled");
                }
                Mustache.parse(table);
                var rendered = Mustache.render(table, jsonData);
                $('#tableResult').html(rendered);
                communityBoxShow();
            }
        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });
}



