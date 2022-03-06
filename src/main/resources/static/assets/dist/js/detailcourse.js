$(document).ready(function() {




getData(function (data){
    var template = '<div>\n' +
        '    {{#data}}\n' +
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
        '                                <p class=" my-3">{{username}}</p>\n' +
        '                                <p class="lead mb-0">\n' +
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
        '                        강사 '+getId().teachName+'님의 답글\n' +
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
        '    {{/data}}\n' +
        '</div>';
    Mustache.parse(template);
    var jsonData = {
        "data" : data
    }
    var rendered = Mustache.render(template, jsonData);
    $('#result').html(rendered);

})


});

function getData(callback) {
    let course=getId();
    $.ajax({
        type : "post",
        url : "/course/commentsview",
        data :  {
            courseId : course.id
        },
        success : function(data){
           callback(data);
        },
        error:function(request, error) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert("세션오류");
        }
    });

}