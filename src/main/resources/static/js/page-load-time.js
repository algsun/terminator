/**
 * 记录页面的加载时时间戳到 body data-client-time
 *
 * @author gaohui
 * @date 2013-01-29 10:19
 * @dependency jquery
 */
(function ($) {
    // 可以借助 html5 performance.timing 对象
    var _pageLoadStartTime = new Date(); //页面加载的开始时间
    $(function () {
        $("body").attr("data-client-time", _pageLoadStartTime.getTime());
    });
})(jQuery);