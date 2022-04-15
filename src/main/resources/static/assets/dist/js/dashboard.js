var chart1 = null;
var chart2 = null;
var chart3 = null;

const rankView =
    '                           {{#data}}\n' +
    '                              <tr>\n' +
    '                                    <td>\n' +
    '                                        <div class="d-flex px-2 py-1">\n' +
    '                                          \n' +
    '                                            <div class="d-flex flex-column justify-content-center">\n' +
    '                                                <h6 class="mb-0 text-sm">{{id}}</h6>\n' +
    '                                            </div>\n' +
    '                                        </div>\n' +
    '                                    </td>\n' +
    '                                    <td>\n' +
    '                                        <div class="avatar-group mt-2">\n' +
    '                                            <p>{{title}}</p>\n' +
    '                                        </div>\n' +
    '                                    </td>\n' +
    '                                    <td class="align-middle text-center text-sm">\n' +
    '                                        <span class="text-xs font-weight-bold">{{views}}</span>\n' +
    '                                    </td>\n' +
    '                                    <td class="align-middle  text-center text-sm">\n' +
    '                                        <span class="text-xs font-weight-bold">{{commentsCount}} </span>\n' +
    '                                    </td>\n' +
    '                                 </tr>  ' +
    '                                 {{/data}}'
$(document).on("click", '#statisticsBtn', function () {
    $("#percentUser").empty();
    $("#percentScope").empty();
    $("#percentComment").empty();
    getDashboardData();



});

function getDashboardData() {
    $.ajax({
        url: '/courseboard/getDashBoard/' + getCourseId(),
        type: 'post',
        success: function (data) {
            $('#resultRank').empty();
            var userPercent = data.todayRegisteredUser - data.yesterdayRegisteredUser;
            var scopePercent =  data.todayScope-data.yesterdayScope;
            var commentPercent = data.todayComment - data.yesterdayComment
            $("#todayUser").text(data.todayRegisteredUser);
            drawChart1(data.DailyForAMonthUser);
            drawChart2(data.DailyForAMonthScope);
            drawChart3(data.DailyForAMonthComments);
            $("#todayScope").text(data.todayScope);
            $("#todayComment").text(data.todayComment);
            $("#allView").text(data.allView);
            userPercent >= 0 ? $("#percentUser").append('어제대비 <span class="text-success text-sm font-weight-bolder">+' + userPercent + '명 </span>') : $("#percentUser").append('어제 대비 <span class="text-danger text-sm font-weight-bolder">' + userPercent + '명 </span>')
            scopePercent >= 0 ? $("#percentScope").append('어제대비 <span class="text-success text-sm font-weight-bolder">' + scopePercent.toFixed(1) + ' 점 증가 </span>') : $("#percentScope").append('어제 대비 <span class="text-danger text-sm font-weight-bolder">"' + scopePercent.toFixed(1) + '"점 감소 </span>')
            commentPercent >= 0 ? $("#percentComment").append('어제대비 <span class="text-success text-sm font-weight-bolder">+' + commentPercent + '개 </span>') : $("#percentComment").append('어제 대비 <span class="text-danger text-sm font-weight-bolder">' + commentPercent + '개 </span>')

            let jsonData = {
                "data": data.RankingView
            };

            Mustache.parse(rankView);
            var rendered = Mustache.render(rankView, jsonData);
            $('#resultRank').html(rendered);

            $('.counter').counterUp({
                delay: 10,
                time: 500
            });

        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });
}

$(document).on('change', 'input[name="user"]',function () {
  let check=$('input[name="user"]:checked').val();
    if (check == 'day') {
        chart1DataUpdate('userDayChart');
    }else if (check == 'month') {
        chart1DataUpdate('userMonthChart');
    } else {
        chart1DataUpdate('userYearChart');
    }
})


$(document).on('change', 'input[name="scope"]',function () {
    let check=$('input[name="scope"]:checked').val();
    if (check == 'day') {
        chart2DataUpdate('scopeDayChart');
    }else if (check == 'month') {
        chart2DataUpdate('scopeMonthChart');
    } else {
        chart2DataUpdate('scopeYearChart');
    }
})

$(document).on('change', 'input[name="comments"]',function () {
    let check=$('input[name="comments"]:checked').val();
    if (check == 'day') {
        chart3DataUpdate('commentsDayChart');
    }else if (check == 'month') {
        chart3DataUpdate('commentsMonthChart');
    } else {
        chart3DataUpdate('commentsYearChart');
    }
})


function chart1DataUpdate(queryString) {
    $.ajax({
        url: '/courseboard/'+queryString+'/' + getCourseId(),
        type: 'post',
        async: false,
        success: function (data) {
            drawChart1(data);
        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });
}

function chart2DataUpdate(queryString) {
    $.ajax({
        url: '/courseboard/'+queryString+'/' + getCourseId(),
        type: 'post',
        async: false,
        success: function (data) {
            drawChart2(data);
        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });
}

function chart3DataUpdate(queryString) {
    $.ajax({
        url: '/courseboard/'+queryString+'/' + getCourseId(),
        type: 'post',
        async: false,
        success: function (data) {
            drawChart3(data);
        },
        error: function (request, error) {
            alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            alert("오류");
        }
    });
}








function drawChart1(data) {
    var ctx = document.getElementById("chart-bars").getContext("2d");
    if (chart1 != null) {
        window.chart1.destroy();
    }
    chart1 = new Chart(ctx, {
        type: "bar",
        data: {
            labels: Object.keys(data),
            datasets: [{
                label: "수강생 수",
                tension: 0,
                borderWidth: 0,
                pointRadius: 5,
                pointBackgroundColor: "rgba(255, 255, 255, .8)",
                pointBorderColor: "transparent",
                borderColor: "rgba(255, 255, 255, .8)",
                borderWidth: 4,
                backgroundColor: "transparent",
                fill: true,
                data: Object.values(data),
                maxBarThickness: 6

            }],
        },
        options: {
            animation: {
                duration: 1000,
            },
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false,
                }
            },
            interaction: {
                intersect: false,
                mode: 'index',
            },
            scales: {
                y: {
                    grid: {
                        drawBorder: false,
                        display: true,
                        drawOnChartArea: true,
                        drawTicks: false,
                        borderDash: [5, 5],
                        color: 'rgba(255, 255, 255, .2)'
                    },
                    ticks: {
                        display: true,
                        padding: 10,
                        color: '#f8f9fa',
                        font: {
                            size: 14,
                            weight: 300,
                            family: "Roboto",
                            style: 'normal',
                            lineHeight: 2
                        },
                    }
                },
                x: {
                    grid: {
                        drawBorder: false,
                        display: false,
                        drawOnChartArea: false,
                        drawTicks: false,
                        borderDash: [5, 5]
                    },
                    ticks: {
                        display: true,
                        color: '#f8f9fa',
                        padding: 10,
                        font: {
                            size: 14,
                            weight: 300,
                            family: "Roboto",
                            style: 'normal',
                            lineHeight: 2
                        },
                    }
                },
            },
        },
    });
}

function drawChart2(data) {
    var ctx2 = document.getElementById("chart-line").getContext("2d");
    if (chart2 != null) {
        window.chart2.destroy();
    }
    chart2 = new Chart(ctx2, {
        type: "bar",
        data: {
            labels: Object.keys(data),
            datasets: [{
                label: "Mobile apps",
                tension: 0,
                borderWidth: 0,
                pointRadius: 5,
                pointBackgroundColor: "rgba(255, 255, 255, .8)",
                pointBorderColor: "transparent",
                borderColor: "rgba(255, 255, 255, .8)",
                borderColor: "rgba(255, 255, 255, .8)",
                borderWidth: 4,
                backgroundColor: "transparent",
                fill: true,
                data: Object.values(data),
                maxBarThickness: 6

            }],
        },
        options: {
            animation: {
                duration: 1000,
            },
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false,
                }
            },
            interaction: {
                intersect: false,
                mode: 'index',
            },
            scales: {
                y: {
                    grid: {
                        drawBorder: false,
                        display: true,
                        drawOnChartArea: true,
                        drawTicks: false,
                        borderDash: [5, 5],
                        color: 'rgba(255, 255, 255, .2)'
                    },
                    ticks: {
                        display: true,
                        color: '#f8f9fa',
                        padding: 10,
                        font: {
                            size: 14,
                            weight: 300,
                            family: "Roboto",
                            style: 'normal',
                            lineHeight: 2
                        },
                    }
                },
                x: {
                    grid: {
                        drawBorder: false,
                        display: false,
                        drawOnChartArea: false,
                        drawTicks: false,
                        borderDash: [5, 5]
                    },
                    ticks: {
                        display: true,
                        color: '#f8f9fa',
                        padding: 10,
                        font: {
                            size: 14,
                            weight: 300,
                            family: "Roboto",
                            style: 'normal',
                            lineHeight: 2
                        },
                    }
                },
            },
        },
    });

}

