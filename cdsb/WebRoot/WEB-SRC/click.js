function readAndZanCtrl($scope, $http) {

	// 通过解析当前url获取articleID
	$scope.url = {
		value : window.location.href,
	};
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
	var cookieName_udid = getUDID("UDID");
	if(cookieName_udid==null){
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
	if(cookieName_udid==null){
		url_read_add = basic + "js/addclick/" + articleID+"/0";
	}else{
		url_read_add = basic + "js/addclick/" + articleID+"/"+cookieName_udid;
	}
	var pictureBasic = oldUrl.substring(0, oldUrl.lastIndexOf("sb/app")+3);
	$scope.pictureUrl = pictureBasic + "WEB-SRC/src/img/zan.png";
			
	//页面初始化——关于点赞模块图片选择
	var cookieName_zan = getCookie(articleID + "zan");
	if (cookieName_zan == null) {
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
	if (cookieName_read == null) {
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
	return null;
};

function getUDID(name) {
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
	return null;
};

