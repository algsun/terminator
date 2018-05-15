/**
 *<pre>
 * 动态设置表格长度
 *</pre>
 *
 * @author: Wang yunlong
 * @time: 13-2-19 上午11:13
 * @check @gaohui 2013-02-25 #1730
 */
var SET_TABLE_WIDTH = {
    // 返回表格的宽度
    tableWidth: function ($table) {
        var $theadTh = $table.find("thead").find("th:visible");
        var totalThWidth = 0;
        $theadTh.each(function (index, th) {
            var $th = $(th);
            totalThWidth += ($th.find("span").text().length * 20);
        });
        return totalThWidth;
    },

    // 返回 表格表头汉字 数量
    getTableLength: function ($table) {
        var $theadTh = $table.find("thead").find("th:visible");
        var totalThWidth = 0;
        $theadTh.each(function (index, th) {
            var $th = $(th);
            totalThWidth += $th.find("span").text().length;
        });
        return totalThWidth;
    },
    // 设置表格宽度
    setTableWidth: function ($table) {
        var tableWidth = this.tableWidth($table);
        var minWidth = $table.parent().parent().parent().width();
        var width = Math.max(tableWidth, minWidth);
        $table.css("width", width);
    },
    setTableColumnWidth: function ($table) {
        var $theadColumns = $table.find("thead").find("tr").children();
        var $tbodyRow = $table.find("tbody").find("tr");
        $theadColumns.each(function (index, column) {
        });
    }
};

// horizontal scroll
var H_SCROLL = {
    afterScrollLeft: 0,
    execute: function ($table, $tableContainer) {
        var H_SCROLL = this;
        $tableContainer.scroll(function () {
            var $thead = $table.find("thead");
            var $deviceNameTh = $thead.find("th[data-type='nodeName']");
//            var deviceNameThLeft = $deviceNameTh.offset().left;
            var deviceNameThIndex = $thead.find("th").index($deviceNameTh);
            var $tbodyTr = $table.find("tr");
            var $this = $(this);
            var scrollLeft = $this.scrollLeft();
            var deviceNameThLeft = 0;
            $thead.find("th:visible").each(function (index, th) {
                var $th = $(th);
                if (index < deviceNameThIndex) {
                    deviceNameThLeft += $th.width();
                }
            });
            if (H_SCROLL.afterScrollLeft < scrollLeft) {
                var changeIndex = deviceNameThIndex + 1;
                if (scrollLeft - deviceNameThLeft > 10) {
                    H_SCROLL.changeColumn($tbodyTr, deviceNameThIndex, changeIndex);
                }
            } else {
                if (deviceNameThIndex > 1) {
                    var changeIndex = deviceNameThIndex - 1;
                    if (scrollLeft - deviceNameThLeft < 10) {
                        H_SCROLL.changeColumn($tbodyTr, deviceNameThIndex, changeIndex);
                    }
                }
            }
            H_SCROLL.afterScrollLeft = scrollLeft;
//            if (scrollLeft - deviceNameThLeft < $thead.find("th").eq(deviceNameThIndex - 1).width()) {
//                H_SCROLL.changeHtml($deviceNameTh, $thead.find("th").eq(deviceNameThIndex - 1),deviceNameThIndex,deviceNameThIndex-1);
//                H_SCROLL.changeColumn($tbodyTr, deviceNameThIndex, deviceNameThIndex - 1);
//            }
        });
    },
    changeColumn: function (tbodyTr, thisIndex, changeIndex) {
        var H_SCROLL = this;
        tbodyTr.each(function (index, tr) {
            var param1 = $(tr).children().eq(thisIndex);
            var param2 = $(tr).children().eq(changeIndex);
            H_SCROLL.changeHtml(param1, param2, thisIndex, changeIndex);
        });
    },
    changeHtml: function (param1, param2, thisIndex, changeIndex) {
        if (thisIndex < changeIndex) {
            //从左往右移
            param2.after(param1);
        } else {
            //从右往左移
            param2.before(param1);
        }
    }
};

