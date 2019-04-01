//JavaScript代码区域
var $,tab,skyconsWeather;
layui.config({
	base : basePath+"js/home/"
}).use(['bodyTab','form','element','layer','jquery'],function(){
	var form = layui.form,
		layer = layui.layer,
		element = layui.element;
		$ = layui.jquery;
		tab = layui.bodyTab();

	//锁屏
	function lockPage(){
		layer.open({
			title : false,
			type : 1,
			content : $("#lock-box"),
			closeBtn : 0,
			shade : 0.9
		})
	}
	$(".lockcms").on("click",function(){
		window.sessionStorage.setItem("lockcms",true);
		lockPage();
	})
	// 判断是否显示锁屏
	if(window.sessionStorage.getItem("lockcms") == "true"){
		lockPage();
	}
	// 解锁
	$("#unlock").on("click",function(){
		if($(this).siblings(".admin-header-lock-input").val() == ''){
			layer.msg("请输入解锁密码！");
		}else{
			if($(this).siblings(".admin-header-lock-input").val() == USERINFO.password){
				window.sessionStorage.setItem("lockcms",false);
				$(this).siblings(".admin-header-lock-input").val('');
				layer.closeAll("page");
			}else{
				layer.msg("密码错误，请重新输入！");
			}
		}
	});
	$(document).on('keydown', function() {
		if(event.keyCode == 13) {
			$("#unlock").click();
		}
	});

	//手机设备的简单适配
	var treeMobile = $('.site-tree-mobile'),
		shadeMobile = $('.site-mobile-shade')

	treeMobile.on('click', function(){
		$('body').addClass('site-mobile');
	});

	shadeMobile.on('click', function(){
		$('body').removeClass('site-mobile');
	});
	//退出
	$("#exitUser").on("click",function(){
		$("#form1").action="login/logOut";
		$("#form1").submit();
	});
	
	// 添加新窗口
	$(".layui-nav .layui-nav-item a").on("click",function(){
		addTab($(this));
		$(this).parent("li").siblings().removeClass("layui-nav-itemed");
	})

	//公告层
	function showNotice(){
		layer.open({
	        type: 1,
	        title: "系统公告", //不显示标题栏
	        closeBtn: false,
	        area: '310px',
	        shade: 0.8,
	        id: 'LAY_layuipro', //设定一个id，防止重复弹出
	        btn: ['火速围观'],
	        moveType: 1, //拖拽模式，0或者1
	        content: '<div style="padding:15px 20px; text-align:justify; line-height: 22px; text-indent:2em;border-bottom:1px solid #e2e2e2;"><p>最近偶然发现贤心大神的layui框架，瞬间被他的完美样式所吸引，虽然功能不算强大，但毕竟是一个刚刚出现的框架，后面会慢慢完善的。很早之前就想做一套后台模版，但是感觉bootstrop代码的冗余太大，不是非常喜欢，自己写又太累，所以一直闲置了下来。直到遇到了layui我才又燃起了制作一套后台模版的斗志。由于本人只是纯前端，所以页面只是单纯的实现了效果，没有做服务器端的一些处理，可能后期技术跟上了会更新的，如果有什么问题欢迎大家指导。谢谢大家。</p><p>在此特别感谢Beginner和Paco，他们写的框架给了我很好的启发和借鉴。希望有时间可以多多请教。</p></div>',
	        success: function(layero){
				var btn = layero.find('.layui-layer-btn');
				btn.css('text-align', 'center');
				btn.on("click",function(){
					window.sessionStorage.setItem("showNotice","true");
				})
				if($(window).width() > 432){  //如果页面宽度不足以显示顶部“系统公告”按钮，则不提示
					btn.on("click",function(){
						layer.tips('系统公告躲在了这里', '#showNotice', {
							tips: 3
						});
					})
				}
	        }
	    });
	}
	//判断是否处于锁屏状态(如果关闭以后则未关闭浏览器之前不再显示)
//	if(window.sessionStorage.getItem("lockcms") != "true" && window.sessionStorage.getItem("showNotice") != "true"){
//		showNotice();
//	}
	$(".showNotice").on("click",function(){
		showNotice();
	})

	//刷新后还原打开的窗口
	if(window.sessionStorage.getItem("menu") != null){
		menu = JSON.parse(window.sessionStorage.getItem("menu"));
		curmenu = window.sessionStorage.getItem("curmenu");
		var openTitle = '';
		for(var i=0;i<menu.length;i++){
			openTitle = '';
			if(menu[i].icon.split("-")[0] == 'icon'){
				openTitle += '<i class="iconfont '+menu[i].icon+'"></i>';
			}else{
				openTitle += '<i class="layui-icon">'+menu[i].icon+'</i>';
			}
			openTitle += '<cite>'+menu[i].title+'</cite>';
			openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="'+menu[i].layId+'">&#x1006;</i>';
			element.tabAdd("bodyTab",{
				title : openTitle,
		        content :"<iframe src='"+menu[i].href+"' data-id='"+menu[i].layId+"'></frame>",
		        id : menu[i].layId
			})
			//定位到刷新前的窗口
			if(curmenu != "undefined"){
				if(curmenu == '' || curmenu == "null"){  //定位到后台首页
					element.tabChange("bodyTab",'');
				}else if(JSON.parse(curmenu).title == menu[i].title){  //定位到刷新前的页面
					element.tabChange("bodyTab",menu[i].layId);
				}
			}else{
				element.tabChange("bodyTab",menu[menu.length-1].layId);
			}
		}
	}

})


