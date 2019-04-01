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
	<form class="layui-form" id="form1"  method="post" style="width:90%;">
		<input id="id" name="id" value="${info.id}" type="hidden" />
		<div class="layui-form-item" style="padding-top:10px;">
			<div class="layui-inline">
				<label class="layui-form-label">商品名称</label>
				<div class="layui-input-inline" style="width: 180px;">
					<input type="text" class="layui-input " name="name" value="${info.name}" lay-verify="required" placeholder="请输入商品名称" class="layui-input">
				</div>
				<label class="layui-form-label">规格型号</label>
				<div class="layui-input-inline" style="width: 180px;">
					<input type="text" class="layui-input"  value="${info.type}" name="type"  placeholder="规格型号" class="layui-input">
				</div>
			</div>
		</div>
<!-- 		<div class="layui-form-item" > -->
<!-- 			<div class="layui-inline"> -->
				
<!-- 			</div> -->
<!-- 		</div> -->
		<div class="layui-form-item">
			<div class="layui-inline">
			    <label class="layui-form-label">单位</label>
			    <div class="layui-input-inline" style="width: 180px;">
			      <input type="text" name="unit" value="${info.unit}"  placeholder="请输入单位"  class="layui-input">
			    </div>
			     <label class="layui-form-label">供应商</label>
					<div class="layui-input-inline" style="width: 180px;" >
						<select name="supplierId" id="supplierId"  lay-filter="supplierId">
					    </select>
					</div>
			  </div>
		 </div> 
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">进价</label>
				<div class="layui-input-inline" style="width: 180px">
					<input type="text" name="purchasePrice" lay-verify="number" value="${info.purchasePrice}" class="layui-input"  placeholder="请输入进价">
				</div>
				<label class="layui-form-label">卖价</label>
				<div class="layui-input-inline" style="width: 180px">
					<input type="text" name="sellingPrice" lay-verify="number" value="${info.sellingPrice}" class="layui-input"  placeholder="请输入卖价">
				</div>
			</div>	
		</div>
		
		<div class="layui-form-item">
		    <div class="layui-inline">
			   
		    </div>
		    <input type="hidden"  id="supplierName"  name="supplierName" value="${info.supplierName}" class="layui-input" >
		</div>
		<div class="layui-form-item">
				<label class="layui-form-label">商品类别</label>
                <div class="layui-input-inline" style="width: 140px" >
                    <select name="fenlei1" id="fenlei1"  lay-filter="fenlei1">
                        <option value="">请选择</option>
                    </select>
                </div>
                <div class="layui-input-inline" style="width: 140px">
                    <select name="fenlei2" id="fenlei2"   lay-filter="fenlei2">
                        <option value="">请选择</option>
                    </select>
                </div>
                <div class="layui-input-inline" style="width: 140px">
                    <select name="fenlei3" id="fenlei3" lay-filter="fenlei3" >
                        <option value="">请选择</option>
                    </select>
                </div>
                 <input type="hidden"  id="goodsType"  name="goodsType" value="${info.goodsType}" class="layui-input" />
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">备注</label>
			<div class="layui-input-block">
				<textarea placeholder="备注" name="remark" class="layui-textarea linksDesc">${info.remark}</textarea>
			</div>
		</div>
		 <div class="layui-form-item">
            <div class="layui-inline">
		 		<label class="layui-form-label">图片：</label>
                <div class="layui-input-block">
                    <div class="layui-upload">
                        <input type="hidden" name="fujian" id="fujian" value="" />
                        <button type="button" class="layui-btn layui-btn-normal" id="testList_fujian">选择多文件</button>&nbsp;<button type="button" class="layui-btn" id="testListAction_fujian">开始上传</button>
                        <div class="layui-upload-list">
                            <table class="layui-table" style="width: 500px;">
                                <thead><tr><th>文件名</th><th>状态</th><th>操作</th></tr></thead>
                                <tbody id="demoList_fujian"></tbody>
                            </table>
                        </div>
                    </div>
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
		var state="${info.supplierId}";
// 		var goodsType="${info.goodsType}";
	</script>
	<script type="text/javascript" src="${ctx}/js/goods/add.js"></script>
</body>
</html>