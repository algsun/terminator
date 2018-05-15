/**
 *
 *<pre>
 * 实时数据监测指标过滤
 *</pre>
 * @author: Wang yunlong
 * @time: 13-2-27 上午9:31
 */
(function ($) {
    $(function () {



        //显示监测指标过滤
        $(".show-filters").click(function () {
            $(".filter_sensorinfoes").show();
            $(this).hide();
            $("#cancel-filter-btn").hide();
            $("#filter-btn").show();
            $(".hide-filters").show();
            $("#select-all").show();
            $("#select-no").show();
            $("#is_show_filters").val("1");
        });
        //隐藏监测指标过滤
        $(".hide-filters").click(function () {
            $(".filter_sensorinfoes").hide();
            $("#cancel-filter-btn").hide();
            $("#filter-btn").hide();
            $(this).hide();
            $(".show-filters").show();
            $("#select-all").hide();
            $("#select-no").hide();
            $("#is_show_filters").val("0");
        });
        $("#select-all").find("input[type='checkbox']").click(function () {
            var $this = $(this);
            var $sensorCheckbox = $("#zone-sensorinfo-filter").find("input[type='checkbox']");
            if ($this.attr("checked")) {
                $sensorCheckbox.attr("checked", true);
            } else {
                $sensorCheckbox.attr("checked", false);
            }
            $("#select-no").find("input[type='checkbox']").attr("checked", false);
        });
        $("#select-no").find("input[type='checkbox']").click(function () {
            var $sensorCheckbox = $("#zone-sensorinfo-filter").find("input[type='checkbox']");
            $sensorCheckbox.each(function (index, checkbox) {
                var $checkbox = $(checkbox);
                $checkbox.attr("checked", !$checkbox.attr("checked"));
            });
            $("#select-all").find("input[type='checkbox']").attr("checked", false);
        });
    });
})(jQuery);
