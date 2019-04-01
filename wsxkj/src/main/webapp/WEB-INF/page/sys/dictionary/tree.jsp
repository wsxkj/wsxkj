<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树</title>
<link rel="stylesheet" type="text/css" href="${ctx}/plugin/EasyUI/EasyUI1.3.6/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/plugin/EasyUI/EasyUI1.3.6/css/common.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/plugin/EasyUI/EasyUI1.3.6/css/icon.css" />
<script type="text/javascript" src="${ctx}/js/jquery/jquery-1.10.1.min.js"></script>
<script type="text/javascript" src="${ctx}/plugin/EasyUI/EasyUI1.3.6/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/plugin/EasyUI/EasyUI1.3.6/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/plugin/EasyUI/EasyUI1.3.6/themes/blue/style.css" />
<%-- <link rel="stylesheet" href="${ctx}/plugin/layui/css/layui.css" media="all" /> --%>
<%-- <script type="text/javascript" src="${ctx}/plugin/layui/layui.all.js"></script>	 --%>
<script type="text/javascript">
var basePath="<%=basePath%>";

$(document).ready(function(){
	   initTree();
	});

	function initTree(){
		$("#tt").tree({
			url:basePath+'dictionary/initTree',
			
			onBeforeExpand:function(node,param){  
		        $('#tt').tree('options').url = basePath+"dictionary/initTree?id=" + node.id;
		    },  
			animate: true,
	         loadFilter: function(data){	
		          if (data.msg){	
		            return data.msg;	
		         } else {	
		           return data;	
		         }	
		     }, 
		     onDblClick:function(){
		    	 var cnode = $("#tt").tree('getSelected');
		    	 if(cnode!=null){
		    		 var pnode=$("#tt").tree('getParent',cnode.target);
// 			    	 listItem(cnode);
		    		 toEdit(pnode,cnode);
		    	 }
		     },

		     onContextMenu: function(e,node){
		  		e.preventDefault();
		  		$("#tt").tree('select',node.target);
		  		$('#mm').menu('show',{
		  			left: e.pageX,
		  			top: e.pageY
		  		});
		  	}
		});
		
	}
	//新增
	function append(){
		var t = $('#tt');
		var node = t.tree('getSelected');
		t.tree('append', {
			parent: (node?node.target:null),
			data: [{
				text: '新建菜单'
			}]
		});
	}
	function editit(){
		 var cnode = $("#tt").tree('getSelected');
	   	 if(cnode!=null){
	   		 var pnode=$("#tt").tree('getParent',cnode.target);
	   		 listItem(cnode);
// 	   		toEdit(pnode,cnode);
	   	 }
	}
	//删除
	function removeit(){
		var node = $('#tt').tree('getSelected');
		if(node!=null){
			parent.removeDep(node.attributes.pk);
		}
	}
	function removeJd(){
		var node = $('#tt').tree('getSelected');
		var pnode=$('#tt').tree('getParent',node.target);
		$('#tt').tree('remove', node.target);
		$('#tt').tree("reload",pnode.target);
		$('#tt').tree("expand",pnode.target);
	}
	function collapse(){
		var node = $('#tt').tree('getSelected');
		$('#tt').tree('collapse',node.target);
	}
	function expand(){
		var node = $('#tt').tree('getSelected');
		$('#tt').tree('expand',node.target);
	}

//刷新树
function toRefreshTree(){
	var node = $('#tt').tree('getSelected');
	var pnode=$('#tt').tree('getParent',node.target);
	if(pnode!=null){
		$('#tt').tree("reload",pnode.target);
		$('#tt').tree("expand",pnode.target);
	}else{
		$('#tt').tree("reload",node);
	}
}

function toEdit(pnode,cnode){
	var cpk="";
	var ppk="";
	if(cnode.attributes!=null){
		cpk=cnode.attributes.pk;
	}
	if(pnode!=null&&pnode.attributes!=null){
		ppk=pnode.attributes.pk;
	}
	if(cpk=="1"||cpk==1){
		alert("根节点不能修改");
	}else{
		parent.openTypeDialog(cpk,ppk);
	}
	
}

function listItem(cnode){
	parent.toListItem(cnode.attributes.pk);
}
</script>
</head>
<body>
	<div>
		<ul id="tt"  ></ul>
	</div>
	<div id="mm" class="easyui-menu" style="width:120px;">
		<div onclick="append()" data-options="iconCls:'icon-add'">新增</div>
		<div onclick="editit()" data-options="iconCls:'icon-edit'">编辑</div>
		<div onclick="removeit()" data-options="iconCls:'icon-remove'">删除</div>
		<div class="menu-sep"></div>
		<div onclick="expand()">打开</div>
		<div onclick="collapse()">关闭</div>
	</div>
</body>
</html>