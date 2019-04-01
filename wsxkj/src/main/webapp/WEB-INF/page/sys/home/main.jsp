<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>首页--layui后台管理模板</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="format-detection" content="telephone=no">
	<link rel="stylesheet" href="${ctx}/plugin/layui/css/font_eolqem241z66flxr.css" media="all" />
	<link rel="stylesheet" href="${ctx}/plugin/layui/css/main.css" media="all" />
	
</head>
<body class="childrenBody">
	<div class="panel_box row">
		<div class="panel col">
			<a href="javascript:;" data-url="page/message/message.html">
				<div class="panel_icon">
					<i class="layui-icon" data-icon="&#xe63a;">&#xe63a;</i>
				</div>
				<div class="panel_word newMessage">
					<span></span>
					<cite>新消息</cite>
				</div>
			</a>
		</div>
		<div class="panel col">
			<a href="javascript:;" data-url="page/user/allUsers.html">
				<div class="panel_icon" style="background-color:#FF5722;">
					<i class="iconfont icon-dongtaifensishu" data-icon="icon-dongtaifensishu"></i>
				</div>
				<div class="panel_word userAll">
					<span></span>
					<cite>新增人数</cite>
				</div>
			</a>
		</div>
		<div class="panel col">
			<a href="javascript:;" data-url="page/user/allUsers.html">
				<div class="panel_icon" style="background-color:#009688;">
					<i class="layui-icon" data-icon="&#xe613;">&#xe613;</i>
				</div>
				<div class="panel_word userAll">
					<span></span>
					<cite>用户总数</cite>
				</div>
			</a>
		</div>
		<div class="panel col">
			<a href="javascript:;" data-url="page/img/images.html">
				<div class="panel_icon" style="background-color:#5FB878;">
					<i class="layui-icon" data-icon="&#xe64a;">&#xe64a;</i>
				</div>
				<div class="panel_word imgAll">
					<span></span>
					<cite>图片总数</cite>
				</div>
			</a>
		</div>
		<div class="panel col">
			<a href="javascript:;" data-url="page/news/newsList.html">
				<div class="panel_icon" style="background-color:#F7B824;">
					<i class="iconfont icon-wenben" data-icon="icon-wenben"></i>
				</div>
				<div class="panel_word waitNews">
					<span></span>
					<cite>待审核文章</cite>
				</div>
			</a>
		</div>
		<div class="panel col max_panel">
			<a href="javascript:;" data-url="page/news/newsList.html">
				<div class="panel_icon" style="background-color:#2F4056;">
					<i class="iconfont icon-text" data-icon="icon-text"></i>
				</div>
				<div class="panel_word allNews">
					<span></span>
					<em>文章总数</em>
					<cite>文章列表</cite>
				</div>
			</a>
		</div>
	</div>
	<ul class="layui-timeline" style="margin: 20px;" id="sjz" lay-filter="sjz">
<!-- 	  <li class="layui-timeline-item"> -->
<!-- 	    <i class="layui-icon layui-timeline-axis"></i> -->
<!-- 	    <div class="layui-timeline-content layui-text"> -->
<!-- 	      <h3 class="layui-timeline-title">8月18日</h3> -->
<!-- 	      <p> -->
<!-- 	        layui 2.0 的一切准备工作似乎都已到位。发布之弦，一触即发。 -->
<!-- 	        <br>不枉近百个日日夜夜与之为伴。因小而大，因弱而强。 -->
<!-- 	        <br>无论它能走多远，抑或如何支撑？至少我曾倾注全心，无怨无悔 <i class="layui-icon"></i> -->
<!-- 	      </p> -->
<!-- 	    </div> -->
<!-- 	  </li> -->
<!-- 	  <li class="layui-timeline-item"> -->
<!-- 	    <i class="layui-icon layui-timeline-axis"></i> -->
<!-- 	    <div class="layui-timeline-content layui-text"> -->
<!-- 	      <h3 class="layui-timeline-title">8月16日</h3> -->
<!-- 	      <p>杜甫的思想核心是儒家的仁政思想，他有<em>“致君尧舜上，再使风俗淳”</em>的宏伟抱负。个人最爱的名篇有：</p> -->
<!-- 	      <ul> -->
<!-- 	        <li>《登高》</li> -->
<!-- 	        <li>《茅屋为秋风所破歌》</li> -->
<!-- 	      </ul> -->
<!-- 	    </div> -->
<!-- 	  </li> -->
<!-- 	  <li class="layui-timeline-item"> -->
<!-- 	    <i class="layui-icon layui-timeline-axis"></i> -->
<!-- 	    <div class="layui-timeline-content layui-text"> -->
<!-- 	      <h3 class="layui-timeline-title">8月15日</h3> -->
<!-- 	      <p> -->
<!-- 	        中国人民抗日战争胜利日 -->
<!-- 	        <br>常常在想，尽管对这个国家有这样那样的抱怨，但我们的确生在了最好的时代 -->
<!-- 	        <br>铭记、感恩 -->
<!-- 	        <br>所有为中华民族浴血奋战的英雄将士 -->
<!-- 	        <br>永垂不朽 -->
<!-- 	      </p> -->
<!-- 	    </div> -->
<!-- 	  </li> -->
<!-- 	  <li class="layui-timeline-item"> -->
<!-- 	    <i class="layui-icon layui-timeline-axis"></i> -->
<!-- 	    <div class="layui-timeline-content layui-text"> -->
<!-- 	      <div class="layui-timeline-title">过去</div> -->
<!-- 	    </div> -->
<!-- 	  </li> -->
	</ul>  
	<script type="text/javascript" src="${ctx}/plugin/layui/js/main.js"></script>
	
</body>
</html>