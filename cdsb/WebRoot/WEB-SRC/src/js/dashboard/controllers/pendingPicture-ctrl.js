/**
 pendingPictureCtrl
 **/
angular.module("Dashboard").controller("pendingPictureCtrl",["$scope","$http",function($scope,$http){

    $scope.pendingPictureTestLog=function() {
        console.log($scope.pendingPictureData);
        console.log($scope.articleSelections);
        console.log($scope.articleSelectionsUrl);
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
        if($scope.pendingPictureSearchData.content==""||$scope.pendingPictureSearchData.content==null){
            $scope.getPendingPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.pendingPictureSearchData.content);
            $scope.getPendingPictureSearchData(1);
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
        if($scope.pendingPictureSearchData.content==""||$scope.pendingPictureSearchData.content==null){
            $scope.getPendingPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.pendingPictureSearchData.content);
            $scope.getPendingPictureSearchData(1);
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
        if($scope.pendingPictureSearchData.content==""||$scope.pendingPictureSearchData.content==null){
            $scope.getPendingPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.pendingPictureSearchData.content);
            $scope.getPendingPictureSearchData(1);
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
        if($scope.pendingPictureSearchData.content==""||$scope.pendingPictureSearchData.content==null){
            $scope.getPendingPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.pendingPictureSearchData.content);
            $scope.getPendingPictureSearchData(1);
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
        if($scope.pendingPictureSearchData.content==""||$scope.pendingPictureSearchData.content==null){
            $scope.getPendingPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.pendingPictureSearchData.content);
            $scope.getPendingPictureSearchData(1);
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
        if($scope.pendingPictureSearchData.content==""||$scope.pendingPictureSearchData.content==null){
            $scope.getPendingPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.pendingPictureSearchData.content);
            $scope.getPendingPictureSearchData(1);
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
    $scope.checkNum=function(str)
    {
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
    $scope.goNewPictureArticle=function(articleId)
    {
        $scope.showPendingPictureArticle(articleId);
        document.getElementById("pendingPicture").className="tab-pane";
        document.getElementById("pendingPictureView").className="tab-pane active";
        document.getElementById("pendingPictureSidebarID").className="sidebar-list";
        if($scope.articleData!={}){
            $scope.closeOver();
        }
    };

    //得到文章的URL
    $scope.getPendingPictureArticleUrl=function(articleId)
    {
        var url=$scope.projectName+"/picture/Pending/"+($scope.pendingPictureData.currentNo).toString()+"/"+articleId;
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
    $scope.showPendingPictureArticle=function(articleId)
    {
        var url=$scope.getPendingPictureArticleUrl(articleId);

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
        var arr=$scope.pendingPictureData.tileList;
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
        $scope.getPendingPictureData($scope.pendingPictureData.currentNo);
    }
    //对选取的文章进行操作
    //删除-------------
    $scope.deletePictureArticleSelections=function()
    {
        $scope.coverIt();
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
            $scope.closeOver();
        }else{
//            if (confirm("确定撤销选中的文章吗？")==true)
//            {
                var url=$scope.projectName+"/picture/Pending/"+($scope.pendingPictureData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.refreshPendingPictureCur();
                    alert("撤销成功");
                    $scope.closeOver();
                });
//            }
        }
    };
//立刻发布--------------------------------------------------------------------------------------------------------
    $scope.publishPictureArticleSelections=function()
    {
        $scope.coverIt();
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
            $scope.closeOver();
        }else{
            var url=$scope.projectName+"/picture/Pending/"+($scope.pendingPictureData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.refreshPendingPictureCur();
                alert("发布成功");
                $scope.closeOver();
            });
        }
    };
    //定时发布----------------------------------------------------------------------------------------------------------
    $scope.publishPictureArticleSelectionsTiming=function()
    {
        $scope.coverIt();
        var myDate=new Date();
        var myDateTime=myDate.getTime();
        var str1=$scope.publishTimePicture.substr(0,10);
        var str2=$scope.publishTimePicture.substr(11,16);
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
            var url=$scope.projectName+"/picture/Pending/"+($scope.pendingPictureData.currentNo).toString()+"/timingpublish/"+$scope.articleSelectionsUrl+"/"+time;
            console.log(url);
            $http.get(url).success(function(){
                alert("定时成功");
                $('#Select_Time_picture').modal('toggle');
                clearArticleSelections();
                $scope.refreshPendingPictureCur();
                $scope.closeOver();
            });
        }
    };
    $scope.checkModifyNoteInPendingPicture=function(id)
    {
        $scope.coverIt();
        var url=$scope.projectName+"/article/Pending/"+($scope.pendingPictureData.currentNo).toString()+"/"+id+"/log";
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
        $scope.getPendingPictureData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.pendingPictureData.currentNo);
    };
}]);