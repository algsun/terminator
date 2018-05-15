/**
 * 站点和区域实时数据
 *
 * @author xu.baoji
 * @time 2013-11-25
 */
var REAL_TIME = {

    $realTimeDiv: $("#table-container"),
    // 固定表头和列后的 实时数据设备表
    $device_realTime_dataTable_body: ".DTFC_LeftBodyWrapper",
    // 固定表头和列后的 实时数据监测指标表（监测指标数据）
    $realTime_dataTable_body: ".dataTables_scrollBody",

    //是否有检测指标过滤
    isFilter: false,
    //上一次请求数据
    preData: [],
    //获取监测指标url
    sensorinfoesUrl: '',
    //获取实时数据的url
    realtimeDataUrl: '',
    realtimeDataFilterUrl: "",
    // 实时数据表入口方法
    execute: function (sensorinfoesUrl, realtimeDataUrl, realtimeDataFilterUrl) {
        var REAL_TIME = this;
        // 初始化 属性
        REAL_TIME.sensorinfoesUrl = sensorinfoesUrl;
        REAL_TIME.realtimeDataUrl = realtimeDataUrl;
        REAL_TIME.realtimeDataFilterUrl = realtimeDataFilterUrl;

        if ($("#is_show_filters").val() == 1) {
            REAL_TIME.isFilter = true;
        };

        //该模块全局变量 是否有按监测指标筛选设备
        //筛选监测指标
        $("#filter-btn").click(function () {
            REAL_TIME.isFilter = true;
            $("#cancel-filter-btn").show();
            REAL_TIME.realtimeDataFilter();
        });
        //取消筛选监测指标
        $("#cancel-filter-btn").click(function () {
            REAL_TIME.isFilter = false;
            $(this).hide();
        });
        //监测指标筛选checkbox点击
        $(document).on("click", ".sensorinfo-filter-input", function () {
            var $sensorinfoes = $("#zone-sensorinfo-filter").find("input:checked");
            if ($sensorinfoes.length == 0) {
                art.dialog({
                    title: message.tips,
                    content: message.noSensor
                });
            }
        });
        var sensorPhysicalIds = [];
        if (REAL_TIME.isFilter) {
            $(".show-filters").trigger("click");
            $("#cancel-filter-btn").show();
            // 获得 选中监测指标的回显
            $(".sensorPhysicalId").each(function () {
                sensorPhysicalIds.push($(this).val());
            });
            $(".sensorinfo-filter-input").each(function () {
                if ($.inArray($(this).attr("data-type"), sensorPhysicalIds) == -1) {
                    $(this).removeAttr("checked");
                }
            });

            $("#real_time_data_table_mould").children("thead").children("tr").children("th").each(function (index, th) {
                if ($(th).attr("data-dead") != 1) {
                    if ($.inArray($(th).attr("data-type"), sensorPhysicalIds) == -1) {
                        $(th).remove();
                    }
                }
            });
            $("#real_time_data_table_mould").children("tbody").children("tr").children("td").each(function (index, td) {
                if ($(td).attr("data-dead") != 1) {
                    if ($.inArray($(td).attr("data-type"), sensorPhysicalIds) == -1) {
                        $(td).remove();
                    }
                }
            })
            REAL_TIME.initRealTimeDataHasSensorFilter(sensorPhysicalIds);
        } else {
            REAL_TIME.initRealTimeDataNoSensorFilter();
        }

        setInterval(function () {
            if (REAL_TIME.isFilter) {
                REAL_TIME.refreshRealTimeDataFiltered(sensorPhysicalIds);
            } else {
                REAL_TIME.refreshRealTimeDataWithoutFilterd();
            }
        }, window.REALTIME_REFRESH_INTERVAL);
    },
    // 刷新 实时数据 表格
    realtimeDataFilter: function () {
        var REAL_TIME = this;
        // 获得选中监测指标 并缓存
        var sensorinfoes = "";
        $("#zone-sensorinfo-filter").find("input:checked").each(function () {
            sensorinfoes += "sensorPhysicalIds=" + $(this).attr("data-type") + "&";
        });
        if (sensorinfoes === "") {
            art.dialog({
                title: message.tips,
                content: message.noSensor
            });
            return;
        }
        window.location.href = REAL_TIME.realtimeDataFilterUrl + "?" + sensorinfoes + "showFilters=1";
    },
    //隐藏实时数据表中 被过滤掉的列
    dealFilterTableColumn: function ($tableColumns, sensorinfoes) {
        $tableColumns.each(function (index, th) {
            if ($(th).attr("data-dead") != '1') {
                if ($.inArray($(th).attr("data-type"), sensorinfoes) == -1) {
                    $(th).hide();
                } else {
                    $(th).show();
                }
            }
        });
    },

    // 初始化 表格 有指标过滤
    initRealTimeDataHasSensorFilter: function (sensorinfoes) {
        var REAL_TIME = this;
        $.getJSON(REAL_TIME.realtimeDataUrl, {sensorPhysicalIds: sensorinfoes}, function (data) {
            REAL_TIME.initRealTimeData(data);
        });
    },

    // 初始化 实时数据 无检测指标过滤
    initRealTimeDataNoSensorFilter: function () {
        var REAL_TIME = this;
        $.getJSON(REAL_TIME.realtimeDataUrl, function (data) {
            REAL_TIME.initRealTimeData(data);
        });
    },

    // 初始化 实时数据表格 方法
    initRealTimeData: function (data) {
        var REAL_TIME = this;
        if (data.length < 1) {
            REAL_TIME.$realTimeDiv.append("<h4>无设备</h4>");
            return;
        }
        var $realTimeDataTable = $('#real_time_data_table_mould');
        $("#real_time_div").append($realTimeDataTable);
        var serverTime = new Date().getTime() - ($("body").attr("data-client-time") - $("body").attr("data-server-time"));
        var deviceIds = [];
        $.each(data, function (i, dataRow) {
            deviceIds.push(dataRow.nodeId);
            $realTimeDataTable.children("tbody").children("#" + dataRow.nodeId).children().each(function (index, td) {
                var $td = $(td);
                var dataType = $td.attr("data-type");
                if ($td.attr("data-dead") === "1") {
                    REALTIME_DATA_UTIL.baseAttrView($td, dataType, dataRow, serverTime);
                } else {
                    if(dataRow.sensorinfoMap != null && dataRow.sensorinfoMap == null){
                        if (dataRow.sensorinfoMap[dataType]) {
                            $td.attr("title", dataRow.sensorinfoMap[dataType].cnName);
                            dataRow.sensorinfoMap[dataType].state == 0 ? $td.text("--") : $td.text(dataRow.sensorinfoMap[dataType].sensorPhysicalValue);
                        }
                    }else if(dataRow.sensorinfoMap == null && dataRow.sensorinfoMap != null){
                        if (dataRow.sensorinfoMap[dataType]) {
                            $td.attr("title", dataRow.sensorinfoMap[dataType].cnName);
                            dataRow.sensorinfoMap[dataType].state == 0 ? $td.text("--") : $td.text(dataRow.sensorinfoMap[dataType].sensorPhysicalValue);
                        }
                    }
                }
            });
        });

        var sScrollY = 0;
        var sScrollX = "";

        // 实时数据表格高度自适应
        var scrollBarWidth = 30; // 浏览器滚动条宽度(比正常稍大一些)
        var $ele = $("#table-container");
        var windowHeight = $(window).height();
        var y = $ele.offset().top;
        $(window).resize(function (event) {
            // 表格高度 = 窗口高度 - 表格位置 - 滚动条宽度
            $ele.css("height", windowHeight - y - scrollBarWidth);
            sScrollY = windowHeight - y - scrollBarWidth - 90;
        }).resize();
        if (data.length <= 14) {
            sScrollY = "";
        }
        $realTimeDataTable.show();
        if (SET_TABLE_WIDTH.getTableLength($realTimeDataTable) >= 80) {

            sScrollX = 1163 + "px";
        }
        REAL_TIME.fixedTable($realTimeDataTable, sScrollY, sScrollX);

        REAL_TIME.preData = data;
    },
    // 固定表头 和 前四 列
    fixedTable: function ($realTimeDataTable, sScrollY, sScrollX) {
        SET_TABLE_WIDTH.setTableWidth($realTimeDataTable);
        var oTable = $realTimeDataTable.dataTable({
            "sScrollY": sScrollY + "px",
            "sScrollX": sScrollX,
            "bPaginate": false,
            "bLengthChange": true,
            "bFilter": false,
            "bSort": false,
            "bInfo": false,
            "bAutoWidth": false,
            "bScrollCollapse": false,
            "bRetrieve": false,
            "bPaginate": false,
            "bDestroy": false
        });
        new FixedColumns(oTable, {
            "iLeftColumns": 3
        });
    },

    //不带监测指标过滤实时数据
    refreshRealTimeDataWithoutFilterd: function () {
        var REAL_TIME = this;
        $.getJSON(REAL_TIME.realtimeDataUrl, function (data) {
            REAL_TIME.renderRealTimeDataView(data);
        });
    },
    //带监测指标过滤实时数据
    refreshRealTimeDataFiltered: function (sensorinfoes) {
        var REAL_TIME = this;
        $.getJSON(REAL_TIME.realtimeDataUrl, {sensorPhysicalIds: sensorinfoes}, function (data) {
            REAL_TIME.renderRealTimeDataView(data);
        });
    },


    // 刷新实时数据
    renderRealTimeDataView: function (data) {
        var REAL_TIME = this;
        var serverTime = new Date().getTime() - ($("body").attr("data-client-time") - $("body").attr("data-server-time"));
        // 往表格里加数据
        $.each(data, function (i, dataRow) {
            // 初始化 设备实时数据
            $(REAL_TIME.$device_realTime_dataTable_body).children("table").children("tbody").children("#" + dataRow.nodeId).children().each(function (index, td) {
                REALTIME_DATA_UTIL.baseAttrView($(td), $(td).attr("data-type"), dataRow, serverTime);
            });
            $(REAL_TIME.$realTime_dataTable_body).children("table").children("tbody").children("#" + dataRow.nodeId).children().each(function (index, td) {
                var $td = $(td);
                var dataType = $td.attr("data-type");
                if ($td.attr("data-dead") === "1") {
                    REALTIME_DATA_UTIL.baseAttrView($td, dataType, dataRow, serverTime);
                }
                if (dataRow.sensorinfoMap[dataType]) {
                    $td.attr("title", dataRow.sensorinfoMap[dataType].cnName);
                    dataRow.sensorinfoMap[dataType].state == 0 ? $td.text("--") : $td.text(dataRow.sensorinfoMap[dataType].sensorPhysicalValue);
                }
            });

            for (var i = 0; i < REAL_TIME.preData.length; i++) {
                // 没有时间戳，直接返回
                if (dataRow.stamp == null) {
                    return;
                }
                // 比较数据是否改变，如果改变则此行颜色动画效果
                if (dataRow.nodeId == REAL_TIME.preData[i].nodeId) {
                    var $appendTr0 = $(REAL_TIME.$device_realTime_dataTable_body).children("table").find("#" + dataRow.nodeId);
                    var $appendTr1 = $(REAL_TIME.$realTime_dataTable_body).children("table").find("#" + dataRow.nodeId);
                    if (dataRow.stamp != REAL_TIME.preData[i].stamp) {
                        //    颜色渐变, 由透明变成 lightgrenn ，然后再变成透明
                        $appendTr0.css("background-color", "transparent");
                        $appendTr1.css("background-color", "transparent");
                        // lightgreen => #90ee90
                        $appendTr0.animate({backgroundColor: "#90ee90"}, 1000)
                            .animate({backgroundColor: "transparent"}, 3000, function () {
                                $appendTr0.removeAttr('style');
                            });
                        $appendTr1.animate({backgroundColor: "#90ee90"}, 1000)
                            .animate({backgroundColor: "transparent"}, 3000, function () {
                                $appendTr1.removeAttr('style');
                            });
                    }
                }
            }
        });
        REAL_TIME.preData = data;
    }
};
