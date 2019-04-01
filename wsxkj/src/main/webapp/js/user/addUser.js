layui.use(['form','layer','jquery','common'],function(){
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : parent.layer;
	var	$ = layui.jquery;
	var common=layui.common;
	setTimeout(() => {
		initData();  
	}, 0);
	function initData(){
		common.initRadio("sex",sex);
		common.initSelect("priority",priority);
		common.initSelect("state",state);
//		common.initArea("");
		//重新渲染样式
		form.render(); 
	}
	
 	var addUserArray = [],addUser;
 	form.on("submit(addUser)",function(data){
// 		var index=null;
 		$.ajax({
                type: "POST",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: basePath+"/userInfo/doAdd" ,//url
                data: data.field,
                success: function (result) {
                    if (result.flag) {
            			top.layer.msg("用户保存成功！");
            			common.layerCloseAllIframe();
//            			layer.closeAll('iframe'); //关闭所有的iframe层
            	 		//刷新父页面
            	 		parent.location.reload();
                    }else{
            			top.layer.msg("用户保存失败！");
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

//格式化时间
function formatTime(_time){
    var year = _time.getFullYear();
    var month = _time.getMonth()+1<10 ? "0"+(_time.getMonth()+1) : _time.getMonth()+1;
    var day = _time.getDate()<10 ? "0"+_time.getDate() : _time.getDate();
    var hour = _time.getHours()<10 ? "0"+_time.getHours() : _time.getHours();
    var minute = _time.getMinutes()<10 ? "0"+_time.getMinutes() : _time.getMinutes();
    return year+"-"+month+"-"+day+" "+hour+":"+minute;
}
