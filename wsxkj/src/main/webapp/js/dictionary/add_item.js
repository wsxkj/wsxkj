layui.use(['form','layer','jquery','common'],function(){
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : parent.layer;
	var	$ = layui.jquery;
	var common=layui.common;
	setTimeout(() => {
		initData();  
	}, 0);
	function initData(){
		common.initSelect("isEnable",isEnable);
	}
	
 	form.on("submit(addItemInfo)",function(data){
 		$.ajax({
                type: "POST",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: basePath+"dictionary/saveItem" ,//url
                data: data.field,
                success: function (result) {
                    if (result.flag) {
            			top.layer.msg("保存成功！");
             			layer.closeAll("iframe");
            	 		//刷新父页面
            	 		parent.location.reload();
                    }else{
            			top.layer.msg("保存失败！");
                    }
                    
                },error: function (XMLHttpRequest, textStatus, errorThrown) {
                	console.log(textStatus);
                	console.log(XMLHttpRequest);
                	console.log(errorThrown);
                },beforeSend: function () {
                },
                complete: function () {
                }
            });
 		return false;
 	});
})

