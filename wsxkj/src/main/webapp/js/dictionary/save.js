layui.use(['form','layer','jquery','common'],function(){
	var layer = parent.layer === undefined ? layui.layer : parent.layer;
	var $ = layui.jquery;
	var common=layui.common;
	var form=layui.form;
	form.on("submit(addType)",function(data){
		$.ajax({
            type: "POST",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: basePath+"dictionary/doAddDictionaryType" ,//url
            data: data.field,
            success: function (result) {
                if (result.flag) {
        			top.layer.msg("保存成功！");
         			layer.closeAll("iframe");
        	 		//刷新父页面tree
        	 		parent.toRefreshTree();
                }else{
//                	top.layer.close(index);
        			top.layer.msg("保存失败！");
                }
                
            },error: function (XMLHttpRequest, textStatus, errorThrown) {
            	console.log(textStatus);
            	console.log(XMLHttpRequest);
            	console.log(errorThrown);
            },beforeSend: function () {
//            	index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
//            	index=layer.load(0, {shade: false}); //0代表加载的风格，支持0-2
            },
            complete: function () {
//            	 top.layer.close(index);
            }
        });
		return false;
	});
	
	
});


