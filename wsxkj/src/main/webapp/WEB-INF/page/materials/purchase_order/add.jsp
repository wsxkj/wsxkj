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
			<label class="layui-form-label">供应商</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input " name="name" value="${info.name}" lay-verify="required" placeholder="请输入供应商名称或维修商" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item" >
			<label class="layui-form-label">地址</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input"  value="${info.address}" name="address"  placeholder="请输入地址" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
		    <label class="layui-form-label">联系人</label>
		    <div class="layui-input-inline">
		      <input type="text" name="contactname" value="${info.contactname}"  placeholder="请输入联系人"  class="layui-input">
		    </div>
		  </div>
		<div class="layui-form-item">
			<label class="layui-form-label">电话号码</label>
			<div class="layui-input-block">
				<input type="text" name="phone" value="${info.phone}" class="layui-input" lay-verify="phone"   placeholder="请输入电话号码">
			</div>
		</div>
		<div class="layui-form-item">
		    <div class="layui-inline">
			    <label class="layui-form-label">状态</label>
				<div class="layui-input-block" >
					<select name="state" id="state"  lay-filter="state">
						<option value="1">供应商</option>
						<option value="2">维修商</option>
				    </select>
				</div>
		    </div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">备注</label>
			<div class="layui-input-block">
				<textarea placeholder="备注" name="remark" class="layui-textarea linksDesc">${info.remark}</textarea>
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
		var state="${info.state}";
	</script>
	<script type="text/javascript" src="${ctx}/js/supplier/add.js"></script>
</body>
</html>