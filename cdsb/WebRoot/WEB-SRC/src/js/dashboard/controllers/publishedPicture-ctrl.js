/**
 publishedPictureCtrl
 **/
angular.module("Dashboard").controller("publishedPictureCtrl",["$scope","$http",function($scope,$http){

    $scope.testLog=function(){
        console.log($scope.publishedPictureData);
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
    //文章跳转,点击文章名（标题）在新建页面显示文章内容------------------------------------------------------------------------------------------------------------
    //转到新建文章页面并重置sidebar的爬虫文章按钮，不然会产生点击无效的BUG
    $scope.goNewPicture=function(articleId)
    {
        $scope.showPublishedArticle(articleId);
        document.getElementById("publishedPicture").className="tab-pane";
        document.getElementById("publishedPictureView").className="tab-pane active";
        document.getElementById("publishedPictureSidebarID").className="sidebar-list";
        if($scope.articleData!={}){
            $scope.closeOver();
        }
    };

    //得到文章的URL
    $scope.getPublishedArticleUrl=function(articleId)
    {
        var url=$scope.projectName+"/picture/Published/"+($scope.publishedPictureData.currentNo).toString()+"/"+articleId;
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
    $scope.showPublishedArticle=function(articleId)
    {
        var url=$scope.getPublishedArticleUrl(articleId);

        $scope.coverIt();
        $http.get(url).success(function(data) {
            $scope.transDataToArticleData(data);
        });
        $scope.transPicPubArtUrl(articleId);
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
        if($scope.publishedPictureSearchData.content==""||$scope.publishedPictureSearchData.content==null){
            $scope.getPublishedPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedPictureSearchData.content);
            $scope.getPublishedPictureSearchData(1);
            $scope.closeOver();
        }
    };

    var newsCommendsOrderStatePublished="desc";
    $scope.orderByNewsCommendsPublished=function(){
        $scope.coverIt();
        if(newsCommendsOrderStatePublished=="desc"){
            $scope.transOrderConditions("/newsCommendsPublish/asc");
            newsCommendsOrderStatePublished="asc";
        }else if(newsCommendsOrderStatePublished=="asc"){
            $scope.transOrderConditions("/newsCommendsPublish/desc");
            newsCommendsOrderStatePublished="desc";
        }
        if($scope.publishedPictureSearchData.content==""||$scope.publishedPictureSearchData.content==null){
            $scope.getPublishedPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedPictureSearchData.content);
            $scope.getPublishedPictureSearchData(1);
            $scope.closeOver();
        }
    };
    var newsCommendsOrderStateUnPublished="desc";
    $scope.orderByNewsCommendsUnPublished=function(){
        $scope.coverIt();
        if(newsCommendsOrderStateUnPublished=="desc"){
            $scope.transOrderConditions("/newsCommendsUnpublish/asc");
            newsCommendsOrderStateUnPublished="asc";
        }else if(newsCommendsOrderStateUnPublished=="asc"){
            $scope.transOrderConditions("/newsCommendsUnpublish/desc");
            newsCommendsOrderStateUnPublished="desc";
        }
        if($scope.publishedPictureSearchData.content==""||$scope.publishedPictureSearchData.content==null){
            $scope.getPublishedPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedPictureSearchData.content);
            $scope.getPublishedPictureSearchData(1);
            $scope.closeOver();
        }
    };
    var crawlerCommendsOrderStatePublished="desc";
    $scope.orderByCrawlerCommendsPublished=function(){
        $scope.coverIt();
        if(crawlerCommendsOrderStatePublished=="desc"){
            $scope.transOrderConditions("/crawlerCommendsPublish/asc");
            crawlerCommendsOrderStatePublished="asc";
        }else if(crawlerCommendsOrderStatePublished=="asc"){
            $scope.transOrderConditions("/crawlerCommendsPublish/desc");
            crawlerCommendsOrderStatePublished="desc";
        }
        if($scope.publishedPictureSearchData.content==""||$scope.publishedPictureSearchData.content==null){
            $scope.getPublishedPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedPictureSearchData.content);
            $scope.getPublishedPictureSearchData(1);
            $scope.closeOver();
        }
    };
    var crawlerCommendsOrderStateUnPublished="desc";
    $scope.orderByCrawlerCommendsUnPublished=function(){
        $scope.coverIt();
        if(crawlerCommendsOrderStateUnPublished=="desc"){
            $scope.transOrderConditions("/crawlerCommendsUnpublish/asc");
            crawlerCommendsOrderStateUnPublished="asc";
        }else if(crawlerCommendsOrderStateUnPublished=="asc"){
            $scope.transOrderConditions("/crawlerCommendsUnpublish/desc");
            crawlerCommendsOrderStateUnPublished="desc";
        }
        if($scope.publishedPictureSearchData.content==""||$scope.publishedPictureSearchData.content==null){
            $scope.getPublishedPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedPictureSearchData.content);
            $scope.getPublishedPictureSearchData(1);
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
        if($scope.publishedPictureSearchData.content==""||$scope.publishedPictureSearchData.content==null){
            $scope.getPublishedPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedPictureSearchData.content);
            $scope.getPublishedPictureSearchData(1);
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
        if($scope.publishedPictureSearchData.content==""||$scope.publishedPictureSearchData.content==null){
            $scope.getPublishedPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedPictureSearchData.content);
            $scope.getPublishedPictureSearchData(1);
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
        if($scope.publishedPictureSearchData.content==""||$scope.publishedPictureSearchData.content==null){
            $scope.getPublishedPictureData(1);
            $scope.closeOver();
        }else{
            console.log($scope.publishedPictureSearchData.content);
            $scope.getPublishedPictureSearchData(1);
            $scope.closeOver();
        }
    };

    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getPublishedPictureData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.publishedPictureData.currentNo);
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
        var arr=$scope.publishedPictureData.tileList;
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
        $scope.getPublishedPictureData($scope.publishedPictureData.currentNo);
    }
    //对选取的文章进行操作
    $scope.deletePictureArticleSelections=function()
    {
        $scope.coverIt();
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
        }else{
//            if (confirm("确定撤销选中的文章吗？")==true)
//            {
                var url=$scope.projectName+"/picture/Published/"+($scope.publishedPictureData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.refreshPublishedPictureCur();
                    alert("撤销成功");
                    $scope.closeOver();
                });
//            }
        }
    };
    //查看修改记录------------------------------------------------------------------------------------------------------
    $scope.checkModifyNoteInPublishedPicture=function(id)
    {
        $scope.coverIt();
        var url=$scope.projectName+"/article/Published/"+($scope.publishedPictureData.currentNo).toString()+"/"+id+"/log";
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