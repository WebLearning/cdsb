/**
 revoked ctrl
 **/

angular.module("Dashboard").controller("revokedCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.testLog=function(){
        console.log($scope.revokedData);
        console.log($scope.articleSelections);
        console.log($scope.articleSelectionsUrl);
    };

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
            return arr.toString();
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
            var date=new Date(dateStr);
            return $scope.formatDate(date);
        }
    };

    //文章跳转------------------------------------------------------------------------------------------------------------
    //转到新建文章页面并重置sidebar的爬虫文章按钮，不然会产生点击无效的BUG
    $scope.goNewArticle=function(articleId)
    {
        $scope.showRevokedArticle(articleId);
        document.getElementById("revoked").className="tab-pane";
        document.getElementById("revokedArticle").className="tab-pane active";
        document.getElementById("revokedSidebarID").className="sidebar-list";
        if($scope.articleData!={}){
            $scope.closeOver();
        }
    };

    //得到文章的URL
    $scope.getRevokedArticleUrl=function(articleId)
    {
        var url=$scope.projectName+"/article/Revocation/"+($scope.revokedData.currentNo).toString()+"/"+articleId;
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
    $scope.showRevokedArticle=function(articleId)
    {
        var url=$scope.getRevokedArticleUrl(articleId);

        $scope.coverIt();
        $http.get(url).success(function(data) {
            $scope.transDataToArticleData(data);
        });
    };

    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getRevokedData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.revokedData.currentNo);
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
        var arr=$scope.revokedData.tileList;
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
        $scope.getRevokedData($scope.revokedData.currentNo);
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
                var url=$scope.projectName+"/article/Revocation/"+($scope.revokedData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.refreshRevokedCur();
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
            var url=$scope.projectName+"/article/Revocation/"+($scope.revokedData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.refreshRevokedCur();
                alert("转暂存成功");
                $scope.closeOver();
            });
        }
    };
    $scope.checkModifyNoteInRevoked=function(id)
    {
        $scope.coverIt();
        var url=$scope.projectName+"/article/Revocation/"+($scope.revokedData.currentNo).toString()+"/"+id+"/log";
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
        if($scope.revokedSearchData.content==""||$scope.revokedSearchData.content==null){
            $scope.getRevokedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.revokedSearchData.content);
            $scope.getRevokedSearchData(1);
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
        if($scope.revokedSearchData.content==""||$scope.revokedSearchData.content==null){
            $scope.getRevokedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.revokedSearchData.content);
            $scope.getRevokedSearchData(1);
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
        if($scope.revokedSearchData.content==""||$scope.revokedSearchData.content==null){
            $scope.getRevokedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.revokedSearchData.content);
            $scope.getRevokedSearchData(1);
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
        if($scope.revokedSearchData.content==""||$scope.revokedSearchData.content==null){
            $scope.getRevokedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.revokedSearchData.content);
            $scope.getRevokedSearchData(1);
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
        if($scope.revokedSearchData.content==""||$scope.revokedSearchData.content==null){
            $scope.getRevokedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.revokedSearchData.content);
            $scope.getRevokedSearchData(1);
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
        if($scope.revokedSearchData.content==""||$scope.revokedSearchData.content==null){
            $scope.getRevokedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.revokedSearchData.content);
            $scope.getRevokedSearchData(1);
            $scope.closeOver();
        }
    };

}]);
