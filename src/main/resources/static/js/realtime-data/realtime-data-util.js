/**
 * 实时数据的数据处理 工具
 *
 * @author Wang yunlong
 * @time 13-2-1 下午2:57
 * @check @gaohui 2013-02-25 #1707
 */
var message = {
    // realtime-data-util
    normal: "正常",
    lowVoltage: "低电",
    powerDown: "掉电",
    overtime: "超时",
    unknown: "未知",
    normalMode: "正常模式",
    inspection: "巡检模式",
    nothing: "暂无",
    noDevice: "无设备",
    relicName:"暂无文物"
};

var REALTIME_DATA_UTIL = {
    // 原始电压转化为页面展示元素
    deviceLowVoltageToHtml2: function (lowvoltage, classStr, name) {
        if (lowvoltage == -1) {
            return "<span class='label " + classStr + "'>" + name + "</span>";
        } else if (lowvoltage == 0) {
            return "<span class='label " + classStr + "'>" + name + "</span>";
        } else {
            return "<span class='label " + classStr + "'>" + lowvoltage + "V</span>";
        }
    },
    // 设备状态
    deviceStateToString: function (lowvoltage, anomaly) {

        var classStr = "";
        if (anomaly == -1) {
            classStr = "label-danger";
            return this.deviceLowVoltageToHtml2(lowvoltage, classStr, message.overtime);
        } else if (anomaly == 0) {
            classStr = "";
            if (lowvoltage == 0) {
                return this.deviceLowVoltageToHtml2(0, classStr, message.unknown);
            } else {
                classStr = "label-success";
                return this.deviceLowVoltageToHtml2(lowvoltage, classStr, message.normal);
            }
        } else if (anomaly == 1) {
            classStr = "label-warning";
            return this.deviceLowVoltageToHtml2(lowvoltage, classStr, message.lowVoltage);
        } else if (anomaly == 2) {
            classStr = "label-warning";
            return this.deviceLowVoltageToHtml2(lowvoltage, classStr, message.powerDown);
        } else {
            return "...";
        }
    },


    // 原始设备anomaly字符串输出
    deviceAnomalyToString: function (anomaly) {
        switch (anomaly) {
            case -1:
                return message.overtime;
            case 0:
                return message.normal;
            case 1:
                return message.lowVoltage;
            case 2:
                return message.powerDown;
            default:
                return "...";
        }
    },
    // 原始设备模式值字符串输出
    deviceModeToString: function (mode) {
        switch (mode) {
            case 0:
                return message.normalMode;
            case 1:
                return message.inspection;
            default:
                return "...";
        }
    },

    // 实时数据非监测指标信息展示
    baseAttrView: function ($td, dataType, dataRow, serverTime) {
        if (dataType == "stamp") {
            this.datetimeView($td, dataRow[dataType], serverTime);
            $td.val(dataRow[dataType]);
        } else if (dataType == "anomaly") {
            $td.text(this.deviceAnomalyToString(dataRow[dataType]));
        } else if (dataType == "deviceMode") {
            $td.text(this.deviceModeToString(dataRow[dataType]));
        } else if (dataType == "lowvoltage") {
            $td.html(this.deviceLowVoltageToHtml2(dataRow["lowvoltage"]));
        } else if (dataType == "state") {
            $td.html(this.deviceStateToString(dataRow["lowvoltage"], dataRow["anomaly"]));
        } else if (dataType == "nodeName") {
            var nodeName = "";
            if (dataRow["nodeName"] == null || dataRow["nodeName"] === '') {
                nodeName = dataRow["locationId"].substring(8);
            } else {
                nodeName = dataRow["nodeName"];
            }
            $td.html("<span>" + nodeName + "</span>").attr("title", dataRow["locationId"]);
        } else if (dataType == "locationName") {
            var locationName = "";
            if (dataRow["locationName"] != null || dataRow["locationName"] != '') {
                locationName = dataRow["locationName"];
            }
            $td.html("<span>" + locationName + "</span>").attr("title", dataRow["locationId"]);
        } else {
            $td.text(dataRow[dataType] == null ? message.relicName : dataRow[dataType]);
        }
    },
    // 时间处理
    datetimeView: function ($td, datetime, serverTime) {
        if (datetime == null) {
            return;
        }
        var FORMAT_LONG = 'YYYY-MM-DD HH:mm:ss';
        var FORMAT2 = 'YYYY-MM-DDTHH:mm:ss';
        var serverNow = moment(parseInt(serverTime));
        var stamp = moment(datetime, FORMAT2);
        $td.attr("title", stamp.format(FORMAT_LONG));
        $td.attr("time-value", stamp.format(FORMAT2));
        // 如果数据时间比当前时间相差不到一个小时, 则用相对时间
        if (serverNow.diff(stamp, 'hours', true) < 1) {
            $td.text(stamp.from(serverNow));
            console.log($td);
            console.log($td.text());
        }
        // 如果是同一年, 则不显示年份
        else if (stamp.year() == serverNow.year()) {
            $td.text(stamp.format("MM-DD HH:mm:ss"));
        } else {
            $td.text(stamp.format(FORMAT_LONG));
        }
    }
};
