<%@page import="com.github.ulwx.aka.fileserver.utils.AkaFileUploadAppConfig"%>
<%@page import="com.github.ulwx.aka.webmvc.utils.JspLog"%>
<%@page import="com.ulwx.tool.CTime"%>
<%@page import="com.ulwx.tool.FileUtils"%>
<%@page import="com.ulwx.tool.StringUtils"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
         import="java.io.File" errorPage=""%>
<%@ page import="java.sql.Date" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>文件列表</title>
<jsp:include page="/head.jsp" flush="true"></jsp:include>
<style>


</style>
<script>
$(document).ready(function(){
	
	$("body").css("visibility", "visible");
});
function openDlg(url){
	$("#imgview").attr("src",url);
	$('#dlg').dialog('open');
}

function viewDlg(url){
	$("#frameview").attr("src",url);
	$('#viewDlg').dialog('open');
}
</script>
</head>
<%
	String uploadDir=AkaFileUploadAppConfig.getUploadDir();
	String httpPrefix=AkaFileUploadAppConfig.getHttpPrefix();
	String dir=request.getParameter("dir");
	if(StringUtils.isEmpty(dir)){
		dir="/";
	}
    if(!dir.endsWith("/")){
        dir=dir+"/";
    }

	String path=uploadDir+dir;
	String parent=FileUtils.getParentPath(dir);
	if(StringUtils.isEmpty(parent)){
		parent="/";
	}
   if(!parent.endsWith("/")){
       parent=parent+"/";
   }

    JspLog.debug("uploadDir="+uploadDir+",httpPrefix="+httpPrefix+",dir="+dir+",path="+path+",parent="+parent);
%>
<body class="easyui-layout commonPage" style="visibility: hidden;">
<div region="center" border="false" style="padding:10px;" fit="true">
<div style="width:100%; margin:0 auto;height:100%">
	<div id="tool">
		<span style="font-size:20px">当前目标[<%=dir%>] </span>
		<span style="display:inline-block;height:20px;line-height:20px;float:right;font-weight:bold">
		<a href="list.jsp?dir=<%=parent %>">返回上一层</a></span>
	</div>
    <table class="easyui-datagrid"   data-options="toolbar:'#tool',fit:true,fitColumns:true" >
        <thead>
            <tr>
                <th data-options="field:'name',width:140">文件名</th>
                <th data-options="field:'view' ,width:180">预览</th>
                <th data-options="field:'size'">大小(K)</th>
                <th data-options="field:'op',width:40">操作</th>
                <th data-options="field:'time'">时间</th>
            </tr>
        </thead>
        <tbody>
<%
		File dirFile=new File(path);
		String cp=dirFile.getCanonicalPath();
		cp=cp.replace("\\", "/");
		uploadDir=uploadDir.replace("\\", "/");
        JspLog.debug("cp="+cp+",uploadDir="+uploadDir);
		if(!cp.toLowerCase().startsWith(uploadDir.toLowerCase())){
		%>
		
			<tr>
			<td>路径非法<%=cp %>,<%=uploadDir %></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td></tr>
		<% 
		}else{
			File[] files=dirFile.listFiles();
			for(File file:files){
				String name=file.getName();
				long motified=file.lastModified();
				String mtime= CTime.formatWholeDate(new Date(motified));
				long size=file.length();
				float m=size/(1024) ;
				//String dirName=dir.equals("/")?"":dir+name;
				String dirName=dir +name;
 %>
            <tr>
                <td><%
                if(file.isFile()){	//文件
                %><%=name%>
                <%} else{
                	
                %>
                	<a href="list.jsp?dir=<%=dirName%>"><%=name%></a>
                <%} %>
                
                </td>
                <td>
                <%
                 if("jpg".equals(FileUtils.getTypePart(name)) || "png".equals(FileUtils.getTypePart(name)) ){
                %>
                	 <img src="<%=httpPrefix%><%=dirName %>" style="height:150px" onclick="openDlg('<%=httpPrefix%><%=dirName %>')"/>
                <%
                 }
                %></td>
                <td><%=m %></td>
                <td>
                <%
                if(file.isFile()){	
                %>
               [ <a href="<%=httpPrefix%><%=dirName %>" download>下载</a> ][
                <a href="javascript:viewDlg('<%=httpPrefix%><%=dirName %>');void(0);">预览</a> ]
                <%
                }%>
                </td>
                <td><%=mtime %></td>
            </tr>
<%
			} 
		}
%>

        </tbody>
    </table>

    <div id="dlg" class="easyui-dialog" title="图片查看" data-options="iconCls:'icon-save',closed:true" 
    style="width:800px;height:500px;padding:10px">
    	<div style="height:100%;line-height:100%">
       		<img id="imgview" src="" style="margin: 0 auto;display: inline-block;"/>
       </div>
    </div>
    
   <div id="viewDlg" class="easyui-dialog" title="图片查看" data-options="iconCls:'icon-save',closed:true" 
    style="width:800px;height:500px;padding:10px">
    	<div style="height:100%;line-height:100%">
       		<iframe  id="frameview" src="" name="view_frame" frameborder="0"  style="width:100%;height:100%"></iframe >
       </div>
    </div>
</div>
</div>
</body>
	
