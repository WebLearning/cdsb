/**
 Published-ctrl
 **/
angular.module("Dashboard").controller("publishedCtrl",["$scope","$http",function($scope,$http){

    $scope.testLog=function(){
        console.log($scope.publishedData);
        console.log($scope.articleSelections);
        console.log($scope.articleSelectionsUrl);
    };
    //检查表的内容 数据若是NULL则显示"无",数组若是空则显示"无数据",转化时间戳为日期显示---------------------
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
    //文章跳转,点击文章名（标题）在新建页面显示文章内容------------------------------------------------------------------------------------------------------------
    //转到新建文章页面并重置sidebar的爬虫文章按钮，不然会产生点击无效的BUG
    $scope.goNewArticle=function(articleId)
    {
        $scope.showPublishedArticle(articleId);
        document.getElementById("published").className="tab-pane";
        document.getElementById("publishedArticle").className="tab-pane active";
        document.getElementById("publishedSidebarID").className="sidebar-list";
//        $scope.curArticleUrl=$scope.projectName+"/app/ios/articledetail/"+articleId;
//        console.log($scope.curArticleUrl);
        if($scope.articleData!={}){
            $scope.closeOver();
        }
//        $scope.closeOver();
    };

    //得到文章的URL
    $scope.getPublishedArticleUrl=function(articleId)
    {
        var url=$scope.projectName+"/article/Published/"+($scope.publishedData.currentNo).toString()+"/"+articleId;
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

//    $scope.setTime=function(b){
//        setTimeout(b,20000);
//    };
    //显示点击的文章
    $scope.showPublishedArticle=function(articleId)
    {
        var url=$scope.getPublishedArticleUrl(articleId);

//        $scope.maskLayer();
        $scope.coverIt();
        $http.get(url).success(function(data) {
            $scope.transDataToArticleData(data);
        });

        $scope.transPubArtUrl(articleId);
//        $scope.closeOver();
//        setTimeout($scope.closeOver(),10000);
    };

    //排序(作者，评论数等）---------------------------------------------------------------------------------------------------------------
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
        if($scope.publishedSearchData.content==""||$scope.publishedSearchData.content==null){
            $scope.getPublishedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedSearchData.content);
            $scope.getPublishedSearchData(1);
            $scope.closeOver();
        }
    };

    var newsCommendsPublishedOrderState="desc";
    $scope.orderByNewsCommendsPublished=function(){
        $scope.coverIt();
        if(newsCommendsPublishedOrderState=="desc"){
            $scope.transOrderConditions("/newsCommendsPublish/asc");
            newsCommendsPublishedOrderState="asc";
        }else if(newsCommendsPublishedOrderState=="asc"){
            $scope.transOrderConditions("/newsCommendsPublish/desc");
            newsCommendsPublishedOrderState="desc";
        }
        if($scope.publishedSearchData.content==""||$scope.publishedSearchData.content==null){
            $scope.getPublishedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedSearchData.content);
            $scope.getPublishedSearchData(1);
            $scope.closeOver();
        }
    };
    var newsCommendsUnPublishedOrderState="desc";
    $scope.orderByNewsCommendsUnPublished=function(){
        $scope.coverIt();
        if(newsCommendsUnPublishedOrderState=="desc"){
            $scope.transOrderConditions("/newsCommendsUnpublish/asc");
            newsCommendsUnPublishedOrderState="asc";
        }else if(newsCommendsUnPublishedOrderState=="asc"){
            $scope.transOrderConditions("/newsCommendsUnpublish/desc");
            newsCommendsUnPublishedOrderState="desc";
        }
        if($scope.publishedSearchData.content==""||$scope.publishedSearchData.content==null){
            $scope.getPublishedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedSearchData.content);
            $scope.getPublishedSearchData(1);
            $scope.closeOver();
        }
    };
    var crawlerCommendsPublishedOrderState="desc";
    $scope.orderByCrawlerCommendsPublished=function(){
        $scope.coverIt();
        if(crawlerCommendsPublishedOrderState=="desc"){
            $scope.transOrderConditions("/crawlerCommendsPublish/asc");
            crawlerCommendsPublishedOrderState="asc";
        }else if(crawlerCommendsPublishedOrderState=="asc"){
            $scope.transOrderConditions("/crawlerCommendsPublish/desc");
            crawlerCommendsPublishedOrderState="desc";
        }
        if($scope.publishedSearchData.content==""||$scope.publishedSearchData.content==null){
            $scope.getPublishedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedSearchData.content);
            $scope.getPublishedSearchData(1);
            $scope.closeOver();
        }
    };
    var crawlerCommendsUnPublishedOrderState="desc";
    $scope.orderByCrawlerCommendsUnPublished=function(){
        $scope.coverIt();
        if(crawlerCommendsUnPublishedOrderState=="desc"){
            $scope.transOrderConditions("/crawlerCommendsUnpublish/asc");
            crawlerCommendsUnPublishedOrderState="asc";
        }else if(crawlerCommendsUnPublishedOrderState=="asc"){
            $scope.transOrderConditions("/crawlerCommendsUnpublish/desc");
            crawlerCommendsUnPublishedOrderState="desc";
        }
        if($scope.publishedSearchData.content==""||$scope.publishedSearchData.content==null){
            $scope.getPublishedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedSearchData.content);
            $scope.getPublishedSearchData(1);
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
        if($scope.publishedSearchData.content==""||$scope.publishedSearchData.content==null){
            $scope.getPublishedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedSearchData.content);
            $scope.getPublishedSearchData(1);
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
        if($scope.publishedSearchData.content==""||$scope.publishedSearchData.content==null){
            $scope.getPublishedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedSearchData.content);
            $scope.getPublishedSearchData(1);
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
        if($scope.publishedSearchData.content==""||$scope.publishedSearchData.content==null){
            $scope.getPublishedData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedSearchData.content);
            $scope.getPublishedSearchData(1);
            $scope.closeOver();
        }
    };

    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getPublishedData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.publishedData.currentNo);
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
        var arr=$scope.publishedData.tileList;
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
        $scope.getPublishedData($scope.publishedData.currentNo);
    }
    //对选取的文章进行操作
    $scope.deleteArticleSelections=function()
    {
        $scope.coverIt();
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
            $scope.closeOver();
        }else{
//            if (confirm("确定撤销选中的文章吗？")==true)
//            {
                var url=$scope.projectName+"/article/Published/"+($scope.publishedData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.refreshPublishedCur();
                    alert("撤销成功");
                    $scope.closeOver();
                });
//            }
        }
    };
    //查看修改记录------------------------------------------------------------------------------------------------------
    $scope.checkModifyNote=function(id){
        $scope.coverIt();
        var url=$scope.projectName+"/article/Published/"+($scope.publishedData.currentNo).toString()+"/"+id+"/log";
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
}]);