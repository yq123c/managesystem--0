$(document).ready(function(){
	
	function getParams(key){
		var reg = new RegExp("(^|&)"+ key +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
			if ( r != null){
				return r[2];
			}
		return null;
	}
	
	
	if (getParams('dispatcher')=='update'){
		update();//修改用户页面
	} else {
		add(); //添加用户页面
	}
	
})
	
//修改用户页面
function update(){
	
	var formObj = JSON.parse(sessionStorage.getItem("formObj"));

	  //查询用户类型
//	$.ajax({
//		   type: "get",
//		   url: "../findUserType.do",
//		   success: function(result){
//			   		var str = "";
//			   		for (var i = 0; i < result.length; i++){
//			   			if (formObj.userType == result[i].VALUE){
//			   				str += "<option value='"+result[i].VALUE+"' selected='selected'>"+result[i].LABEL+"</option>";
//			   			}else{
//			   				str += "<option value='"+result[i].VALUE+"'>"+result[i].LABEL+"</option>";
//			   			}
//			   		}
//				   
//				   $("#userType").append("<option>  </option>"+str);
//			   }
//	});
	
    	//初始化参数
		$("#index").val(formObj.index);
		$("#id").val(formObj.id);
		$("#deptID").val(formObj.dept_id);
		var dname = formObj.dept_name;
		$("#deptName").val(dname==null||dname=='null'?"":dname);
		$("#organizationID").val(formObj.organizationID);
		var oname = formObj.organizationName;
		$("#organizationName").val(oname==null||oname=='null'?"":oname);
		$("#realName").val(formObj.realName);
		$("#loginName").val(formObj.loginName);
		$("#password").val(formObj.password);
		$("#rePassword").val(formObj.password);
		$("#email").val(formObj.email);
		$("#phone").val(formObj.phone);
		$("#mobile").val(formObj.mobile);
				
		//是否锁定
		$("#locked option").each(function(){
			if($(this).val() == formObj.locked)
				$(this).attr("selected",true);
			else
				$(this).attr("selected",false);
		});
		
		//加载所有角色
		$.ajax({
			   type: "get",
			   url: "../../role/get_list.do?limit=100&offset=0",
			   success: function(result){
				   var str = "";
				   for (var i = 0; i < result.rows.length; i++){
					   
					   str+='<input type="checkbox" name="roleCheckBoxs" value="'+result.rows[i].id+'" >'+result.rows[i].description+'</input>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';

					   if ((i+1)%2==0){
						   $(".roles-div").append("<div class='form-inline'>"+str+"</div>");
						   str = "";
					   }
				   }
				   
				   $(".roles-div").find("input").each(function(){
					    for (var i = 0; i < formObj.roles.length; i++){
					    	if (formObj.roles[i].ID == $(this).val()){
					    		$(this).attr("checked",true);
					    	}
					    }
				   });
			   }
		});
		
		
		//保存修改
		$("#btn_submit").on("click", function(){
			
			if ($("#rePassword").val() != $("#password").val() ){
				alert("两次输入的密码不相同");
				return;
			}

			$.ajax({
				   type: "post",
				   url: "../updateUser.do",
				   data:$("#form").serialize(),
				   success: function(result){
					   		if (result.state==0){
					   			layer.alert(result.messages);
					   			
					   			//$('#userList-table').bootstrapTable('updateRow',{index:$("#index").val(),row:result.data});
					   		} else if (result.state=1){
					   			layer.msg(result.messages.replace("~", "<br/>"));
					   		} else {
					   			layer.alert(result.messages);
					   		}
					   		
				   }
			},"json");
		});						
}
//添加用户页面
 function add(){
		
//		$.ajax({
//			   type: "POST",
//			   url: "../findUserType.do",
//			   success: function(result){
//				   		var str = "";
//				   		for (var i = 0; i < result.length; i++){
//				   			str += "<option value='"+result[i].VALUE+"'>"+result[i].LABEL+"</option>";
//				   		}
//					   $("#userType").append(str);
//				   }
//			});
		
		$("#btn_submit").on("click",function(){
			$.ajax({
				   type: "POST",
				   url: "../addUser.do",
				   data: $("#form").serialize(),
				   success: function(result){
					   	console.log(result);
				   }
			
			});
		});
	
 }
 
 //关闭
$("#close").on("click",function(){
	window.close();
});
	


var setting = {
    async: {//异步加载获取数据
        enable: true,
        url: "../../get_unit_info.do",//地址
        autoParam: ["id"],//提交参数
        type: 'post',
        dataType: 'json',
        dataFilter:filter//数据过滤
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
        enable:false,//是否显示radio/checkbox
        chkStyle:"checkbox",//值为checkbox或者radio表示
        checkboxType:{p:"",s:""},//表示父子节点的联动效果
        radioType:"level"//设置tree的分组
    },			  
    callback:{//事件处理函数
    	onDblClick:onDblClick//双击选择
    }
}
var zTree;//树
var init_node;//初始化节点
function initTree(){	
	$.fn.zTree.init($("#ztree"),setting,init_node);
	zTree = $.fn.zTree.getZTreeObj("ztree");
}
/*过虑数据返回所有子节点中机构类型是1的数据*/
function filter(treeId, parentNode, childNodes) {
	if( init_node ){//选择处室，无需过滤
		return childNodes;
	}
	var newChildNodes = [];	
	for( index in  childNodes){
		if( "1" == childNodes[index].type ){			
			newChildNodes[ newChildNodes.length] = childNodes[index];
		}		
	}
	return newChildNodes;
}
//打开选择窗口，type用于标识选单位还是处室
var op_office;//操作类型，用于判断选择单位操作或者是选择处室时的一些提示
function choose(type){
	op_office = type;
	if( "unit" === type ){//选择单位
		$(".choose_unit").attr("onclick","choose_unit('deptName')");
		init_node = null;
		initTree();//初始化机构树
	}else if( "office" === type ){//选择处室
		if( !init_node ){
			show_message("提示","请先选择归属单位!!");
			return;
		}
		$(".choose_unit").attr("onclick","choose_unit('organizationName')");
		initTree();//初始化机构树
	}	
	$("#click_me").click();
}
//双击事件
function onDblClick(){
	$(".choose_unit").click();
	choose_unit();
}
/*选中节点，赋值 id:元素id */
function choose_unit(id){
	var node = zTree.getSelectedNodes()[0];//选中的节点
	if( !init_node ){
		init_node = node;//赋值供处室选择使用
	}else if("office" === op_office){//选择处室	
		if( "1" == node.type ){
			show_message("错误信息","请选择处室!!");
			return;
		}
	}
	$("#"+id).val(node.name);//单位名
	$("."+id).val(node.id);//单位id
}
