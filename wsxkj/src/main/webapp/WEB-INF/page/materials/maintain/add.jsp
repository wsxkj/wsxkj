<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ include file="/common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>新增</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="format-detection" content="telephone=no">
	<style type="text/css">
		.layui-form-item .layui-inline{ 
			width:33.333%; 
			float:left; 
			margin-right:0;
			
			 }
		@media(max-width:1240px){
			.layui-form-item .layui-inline{ width:100%; float:none; }
		}
		.layui-upload-img{width: 92px; height: 92px; margin: 0 10px 10px 0;}
	</style>
</head>
<body class="childrenBody">
	<form class="layui-form" id="form1"  method="post" style="width:80%;">
		<input id="id" name="id" value="${info.id}" type="hidden" />
		<div class="layui-form-item" style="padding-top:10px;">
			<label class="layui-form-label">登记日期</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input " id="registertime" name="registertime" value="${info.registertime}"  placeholder="日期" >
			</div>
		</div>
		<div class="layui-form-item" style="padding-top:10px;">
			<label class="layui-form-label">维修用户</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input " name="username" value="${info.username}"  placeholder="修理人的姓名" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">内容</label>
			<div class="layui-input-block">
				<textarea placeholder="内容" name="remark" class="layui-textarea linksDesc">${info.remark}</textarea>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">金额</label>
			<div class="layui-input-block">
				<input type="text" name="jine" lay-verify="number" value="${info.jine}" class="layui-input"  placeholder="金额">
			</div>
		</div> 
		<div class="layui-form-item">
		    <div class="layui-inline">
			    <label class="layui-form-label">是否付款</label>
				<div class="layui-input-block" >
					<select name="isPay" id="isPay"  lay-filter="isPay">
						<option value="1">已付款</option>
						<option value="0">欠账</option>
				    </select>
				</div>
		    </div>
		</div>
		<div class="layui-form-item">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit="" lay-filter="addInfo">提交</button>
				<button type="reset" class="layui-btn layui-btn-primary">取消</button>
		    </div>
		</div>
	</form>
	<script type="text/javascript">
		var state="${info.isPay}";
	</script>
	<script type="text/javascript" src="${ctx}/js/maintain/add.js"></script>
</body>
</html>