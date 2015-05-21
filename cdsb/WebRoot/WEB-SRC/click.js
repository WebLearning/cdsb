function readAndZanCtrl($scope, $http) {
	$scope.visible = false;		
	// 通过解析当前url获取articleID
	$scope.url = {
		value : window.location.href,
	};
	
//	var oldUrl = "http://www.cdsb.mobi/cdsb/app/ios/articledetail/20134";
	var oldUrl = $scope.url.value;
	var articleID =0;
	var flag = oldUrl.lastIndexOf("articledetail")+14;
	if(oldUrl.length -flag >20){
	    articleID = oldUrl.substring(oldUrl.lastIndexOf("articledetail")+14,oldUrl.lastIndexOf("?"));
	}else{
	    articleID = oldUrl.substring(oldUrl.lastIndexOf("articledetail") + 14);
	}
	console.log(articleID);
	//解析articleId end
	
	//页面初始化——关于通过UDID决定是否显示app下载链接 以及向后台传送UDID
	var cookieName_udid = getCookieValue("UDID");
	
	if(cookieName_udid=="false"){
		//获取不到udid 为网页访问
		$scope.visible = true;
	}else{
		//能获取到udid 为app访问，将div banner 隐藏
		$scope.visible = false;				
	}
	
	//拼接相关URL地址
	var basic = oldUrl.substring(0, oldUrl.lastIndexOf("sb/app") + 7);

	var url_zan_load = basic + "js/getlike/" + articleID;
	var url_zan_add = basic + "js/addlike/" + articleID;

	var url_read_load = basic + "js/getclick/" + articleID;
	var url_read_add="";//根据
	if(cookieName_udid=="false"){
		url_read_add = basic + "js/addclick/" + articleID+"/0";
	}else{
		url_read_add = basic + "js/addclick/" + articleID+"/"+cookieName_udid;
	}
	var pictureBasic = oldUrl.substring(0, oldUrl.lastIndexOf("sb/app")+3);
	$scope.pictureUrl = pictureBasic + "WEB-SRC/src/img/zan.png";
		
	var url_newID=basic+"authinfo/";
	
	//处理是否存入新cookie问题
	var loginCookie = getCookieValue("SPRING_SECURITY_REMEMBER_ME_COOKIE");
//	alert(loginCookie);	
	var newIDCheck = getCookieValue("AuthStr"); 	
	console.log(newIDCheck);
	if(newIDCheck=="false"){		
		var postStr = cookieName_udid+"/"+loginCookie;	
//		alert(url_newID+postStr);	
		$http.get(url_newID+postStr).success(function(response) {
			console.log("aaaaaaa"+response);
			addCookie("AuthStr", response, 999);
		});
	}else{
		var one = newIDCheck.substr(0,1);
		var two = newIDCheck.substr(1,1);
		var udidFlag=1;
		var securityFlag=1;
		if(cookieName_udid=="false"){
			udidFlag=0;
		}
		if(loginCookie=="false"){
			securityFlag=0;
		}
		
		var lastDate= newIDCheck.substr(2,8);
		var nowDate=new Date();
		var year = Number(nowDate.getFullYear());
		var month = Number(Number(nowDate.getMonth())+1);
		if(month < 10){
			month="0"+month;
		}
		var day = Number(nowDate.getDate());
		if(day < 10){
			day="0"+day;
		}
		var ddd=year+""+month+""+day;
		console.log(one+" "+two+" "+lastDate);
		console.log(udidFlag+" "+securityFlag+" "+ddd);
		if((one==udidFlag)&&(two==securityFlag)&&(lastDate==ddd)){		
			console.log("equal");	
		}else{
			var postStr = cookieName_udid+"/"+loginCookie;	
//			alert(url_newID+postStr);
			$http.get(url_newID+postStr).success(function(response) {
				console.log(response);
				addCookie("AuthStr", response, 999);
			});
		}
	}
	
	//页面初始化——关于点赞模块图片选择
	var cookieName_zan = getCookie(articleID + "zan");
	if (cookieName_zan == "false") {
		$scope.pictureUrl = pictureBasic + "WEB-SRC/src/img/zan.png";
		console.log("not not");
	} else if (cookieName_zan == "true") {
		$scope.pictureUrl = pictureBasic + "WEB-SRC/src/img/zan2.png";
		console.log("yes yes");
	} else {
		alert("error!!!");
	}

	//页面初始化——获取赞数
	console.log(url_zan_load);
	$http.get(url_zan_load).success(function(data) {
		console.log(data);
		$scope.zanNum = data;
		console.log($scope.zanNum);
	});

	//页面初始化——关于阅读数模块
	$scope.clickNum = 0;
	var cookieName_read = getCookie(articleID + "read");
	if (cookieName_read == "false") {
		console.log(url_read_add);
		// $scope.clickNum = Number(Number($scope.clickNum) + 1);
		$http.put(url_read_add).success(function(response) {
			console.log(response);
			$scope.clickNum = response;
		});
		addCookie(articleID + "read", "true", 999);
	} else if (cookieName_read == "true") {
		console.log("---read num added---");
		$http.get(url_read_load).success(function(response) {
			$scope.clickNum = response;
			console.log($scope.clickNum);
		});
	} else {
		alert("error!!!");
	}

	//点赞操作
	$scope.zanAdd = function(zanNum, pictureUrl) {
		if (pictureUrl == pictureBasic + "WEB-SRC/src/img/zan.png") {
			// $scope.zanNum = Number(Number(zanNum) + 1);
			$scope.pictureUrl = pictureBasic + "WEB-SRC/src/img/zan2.png";

			console.log(url_zan_add);
			// url_zan_add
			// ="http://192.168.1.112:8080/shangbao02/dianzan/zan";//test ......
			$http.put(url_zan_add).success(function(response) {
				console.log(response);
				$scope.zanNum = response;
			});
			addCookie(articleID + "zan", "true", 999);
		}
	};

};

function addCookie(name, value, expiresHours) {
	var cookieString = name + "=" + escape(value);
	var date = new Date();
	date.setTime(date.getTime() + expiresHours * 3600 * 1000);
	cookieString = cookieString + "; expires=" + date.toGMTString()
			+ "; path=/";//
	console.log(cookieString);
	document.cookie = cookieString;
};

function getCookie(name) {
	console.log("get cookie ----come in");
	var strCookie = document.cookie;
	console.log(strCookie);
	var arrCookie = strCookie.split("; ");
	for (var i = 0; i < arrCookie.length; i++) {
		var arr = arrCookie[i].split("=");
		if (arr[0] == name) {
			console.log(arr[0]);
			console.log(arr[1]);
			return "true";
		}
	}
	return "false";
};


function getCookieValue(name) {
	console.log("get udid ----come in");
	var strCookie = document.cookie;
console.log(strCookie);
	var arrCookie = strCookie.split("; ");
	for (var i = 0; i < arrCookie.length; i++) {
		var arr = arrCookie[i].split("=");
		if (arr[0] == name) {
			console.log(arr[0]);
			console.log(arr[1]);
			return arr[1];
		}
	}
	return "false";
};

