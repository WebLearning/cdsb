/**
 * Created by QK on 2014/12/4.
 */

angular.module("Dashboard").controller("commentCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.testLog=function(){
        console.log($scope.commentData);
    };

    $scope.addComment=function()
    {
        var testCommentData={
            commendId:"2",
            userName:"",
            userId:77,
            timeDate:new Date(),
            level:77,
            state:"published",
            from:"home",
            content:"hello this is a test comment",
            reply:"I get it"
        };
        var url=$scope.projectName+'/commend/1/'+'7/'+'news';
        console.log(url);
        var jsonString=JSON.stringify(testCommentData);
        console.log(jsonString);
        $http.post(url,jsonString).success(function(data){
            console.log("添加成功");
        });
    };
    //检查表的内容 数据若是NULL则显示"无",状态转化成中文
    $scope.checkIfNull=function(str)
    {
        var checkedStr;
        if(str==null){
            checkedStr="无";
        }else{
            checkedStr=str;
        }
        return checkedStr;
    };
    $scope.checkAndTranslate=function(str)
    {
        var checkedStr;
        if(str==null){
            checkedStr="无";
        }else if(str=="Temp"){
            checkedStr="草稿";
        }else if(str=="Deleted"){
            checkedStr="已删除";
        }else if(str=="Pending"){
            checkedStr="待审";
        }else if(str=="Crawler"){
            checkedStr="爬虫";
        }else if(str=="Published"){
            checkedStr="已发布";
        }else if(str=="Revocation"){
            checkedStr="已撤销"
        }else{
            checkedStr=str;
        }
        return checkedStr;
    };

    //文章评论跳转------------------------------------------------------------------------------------------------------------


    //页面跳转----------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getCommentData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.commentData.currentNo);
    };
    $scope.checkNextPageNumClass=function(pageNum){
        if(pageNum+1>$scope.commentData.pageCount){
            return "disabled";
        }
    };

    //排序---------------------------------------------------------------------------------------------------------------
    var orderState="desc";
    $scope.orderByState=function(){
        $scope.coverIt();
        if(orderState=="desc"){
//            $scope.orderCondition="/state";
            $scope.transOrderConditions("/state/asc");
            orderState="asc";
        }else if(orderState=="asc"){
            $scope.transOrderConditions("/state/desc");
            orderState="desc";
//            $scope.getCommentData(1);
        }
        $scope.getCommentData(1);
        $scope.closeOver();
    };
    var orderNewsCommendsPublish="desc";
    $scope.orderByNewsCommendsPublish=function(){
        $scope.coverIt();
        if(orderNewsCommendsPublish=="desc"){
//            $scope.orderCondition="/newsCommends";
            $scope.transOrderConditions("/newsCommendsPublish/asc");
            orderNewsCommendsPublish="asc";
//            $scope.getCommentData(1);
        }else if(orderNewsCommendsPublish=="asc"){
//            $scope.orderCondition="";
            $scope.transOrderConditions("/newsCommendsPublish/desc");
            orderNewsCommendsPublish="desc";
        }
        $scope.getCommentData(1);
        $scope.closeOver();
    };
    var orderNewsCommendsUnPublish="desc";
    $scope.orderByNewsCommendsUnPublish=function(){
        $scope.coverIt();
        if(orderNewsCommendsUnPublish=="desc"){
//            $scope.orderCondition="/newsCommends";
            $scope.transOrderConditions("/newsCommendsUnpublish/asc");
            orderNewsCommendsUnPublish="asc";
//            $scope.getCommentData(1);
        }else if(orderNewsCommendsUnPublish=="asc"){
//            $scope.orderCondition="";
            $scope.transOrderConditions("/newsCommendsUnpublish/desc");
            orderNewsCommendsUnPublish="desc";
        }
        $scope.getCommentData(1);
        $scope.closeOver();
    };
    var orderCrawlerCommendsPublish="desc";
    $scope.orderByCrawlerCommendsPublish=function(){
        $scope.coverIt();
        if(orderCrawlerCommendsPublish=="desc"){
//            $scope.orderCondition="/crawlerCommends";
            $scope.transOrderConditions("/crawlerCommendsPublish/asc");
            orderCrawlerCommendsPublish="asc";
//            $scope.getCommentData(1);
        }else if(orderCrawlerCommendsPublish=="asc"){
//            $scope.orderCondition="";
            $scope.transOrderConditions("/crawlerCommendsPublish/desc");
            orderCrawlerCommendsPublish="desc";
//            $scope.getCommentData(1);
        }
        $scope.getCommentData(1);
        $scope.closeOver();
    };
    var orderCrawlerCommendsUnPublish="desc";
    $scope.orderByCrawlerCommendsUnPublish=function(){
        $scope.coverIt();
        if(orderCrawlerCommendsUnPublish=="desc"){
//            $scope.orderCondition="/crawlerCommends";
            $scope.transOrderConditions("/crawlerCommendsUnpublish/asc");
            orderCrawlerCommendsUnPublish="asc";
//            $scope.getCommentData(1);
        }else if(orderCrawlerCommendsUnPublish=="asc"){
//            $scope.orderCondition="";
            $scope.transOrderConditions("/crawlerCommendsUnpublish/desc");
            orderCrawlerCommendsUnPublish="desc";
//            $scope.getCommentData(1);
        }
        $scope.getCommentData(1);
        $scope.closeOver();
    };
}]);


