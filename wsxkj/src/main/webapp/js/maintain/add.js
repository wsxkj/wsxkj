layui.use(['form','layer','jquery','laydate','common'],function(){
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : parent.layer;
	var	$ = layui.jquery;
	var common=layui.common;
	var laydate = layui.laydate;
	
	
	setTimeout(() => {
		initData();  
	}, 0);
	function initData(){
		
		laydate.render({
		    elem: '#registertime' //指定元素
		});
		
		common.initSelect("isPay",state);
        //重新渲染样式
		form.render(); 
		
	}
	//监听select框
//	form.on('select(isPay)', function(data){
//		$("#supplierName").val(data.elem[data.elem.selectedIndex].text);
//	});
 	form.on("submit(addInfo)",function(data){
 		console.log(data);
 		$.ajax({
                type: "POST",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: basePath+"maintainInfo/doAdd" ,//url
                data: data.field,
                success: function (result) {
                    if (result.flag) {
            			top.layer.msg("保存成功！");
             			layer.closeAll("iframe");
            	 		//刷新父页面
            	 		parent.location.reload();
                    }else{
//                    	top.layer.close(index);
            			top.layer.msg("保存失败！");
                    }
                    
                },error: function (XMLHttpRequest, textStatus, errorThrown) {
                	console.log(textStatus);
                	console.log(XMLHttpRequest);
                	console.log(errorThrown);
                },beforeSend: function () {
//                	index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
//                	index=layer.load(0, {shade: false}); //0代表加载的风格，支持0-2
                },
                complete: function () {
//                	 top.layer.close(index);
                }
            });
 		return false;
 	});
})

