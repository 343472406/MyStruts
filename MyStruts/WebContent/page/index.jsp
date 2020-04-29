<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- <form action="/MyStruts/user/UserAction.do">
		userName
	</form> -->
	<a href="/MyStruts/user/UserAction.do?name=Marry&password=789&age=18">User</a>
	<a href="/MyStruts/dept/DeptAction.do?name=管理&deptNo=001">Dept</a><br>
	<a href="/MyStruts/user/DispatchUserAction.do?method=login&name=Marry&password=789&age=18">DispatchUser</a>
</body>
</html>