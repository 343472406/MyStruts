<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<span style="color:red;">${errorMsg}</span>
<span style="color:green;">${successMsg}</span>
<fieldset>
	<legend>单个文件上传</legend>
	<form action="${pageContext.request.contextPath}/upload.do" 
		method="post" enctype="multipart/form-data">
		姓名: <input name="username"><br>
		头像: <input type="file" name="headImg"><br>
		<button type="submit">提交</button>
	</form>
</fieldset>
<fieldset>
	<legend>多个文件上传</legend>
	<form action="${pageContext.request.contextPath}/uploads.do" method="post" enctype="multipart/form-data">
		姓名: <input name="username"><br>
		文件1: <input type="file" name="headImgs"><br>
		文件2: <input type="file" name="headImgs"><br>
		文件3: <input type="file" name="headImgs"><br>
		<button type="submit">提交</button>
	</form>
</fieldset>

</body>
</html>