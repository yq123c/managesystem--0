/**通用工具 -js*/
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
/*
*通过弹窗显示信息 
*title:弹窗的信息类型
*content：弹窗内容
*/
function show_message(title,content){
	var modal = "<div>"+
	"		<button type=\"button\" id=\"modal\" data-toggle=\"modal\" data-target=\"#myModal\" style=\"display: none;\">"+
	"		  </button>"+
	"		  <!-- 模态框 -->"+
	"		  <div class=\"modal fade\" id=\"myModal\">"+
	"		    <div class=\"modal-dialog modal-sm\">"+
	"		      <div class=\"modal-content\">		   "+
	"		        <!-- 模态框头部 -->"+
	"		        <div class=\"modal-header\">"+
	"		          <h4 class=\"modal-title\" id=\"messane_title\">模态框头部</h4>"+
	"		          <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>"+
	"		        </div>		   "+
	"		        <!-- 模态框主体 -->"+
	"		        <div class=\"modal-body\" id=\"message_body\">"+
	"		          模态框内容.."+
	"		        </div>		   "+
	"		        <!-- 模态框底部 -->"+
	"		        <div class=\"modal-footer\">"+
	"		         	<button type=\"button\" class=\"btn btn-secondary btn-cancel\" data-dismiss=\"modal\">关闭</button>"+
	"		        </div>		   "+
	"		      </div>"+
	"		    </div>"+
	"		  </div>"+
	"	</div>";
	$("body").append(modal);
	$("#messane_title").text(title);
	$("#message_body").text(content);
	$("#modal").click();		
}
/**
 * 临时保存数据（sessionStorage），可用于页面跳转之间传值
 * key:数据的键,类型为String
 * value：数据--可以是String、int，也可以是对象（json）
 * */
function setURLValue(key,value){
	return window.sessionStorage.setItem(key,JSON.stringify(value));
}
/**
 * 获取值，用于跳转之后的页面取值进行初始化
 * key:数据的键,类型为String
 * value：数据--可以是String、int，也可以是对象（json）
 */
function getURLValue(key){
	return JSON.parse(window.sessionStorage.getItem(key));
}
/**
 * 通过参数名获取get参数的值
 * @param name
 * @returns
 */
function getUrlParamValue(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}