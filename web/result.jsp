<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "com.company.*"%>
<!DOCTYPE html>  
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>搜索结果展示</title>
    <link rel="stylesheet" href="./result/result.css">
</head>
<%
	String query = request.getParameter("query");
	String type = request.getParameter("type");
	String typeEn;
	int pageTotal = 1;
	int pageValue = Integer.parseInt(request.getParameter("page"));
	double pageNum = 1;
	double pageSize = 10;
	switch(type){
		case("标题"):typeEn = "title";break;
		case("作者"):typeEn = "author";break;
		case("地区"):typeEn = "address";break;
		case("摘要"):typeEn = "abstract";break;
		case("类型"):typeEn = "affiliation";break;
		case("日期"):typeEn = "date";break;
		default:typeEn = "fulltext";break;
	}

%>
<script>
	function changeUrlArg(url,arg,arg_val){
		var pattern=arg+'=([^&]*)';
		var replaceText=arg+'='+arg_val;
		if(url.match(pattern)){
			var tmp='/('+ arg+'=)([^&]*)/gi';
			tmp=url.replace(eval(tmp),replaceText);
			return tmp;
		}else{
			if(url.match('[\?]')){
				return url+'&'+replaceText;
			}else{
				return url+'?'+replaceText;
			}
		}
	}
	function getUrlArg(url,arg) {
		var reg = new RegExp('(^|&)' + arg + '=([^&]*)(&|$)', 'i');
		var r = url.substr(1).match(reg);
		if (r != null) {
			return unescape(r[2]);
		}
		return null;
	}
</script>
<body>
	<span class="text1">文献检索系统</span>
	<div class="search-box">
		<input class="search-txt" type="text" id="inp" value = "<%=query%>"/>
		<select class="select1" id="type">
			<option value="全文">全文</option>
			<option value="标题">标题</option>
			<option value="作者">作者</option>
			<option value="地区">地区</option>
			<option value="类型">类型</option>
			<option value="日期">日期</option>
		</select>
		<script>
			var type = getUrlArg(decodeURI(window.location.href),"type");
			var obj = document.getElementById("type");
			for(var i=0;i<obj.length;i++){
				if(obj[i].value === type)
					obj[i].selected = true;
			}
		</script>
		<input class="search-btn" type='submit' id='btn_1' value='&#xf002' />
	</div><br>
	<div>
		<span class="text2">单页显示结果数</span>
		<select class="select2" id="pageSize" onChange="pagePic(this.value)">
			<option value = "10" >10条</option>
			<option value = "12" >12条</option>
			<option value = "14" >14条</option>
			<option value = "16" >16条</option>
			<option value = "18" >18条</option>
			<option value = "20" >20条</option>
		</select>
		<script>
			function pagePic(val) {
				window.location.href = changeUrlArg(window.location.href,'pageSize',val);
			}
		</script>
	</div><br>
	<script>
		var pageLim = getUrlArg(window.location.href,"pageSize");
		var obj = document.getElementById("pageSize");
		for(var i=0;i<obj.length;i++){
			if(obj[i].value === pageLim)
				obj[i].selected = true;
		}
	</script>
	<%

		String path = request.getContextPath()+"/oriPDFs/";
		query = query.replaceAll("\\s*|\t|\r|\n", "");
		if(query!=null && query.length() > 0) {
			IndexSearch is = new IndexSearch();
			String[] result = is.IndexSearch_Type(query , typeEn);
			if(result.length > 0){
			String result_title = result[0];
			String result_abstract = result[1];
			String[] re_title = result_title.split("\n");
			String[] re_abstract = result_abstract.split("\n");
			pageTotal = re_title.length;
			pageSize = Float.parseFloat(request.getParameter("pageSize"));
			pageNum = Math.ceil(pageTotal/pageSize);
			out.print("<div class=\"dispAll\">");
			int pageStart = (int)((pageValue - 1) * pageSize);
			int pageEnd = (int)(pageValue * pageSize);
			pageEnd = (pageSize > (pageTotal-pageStart))?pageTotal:pageEnd;
			for (int i = pageStart; i < pageEnd; i++) {
				out.print("<div class=\"dispSmall\">");
				out.print("<a href = \"" + path + re_title[i]+".pdf\" target=\"_blank\" class=\"dispa\">");
				out.print(re_title[i]);
				out.print("</a><br>");
				out.print("<p class=\"dispp\">");
				out.print(re_abstract[i]);
				out.print("</p><hr>");
				out.print("</div>");
			}
			out.print("<div>");
			out.print("<button class=\"page\" value=\"upPage\" onclick=\"changePage(this.value)\">«</button>");
			for(int i = 1;i <= pageNum;i++){
				out.print("<button class=\"page\" id=\""+i+"\" value=\""+i+"\" onclick=\"changePage(this.value)\">"+i+"</button>");
			}
			out.print("<button class=\"page\" value=\"downPage\" onclick=\"changePage(this.value)\">»</button>");
			out.print("</div>");
			}
		}

	%>
	<script>
		var num = "<%=pageValue%>";
		document.getElementById(num).className = "pageSelect";
	</script>
	<script>
		var oBtn = document.getElementById('btn_1');
		var pageChange = document.getElementsByClassName("page");
		oBtn.onclick = function () {
			Search();
		}
		 function changePage(val) {
			var currentNum = getUrlArg(window.location.href,"page");
			var totalNum = "<%=pageNum%>";
			totalNum = parseInt(totalNum);
			currentNum = parseInt(currentNum);
			var changeNum;

			switch(val){
				case("upPage"):changeNum = ((currentNum - 1)>0?currentNum-1:currentNum);break;
				case("downPage"):changeNum = ((currentNum + 1)<=totalNum?currentNum+1:currentNum);break;
				default:changeNum = val;break;
			}
			window.location.href = changeUrlArg(window.location.href,"page",changeNum);
		}

		document.onkeydown = function () {
			if (event.keyCode == 13) {
				Search();
			}
		}
		function Search() {
			var oInp = document.getElementById("inp").value;
			var oSel = document.getElementById("type").value;
			var url = './result.jsp?query=' + oInp + '&type=' + oSel + '&pageSize=10&page=1';
			window.open(url,'_self');
		}
	</script>
	
</body>
</html>