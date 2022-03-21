const template = ' ' +
    ' {{#data}}' +
    '<main class="col-md-12 ms-sm-auto col-lg-12  px-md-4">\n' +
    '\n' +
    '\n' +
    '                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">\n' +
    '                    <h1 class="h2">{{title}}</h1>\n' +
    '                    <div class="btn-toolbar mb-2 mb-md-0">\n' +
    '\n' +
    '\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '\n' +
    '              <div class="d-flex justify-content-between">' +
    '                 <div class="my-4 w-100" id="content" width="900" height="380">' +
    '                   {{{contents}}}                                                            ' +
    '                </div>\n' +
    ' {{/data}}' +
    ' {{#fileCheck}}' +
    '                 <div class="dropdown">\n' +
    '                           <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">\n' +
    '    첨부파일\n' +
    '  </button>\n' +
    '  <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">\n' +
    '                               {{#data.files}}   ' +
    '    <li><a class="dropdown-item" href="/file/download/{{courseId}}/{{data.id}}/{{id}}">{{origFileName}} size:{{fileSize}}kb</a><li>\n' +
    '                               {{/data.files}}   ' +

    '  </ul>\n' +
    '</div>' +
    ' {{/fileCheck}}' +
    '              </div>  ' +
    '\n' +
    '                <h2>Section title</h2>\n' +
    '                <div class="table-responsive">\n' +
    '\n' +
    '\n' +
    '                </div>\n' +
    '            </main>'


const templateNav = '' +
    '{{#data}}' +
    ' <li class="nav-item" s>\n' +
    '                            <a role="button" class="courseboard-href nav-link" aria-current="page" data-id="{{id}}">\n' +
    '                                {{title}}\n' +
    '                            </a>\n' +
    '                        </li>' +
    ' {{/data}}'


var courseBoard = getData();
var courseBoardId;
$(document).ready(function () {


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


    $(document).on("click", ".courseboard-href", function () {
        viewBoxShow();
        let id = $(this).data('id');
        $(".nav-link").removeClass("active");
        $(this).addClass('active');
        mainRender(id, courseBoard);
    });




})

function viewBoxShow() {
    $('#viewBox').removeAttr("hidden");
    $('#updateBox').attr("hidden", "hidden");
    $('#createBox').attr("hidden", "hidden");
}

function createBoxShow() {
    $('#createBox').removeAttr("hidden");
    $('#updateBox').attr("hidden", "hidden");
    $('#viewBox').attr("hidden", "hidden");
}

function updateBoxShow() {
    $('#updateBox').removeAttr("hidden");
    $('#viewBox').attr("hidden", "hidden");
    $('#createBox').attr("hidden", "hidden");
}


function getData() {
    var value;
    var courseId = getCourseId();
    $.ajax({
        type: "get",
        url: "/courseboard/data/" + courseId,
        async: false,
        success: function (data) {
            value = data;
        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });
    return value;
}

function navRender(data) {
    let courseBoard = data;
    $('.navResult').empty();
    let jsonData = {
        "data": courseBoard
    };
    Mustache.parse(templateNav);
    var randeredNav = Mustache.render(templateNav, jsonData);
    $('#navResult').html(randeredNav);
}

function mainRender(id, data) {
    let courseBoard = data;
    var someTabTriggerEl = document.querySelector('#main-tab');
    var tab = new bootstrap.Tab(someTabTriggerEl);
    tab.show();
    $('#content').empty();
    let content = courseBoard.filter(x => x.id === id);
    let fileCheck = content[0].files.length == 0 ? null : true;

    let jsonData = {
        "data": content[0],
        "fileCheck": fileCheck,
        "courseId": getCourseId(),
    };
    Mustache.parse(template);
    var rendered = Mustache.render(template, jsonData);
    $('#result').html(rendered);
    courseBoardId = content[0].id;
}


