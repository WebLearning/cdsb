//function getAllUsers() {
//	console.log("enter get all users");	
//	var xmlhttp;
//    if (window.XMLHttpRequest)
//    {// code for IE7+, Firefox, Chrome, Opera, Safari
//        xmlhttp=new XMLHttpRequest();
//    }
//    else
//    {// code for IE6, IE5
//        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
//    }
//
//    var url = "/Shangbao01/user/users";
//    xmlhttp.open("GET",url,true);
//    xmlhttp.send();
//
//    xmlhttp.onreadystatechange=function()
//    {
//        if (xmlhttp.readyState==4 && xmlhttp.status==200)
//        {
//            var res = xmlhttp.responseText;
//            console.log(res);
//        }
//    };
//}


function usersCtrl($scope, $http) {
	$scope.users="";
	$scope.orderProp = 'name';
	
	$http.get('http://localhost:8080/Shangbao01/user/users')
			.success(function(data) {
				$scope.usersTemp = data;				
			});
	
	$scope.showAllUsers=function(){
		$scope.users=$scope.usersTemp;
		console.log($scope.users);
	};
}