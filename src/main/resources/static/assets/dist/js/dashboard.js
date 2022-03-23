$(document).on("click",'#statisticsBtn',function () {
    statisticsBoxShow();
    $('.counter').counterUp({
        delay: 10,
        time: 500
    });
});