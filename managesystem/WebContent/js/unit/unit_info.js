
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
			$("#name").val("");
			$("#org_code").val("");
			$("#org_type").val("0");
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
	
	/*删除本级单位，会将本级和所有下属单位都删除*/
	function removeTreeNode(id,table_obj){
		
		var nodes = zTree.getNodeByParam("id",id);//ztree节点
		console.log(nodes);
		console.log(table_obj.data("tt-branch"));
		var hasChil = nodes == null ? table_obj.data("tt-branch"):nodes.isParent;
		$.ajax({
			url:"../../delete_duty.do",
			data:{"id":id},
			type:"POST",
			dataType:"json",
			async:true,
			beforeSend:function(){
				if ( hasChil ) {
					var msg = "要删除的节点是父节点，如果删除将连同下属单位一起删掉。\n\n请确认是否要删除？";					
					return confirm(msg);
				}else{
					var msg = "请确认是否要删除？";								
					return confirm(msg);
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