<#--
页面 script, link 标签支持, 并提供缓存过期机制
-->

<#--
base 标签
-->
<#macro baseTag subPath>
<base href="${basePath()}/${subPath}/" data-root="${basePath()}">
</#macro>

<#--
base 标签, href 为 root
-->
<#macro baseTagRoot >
<base href="${basePath()}/" data-root="${basePath()}">
</#macro>

<#--
script for javascript
-->
<#macro scriptTag src>
<script type="text/javascript" src="${src}?_=${Application['app.startTime']?long?c}"></script>
</#macro>


<#--
link for css
-->
<#macro linkTag href type= "text/css">
<link type="${type}" rel="stylesheet" href="${href}?_=${Application['app.startTime']?long?c}">
</#macro>

<#--
辅助 select 标签的 option 标签
@param values 为 option 的 数组, 其中一个 option 也为一个数组, option[0] 为 option 的 value, option[1] 为 option  的 label
              例如：[[7, "网关"], [2, "中继"], [1, "节点"]]
-->
<#macro optionsTag values, selected2>
    <#list values as option>
    <option value="${option[0]!}" <@selected selected2, option[0]/>>${option[1]!}</option>
    </#list>
</#macro>

<#--
辅助 select 标签 option 的 selected 属性
@param expected 期待的值
@param value 实际的值
-->
<#macro selected expected, value>
    <#if expected == value>selected="selected"</#if>
</#macro>

<#macro checked expected, value>
    <#if expected == value>checked="checked"</#if>
</#macro>

<#macro disabled value>
    <#if value>disabled</#if>
</#macro>

<#-- 返回当前web应用的基路径, 注意结尾不包含 "/" -->
<#function basePath>
    <#local __base =  request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()?c + request.getContextPath()>
    <#return __base>
</#function>
