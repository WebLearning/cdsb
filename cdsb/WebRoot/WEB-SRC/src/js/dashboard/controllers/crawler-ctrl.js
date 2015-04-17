
angular.module("Dashboard").controller("crawlerCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.testLog=function(){
        console.log($scope.crawlerData);
        console.log($scope.articleSelections);
        console.log($scope.articleSelectionsUrl);
    };

//    $scope.crawlerData=null;
//    $scope.orderCondition="";
//
//    //初始化页面，获取爬虫第一页的数据,返回的是一个titleList-------------------------------------------------------------------
//    $scope.getCrawlerData=function(pageID)
//    {
//        var url=$scope.projectName+'/article/Crawler/'+pageID.toString()+$scope.orderCondition;
//        //console.log(url);
//        $http.get(url).success(function(data){
//            $scope.crawlerData=data;
//            $scope.pageNums=getPageNums($scope.crawlerData.pageCount);
//            //console.log("成功获取数据");
//        });
//    };
//    $scope.getCrawlerData(1);//会在生成页面的时候直接运行!
//
//    $scope.refreshCrawler=function()
//    {
//        clearArticleSelections();
//        $scope.orderCondition="";
//        $scope.getCrawlerData(1);
//    };

    //初始化表头
//    $scope.tableHeadsData=["标题","标题Url","文章ID","作者","时间","来源","分类","等级","点击数","评论数","赞数","摘要","字数","活动","选择"];

    //检查表的内容 数据若是NULL则显示"无",数组若是空则显示"无数据",转化时间戳为日期显示
    $scope.checkSummary=function(str){
        var checkedStr;
        if(str==null||str==""){
            checkedStr="无";
        }else if(str.length>26){
            checkedStr=str.substr(0,26)+"...";
        }else{
            checkedStr=str;
        }
        return checkedStr;
    };
    $scope.checkIfNull=function(str)
    {
        var checkedStr;
        if(str==null||str==""){
            checkedStr="无";
        }else{
            checkedStr=str;
        }
        return checkedStr;
    };
    $scope.checkIfEmpty=function(arr)
    {
        var checkedStr;
        if(arr.length==0){
            checkedStr="无数据";
            return checkedStr;
        }else{
            return arr;
        }
    };
    $scope.checkNum=function(str)
    {
        var checkedStr;
        if(str==null||str==""||str=="0"){
            checkedStr="0";
        }else{
            checkedStr=str;
        }
        return checkedStr;
    };

    $scope.dateStringToDate=function(dateStr)
    {
        if(dateStr==null||dateStr==""){
            return "无";
        }else{
//            return new Date(parseInt(dateStr)*1000).toLocaleString().replace(/年|月/g,"-").replace(/日/g," ");
            var date=new Date(dateStr);
            return $scope.formatDate(date);
        }
    };

    //文章跳转------------------------------------------------------------------------------------------------------------
    //转到新建文章页面并重置sidebar的爬虫文章按钮，不然会产生点击无效的BUG
    $scope.goNewArticle=function(articleId)
    {
        $scope.showCrawlerArticle(articleId);
        document.getElementById("crawler").className="tab-pane";
        document.getElementById("crawlerArticle").className="tab-pane active";
        document.getElementById("crawlerSidebarID").className="sidebar-list";
        if($scope.articleData!={}){
            $scope.closeOver();
        }
    };

    //得到文章的URL
    $scope.getCrawlerArticleUrl=function(articleId)
    {
        var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/"+articleId;
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
    $scope.showCrawlerArticle=function(articleId)
    {
        var url=$scope.getCrawlerArticleUrl(articleId);

        $scope.coverIt();
        $http.get(url).success(function(data) {
//            console.log(data);
            $scope.transDataToArticleData(data);
        });
//        $scope.closeOver();
    };

    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getCrawlerData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.crawlerData.currentNo);
    };

    //得到页码数组的函数
