layui.config({
    base: basePath+"js/util/"      //自定义layui组件的目录
}).extend({ //设定组件别名
    common:   'common',
});

layui.use(['form','element','layer','jquery','common'],function(){
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		element = layui.element,
		$ = layui.jquery;
	var common=layui.common;
	$(".panel a").on("click",function(){
		window.parent.addTab($(this));
	})
	initSJZ();
	function initSJZ(){
		common.ajaxMethod(basePath+'maintainInfo/initList?page=1&limit=10',{},"POST",
				function (result) {
			if(result.msg=="success"){
				const arr=result.data;
				var str='';
				for(var i=0;i<arr.length;i++){
					str+='<li class="layui-timeline-item">';
					str+='<i class="layui-icon layui-timeline-axis"></i>';
					str+='<div class="layui-timeline-content layui-text">';
					str+='<h3 class="layui-timeline-title">'+(arr[i].registertime).substring(0,10)+'</h3>';
					str+='<p style="font-size:20px;line-height:30px;">';
					str+=arr[i].username;
					str+='<br/>'+arr[i].remark;
					str+='<br/>'+arr[i].jine+'元';
					if(arr[i].isPay=="0"){
						str+='<br/><span style="color:red;">未付款</span>';
					}else if(arr[i].isPay=="1"){
						str+='<br/><span style="color:green;">已付款</span>';
					}
				}
				str+='</p>';
				str+='</div>';
				str+='</li>';
				$("#sjz").html(str);
			}
                    
         });
	}
	
	
	//动态获取文章总数和待审核文章数量,最新文章
	$.get("../plugin/layui/json/newsList.json",
		function(data){
			var waitNews = [];
			$(".allNews span").text(data.length);  //文章总数
			for(var i=0;i<data.length;i++){
				var newsStr = data[i];
				if(newsStr["newsStatus"] == "待审核"){
					waitNews.push(newsStr);
				}
			}
			$(".waitNews span").text(waitNews.length);  //待审核文章
			//加载最新文章
			var hotNewsHtml = '';
			for(var i=0;i<5;i++){
				hotNewsHtml += '<tr>'
		    	+'<td align="left">'+data[i].newsName+'</td>'
		    	+'<td>'+data[i].newsTime+'</td>'
		    	+'</tr>';
			}
			$(".hot_news").html(hotNewsHtml);
		}
	)

	//图片总数
	$.get("../plugin/layui/json/images.json",
		function(data){
			$(".imgAll span").text(data.length);
		}
	)

	//用户数
	$.get("../plugin/layui/json/usersList.json",
		function(data){
			$(".userAll span").text(data.length);
		}
	)

	//新消息
	$.get("../plugin/layui/json/message.json",
		function(data){
			$(".newMessage span").text(data.length);
		}
	)



})