//打开新窗口
function addTab(_this){
	tab.tabAdd(_this);
}
//暂时先写死的菜单
var navs = [{
	"title" : "后台首页",
	"icon" : "icon-computer",
	"href" : "page/main.html",
	"spread" : false
},
//{
//	"title" : "友情链接",
//	"icon" : "icon-text",
//	"href" : "page/links/linksList.html",
//	"spread" : false
//},{
//	"title" : "404页面",
//	"icon" : "&#xe61c;",
//	"href" : "page/404.html",
//	"spread" : false
//},{
//	"title" : "系统基本参数",
//	"icon" : "&#xe631;",
//	"href" : "page/systemParameter/systemParameter.html",
//	"spread" : false
//},
{
	"title" : "系统管理",
	"icon" : "&#xe61c;",
	"href" : "",
	"spread" : false,
	"children" : [
		{
			"title" : "用户管理",
			"icon" : "&#xe631;",
			"href" : basePath+"userInfo/toList",
			"spread" : false
		},{
			"title" : "字典管理",
			"icon" : "icon-text",
			"href" : basePath+"dictionary/toList",
			"spread" : false
		}
	]
},{
	"title" : "物资管理",
	"icon" : "&#xe61c;",
	"href" : "",
	"spread" : false,
	"children" : [
		{
			"title" : "维修登记列表",
			"icon" : "&#xe631;",
			"href" : basePath+"maintainInfo/toList",
			"spread" : false
		},{
			"title" : "供应商管理",
			"icon" : "icon-text",
			"href" : basePath+"supplierInfo/toList",
			"spread" : false
		},{
			"title" : "商品信息管理",
			"icon" : "icon-text",
			"href" : basePath+"goodsInfo/toList",
			"spread" : false
		}
	]
}];

//初始化左侧菜单栏
function navBar(data){
	var ulHtml = '<ul class="layui-nav layui-nav-tree">';
	for(var i=0;i<data.length;i++){
		if(data[i].spread){
			ulHtml += '<li class="layui-nav-item layui-nav-itemed">';
		}else{
			ulHtml += '<li class="layui-nav-item">';
		}
		if(data[i].children != undefined && data[i].children.length > 0){
			ulHtml += '<a href="javascript:;">';
			if(data[i].icon != undefined && data[i].icon != ''){
				if(data[i].icon.indexOf("icon-") != -1){
					ulHtml += '<i class="iconfont '+data[i].icon+'" data-icon="'+data[i].icon+'"></i>';
				}else{
					ulHtml += '<i class="layui-icon" data-icon="'+data[i].icon+'">'+data[i].icon+'</i>';
				}
			}
			ulHtml += '<cite>'+data[i].title+'</cite>';
			ulHtml += '<span class="layui-nav-more"></span>';
			ulHtml += '</a>'
			ulHtml += '<dl class="layui-nav-child">';
			for(var j=0;j<data[i].children.length;j++){
				ulHtml += '<dd><a href="javascript:;" data-url="'+data[i].children[j].href+'">';
				if(data[i].children[j].icon != undefined && data[i].children[j].icon != ''){
					if(data[i].children[j].icon.indexOf("icon-") != -1){
						ulHtml += '<i class="iconfont '+data[i].children[j].icon+'" data-icon="'+data[i].children[j].icon+'"></i>';
					}else{
						ulHtml += '<i class="layui-icon" data-icon="'+data[i].children[j].icon+'">'+data[i].children[j].icon+'</i>';
					}
				}
				ulHtml += '<cite>'+data[i].children[j].title+'</cite></a></dd>';
			}
			ulHtml += "</dl>"
		}else{
			ulHtml += '<a href="javascript:;" data-url="'+data[i].href+'">';
			if(data[i].icon != undefined && data[i].icon != ''){
				if(data[i].icon.indexOf("icon-") != -1){
					ulHtml += '<i class="iconfont '+data[i].icon+'" data-icon="'+data[i].icon+'"></i>';
				}else{
					ulHtml += '<i class="layui-icon" data-icon="'+data[i].icon+'">'+data[i].icon+'</i>';
				}
			}
			ulHtml += '<cite>'+data[i].title+'</cite></a>';
		}
		ulHtml += '</li>'
	}
	ulHtml += '</ul>';
	return ulHtml;
}










