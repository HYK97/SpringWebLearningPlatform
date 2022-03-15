const template =' ' +
    ' {{#data.CourseBoardData}}' +
    '<main class="col-md-9 ms-sm-auto col-lg-12  px-md-4">\n' +
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
    '                <div class="my-4 w-100" id="content" width="900" height="380">' +
    '                   {{contents}}                                                            ' +
    '                </div>\n' +
    '\n' +
    '                <h2>Section title</h2>\n' +
    '                <div class="table-responsive">\n' +
    '\n' +
    '\n' +
    '                </div>\n' +
    '            </main>' +
    '' +
    ' {{/data.CourseBoardData}}'







$(document).ready(function(){
  let courseBoard = getCourseBoard();
  if (courseBoard.length > 0) {
    $('#viewBox').removeAttr("hidden");
    let index = courseBoard[0].CourseBoardData.id;
    render(index);
    let k = $(".courseboard-href").first().addClass('active');
  } else {

    alert("생성된 강의 목차가없습니다. 강의 회차를 추가해주세요");
  }




  $(document).on("click", ".courseboard-href", function () {
    $('#viewBox').removeAttr("hidden");
    $('#createBox').attr("hidden","hidden");
    let id = $(this).data('id');
    $(".courseboard-href").removeClass("active");
    $(this).addClass('active');
    render(id);
  });


  $(document).on("click", "#addBtn", function () {
    $('#viewBox').attr("hidden","hidden");
    $('#createBox').removeAttr("hidden");

  });



})

function render(id) {
  let courseBoard = getCourseBoard();
  $('#content').empty();
  Mustache.parse(template);
  let content =courseBoard.filter(x => x.CourseBoardData.id === ''+id+'');
  var jsonData = {
    "data": content[0]
  };
  var rendered = Mustache.render(template, jsonData);
  $('#result').html(rendered);
}