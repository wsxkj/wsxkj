<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page language="java"  pageEncoding="UTF-8"%>  <!-- errorPage="/error.jsp" -->

<%

// String path = request.getContextPath();
// String basePath = request.getScheme() + "://"
// 		+ request.getServerName() + ":" + request.getServerPort()
// 		+ path + "/";
//
String basePath="http://127.0.0.1:8080"+request.getContextPath()+"/";
pageContext.setAttribute("basePath", basePath); 
%>
<%-- JSTL 标签 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="mf" uri="/MyFramework-tags"%>
<%-- <%@ taglib prefix="mf" uri="/MyFramework-tags"%> --%>
<%-- 绝对路径 --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/plugin/layui/css/layui.css" media="all" />
<script type="text/javascript" src="${ctx}/plugin/layui/layui.js"></script>	
<script type="text/javascript">
var CTX="${ctx}";
var basePath="<%=basePath%>";
layui.config({
    base: basePath+"js/util/"      //自定义layui组件的目录
}).extend({ //设定组件别名
    common:   'common',
});
</script>