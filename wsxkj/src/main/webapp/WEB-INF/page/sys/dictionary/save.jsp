<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<title>字典页面</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="format-detection" content="telephone=no">
	<meta name="renderer" content="webkit">
	<script type="text/javascript" src="${ctx}/js/dictionary/save.js"></script>
	<style type="text/css">
/* 	.layui-form-item .layui-form-label{  */
/* 			width:100px;  */
/* /* 			float:left;  */ */
/* 			 } */
/* 	.layui-form-item  .layui-input-block{ */
/* 		width: 200px; */
/* 	}		  */
	</style>
</head>
<body >
	<div style="padding: 10px;">
		<form class="layui-form" action="">
			<div class="layui-form-item">
				<div class="layui-inline">
				    <label class="layui-form-label">上级分类名称</label>
				    <div class="layui-input-block">
				      <input type="text" name="parentTypeName" value="${dt.parentTypeName}" class="layui-input"  readonly="readonly" />
				    	<input type="hidden" name="parentTypeid" value="${dt.parentTypeid}"  />
				    	<input type="hidden" name="id" value="${dt.id}" />
				    </div>
				 </div>   
			 </div>
		  <div class="layui-form-item">
		  	<div class="layui-inline">
		    <label class="layui-form-label">类别名称</label>
		    <div class="layui-input-block">
		      <input type="text" name="typeName" value="${dt.typeName}" autocomplete="off" placeholder="类别名称" class="layui-input">
		    </div>
		    </div>
		  </div>
		  <div class="layui-form-item">
		  <div class="layui-inline">
		    <label class="layui-form-label">类别编码</label>
		    <div class="layui-input-block">
		      <input type="text" name="typeCode" value="${dt.typeCode}"  autocomplete="off" placeholder="类别编码" class="layui-input">
		    </div>
		  </div>
		  </div>
		   <div class="layui-form-item">
		  <div class="layui-inline">
		    <label class="layui-form-label">排序</label>
		    <div class="layui-input-block">
		      <input type="text" name="orderNum" value="${dt.orderNum}"  autocomplete="off" placeholder="排序" class="layui-input">
		    </div>
		  </div>
		  </div>
		  <div class="layui-form-item">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit="" lay-filter="addType">提交</button>
				<button type="reset" class="layui-btn layui-btn-primary">取消</button>
		    </div>
		</div>
		</form>
	 </div> 
</body>
</html>