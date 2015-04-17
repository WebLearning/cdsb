/**
 Pending Controller
 **/
angular.module("Dashboard").controller("pendingCtrl",["$scope","$http",function($scope,$http){
    $scope.pendingTestLog=function() {
        console.log($scope.pendingData);
        console.log($scope.articleSelections);
        console.log($scope.articleSelectionsUrl);
    };

//    $scope.pendingData=null;
//    $scope.orderCondition="";

    //4.1点击待审文章，初始化页面，获得待审的第一页数据----------------------------------------------------------------------------------

//    $scope.getPendingData=function(pageID){
//
//        var url=$scope.projectName+'/article/Pending/'+pageID.toString()+$scope.orderCondition;
//        //console.log(url);
//        $http.get(url).success(function(data){
//            $scope.pendingData=data;
//            $scope.pageNums=getPageNums($scope.pendingData.pageCount);
//            //console.log("成功获取数据");
//        });
//    };
//    $scope.getPendingData(1);//生成待审页面时即产生第一页数据
//
//    $scope.refreshPending=function()
//    {
//        clearArticleSelections();
//        $scope.orderCondition="";
//        $scope.getPendingData(1);
//    };

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
        if($scope.pendingSearchData.content==""||$scope.pendingSearchData.content==null){
            $scope.getPendingData(1);
            $scope.closeOver();
        }else{
            console.log($scope.pendingSearchData.content);
            $scope.getPendingSearchData(1);
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
        if($scope.pendingSearchData.content==""||$scope.pendingSearchData.content==null){
            $scope.getPendingData(1);
            $scope.closeOver();
        }else{
            console.log($scope.pendingSearchData.content);
            $scope.getPendingSearchData(1);
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
        if($scope.pendingSearchData.content==""||$scope.pendingSearchData.content==null){
            $scope.getPendingData(1);
            $scope.closeOver();
        }else{
            console.log($scope.pendingSearchData.content);
            $scope.getPendingSearchData(1);
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
        if($scope.pendingSearchData.content==""||$scope.pendingSearchData.content==null){
            $scope.getPendingData(1);
            $scope.closeOver();
        }else{
            console.log($scope.pendingSearchData.content);
            $scope.getPendingSearchData(1);
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
        if($scope.pendingSearchData.content==""||$scope.pendingSearchData.content==null){
            $scope.getPendingData(1);
            $scope.closeOver();
        }else{
            console.log($scope.pendingSearchData.content);
            $scope.getPendingSearchData(1);
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
        if($scope.pendingSearchData.content==""||$scope.pendingSearchData.content==null){
            $scope.getPendingData(1);
            $scope.closeOver();
        }else{
            console.log($scope.pendingSearchData.content);
            $scope.getPendingSearchData(1);
            $scope.closeOver();
        }
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
            var date=new Date(dateStr);
            return $scope.formatDate(date);
        }
    };

    //文章跳转------------------------------------------------------------------------------------------------------------
    //转到新建文章页面并重置sidebar的爬虫文章按钮，不然会产生点击无效的BUG
    $scope.goNewArticle=function(articleId)
    {
        $scope.showPendingArticle(articleId);
        document.getElementById("pendingTrial").className="tab-pane";
        document.getElementById("pendingArticle").className="tab-pane active";
        document.getElementById("pendingSidebarID").className="sidebar-list";
        if($scope.articleData!={}){
            $scope.closeOver();
        }
    };

    //得到文章的URL
    $scope.getPendingArticleUrl=function(articleId)
    {
        var url=$scope.projectName+"/article/Pending/"+($scope.pendingData.currentNo).toString()+"/"+articleId;
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
    $scope.showPendingArticle=function(articleId)
    {
        var url=$scope.getPendingArticleUrl(articleId);

        $scope.coverIt();
        $http.get(url).success(function(data) {
            $scope.transDataToArticleData(data);
        });
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
        var arr=$scope.pendingData.tileList;
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
        $scope.getPendingData($scope.pendingData.currentNo);
    }
    //对选取的文章进行操作
    //删除-------------
    $scope.deleteArticleSelections=function()
    {
        $scope.coverIt();
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
            $scope.closeOver();
        }else{
//            if (confirm("确定撤销选中的文章吗？")==true)
//            {
                var url=$scope.projectName+"/article/Pending/"+($scope.pendingData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.refreshPendingCur();
                    alert("撤销成功");
                    $scope.closeOver();
                });
//            }
        }
    };
//立刻发布--------------------------------------------------------------------------------------------------------
    $scope.publishArticleSelectionsNow=function()
    {
        $scope.coverIt();
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
            $scope.closeOver();
        }else{
            var url=$scope.projectName+"/article/Pending/"+($scope.pendingData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.refreshPendingCur();
                alert("发布成功");
                $scope.closeOver();
            });
        }
    };
    //定时发布----------------------------------------------------------------------------------------------------------
    $scope.publishArticleSelectionsTiming=function()
    {
        $scope.coverIt();
            var myDate=new Date();
            var myDateTime=myDate.getTime();
            var str1=$scope.publishTime.substr(0,10);
            var str2=$scope.publishTime.substr(11,16);
            var str3=str1.concat(" ");
            var str4=str3.concat(str2);
            var str5=new Date(str4);
            var myPublishedTime=str5.getTime();
//            console.log(myDateTime);
//            console.log(myPublishedTime);
            var time=myPublishedTime-myDateTime;
            console.log(time);
            if($scope.articleSelectionsUrl==""){
                alert("未选取文章");
                $scope.closeOver();
            }else{
                var url=$scope.projectName+"/article/Pending/"+($scope.pendingData.currentNo).toString()+"/timingpublish/"+$scope.articleSelectionsUrl+"/"+time;
                console.log(url);
                $http.get(url).success(function(){
                    $('#Select_Time').modal('toggle');
                    clearArticleSelections();
                    $scope.refreshPendingCur();
                    alert("定时成功");
                    $scope.closeOver();
                });
            }
    };
    $scope.checkModifyNoteInPending=function(id){
        $scope.coverIt();
        var url=$scope.projectName+"/article/Pending/"+($scope.pendingData.currentNo).toString()+"/"+id+"/log";
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

    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getPendingData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.pendingData.currentNo);
    };

    //得到页码数组的函数
    function getPageNums(pageCount)
    {
        if(pageCount==1||pageCount<1){
            return [1];
        }else{
            var arr=[];
            for(i=0;i<pageCount;i++){
                arr.push(i+1);
            }
            return arr;
        }
    }

}]);