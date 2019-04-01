<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/common/header.jsp"%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge"> 
<meta name="viewport" content="width=device-width, initial-scale=1"> 
<title>login</title>
<link rel="stylesheet" type="text/css" href="${ctx}/css/login/normalize.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/login/demo.css" />
<!--必要样式-->
<link rel="stylesheet" type="text/css" href="${ctx}/css/login/component.css" />
<!--[if IE]>
<script src="${ctx}/js/login/html5.js"></script>
<![endif]-->
</head>
<body>
		<div class="container demo-1">
			<div class="content">
				<div id="large-header" class="large-header">
					<canvas id="demo-canvas"></canvas>
					<div class="logo_box">
						<h3>后台管理系统</h3>
						<form action="${url}" name="form1" id="form1" method="post">
							<div class="input_outer">
								<span class="u_user"></span>
								<input name="username" class="text" style="color: #FFFFFF !important" type="text" placeholder="请输入账户">
							</div>
							<div class="input_outer">
								<span class="us_uer"></span>
								<input name="password" class="text" style="color: #FFFFFF !important; position:absolute; z-index:100;"value="" type="password" placeholder="请输入密码">
							</div>
							<div class="mb2"><a class="act-but submit" onclick="checkLogin()" href="javascript:;" style="color: #FFFFFF">登录</a></div>
						</form>
					</div>
				</div>
			</div>
		</div><!-- /container -->
		<script src="${ctx}/js/login/TweenLite.min.js"></script>
		<script src="${ctx}/js/login/EasePack.min.js"></script>
		<script src="${ctx}/js/login/rAF.js"></script>
		<script src="${ctx}/js/login/demo-1.js"></script>
		<script src="${ctx}/js/jquery/jquery-1.10.1.min.js"></script>
		<script type="text/javascript">
			 $(document).ready(function(){
					document.onkeydown = function (event) {
		            var e = event || window.event || arguments.callee.caller.arguments[0];
		            if (e && e.keyCode == 13) {
			                // enter 键
			                checkLogin();
			            }
			        }
				});
				function checkLogin() {
					form1.action="checkLogin";
					form1.submit();
				}
		</script>
		<div style="text-align:center;">
</div>
	</body>
</html>