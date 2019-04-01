<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ include file="/common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>新增用户</title>
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
		<input id="id" name="id" value="${userInfo.id}" type="hidden" />
		<div class="layui-form-item" style="padding-top:10px;">
			<label class="layui-form-label">用户名</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input " name="loginName" value="${userInfo.loginName}" lay-verify="required" placeholder="请输入用户名,例如：zxj" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item" >
			<label class="layui-form-label">真实姓名</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input" required value="${userInfo.name}" name="name" lay-verify="required" placeholder="请输入姓名,例如：朱小军" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
		    <label class="layui-form-label">密码</label>
		    <div class="layui-input-inline">
		      <input type="password" name="password" value="${userInfo.password}" required lay-verify="required" placeholder="请输入密码" autocomplete="off" class="layui-input">
		    </div>
		    <div class="layui-form-mid layui-word-aux">密码最好6位以上</div>
		  </div>
		<div class="layui-form-item">
			<label class="layui-form-label">电子邮箱</label>
			<div class="layui-input-block">
				<input type="text" name="email" value="${userInfo.email}" class="layui-input" lay-verify="email" placeholder="请输入电子邮箱 ,例如：abc@163.com">
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
			    <label class="layui-form-label">性别</label>
			    <div class="layui-input-block sex">
			      	<input type="radio" name="sex" value="0" title="男" checked>
			      	<input type="radio" name="sex" value="1" title="女">
			      	<input type="radio" name="sex" value="2" title="保密">
			    </div>
		    </div>
		    <div class="layui-inline">
			    <label class="layui-form-label">等级</label>
				<div class="layui-input-block" >
					<select name="priority" id="priority"  class="priority" lay-filter="priority" >
						<option value="0">普通用户</option>
						<option value="1">注册用户</option>
						<option value="2">中级用户</option>
				        <option value="3">高级用户</option>
				    </select>
				</div>
		    </div>
		    <div class="layui-inline">
			    <label class="layui-form-label">状态</label>
				<div class="layui-input-block" >
					<select name="state" id="state" class="state" lay-filter="state">
						<option value="0">正常</option>
						<option value="1">禁用</option>
				    </select>
				</div>
		    </div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">描述</label>
			<div class="layui-input-block">
				<textarea placeholder="描述信息" name="description" class="layui-textarea linksDesc">${userInfo.description}</textarea>
			</div>
		</div>
		 
		<div class="layui-form-item">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit="" lay-filter="addUser">提交</button>
				<button type="reset" class="layui-btn layui-btn-primary">取消</button>
		    </div>
		</div>
		
		
	</form>
	<script type="text/javascript">
		var sex="${userInfo.sex}";
		var priority="${userInfo.priority}";
		var state="${userInfo.state}";
	</script>
	<script type="text/javascript" src="${ctx}/js/user/addUser.js"></script>
</body>
</html>