//    function getPageNums(pageCount)
//    {
//        if(pageCount==1||pageCount<1){
//            return [1];
//        }else{
//            var arr=[];
//            for(i=0;i<pageCount;i++){
//                arr.push(i+1);
//            }
//            return arr;
//        }
//    }

    //文章的选取和操作------------------------------------------------------------------------------------------------------
    //文章的选取
    $scope.articleSelections=[];
    $scope.articleSelectionsUrl="";

    $scope.selectArticle=function(articleId,selectState)
    {
        if(!selectState){
            $scope.articleSelections.push(articleId);
        }else{
            var index=$scope.articleSelections.indexOf(articleId);
            $scope.articleSelections.splice(index,1);
        }
//        console.log($scope.articleSelections);

        if($scope.articleSelections.length>0){
            var str="";
            for(i=0;i<$scope.articleSelections.length;i++){
                str+=($scope.articleSelections[i]+"_");
                $scope.articleSelectionsUrl=str.substr(0,str.length-1);
            }
        }else{
            $scope.articleSelectionsUrl="";
        }
//        console.log($scope.articleSelectionsUrl);
    };

    $scope.checkSelectState=function(articleId)
    {
        if($scope.articleSelections.length>0){
            for(i=0;i<$scope.articleSelections.length;i++){
                if(articleId==$scope.articleSelections[i]){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    };

    function clearArticleSelections()
    {
        $scope.articleSelections=[];
        $scope.articleSelectionsUrl="";
    }

    var allSelectState="none";
    $scope.selectAll=function()
    {
        var arr=$scope.crawlerData.tileList;
        if(allSelectState=="none"){
            selectByArr(arr);
            allSelectState="all";
        }else if(allSelectState=="all"){
            selectByArr([]);
            allSelectState="none";
        }
    };
    function selectByArr(arr){
        if(arr.length>0){
            for(i=0;i<arr.length;i++){
                $scope.articleSelections.push(arr[i].articleId);
            }
        }else{
            $scope.articleSelections=[];
        }
        if($scope.articleSelections.length>0){
            var str="";
            for(i=0;i<$scope.articleSelections.length;i++){
                str+=($scope.articleSelections[i]+"_");
                $scope.articleSelectionsUrl=str.substr(0,str.length-1);
            }
        }else{
            $scope.articleSelectionsUrl="";
        }
        $scope.getCrawlerData($scope.crawlerData.currentNo);
    }
    //对选取的文章进行操作
    $scope.deleteArticleSelections=function()
    {
        $scope.coverIt();
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
            $scope.closeOver();
        }else{
//            if (confirm("确定删除选中的文章吗？")==true)
//            {
                var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.refreshCrawlerCur();
                    alert("删除成功");
                    $scope.closeOver();
                });
//            }
        }
    };

    $scope.saveArticleSelections=function()
    {
        $scope.coverIt();
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
            $scope.closeOver();
        }else{
            var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.refreshCrawlerCur();
                alert("转草稿箱成功");
                $scope.closeOver();
            });
        }
    };
    $scope.publishArticleSelectionsNowOutCrawler=function()
    {
        $scope.coverIt();
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
            $scope.closeOver();
        }else{
            var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.refreshCrawlerCur();
                alert("发布成功");
                $scope.closeOver();
            });
        }
    };
    $scope.publishArticleSelectionsTimingOutCrawler=function()
    {
        $scope.coverIt();
        var myDate=new Date();
        var myDateTime=myDate.getTime();
        var str1=$scope.publishTimeOutCrawler.substr(0,10);
        var str2=$scope.publishTimeOutCrawler.substr(11,16);
        var str3=str1.concat(" ");
        var str4=str3.concat(str2);
        var str5=new Date(str4);
        var myPublishedTime=str5.getTime();
        var time=myPublishedTime-myDateTime;
        console.log(time);
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
            $scope.closeOver();
        }else{
            var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/timingpublish/"+$scope.articleSelectionsUrl+"/"+time;
            console.log(url);
            $http.get(url).success(function(){
                $('#Select_TimeOutCrawler').modal('toggle');
                clearArticleSelections();
                $scope.refreshCrawlerCur();
                alert("定时成功");
                $scope.closeOver();
            });
        }
    };
    //查看修改记录------------------------------------------------------------------------------------------------------
    $scope.checkModifyNoteInCrawler=function(id){
        var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/"+id+"/log";
        console.log(url);
        $scope.coverIt();
        $http.get(url).success(function(data){
            console.log(data);
            if(data.length>0){
                alert("操作记录："+"["+data+"]");
                $scope.closeOver();
            }else{
                alert("无记录");
                $scope.closeOver();
            }
        });
    };

    //排序---------------------------------------------------------------------------------------------------------------
    var wordsOrderState="desc";
    $scope.orderByWords=function(){
        $scope.coverIt();
        if(wordsOrderState=="desc"){
            $scope.transOrderConditions("/words/asc");
            wordsOrderState="asc";
        }else if(wordsOrderState=="asc"){
            $scope.transOrderConditions("/words/desc");
            wordsOrderState="desc";
        }
        if($scope.crawlerSearchData.content==""||$scope.crawlerSearchData.content==null){
            $scope.getCrawlerData(1);
            $scope.closeOver();
        }else{
            console.log($scope.crawlerSearchData.content);
            $scope.getCrawlerSearchData(1);
            $scope.closeOver();
        }
    };

    var newsCommendsOrderState="desc";
    $scope.orderByNewsCommends=function(){
        $scope.coverIt();
        if(newsCommendsOrderState=="desc"){
            $scope.transOrderConditions("/newsCommends/asc");
            newsCommendsOrderState="asc";
        }else if(newsCommendsOrderState=="asc"){
            $scope.transOrderConditions("/newsCommends/desc");
            newsCommendsOrderState="desc";
        }
        if($scope.crawlerSearchData.content==""||$scope.crawlerSearchData.content==null){
            $scope.getCrawlerData(1);
            $scope.closeOver();
        }else{
            console.log($scope.crawlerSearchData.content);
            $scope.getCrawlerSearchData(1);
            $scope.closeOver();
        }
    };
    var crawlerCommendsOrderState="desc";
    $scope.orderByCrawlerCommends=function(){
        $scope.coverIt();
        if(crawlerCommendsOrderState=="desc"){
            $scope.transOrderConditions("/crawlerCommends/asc");
            crawlerCommendsOrderState="asc";
        }else if(crawlerCommendsOrderState=="asc"){
            $scope.transOrderConditions("/crawlerCommends/desc");
            crawlerCommendsOrderState="desc";
        }
        if($scope.crawlerSearchData.content==""||$scope.crawlerSearchData.content==null){
            $scope.getCrawlerData(1);
            $scope.closeOver();
        }else{
            console.log($scope.crawlerSearchData.content);
            $scope.getCrawlerSearchData(1);
            $scope.closeOver();
        }
    };

    var timeOrderState="desc";
    $scope.orderByTime=function(){
        $scope.coverIt();
        if(timeOrderState=="desc"){
            $scope.transOrderConditions("/time/asc");
            timeOrderState="asc";
        }else if(timeOrderState=="asc"){
            $scope.transOrderConditions("/time/desc");
            timeOrderState="desc";
        }
        if($scope.crawlerSearchData.content==""||$scope.crawlerSearchData.content==null){
            $scope.getCrawlerData(1);
            $scope.closeOver();
        }else{
            console.log($scope.crawlerSearchData.content);
            $scope.getCrawlerSearchData(1);
            $scope.closeOver();
        }
    };

    var clicksOrderState="desc";
    $scope.orderByClicks=function(){
        $scope.coverIt();
        if(clicksOrderState=="desc"){
            $scope.transOrderConditions("/clicks/asc");
            clicksOrderState="asc";
        }else if(clicksOrderState=="asc"){
            $scope.transOrderConditions("/clicks/desc");
            clicksOrderState="desc";
        }
        if($scope.crawlerSearchData.content==""||$scope.crawlerSearchData.content==null){
            $scope.getCrawlerData(1);
            $scope.closeOver();
        }else{
            console.log($scope.crawlerSearchData.content);
            $scope.getCrawlerSearchData(1);
            $scope.closeOver();
        }
    };

    var likesOrderState="desc";
    $scope.orderByLikes=function(){
        $scope.coverIt();
        if(likesOrderState=="desc"){
            $scope.transOrderConditions("/likes/asc");
            likesOrderState="asc";
        }else if(likesOrderState=="asc"){
            $scope.transOrderConditions("/likes/desc");
            likesOrderState="desc";
        }
        if($scope.crawlerSearchData.content==""||$scope.crawlerSearchData.content==null){
            $scope.getCrawlerData(1);
            $scope.closeOver();
        }else{
            console.log($scope.crawlerSearchData.content);
            $scope.getCrawlerSearchData(1);
            $scope.closeOver();
        }
    };

}]);
//    $scope.goCrawlerList=function()
//    {
//        document.getElementById("crawlerArticle_article").className="tab-pane";
//        document.getElementById("crawlerArticle_list").className="tab-pane active";
//    };
