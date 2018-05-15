/**
 * Created by lijianfei on 17/3/22.
 */
$.validator.setDefaults({

    errorElement: 'span',
    errorClass: 'help-block',
    highlight: function (element) {
        $(element).closest('.form-group').addClass('has-error');
    },
    success: function (element) {
        $(element).closest('.form-group').removeClass('has-error');
    },
    errorPlacement: function (error, element) {
        if (element.data('toggle') == 'city-picker') {
            error.insertAfter(element.parent().find('.city-picker-dropdown'));
        } else if (element.parent('.input-group').length || element.prop('type') === 'checkbox' || element.prop('type') === 'radio') {
            error.appendTo(element.parent().parent());
        } else {
            error.insertAfter(element);
        }
    }
});

// 手机号码验证
jQuery.validator.addMethod("mobileCN", function (value, element) {
    var length = value.length;
    return this.optional(element) || (length == 11 && /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1})|(147))+\d{8})$/.test(value));
}, "请输入正确的手机号码");

// 电话号码验证
jQuery.validator.addMethod("phoneCN", function (value, element) {
    var tel = /^(\d{3,4}-?)?\d{7,9}$/g;
    return this.optional(element) || (tel.test(value));
}, "请输入正确的电话号码");

// 邮政编码验证
jQuery.validator.addMethod("zipcodeCN", function (value, element) {
    var tel = /^[0-9]{6}$/;
    return this.optional(element) || (tel.test(value));
}, "请输入正确的邮政编码");

// 经度验证
jQuery.validator.addMethod("lng", function (value, element) {
    var reg = /^-?(?:(?:180(?:\.0{1,5})?)|(?:(?:(?:1[0-7]\d)|(?:[1-9]?\d))(?:\.\d{1,6})?))$/;
    return this.optional(element) || (reg.test(value));
}, "请输入正确的经度");

// 纬度验证
jQuery.validator.addMethod("lat", function (value, element) {
    var reg = /^-?(?:90(?:\.0{1,5})?|(?:[1-8]?\d(?:\.\d{1,6})?))$/;
    return this.optional(element) || (reg.test(value));
}, "请输入正确的纬度");