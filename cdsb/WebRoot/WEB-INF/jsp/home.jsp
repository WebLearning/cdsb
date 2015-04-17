<!DOCTYPE html>
<html ng-app>
<head>
<title>home.html</title>

<meta name="keywords" content="keyword1,keyword2,keyword3">
<meta name="description" content="this is my page">
<meta name="content-type" content="text/html; charset=UTF-8">

<!--<link rel="stylesheet" type="text/css" href="./styles.css">-->

<script src="/Shangbao01/WEB-SRC/src/js/angular.js"></script>
<script src="/Shangbao01/WEB-SRC/src/js/loginSuccess-ctrl.js"></script>

</head>

<body>
	<div ng-controller="usersCtrl">
		<div>
		Login success! <a
			href="http://localhost:8080/Shangbao01/login.jsp">login</a> <a
			href="" ng-click="showAllUsers()">show all users</a>
		</div>
		<br>
		<div>
			<div>
				Search: <input ng-model="query"> 
				Sort by: <select ng-model="orderProp">
					<option value="name">name</option>
					<option value="id">id</option>
				</select>
			</div>
			<ul>
				<li ng-repeat="user in users| filter:query| orderBy:orderProp">
					<p>name: {{user.name}}</p>
					<p>password: {{user.passwd}}</p>
					<p>id: {{user.id}}</p>
				</li>
			</ul>
		</div>
	</div>
</body>
</html>
