<#import "/spring.ftl" as spring>
<#assign basePath><@spring.url relativeUrl="/"/></#assign>
<meta charset="utf-8">
<base href="${basePath}">
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<#include "tags.ftl">