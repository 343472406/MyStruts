<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 引入jstl标签库 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3><a href="http://localhost:8080/springmvc-demo/user/list.do">获取用户列表</a></h3>
<h3>用户列表</h3>
<table border="1" style="border-collapse: collapse;">
	<tr>
		<th>id</th>
		<th>name</th>
		<th>password</th>
		<th>age</th>
		<th>操作</th>
	</tr>
	
	<c:forEach items="${users}" var="user">
		<tr>
			<td>${user.id}</td>
			<td>${user.name}</td>
			<td>${user.password}</td>
			<td>${user.age}</td>
			<td>
				<a onclick="deleteUser(${user.id});" href="javascript:void(0);">删除</a> &nbsp;&nbsp;
				<a href="">修改</a> 
			</td>
		</tr>
	
	</c:forEach>
	
</table>
<script type="text/javascript">

	function deleteUser(userId){
		if(confirm("亲，您确定要删除此条数据么？")){
			
			location.href="${pageContext.request.contextPath}/user/delete.do?userId="+userId
		}
	}
</script>
	
</body>
</html>