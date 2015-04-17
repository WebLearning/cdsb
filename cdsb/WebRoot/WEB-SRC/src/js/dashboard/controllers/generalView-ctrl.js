// angular.module("Dashboard", []);

angular.module("Dashboard").controller("generalViewCtrl", ["$scope","$http", function ($scope, $http) {

//    $scope.refreshGeneralView=function(){
//        var url=$scope.projectName+'/backapp/refresh';
//        //console.log(url);
//        $http.get(url).success(function(data){
//            //console.log(data);
//            $scope.newGeneralViewSections=data;
//        })
//    };
    $scope.generalViewSections=[
        {"name":"热点",
            "content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题","新闻6 标题","新闻7 标题","新闻8 标题","新闻9 标题"]},
        {"name":"本地",
            "content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]},
        {"name":"原创",
            "content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]},
        {"name":"娱乐",
            "content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]},
        {"name":"体育",
            "content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]},
        {"name":"财经",
            "content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]}
    ];

//    $scope.newGeneralViewSections=null;

    //返回当前所有的分类以及分类的文章----------------------------------------------------------------------------------
//    $scope.getNewGeneralViewData=function()
//    {
//        var url=$scope.projectName+'/backapp/all';
//        //console.log(url);
//        $http.get(url).success(function(data){
//            //console.log(data);
//            $scope.newGeneralViewSections=data;
//            //console.log("成功获取数据");
//        });
//    };
//    $scope.getNewGeneralViewData();
    $scope.checkTitle=function(str){
        var checkedStr;
        if(str==null||str==""){
            checkedStr="无";
        }else if(str.length>10){
            checkedStr=str.substr(0,16)+"...";
        }else{
            checkedStr=str;
        }
        return checkedStr;
    };

    //设置文章的位置(上移一位，下移一位)--------------------------------------------------------------------------------
    $scope.upGeneralViewArticle=function(channelEnglishName,index){
        $scope.coverIt();
        var url=$scope.projectName+'/backapp/setlocation/'+channelEnglishName+'/'+index+'/true';
        //console.log(url);
        $http.put(url).success(function(){
            console.log("上移成功");
            $scope.getNewGeneralViewData();
            $scope.closeOver();
//            $scope.getNewGeneralViewData();
            //console.log("上移成功");
        });
    };
    $scope.downGeneralViewArticle=function(channelEnglishName,index){
        $scope.coverIt();
        var url=$scope.projectName+'/backapp/setlocation/'+channelEnglishName+'/'+index+'/false';
        //console.log(url);
        $http.put(url).success(function(){
            console.log("下移成功");
            $scope.getNewGeneralViewData();
            $scope.closeOver();
            //console.log("下移成功");
        });
    };
    //将文章置顶--------------------------------------------------------------------------------------------------------
    $scope.urlForTop="";
    $scope.topGeneralViewArticle=function(channelEnglishName,index,top){
        $scope.coverIt();
        if(top==true){
            $scope.urlForTop=$scope.projectName+'/backapp/unsettop/'+channelEnglishName+'/'+index;
            $http.put($scope.urlForTop).success(function(){
                //console.log(data);
                alert("取消置顶成功");
                console.log("取消置顶成功");
                $scope.getNewGeneralViewData();
                $scope.closeOver();
            });
        }else if(top==false){
            $scope.urlForTop=$scope.projectName+'/backapp/settop/'+channelEnglishName+'/'+index;
            $http.put($scope.urlForTop).success(function(){
                alert("置顶成功");
                //console.log(data);
                console.log("置顶成功");
                $scope.getNewGeneralViewData();
                $scope.closeOver();
            });
        }
//        //console.log(url);
//        $http.put($scope.urlForTop).success(function(){
//            //console.log(data);
//            console.log("置顶成功");
//            $scope.getNewGeneralViewData();
//            $scope.closeOver();
//        });
    };
//-----------------------------------------------------------------------------------------------------------------------
    //转到新建文章页面并重置sidebar的爬虫文章按钮，不然会产生点击无效的BUG---------------------------------------------
    $scope.goNewGeneralArticle=function(articleId)
    {
        $scope.showGeneralArticle(articleId);
        document.getElementById("generalView").className="tab-pane";
        document.getElementById("generalViewArticle").className="tab-pane active";
        document.getElementById("generalSidebarID").className="sidebar-list";
        $scope.closeOver();
    };

    //得到文章的URL
    $scope.getGeneralArticleUrl=function(articleId)
    {
//        var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/"+articleId;
        var url=$scope.projectName+"/article/Published/1/"+articleId;
        return url;
    };

    //将文章数据传输给全局变量articleData
    $scope.transDataToArticleData=function(data)
    {
        for(p in $scope.articleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"){
                for(i in data[p]){
                    $scope.articleData[p][i]=data[p][i];
                }
            }else{
                $scope.articleData[p]=data[p];
            }
        }
    };

    //显示点击的文章
    $scope.showGeneralArticle=function(articleId)
    {
        var url=$scope.getGeneralArticleUrl(articleId);

        $scope.coverIt();
        $http.get(url).success(function(data) {
//            console.log(data);
            $scope.transDataToArticleData(data);
        });
    };
}]);