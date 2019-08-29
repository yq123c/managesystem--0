$(document).ready(function(){
	//表格的事件
    window.operateEvents = {
    	    'click .btn-delete': function(e, value, row, index) {
    	    	var id =[];
    	    	id.push(row.id);    	    	
    	    	layer.confirm("确定要删除吗?",{offset: '188px',btn: ['确定', '取消'],title:"请确认"},function(){
    	    		$.ajax({
    	    			   type: "post",
    	    			   url: "../../role/delete.do",
    	    			   data: "id="+row.id,
    	    			   success: function(result){
    	    				   if (result.state == 1){
    	    					   $('#roleList-table').bootstrapTable('remove', { field:'id', values:id });
    	    					   layer.msg("删除成功", {offset: '188px',icon: 1});
    	    				   }else{
    	    					   layer.msg(result.messages, {offset: '188px',icon: 2});
    	    				   }    	                       
    	    			   }
    	    			});
    	    	});    	    			    		
    	    },
    		'click .btn-detail': function(e, value, row, index){    			    		
    			setURLValue("row",row);
    			window.parent.showContent("role_list","role/role_detail.html?update=update");
    	    },
    	    'click .btn-add': function(e, value, row, index){
    	    	$(".add_Modal").click();
    	    },
		    'click .btn-edit': function(e, value, row, index){
		    	var item =getURLValue("row");    			   			
		    	console.log(item);
		    }
    }
    
	//加载表格数据
    $('#roleList-table').bootstrapTable({
        url:"../../role/get_list.do",
        method:"get",
        striped: true, // 隔行加亮
        resizable: false,
        pagination: true,//是否显示分页（*）
        sidePagination: 'server',//分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,//初始化加载第一页，默认第一页,并记录
        pageSize:8,                       //每页的记录行数（*）
        pageList: [8,12,15,20],        //可供选择的每页的行数（*）
        toolbar: '#toolbar',                //工具按钮用哪个容器        
        search: false,//是否显示表格搜索
        showFullscreen: true,        
        showRefresh : true,//刷新按钮()
        showLoading: true,
        cardView: false,
        showToggle: true,//切换视图
        showToggle: false, //是否显示详细视图和列表视图的切换按钮
        showColumns: false,//是否显示所有的列（选择显示的列）
        queryParams: function (params) {
            var temp = {  
                    limit: params.limit,   //单页记录数
                    offset: params.offset,  //页码
                    sort: params.sort,      //排序列名  
                    sortOrder: params.order //排序方式
                };
                return temp;
            },
        rowStyle: function (row, index) {//row 表示行数据，object,index为行索引，从开始
            var style = "";
            if (index % 2 == 0) {
                style = { css: { 'color': '#212529' ,'background-color':'#60cc79'} };
            }else{
           	 style = { css: { 'color': '#white' ,'background-color':'#c5a442'} };
            }                      
            return  style;
        }, 
    	columns: [{
            field: 'id',
            title: 'id',
            visible:false
        },{
            field: 'name',
            title: '角色名',
            align: 'center',
            sortable: true,
            events: operateEvents,
            formatter:function(value,row,index){
            	return '<a class="btn btn-default text-primary btn-detail">'+value+'</a>'
            }
        },{
            field: 'description',
            title: '描述',
            align: 'center',
            sortable: true
        },{
            field: 'create_time',
            title: '创建时间',
            align: 'center'
        },{
        	field: 'operation',
        	title: '操作',
        	align: 'center',
        	events: operateEvents,
        	formatter: function(value, row, index) {
            	return  '<button type="button" class="btn btn-sm btn-xs btn-primary btn-add">新增</button> <button type="button" class="btn btn-sm btn-xs btn-secondary btn-edit">编辑</button> <button type="button" class="btn btn-sm btn-xs btn-danger btn-delete">删除</button>';
            }             	
        }]
    });
	
	//保存修改
	$("#btn_submit").on("click", function(){
		$.ajax({
			   type: "post",
			   url: "",
			   data:$("#update-form").serialize(),
			   success: function(result){
				   		alert(result);
			   }
		},"json");
	});
});
//新增
function ensure_add(){
	$.ajax({
		url:"../../role/add.do",
		type:"post",
		data:JSON.stringify(serializeToJSON($("#form_role"))),
		dataType:"json",
		contentType:"application/json;charset=utf-8",
		beforeSend:function(){
			if(!$("#role_name").val()){
				$("#role_name").focus();
				return false;
			}else if(!$("#description").val()){
				$("#description").focus();
				return false;
			}	
			console.log();
		},
		success:function(data){
			$("#close").click();
			if(data.state == 1){
				$("#role_name").val("");
				$("#description").val("");
				show_message("信息","已成功添加！");
				$('#roleList-table').bootstrapTable("refresh");
			}else{
				show_message("信息",data.messages);
			}
		},
		error:function(e){
			$("#close").click();
			show_message("出错了",data.messages);
		}
	});
}