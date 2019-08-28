$(document).ready(function(){
	//表格的事件
    window.operateEvents = {
    	    'click .btn-delete': function(e, value, row, index) {
    	    	var id =[];
    	    	id.push(row.ID);
    	    	
    	    	layer.confirm("确定要删除吗?",{btn: ['确定', '取消'],title:"提示"},function(){
    	    		$.ajax({
    	    			   type: "post",
    	    			   url: "../deleteById.do",
    	    			   data: {id:row.ID},
    	    			   success: function(result){
    	    				   if (result.state == 0){
    	    					   $('#userList-table').bootstrapTable('remove', { field:'ID', values:id });
    	    					   layer.msg(result.messages, {icon: 1});
    	    				   }else{
    	    					   layer.msg(result.messages, {icon: 2});
    	    				   }
    	                       
    	    			   }
    	    			});
    	    	});
    	    		
    	    		
    	    },
    		'click .btn-edit , .btn-name': function(e, value, row, index) {
    			saveFormValues(row,index);   			
    		}
    }
    
    
    function saveFormValues(row,index){  	
    	var obj = new Object();
    	obj.index = index;
    	obj.id = row.ID;
    	obj.dept_id = row.DEPT_ID;
    	obj.dept_name = row.DEPT_NAME;
    	obj.organizationID = row.ORGANIZATION_ID;
    	obj.organizationName = row.ORGANIZATION_NAME;
    	obj.realName = row.REAL_NAME;
    	obj.loginName = row.LOGIN_NAME;
    	obj.password = row.PASSWORD;
    	obj.email = row.EMAIL;
    	obj.phone = row.PHONE;
    	obj.mobile = row.MOBILE;
    	obj.locked = row.LOCKED;
    	obj.userType = row.USER_TYPE;
    	obj.roles = row.ROLES;
    	
    	sessionStorage.setItem("formObj",JSON.stringify(obj));
    }
    
    
	//加载表格数据
    $('#userList-table').bootstrapTable({
        url:"../findUser.do",
        method:"get",
        queryParams: function (params) {
            var temp = {
                    limit: params.limit,   //页面大小
                    offset: params.offset,  
                    page: (params.offset / params.limit) + 1, //页码
                    sort: params.sort,      //排序列名  
                    sortOrder: params.order, //排序方式
                    loginName: $("#search_loginName").val(),
                    userType: $("#userType_search").val()
                    
                };
                return temp;
            },
    	columns: [{
            field: 'ID',
            title: 'id'
        },{
            field: 'LOGIN_NAME',
            title: '登录名',
            align: 'center',
            sortable: true,
            events: operateEvents,
            formatter:function(value,row,index){
            	return '<a class="btn btn-name" href="userOperate.html?dispatcher=update" target="user_list" >'+value+'</a>'
            }
        },{
            field: 'ORGANIZATION_NAME',
            title: '所属机构',
            align: 'center',
            sortable: true
        },{
            field: 'DEPT_NAME',
            title: '所属处室',
            align: 'center',
            sortable: true
        },{
            field: 'EMAIL',
            title: '邮箱',
            align: 'center',
            sortable: true
        },{
            field: 'PHONE',
            title: '电话',
            align: 'center',
            sortable: true
        },{
            field: 'MOBILE',
            title: '手机号',
            align: 'center',
            sortable: true
        },{
            field: 'LOCKED',
            title: '锁定',
            align: 'center',
            sortable: true,
            formatter:function(value, row, index) {
            	return value == '1' ? "是" : "否";
            }
        },{
            field: 'USER_TYPE',
            title: '用户类型(value)',
            align: 'center',
        },
        {
            field: 'USER_LABEL',
            title: '用户类型',
            align: 'center',
            formatter:function(value, row, index) {
                return value == '请选择' ? "-" : value;
            }
        },{
        	field: 'OPERATE',
        	title: '操作',
        	align: 'center',
        	events: operateEvents,
        	formatter: function(value, row, index) {
            	return  '<button type="button" class="btn btn-xs btn-primary btn-edit">编辑</button> <button type="button" class="btn btn-xs btn-danger btn-delete">删除</button>';
            }
        
        	
        },{
        	field: 'ROLES',
        	title: '角色',
        	align: 'center',
        	formatter: function(value, row, index) {
        		return JSON.stringify(value);
        	}
        }],
        striped: true, // 隔行加亮
        resizable: false,
        pagination: true,
        sidePagination: 'server',//client,server
        pageNumber: 1,
        pageSize: 10,
        pageList: [10,20,50,'All'],
        search: false,
        showRefresh: false,
        showToggle: false,
        showColumns: false
    });
    
    //隐藏id列
    $('#userList-table').bootstrapTable('hideColumn', 'ID');
    $('#userList-table').bootstrapTable('hideColumn', 'USER_TYPE');
    $('#userList-table').bootstrapTable('hideColumn', 'ROLES');
    
  //查询用户类型
	$.ajax({
		   type: "get",
		   url: "../findUserType.do",
		   success: function(result){
			   		var str = "";
			   		for (var i = 0; i < result.length; i++){
			   			str += "<option value='"+result[i].VALUE+"'>"+result[i].LABEL+"</option>";
			   		}
				   
				   $("#userType_search").append("<option>  </option>"+str);
			   }
	});
	
	//查询
	$("#search").on("click", function(){
		$('#userList-table').bootstrapTable('refresh');
	});
	
	
});