<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<title>主页</title>
<meta charset="utf-8">
<!-- 移动设备优先 -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap4 核心 CSS 文件 -->
<link rel="stylesheet" href="../resources/js/bootstrap/css/bootstrap.min.css">
<script src="../resources/js/jquery/jquery-3.4.0.min.js"></script>
<!-- 最新的 Bootstrap4 核心 JavaScript 文件 -->
<script src="../resources/js/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="../js/index/index.js"></script>
<link rel="stylesheet" href="../resources/font-awesome-4.7.0/css/font-awesome.css">
<style type="text/css">
  .fakeimg {
      height: 200px;
      background: #aaa;
  }
  body,td,.p1,.p2,.i{font-family:arial} body{margin:6px 0 0 0;background-color:#fff;color:#000;}  
                table{border:0} #cal{width:434px;border:1px solid #c3d9ff;font-size:12px;margin:8px  
                0 0 15px} #cal #top{height:29px;line-height:29px;background:#e7eef8;color:#003784;padding-left:70px}  
                #cal #top select{font-size:12px} #cal #top input{padding:0} #cal ul#wk{margin:0;padding:0;height:25px}  
                #cal ul#wk li{float:left;width:60px;text-align:center;line-height:25px;list-style:none}  
                #cal ul#wk li b{font-weight:normal;color:#c60b02} #cal #cm{clear:left;border-top:1px  
                solid #ddd;border-bottom:1px dotted #ddd;position:relative} #cal #cm .cell{position:absolute;width:42px;height:36px;text-align:center;margin:0  
                0 0 9px} #cal #cm .cell .so{font:bold 16px arial;} #cal #bm{text-align:right;height:24px;line-height:24px;padding:0  
                13px 0 0} #cal #bm a{color:7977ce} #cal #fd{display:none;position:absolute;border:1px  
                solid #dddddf;background:#feffcd;padding:10px;line-height:21px;width:150px}  
                #cal #fd b{font-weight:normal;color:#c60a00}  
</style>
</head>
<body>
<nav class="navbar navbar-expand-sm bg-primary navbar-dark">
		<a class="navbar-brand" href="#" onclick="showContent('home','sys/home.html')">主页</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"data-target="#collapsibleNavbar">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="collapsibleNavbar">
			
			<i class="fa fa-cog fa-spin fa-3x"></i>
		</div>
		<span class="navbar-text text-dark font-weight-bold">当前登录用户：<span id="login_user"></span></span> &nbsp;
		<button class="btn btn-sm btn-danger" type="button" onclick="login_out()">退出登录</button>
</nav>
<div class="container-fluid" style="margin-top:30px">
  <div class="row">
  <div class="col-sm-1">			
		<div class="card">
	      <div class="card-header">
	        <a class=" collapsed card-link " data-toggle="collapse" href="#collapseOne">
	          用户管理
	        </a>
	      </div>
	      <div id="collapseOne" class="collapse  show" data-parent="">
	        <div class="card-body">
	          <a  class="btn btn-primary btn-sm"  href="##" onclick="showContent('user_list','user/userList.html')">用户列表</a>
	          <br><br>
	           <a  class="btn btn-primary btn-sm"  href="##" onclick="showContent('add_user','user/userOperate.html?dispatcher=add_user')">用户添加</a>
	           <br><br>
	           <a  class="btn btn-primary btn-sm"  href="##" onclick="showContent('')">修改密码</a>
	        </div>
	      </div>
	    </div>
	    <div class="card">
	      <div class="card-header">
	        <a class="collapsed card-link " data-toggle="collapse" href="#collapseTwo">
	        机构管理
	      </a>
	      </div>
	      <div id="collapseTwo" class="collapse " data-parent="">
	        <div class="card-body">
	           <a  class="btn btn-primary btn-sm"  href="##" onclick="showContent('unit_info','unit/unit_info.html')">机构列表</a>
	        </div>
	      </div>
	    </div>
	    <div class="card">
	      <div class="card-header ">
	        <a class="collapsed card-link " data-toggle="collapse" href="#role_manage">
	        角色管理
	      </a>
	      </div>
	      <div id="role_manage" class="collapse " data-parent="">
	        <div class="card-body">
	           <a  class="btn btn-primary btn-sm"  href="##" onclick="showContent('role_list','role/role_list.html')">角色列表</a>
	           <br><br>
	           <a  class="btn btn-primary btn-sm"  href="##" onclick="showContent('role_add','role/add_role.html')">角色添加</a>
	            <br><br>
	           <a  class="btn btn-primary btn-sm"  href="##" onclick="showContent('permission_list','role/permission_list.html')">权限列表</a>
	        </div>
	      </div>
	    </div>			
  </div>
    <div class="container">
    	 <div class="col-sm text-center">
    	 	<h2>主页</h2>
     		 <h5>权限系统</h5>
      		<p>欢迎访问权限系统主页</p>
      <div class="fakeimg">图像</div>
      <p>一些文本..</p>
      <p>菜鸟教程，学的不仅是技术，更是梦想！！！菜鸟教程，学的不仅是技术，更是梦想！！！菜鸟教程，学的不仅是技术，更是梦想！！！</p>
    	</div>
	    <div class="col-sm">
	      <table class="text-center col offset-md-4" cellpadding="0" cellspacing="0" id="1">  
			    <tr>  
			        <td>     
			            <div id="cal">  
			                <div id="top">  
						                    公元  
						                    <select>  
						                    </select>  
						                    年  
						                    <select>  
						                    </select>  
						                    月 农历  
						                    <span>  
						                    </span>  
						                    年 [  
						                    <span>  
						                    </span>  
						                    年 ]  
						                    <input type="button" value="回到今天" title="点击后跳转回今天" style="padding:0px">  
			                </div>  
			                <ul id="wk">  
			                    <li>  
			                     	   一  
			                    </li>  
			                    <li>  
			                     	   二  
			                    </li>  
			                    <li>  
			                   	              三  
			                    </li>  
			                    <li>  
			                     	   四  
			                    </li>  
			                    <li>  
			                    	    五  
			                    </li>  
			                    <li>  
			                        <b>  
			                     	       六  
			                        </b>  
			                    </li>  
			                    <li>  
			                        <b>  
			                       	     日  
			                        </b>  
			                    </li>  
			                </ul>  
			                <div id="cm">  
			                </div>  
			                <div id="bm">  
			                </div>  
			            </div>  
			        </td>  
			    </tr>  
			</table> 
	    </div>	    
    </div>
  </div>
</div>
<div class="fixed-bottom text-center " style="width: 100%;height:75px; background-color: #e9ecef;padding: 7px 0px 0px 0px;margin-top: 0px">
  <p>技术支持-by-叶秋</p>
  <p>qq:11570287389@qq.com</p>
</div>
<script type="text/javascript" src="../js/index/calendar.js"></script>
</body>
</html>