let communityDetail=' ' +
    ' <h1 class="h2" id="communityDetailTitle">{{title}}</h1>\n' +
    '            <div class="btn-toolbar mb-2 mb-md-0">\n' +
    '            </div>\n' +
    '        </div>\n' +
    '        <div class="d-flex justify-content-between">\n' +
    '            <div class="my-4 w-100" id="content" width="900" height="380" id="communityDetailContests">\n' +
    '                {{{contents}}}\n' +
    '            </div>'

$(document).on('click', '.communityCol', function () {
    $('.hiddenBox').attr("hidden", "hidden");
    let communityId = $(this).data("id");
    $.ajax({
        type: "post",
        url: "/community/getCommunity/" + communityId,
        async: false,
        success: function (data) {
            if (data != null) {

                Mustache.parse(communityDetail);
                var rendered = Mustache.render(communityDetail, data);
                $('#titleAndContents').html(rendered);
                $('#detailCommunityBox').removeAttr("hidden");
            } else {
                alert("존재하지않는 게시판입니다.");
            }
        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });
});

$(document).on('click', '.backBtn', function () {
    $('.hiddenBox').attr("hidden", "hidden");
    $('#communityBox').removeAttr("hidden");
});





