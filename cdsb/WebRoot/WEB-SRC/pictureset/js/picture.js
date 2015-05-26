function pictureCtrl($scope,$http) {
	$scope.titleList={
		date:"20150518",
		name:"某某",
	};
	
	
	$scope.pictureData = [
			{picUrl : "img/1.jpg",title : "标题标题1天气超好哦",content : "内容内容内容内容内容内容Levenshtein 距离，又称编辑距离，指的是两个字符串之间，由一个转换成另一个所需的最少编辑操作次数"	},	
			{picUrl : "img/2.jpg",title : "标题标题2",content : "内容内容内容内容内容内容"	},
			{picUrl : "img/3.jpg",title : "标题标题3",content : "内容内容"	},
			{picUrl : "img/4.jpg",title : "标题标题4",content : "内容内容"	},
		];
}