function drawChart3(data) {
    var ctx3 = document.getElementById("chart-line-tasks").getContext("2d");

    if (chart3 != null) {
        window.chart3.destroy();
    }


    chart3 = new Chart(ctx3, {
        type: "bar",
        data: {
            labels: Object.keys(data),
            datasets: [{
                label: "Mobile apps",
                tension: 0,
                borderWidth: 0,
                pointRadius: 5,
                pointBackgroundColor: "rgba(255, 255, 255, .8)",
                pointBorderColor: "transparent",
                borderColor: "rgba(255, 255, 255, .8)",
                borderWidth: 4,
                backgroundColor: "transparent",
                fill: true,
                data: Object.values(data),
                maxBarThickness: 6

            }],
        },
        options: {
            animation: {
                duration: 1000,
            },
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false,
                }
            },
            interaction: {
                intersect: false,
                mode: 'index',
            },
            scales: {
                y: {
                    grid: {
                        drawBorder: false,
                        display: true,
                        drawOnChartArea: true,
                        drawTicks: false,
                        borderDash: [5, 5],
                        color: 'rgba(255, 255, 255, .2)'
                    },
                    ticks: {
                        display: true,
                        padding: 10,
                        color: '#f8f9fa',
                        font: {
                            size: 14,
                            weight: 300,
                            family: "Roboto",
                            style: 'normal',
                            lineHeight: 2
                        },
                    }
                },
                x: {
                    grid: {
                        drawBorder: false,
                        display: false,
                        drawOnChartArea: false,
                        drawTicks: false,
                        borderDash: [5, 5]
                    },
                    ticks: {
                        display: true,
                        color: '#f8f9fa',
                        padding: 10,
                        font: {
                            size: 14,
                            weight: 300,
                            family: "Roboto",
                            style: 'normal',
                            lineHeight: 2
                        },
                    }
                },
            },
        },
    });
}
