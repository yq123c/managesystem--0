var width;
var height;
$(function(){
	width = $(window).width();
	height = $(window).height()-152;
	$("iframe").each(function(){	//	
		$(this).css("height",(height)+"px");
	});
	init();	
})

function init(){	
	var login_info = getURLValue("login_info");
	if( login_info == null ){
		$.ajax({
			url:"../web/get_loginInfo.do",
			type:"post",
			dataType:"json",
			success:function(data){		
					if( data.state == 1 ){
						setURLValue("login_info",data);
						console.log(data);
						$("#login_user").text(data.data.name);						
					}				
			}
		});
	}else{
		$("#login_user").text(login_info.data.name);	
	}	
}
$(window).resize(function () {          //当浏览器大小变化时
	width = $(window).width();
	height = $(window).height()-152;
	$("iframe").each(function(){	//	
		$(this).css("height",(height)+"px");
	});
	if( width <= 1230 ){
		$(".hid").hide('normal');
		$(".menu").css("width","55px");
	} 
	
});
/*菜单的隐藏*/
function hidden_menu(){
	$(".hid").toggle('normal');

	if( $(".menu").width() > 55 ){		
		$(".menu").css("width","55px");
	}else{
		$(".menu").css("width","152px");
	}		
}
/*退出登录*/
function login_out(){
	$.ajax({
		url:"../web/login_out.do",
		type:"get",
		dataType:"json",
		success:function(data){			
				if( data.state == 1 ){
					location.reload();
				}					
		}		
	});
}
/*显示内容*/
function showContent(id,src){	
	if($("#"+id).length == 0){
		$("#home").after(" <iframe id=\""+id+"\" class=\"col-sm-11\" src=\""+src+"\" style=\"border:0px;height:"+height+"px;\"></iframe>");
	}else{
		if( src.indexOf("?update=") > 0 ){
			$("#"+id).attr("src",src);
		}
	}
	$("iframe").each(function(){	//先隐藏所有的iframe	
		$(this).hide();
	});
	$("#"+id).show();//显示当前窗口	
}