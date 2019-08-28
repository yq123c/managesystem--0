
$(function(){
		//initIframe();//初始化窗口
		initTree();//初始化机构树		
		init_table(); //表格初始化
	})
	/*基础设置*/
	var setting = {
			    async: {//异步加载获取数据
			        enable: true,
			        url: "../../get_unit_info.do",//地址
			        autoParam: ["id"],//提交参数
			        type: 'post',
			        dataType: 'json',
			        dataFilter:filter
				},
			    data:{//表示tree的数据格式
			        simpleData:{
			            enable:true,//表示使用简单数据模式
			            idKey:"id",//设置之后id为在简单数据模式中的父子节点关联的桥梁
			            pidKey:"parent_id",//设置之后pid为在简单数据模式中的父子节点关联的桥梁和id互相对应
			            rootId:"-1"//pid为null的表示根节点
			        }
			    },
			    view:{//表示tree的显示状态
			        selectMulti:false,//表示禁止多选
			    },
			    check:{//表示tree的节点在点击时的相关设置
			        enable:true,//是否显示radio/checkbox
			        chkStyle:"checkbox",//值为checkbox或者radio表示
			        checkboxType:{p:"",s:""},//表示父子节点的联动效果
			        radioType:"level"//设置tree的分组
			    },			
			    callback:{//事件处理函数
			    	onAsyncSuccess:asyncSuccess,
			    	onClick:onClick,//单击
			        onRightClick:OnRightClick
			    }
			}
	var zTree, rMenu,add_Menu;
	function initTree(){
		$.fn.zTree.init($("#ztree"),setting);
		zTree = $.fn.zTree.getZTreeObj("ztree");
		rMenu = $("#rMenu");
		add_Menu = $("#add_Menu");
	}
	function asyncSuccess(){
      var nodes = zTree.getNodes();
         if (nodes.length>0) {
          for(var i=0;i<nodes.length;i++){
         	 zTree.expandNode(nodes[i], true, false, false);//默认展开第一级节点
          }
			var childs = new Array();
			var treeNode = nodes[0];
			childs.push(treeNode);
			if(treeNode.children){
				 for( var i = 0 ; i < treeNode.children.length; i ++ ){
					 	childs.push(treeNode.children[i]);
				    }	
			}	
			var html = generateHtml(childs);    
			$("#tree_table tbody").empty();//清除
			var node;  
			$("#tree_table").treetable("loadBranch", node, html);
      }
	}

	/*过虑数据返回所有子节点*/
	function filter(treeId, parentNode, childNodes) {
		return childNodes;
	}

	/*点击节点事件*/
	function onClick(event,treeId,treeNode){		
		var childs = new Array();
		childs.push(treeNode);
		if(treeNode.children){
			 for( var i = 0 ; i < treeNode.children.length; i ++ ){
				 	childs.push(treeNode.children[i]);
			    }	
		}	
		var html = generateHtml(childs);    
		$("#tree_table tbody").empty();//清除
		var node;  
		$("#tree_table").treetable("loadBranch", node, html);	  
	}
	//数组对象拼接
	function generateHtml(node){            
	    var html = '';  
	    for( var i = 0 ; i < node.length; i ++ ){
	    	html += getHtml(node[i] );
	    }	
	   return html;  
	}
	function getHtml(item){
		var html = '';
		var org_type = "";
		switch(item.org_type){
			case "1":
				org_type = "单位";
				break;
			case "2":
				org_type = "部门";
				break;
		    default: 
		    	org_type = "...";
		}
		html += '<tr data-id="' + item.id + '"data-parent-id="'+item.parent_id+'"data-tt-branch="' +item.isParent+'"data-parent-ids="' +item.parent_ids+ '">' +   
		    '<td>' +   
		    (item.isParent ? '<span class="folder"></span>' :'<span class="file"></span>') +  
		    item.name + '</td>' +   
		    '<td>' +  item.org_code + '</td>' +  
		    '<td>' +  org_type + '</td>' + 
		    '<td><button type="button" class="add">新增</button><button type="button" class="edit">编辑</button><button type="button" class="del">删除</button> '+
		    '</td>'+ 
		    '</tr>';  
	    return html;
	}
	function init_table(){
		$("#tree_table").treetable({  
			theme:"default",
			nodeIdAttr: "id",  
			parentIdAttr: "parentId",  
			stringCollapse: "收起",  
			stringExpand: "展开",  
			expandable: true,                    
			expandLevel:2,
			onNodeExpand: function(){  //展开时调用 
	        	var node = this;  
	            //是否已经加载  
	            if(node.children && !node.children.length ){  
	          	  var data = "id="+node.id;
	          	  $.ajax({            
	      			  loading : false,            
	      			  sync: true,// Must be false, otherwise loadBranch happens after showChildren?         
	 					  url : "../../get_unit_info.do",             
	 					  data: data,  
	 					  type:"post",
	 					  dataType:"json",
	 					  success:function(data) { 	 						
	 						var data = JSON.parse(data);    		
	  					 	var html = generateHtml(data);      		
	                    	$("#tree_table").treetable("loadBranch",node,html);    
	      				}      
	      		  });                     
	            }               
	        }  
		}).on("click",".del",function(e){  
			e.preventDefault();  
			var id = $(this).closest("tr").data("id");   
			var table_tr = $(this).closest("tr");
			removeTreeNode(id,table_tr);       
		}).on("click",".add",function(e){  
			// e.preventDefault();  
			var id = $(this).closest("tr").data("id");  
			var parent_ids = $(this).closest("tr").data("parent-ids");   
			$("#parent_id").val(id);
			$("#parent_ids").val(parent_ids);
			$("#submit").attr("onclick","submit_data()");	 
			$(".add_Modal").click();	  //打开窗口
			//修改节点图标  
			$(this).closest("tr").find("td .file").removeClass("file").addClass("folder");    
		}).on("click",".edit",function(e){  
			e.preventDefault();         
			$("#submit").attr("onclick","edit_data()");	 
			$(".add_Modal").click();	  //打开窗口
		}); 
	}
	/*右键处理函数*/
	function OnRightClick(event, treeId, treeNode) {	
		if (!treeNode && event.target.tagName.toLowerCase() != "button" 
			&& $(event.target).parents("a").length == 0 && event.eventPhase == 3
			) {//根节点
			zTree.cancelSelectedNode();
			showRMenu("root", event.clientX, event.clientY);//右键菜单
		} else if (treeNode && !treeNode.noR) {//普通节点
			zTree.selectNode(treeNode);
			showRMenu("node", event.clientX, event.clientY);//右键菜单
		}
	}
	/*右键菜单函数*/
	function showRMenu(type, x, y) {
		$("#rMenu ul").show();
		if (type=="root") {
			$("#m_del").hide();
			$("#m_modify").hide();
		} else {
			$("#m_del").show();
			$("#m_modify").show();
		}
		rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});//加样式

		$("body").bind("mousedown", onBodyMouseDown);
	}
	/*选择之后隐藏菜单*/
	function hideRMenu() {
		if (rMenu) rMenu.css({"visibility": "hidden"});
		$("body").unbind("mousedown", onBodyMouseDown);
	}
	/*选择菜单打开之后，单击空白隐藏*/
	function onBodyMouseDown(event){
		if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
			rMenu.css({"visibility" : "hidden"});//选择菜单
		}
	}
	/*添加下级单位*/
	function addTreeNode(){
		hideRMenu();
		var nodes = zTree.getSelectedNodes();//选中的节点
		var id = nodes[0].id;
		$("#up_duty_name").val(nodes[0].name);
		$("#duty_name").val("");
		var e = getEvent();
		showAddMenu(e.clientX, e.clientY);
	}
	/*删除本级单位，会将本级和所有下属单位都删除*/
	function removeTreeNode(id,table_obj){
		
		var nodes = zTree.getNodeByParam("id",id);//ztree节点
		console.log(nodes.children);
		$.ajax({
			url:"../../delete_duty.do",
			data:{"id":id},
			type:"POST",
			dataType:"json",
			async:true,
			beforeSend:function(){
				if (nodes) {
					if (nodes.children && nodes.children.length > 0) {
						var msg = "要删除的节点是父节点，如果删除将连同下属单位一起删掉。\n\n请确认是否要删除？";					
						return confirm(msg);
					}else{
						var msg = "请确认是否要删除？";								
						return confirm(msg);
					}
				}				
			},
			success:function(response){
				console.log(response);
				if( response.state == 1 ){
					zTree.removeNode(nodes);
					table_obj.remove();
					show_message('信息','删除成功');
				}else{
					show_message('信息',response.messages);	
				}
				
			},
			error:function(e){
				console.log(e);	
			}
		});
	}
	/*修改单位*/
	function modifyTree(){
		hideRMenu();
		console.log("修改");
	}
	/*右键菜单函数*/
	function showAddMenu(x, y) {
		add_Menu.show();
		$("#page_bg").show();//开背景
		add_Menu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});//加样式

		$("body").bind("mousedown", onBodyMouseDown);
	}
	var getEvent = function(){
	    return window.event || arguments.callee.caller.arguments[0];
	}
	function closeAddMenu(){
		add_Menu.css({"visibility" : "hidden"});//关闭单位信息添加菜单
		$("#page_bg").show();
		$("#page_bg").hide();//关背景		
	}
	/*提交新添加的单位信息*/
	function sumitAddInfo(){		
		var nodes = zTree.getSelectedNodes()[0];
		/*获取表单中的信息*/	
		var data = serializeToJSON($("#form_add_unit"));
		data.parent_id = nodes.id;
		data.parent_ids = nodes.parent_ids;
		$.ajax({
			url:"../../add_unit.do",
			data:JSON.stringify(data),
			type:"POST",
			dataType:"json",
			contentType:"application/json;charset=UTF-8",
			async:true,
			beforeSend:function(){
				/*获取表单中的信息，并做非空判断*/	
				var up_duty_name = $("#up_duty_name").val();
				var name = $("#name").val();
				var sort = $("#sort").val();
				var area_id = $("#area_id").val();
				var code = $("#code").val();
				var type = $("#type").val();
				if( "" == up_duty_name||"" == name 
					||"" == sort ||	"" == area_id
					||"" == code||"" == type){
					show_message("提示","*号是必填项，请确认填写完整！");
					return false;
				}
			},
			success:function(response){
				if( response.state == 1 ){
					closeAddMenu();//关闭窗口
					show_message("信息","已成功添加");
					var newNode = response.data;										
					if (nodes) {
						newNode.checked = nodes.checked;
						zTree.addNodes(nodes, newNode);
					} else {
						zTree.addNodes(null, newNode);
					}
				}else{
					show_message("提示",response.messages);
				}
			},
			error:function(e){
				console.log(e);
			},
		});		
	}
	//表单中含name属性的全部转json
	function serializeToJSON(form) {
		var o = {};
		var a = form.serializeArray();
		$.each(a, function () {
			if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
			o[this.name] = [o[this.name]];
			}
			o[this.name].push(this.value || '');
			} else {
			o[this.name] = this.value || '';
			}
		});
		return o;
	};
	/*机构树隐藏*/
	function hidden_tree(){
		if( $("#tree_h_s").hasClass("fa-minus-square") ){
			$("#main_left").removeClass("col-sm-2").addClass("col-sm-1");
			$("#main_right").removeClass("col-sm-10").addClass("col-sm-11");
			$("#tree_h_s").removeClass("fa-minus-square").addClass("fa-plus-square");
		}else{
			$("#main_left").removeClass("col-sm-1").addClass("col-sm-2");
			$("#main_right").removeClass("col-sm-11").addClass("col-sm-10");
			$("#tree_h_s").removeClass("fa-plus-square").addClass("fa-minus-square");
		}	
	}
	//新增
	function submit_data(){
		var obj = {};
		if( !$("#name").val() ){
			$("#name").focus();
			return;
		}
		if( !$("#org_code").val() ){
			$("#org_code").focus();
			return;
		}
		var org_type = $("#org_type option:selected").val();
		if( org_type == 0 ){
			$("#org_type").focus();
			return;
		}
		$("#form_org input").each(function(){//数据序列化到obj中
			var item = $(this);
			var temp = item.attr("id");
			obj[temp]=item.val();		
		});
		obj.org_type = org_type;
		$("#close").click();
		var data = JSON.stringify(obj);
		$.ajax({            
			  loading : false,            
			  sync: false,     
			  url : "../../add_unit.do",             
			  data: data,  
			  type:"post",
			  dataType:"json",
			  contentType:"application/json;charset=UTF-8",
			  success:function(data) { 
				if(data.state == 1){
					var html = getHtml(data.data);  
				    
				    var node = $("#tree_table").treetable("node",$("#parent_id").val());//获取当前节点				         
				    $("#tree_table").treetable("loadBranch",node,html); //更新表格
				    var nodes = zTree.getNodeByParam("id",$("#parent_id").val() );
				    zTree.addNodes(nodes, data.data);
				}else{
					show_message("错误信息",data.messages);
				}
			},
			error:function(e){
				show_message("错误","服务器出点问题");
			}
			
		});        		    
	}