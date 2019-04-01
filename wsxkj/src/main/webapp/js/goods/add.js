layui.use(['form','layer','jquery','common','upload'],function(){
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : parent.layer;
	var	$ = layui.jquery;
	var common=layui.common;
	var upload=layui.upload;
	setTimeout(() => {
		initData();  
		initSPFL('DPSP','fenlei1');
	}, 0);
	function initData(){
		common.ajaxMethod(basePath+"/supplierInfo/initSupplierSelect",{},"POST",
				function (result) {
                    if (result.flag) {
                    	var arr=result.list;
                    	var str="<option value='' selected>请选择</option>";
                    	for(var i=0;i<arr.length;i++){
                    		str+="<option value="+arr[i].id+">"+arr[i].name+"</option>";
                    	}
                    	$("#supplierId").append(str); 
                    	common.initSelect("supplierId",state);
                    }else{
            			layer.msg("初始化下拉框失败！",{time:3000});
                    }
                    //重新渲染样式
            		form.render(); 
            		
                });
		
		common.ajaxMethod(basePath+"/file/findFiles",{'tableid':$('#id').val(),'modeltype':'goodsPic'},"POST",
				function (objs) {
	    	if(objs!=null&&objs.length>0){
	             for (var j = 0; j < objs.length; j++) {
	                 var obj = objs[j];
	                 if (obj != "") {
	                     var tr = $(['<tr id="upload-' + j + '">',
	                         '<td>' + obj.name + '</td>',
	                         '<td><span style="color: #5FB878;">已上传</span></td>',
	                         '<td>',
	                         '<input type="button" class="layui-btn layui-btn-mini layui-btn-danger delete-file" name="'+obj.path+'"  id="'+obj.id+'" value="删除"></input>',
	                         '</td>',
	                         '</tr>'].join(''));
	                     tr.find('.delete-file').on('click', function(){
	        	  	    		var tempObj=$(this);
	     		                var fileid=$(this).attr("id");
	        	  	    		var filepath=$(this).attr("name");
	        	  	    		common.ajaxMethod(basePath+"/file/delFile",{'fileid':fileid,'filepath':filepath},"GET",
	      							function (result) {
	      			                    if (result=="0") {
	      			            			layer.msg("删除成功！",{time:1000});
	      			            			tempObj.parent().parent().remove();
	      			                    }else{
	      			            			layer.msg("删除失败！",{time:1000});
	      			                    }
	      			                    
	      			                });
	     	                });
	                     demoListView.append(tr);
	                 }
	             }
	    	}
	    	
	    });
	    form.render();
		
	}
	//初始化商品分类
	function initSPFL(type,id){
		if(type==""||type==null||type==undefined){
			return;
		}
		common.ajaxMethod(basePath+"/goodsInfo/initFenlei",{"type":type},"POST",
				function (result) {
                    if (result.flag) {
                    	var arr=result.list;
                    	var str="";
                    	if(arr==null||arr.length==0){
                    		return;
                    	}
                    	for(var i=0;i<arr.length;i++){
                    		str+="<option value="+arr[i].typeCode+">"+arr[i].typeName+"</option>";
                    	}
                    	$("#"+id).html("<option value='' selected>请选择</option>");
                    	$("#"+id).append(str); 
                    }else{
            			layer.msg("初始化下拉框失败！",{time:3000});
                    }
                    var goodsType=$("#goodsType").val();
                    if(goodsType!=""){
                    	var arr=goodsType.split(",");
                    	if(id=="fenlei1"){
                        	$("#fenlei1").val(arr[0]);
                        	initSPFL(arr[0],'fenlei2');
                        }else if(id=="fenlei2"){
                        	$("#fenlei2").val(arr[1]);
                        	initSPFL(arr[1],'fenlei3');
                        }else if(id=="fenlei3"){
                        	$("#fenlei3").val(arr[2]);
                        }
                    }
                    //重新渲染样式
            		form.render(); 
                });
	}
	
	
	//监听select框
	form.on('select(supplierId)', function(data){
		$("#supplierName").val(data.elem[data.elem.selectedIndex].text);
	});
	
	form.on('select(fenlei1)', function(data){
		initSPFL(data.value,'fenlei2');
	});
	form.on('select(fenlei2)', function(data){
		initSPFL(data.value,'fenlei3');
	});
	form.on('select(fenlei3)', function(data){
		var gt=$("#fenlei1").val()+","+$("#fenlei2").val()+","+$("#fenlei3").val();
 		$("#goodsType").val(gt);
	});
 	var addUserArray = [],addUser;
 	form.on("submit(addInfo)",function(data){
 		$.ajax({
                type: "POST",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: basePath+"goodsInfo/doAdd" ,//url
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
 	
 	
 	//多文件上传
    var demoListView = $('#demoList_fujian'),
    uploadListIns = upload.render({
        elem: '#testList_fujian',
        url: basePath+'file/uploadMultiply',
        accept: 'images',
        acceptMime: 'image/gif,image/jpg,image/jpeg,image/png,image/bmp',
        multiple: true,
        auto: false,
        data: {
        	tableid: $('#id').val(),
      	    modeltype:'goodsPic'
      	},
        bindAction: '#testListAction_fujian',
        choose: function (obj) {
            var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
            //读取本地文件
            obj.preview(function (index, file, result) {
                var tr = $(['<tr id="upload-' + index + '">'
                                , '<td>' + file.name + '</td>'
                //, '<td>' + (file.size / 1014).toFixed(1) + 'kb</td>'
                                , '<td>等待上传</td>'
                                , '<td>'
                                , '<button class="layui-btn layui-btn-mini demo-reload layui-hide">重传</button>'
                                , '<button class="layui-btn layui-btn-mini demo-delete">取消</button>'
                                , '</td>'
                            , '</tr>'].join(''));

                //单个重传
                tr.find('.demo-reload').on('click', function () {
                    obj.upload(index, file);
                });

                //删除
                tr.find('.demo-delete').on('click', function () {
                    delete files[index]; //删除对应的文件
                    tr.remove();
                    uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                });

                demoListView.append(tr);
            });
        },
        done: function (res, index, upload) {
            if (res.code == 0) { //上传成功
            	var fp=$("#fujian").val();
            	if(fp!=""){
            		$("#fujian").val(fp+","+res.path);
            	}else{
            		$("#fujian").val(res.path);
            	}
                var tr = demoListView.find('tr#upload-' + index), tds = tr.children();
                tds.eq(1).html('<span style="color: #5FB878;">上传成功</span>');
                tds.eq(2).html('<input type="button" class="layui-btn layui-btn-mini layui-btn-danger delete-file" name="'+res.path+'"  id="'+res.id+'" value="删除"></input>'); //清空操作
                tr.find('.delete-file').on('click', function(){
   	  	    		var tempObj=$(this);
		            var fileid=$(this).attr("id");
   	  	    		var filepath=$(this).attr("name");
   	  	    		common.ajaxMethod(basePath+"/file/delFile",{'fileid':fileid,'filepath':filepath},"GET",
 							function (result) {
 			                    if (result=="0") {
 			            			layer.msg("删除成功！",{time:1000});
 			            			tempObj.parent().parent().remove();
 			                    }else{
 			            			layer.msg("删除失败！",{time:1000});
 			                    }
 			                    
 			                });
	                });
                return delete this.files[index]; //删除文件队列已经上传成功的文件
            }
            this.error(index, upload);
        },error: function(index, upload){
            var tr = demoListView.find('tr#upload-'+ index)
          ,tds = tr.children();
          tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
          tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
        }
    });
    
    
 	
})

