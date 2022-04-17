let communityDetail = ' ' +
    ' <div class="d-flex justify-content-between">' +
    '   <div>' +
    '       <h1 class="h2" id="communityDetailTitle">{{title}}</h1>' +
    '   </div>' +
    '   <div>' +
    '{{#myCommunity}}' +
    '       <button class="btn btn-secondary" id="updateCommunity" data-id="{{id}}">수정</button>' +
    '       <button class="btn btn-secondary" id="deleteCommunity" data-id="{{id}}">삭제</button>' +
    '{{/myCommunity}}' +
    '   </div>' +
    ' </div>\n' +
    '        <div class="d-flex justify-content-end  border-bottom"> ' +
    '                작성일 : {{createDate}}  /  ' +
    '                글쓴이 : {{user.nickname}}' +
    '            </div>\n' +
    '        </div>\n' +
    '        <div class="d-flex justify-content-between">\n' +
    '            <div class="my-4 w-100"  id="communityDetailContests">\n' +
    '                {{{contents}}}\n' +
    '            </div>'

$(document).on('click', '.communityCol', function () {
    formReset();
    $('.hiddenBox').attr("hidden", "hidden");
    let id = $(this).data("id");
    communityId = id;
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
                commentsRender(0);
                resizeIframe();

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
    renderCommunityList(pageNumberCommunity, searchKeyword);
    $('.hiddenBox').attr("hidden", "hidden");
    $('#communityBox').removeAttr("hidden");
});

$(document).on('click', '#deleteCommunity', function () {
    let id = $(this).data('id');
    let result = confirm("삭제하실건가요?");
    if (result) {
        $.ajax({
            type: "post",
            url: "/community/deleteCommunity/" + id,
            async: false,
            success: function (data) {
                if (data ==1) {
                    alert("삭제되었습니다.");
                    renderCommunityList(pageNumberCommunity, searchKeyword);
                    $('.hiddenBox').attr("hidden", "hidden");
                    $('#communityBox').removeAttr("hidden");
                } else {
                    alert("오류가 발생했습니다.");
                }
            },
            error: function (request, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                alert("오류");
            }
        });

    }
});


$(document).on('click', '#updateCommunity', function () {
    let id = $(this).data('id');
    $('.communityCreateBtn').attr("hidden", "hidden");
    $('#communityUpdateBtn').removeAttr("hidden");
    $.ajax({
        type: "post",
        url: "/community/getCommunity/" + id,
        async: false,
        success: function (data) {

            $("#communityContentsArea").summernote('code', data.contents);
            $("#communityTitleInput").val(data.title);
            $('#communityUpdateBtn').data('id',id);
            $('.hiddenBox').attr("hidden", "hidden");
            $('#createCommunityBox').removeAttr("hidden");
        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });


});


$(document).on('click', '#communityUpdateBtn', function () {
    let id = $(this).data('id');
    var check = formBtn('#communityCreateForm');
    if (check == 1) {
        return;
    }
    let data = $("#communityCreateForm").serialize();
    $.ajax({
        type: "post",
        url: "/community/modifyCommunity/" + id,
        async: false,
        data: data,
        success: function (data) {
            if (data != null) {
                alert("수정 성공");
                $('.hiddenBox').attr("hidden", "hidden");
                Mustache.parse(communityDetail);
                var rendered = Mustache.render(communityDetail, data);
                $('#titleAndContents').html(rendered);
                $('#detailCommunityBox').removeAttr("hidden");
                commentsRender(0);
                resizeIframe();

            } else {
                alert("오류");
            }

        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });


});




