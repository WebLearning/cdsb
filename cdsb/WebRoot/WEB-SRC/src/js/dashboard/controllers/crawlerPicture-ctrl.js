/**
 crawlerPicture-controller
 **/
angular.module("Dashboard").controller("crawlerPictureCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.testLog=function(){
        $scope.getCrawlerPictureData(1);
//        console.log($scope.crawlerPictureData);
//        console.log($scope.articleSelections);
//        console.log($scope.articleSelectionsUrl);
    };
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
    $scope.checkNum=function(str){
        var checkedStr;
        if(str=="0"||str==null||str==""){
            checkedStr="0";
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
    $scope.dateStringToDate=function(dateStr)
    {
        if(dateStr==null||dateStr==""){
            return "无";
        }else{
            var date=new Date(dateStr);
            return $scope.formatDate(date);
        }
    };

    //文章跳转------------------------------------------------------------------------------------------------------------
    //转到新建文章页面并重置sidebar的爬虫文章按钮，不然会产生点击无效的BUG
    $scope.goNewPicture=function(articleId)
    {
        $scope.showCrawlerPicture(articleId);
        document.getElementById("crawlerPicture").className="tab-pane";
        document.getElementById("crawlerPictureView").className="tab-pane active";
        document.getElementById("crawlerPictureSidebarID").className="sidebar-list";
        if($scope.articleData!={}){
            $scope.closeOver();
        }
    };

    //得到文章的URL
    $scope.getCrawlerPictureUrl=function(articleId)
    {
        var url=$scope.projectName+"/picture/Crawler/"+($scope.crawlerPictureData.currentNo).toString()+"/"+articleId;
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
    $scope.showCrawlerPicture=function(articleId)
    {
        var url=$scope.getCrawlerPictureUrl(articleId);

        $scope.coverIt();
        $http.get(url).success(function(data) {
//            console.log(data);
            $scope.transDataToArticleData(data);
        });
    };

    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getCrawlerPictureData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.crawlerPictureData.currentNo);
    };

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
        var arr=$scope.crawlerPictureData.tileList;
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
        $scope.getCrawlerPictureData($scope.crawlerPictureData.currentNo);
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
                var url=$scope.projectName+"/picture/Crawler/"+($scope.crawlerPictureData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.refreshCrawlerPictureCur();
                    alert("删除成功");
                    $scope.closeOver();
                });
//            }
        }
    };

    $scope.savePictureArticleSelections=function()
    {
        $scope.coverIt();
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
            $scope.closeOver();
        }else{
            var url=$scope.projectName+"/picture/Crawler/"+($scope.crawlerPictureData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.refreshCrawlerPictureCur();
                alert("转暂存成功");
                $scope.closeOver();
            });
        }
    };
    $scope.publishedPictureArticleSelectionsOutCrawlerPictureNow=function()
    {
        $scope.coverIt();
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
            $scope.closeOver();
        }else{
            var url=$scope.projectName+"/picture/Crawler/"+($scope.crawlerPictureData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.refreshCrawlerPictureCur();
                alert("发布成功");
                $scope.closeOver();
            });
        }
    };
    $scope.publishedPictureArticleSelectionsTimingOutCrawlerPicture=function()
    {
        $scope.coverIt();
        var myDate=new Date();
        var myDateTime=myDate.getTime();
        var str1=$scope.publishTimeOutCrawlerPicture.substr(0,10);
        var str2=$scope.publishTimeOutCrawlerPicture.substr(11,16);
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
            var url=$scope.projectName+"/picture/Crawler/"+($scope.crawlerPictureData.currentNo).toString()+"/timingpublish/"+$scope.articleSelectionsUrl+"/"+time;
            console.log(url);
            $http.get(url).success(function(){
                $('#Select_TimeOutCrawlerPicture').modal('toggle');
                clearArticleSelections();
                $scope.refreshCrawlerPictureCur();
                alert("定时成功");
                $scope.closeOver();
            });
        }
    };
    //查看修改记录------------------------------------------------------------------------------------------------------
    $scope.checkModifyNoteInCrawlerPicture=function(id)
    {
        $scope.coverIt();
        var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerPictureData.currentNo).toString()+"/"+id+"/log";
        console.log(url);
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
        if($scope.crawlerPictureSearchData.content==""||$scope.crawlerPictureSearchData.content==null){
            $scope.getCrawlerPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.crawlerPictureSearchData.content);
            $scope.getCrawlerPictureSearchData(1);
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
        if($scope.crawlerPictureSearchData.content==""||$scope.crawlerPictureSearchData.content==null){
            $scope.getCrawlerPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.crawlerPictureSearchData.content);
            $scope.getCrawlerPictureSearchData(1);
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
        if($scope.crawlerPictureSearchData.content==""||$scope.crawlerPictureSearchData.content==null){
            $scope.getCrawlerPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.crawlerPictureSearchData.content);
            $scope.getCrawlerPictureSearchData(1);
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
        if($scope.crawlerPictureSearchData.content==""||$scope.crawlerPictureSearchData.content==null){
            $scope.getCrawlerPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.crawlerPictureSearchData.content);
            $scope.getCrawlerPictureSearchData(1);
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
        if($scope.crawlerPictureSearchData.content==""||$scope.crawlerPictureSearchData.content==null){
            $scope.getCrawlerPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.crawlerPictureSearchData.content);
            $scope.getCrawlerPictureSearchData(1);
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
        if($scope.crawlerPictureSearchData.content==""||$scope.crawlerPictureSearchData.content==null){
            $scope.getCrawlerPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.crawlerPictureSearchData.content);
            $scope.getCrawlerPictureSearchData(1);
            $scope.closeOver();
        }
    };
}]);
