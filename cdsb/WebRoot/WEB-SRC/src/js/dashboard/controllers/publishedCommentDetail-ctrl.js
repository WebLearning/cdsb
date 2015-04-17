
angular.module("Dashboard").controller("publishedCommentDetailsCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.addReplyCommentIdInPublished="";

    $scope.testLog=function()
    {
        console.log($scope.commentDetailDataInPublished);
        console.log($scope.commentDetailsUrlInPublishedFor);
        console.log($scope.commentSelectionsInPub);
        console.log($scope.commentSelectionsUrlInPub);
    };

    $scope.refreshCommentDetailsInPub=function()
    {
        clearArticleSelections();
//        $scope.orderCondition="";
        $scope.transOrderConditions("");
        $scope.getCommentDetailDataInPublished(1);
    };
    $scope.goPublishedFromCommentDetail=function()
    {
        document.getElementById("published").className="tab-pane active";
        document.getElementById("publishedCommentDetail").className="tab-pane";
        clearArticleSelections();
//        $scope.refreshComment();
//        $scope.commentDetailDataInPublished="";
//        $scope.clearCommentDetailInPub();
        $scope.refreshPublishedCur();
    };

    //检查表的内容 数据若是NULL则显示"无",数组若是空则显示"无数据",转化时间戳为日期显示
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
    $scope.checkState=function(str){
        var checkedState;
        if(str==null||str==""){
            checkedState="无";
        }else if(str=="published"){
            checkedState="已发布";
        }else if(str=="unpublished"){
            checkedState="未发布";
        }
        return checkedState;
    };



    $scope.replyBtnStr=function(str,commentId)
    {
        $scope.addReplyCommentIdInPublished=commentId;

        if(str==""||str==null){
            return "btn btn-xs btn-success";
        }else{
            return "btn btn-xs btn-success sr-only";
        }
    };
    $scope.addReplyUrlInPublished="";
    $scope.replyTest=function(commentId)
    {
        console.log("reply test");
        console.log(commentId);
        console.log($scope.commentDetailsUrlInPublishedFor);
        $scope.addReplyUrlInPublished=$scope.commentDetailsUrlInPublishedFor+'/'+commentId;
        console.log($scope.addReplyUrlInPublished);
    };
    $scope.replyDataInPublished={
        reply:""
    };
    $scope.addReplyInPub=function(){
        var url=$scope.addReplyUrlInPublished;
//        console.log(url);
        var jsonString=JSON.stringify($scope.replyDataInPublished);
//        console.log(jsonString);
        $http.post(url,jsonString).success(function(data){
            console.log("添加成功");
            $scope.getCommentDetailDataInPublished($scope.commentDetailDataInPublished.currentNo);
            $('#myModal_addReplyInPub').modal('toggle');
        });
//
//        $('#myModal_addReply').modal('toggle');
//        $scope.refreshCommentDetails();
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


    //返回评论列表--------------------------------------------------------------------------------------------------------
//    $scope.goCommentList=function()
//    {
//        document.getElementById("comment").className="tab-pane active";
//        document.getElementById("commentDetails").className="tab-pane";
//        $scope.refreshComment();
//    };


    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getCommentDetailDataInPublished(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.commentDetailDataInPublished.currentNo);
    };



    //评论的选取和操作------------------------------------------------------------------------------------------------------
    //评论的选取
    $scope.commentSelectionsInPub=[];
    $scope.commentSelectionsUrlInPub="";

    $scope.selectArticle=function(commendId,selectState)
    {
        if(!selectState){
            $scope.commentSelectionsInPub.push(commendId);
        }else{
            var index=$scope.commentSelectionsInPub.indexOf(commendId);
            $scope.commentSelectionsInPub.splice(index,1);
        }
//        console.log($scope.commentSelectionsInPub);

        if($scope.commentSelectionsInPub.length>0){
            var str="";
            for(i=0;i<$scope.commentSelectionsInPub.length;i++){
                str+=($scope.commentSelectionsInPub[i]+"_");
                $scope.commentSelectionsUrlInPub=str.substr(0,str.length-1);
            }
        }else{
            $scope.commentSelectionsUrlInPub="";
        }
//        console.log($scope.commentSelectionsUrlInPub);
    };

    $scope.checkSelectState=function(commendId)
    {
        if($scope.commentSelectionsInPub.length>0){
            for(i=0;i<$scope.commentSelectionsInPub.length;i++){
                if(commendId==$scope.commentSelectionsInPub[i]){
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
        $scope.commentSelectionsInPub=[];
        $scope.commentSelectionsUrlInPub="";
    }

    var allSelectState="none";
    $scope.selectAll=function()
    {
        var arr=$scope.commentDetailDataInPublished.commendList;
        console.log($scope.commentDetailDataInPublished.commendList);
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
                $scope.commentSelectionsInPub.push(arr[i].commendId);
            }
        }else{
            $scope.commentSelectionsInPub=[];
        }
        if($scope.commentSelectionsInPub.length>0){
            var str="";
            for(i=0;i<$scope.commentSelectionsInPub.length;i++){
                str+=($scope.commentSelectionsInPub[i]+"_");
                $scope.commentSelectionsUrlInPub=str.substr(0,str.length-1);
            }
        }else{
            $scope.commentSelectionsUrlInPub="";
        }
        $scope.getCommentDetailDataInPublished($scope.commentDetailDataInPublished.currentNo);
    }
    //对选取的文章进行操作
    $scope.deleteCommentSelections=function()
    {
        if($scope.commentSelectionsUrlInPub==""){
            alert("未选取评论");
        }else{
//            if (confirm("确定删除选中的评论吗？")==true)
//            {
                var url=$scope.commentDetailsUrlInPublishedFor+'/'+$scope.commentSelectionsUrlInPub;
                console.log(url);
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.getCommentDetailDataInPublished($scope.commentDetailDataInPublished.currentNo);
                    alert("删除成功");
                });
//            }
        }
    };

    $scope.publishCommentSelections=function()
    {
        if($scope.commentSelectionsUrlInPub==""){
            alert("未选取评论");
        }else{
            var url=$scope.commentDetailsUrlInPublishedFor+'/'+$scope.commentSelectionsUrlInPub;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.getCommentDetailDataInPublished(1);
                alert("发布成功");
            });
        }
    };

    //排序---------------------------------------------------------------------------------------------------------------
    var orderFromState="desc";
    $scope.orderByFrom=function(){
        $scope.coverIt();
        if(orderFromState=="desc"){
            $scope.transOrderConditions("/from/asc");
            orderFromState="asc";
            $scope.getCommentDetailDataInPublished(1);
            $scope.closeOver();
        }else if(orderFromState=="asc"){
            $scope.transOrderConditions("/from/desc");
            orderFromState="desc";
            $scope.getCommentDetailDataInPublished(1);
            $scope.closeOver();
        }
    };
    var orderLevelState="desc";
    $scope.orderByLevel=function(){
        $scope.coverIt();
        if(orderLevelState=="desc"){
            $scope.transOrderConditions("/level/asc");
            orderLevelState="asc";
        }else if(orderLevelState=="asc"){
            $scope.transOrderConditions("/level/desc");
            orderLevelState="desc";
        }
        $scope.getCommentDetailDataInPublished(1);
        $scope.closeOver();
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
        $scope.getCommentDetailDataInPublished(1);
        $scope.closeOver();
    };
    var orderStateState="desc";
    $scope.orderByState=function(){
        $scope.coverIt();
        if(orderStateState=="desc"){
            $scope.transOrderConditions("/state/asc");
            orderStateState="asc";
        }else if(orderStateState=="asc"){
            $scope.transOrderConditions("/state/desc");
            orderStateState="desc";
        }
        $scope.getCommentDetailDataInPublished(1);
        $scope.closeOver();
    };

    //对一篇文章新建评论------------------------------------------------------------------------------------------------
    $scope.inputCommentDataInPub={
        commendId:"2",
        userName:"",
        userId:77,
        timeDate:"",
        level:77,
        state:null,
        from:"",
        content:"",
        reply:""
    };
    $scope.getCurrentDatetime=function()
    {
        $scope.inputCommentDataInPub.timeDate=new Date();
    };
    $scope.testInputCommentData=function(){
        console.log($scope.inputCommentDataInPub);
    };
    $scope.addCommentsInPub=function(){
        var url=$scope.commentDetailsUrlInPublishedFor;
        console.log(url);
        var jsonString=JSON.stringify($scope.inputCommentDataInPub);
        console.log($scope.inputCommentDataInPub);
        console.log(jsonString);
//        console.log($scope.commentDetailDataInPublished.currentNo);
        $http.post(url,jsonString).success(function(data){
            console.log("添加成功");
            console.log($scope.commentDetailDataInPublished.currentNo);
            $('#myModal_addCommentInPub').modal('toggle');
            //$scope.getCommentDetailDataInPublished(1);
            if($scope.commentDetailDataInPublished.currentNo==0){
                $scope.getCommentDetailDataInPublished(1);
            }else{
                $scope.getCommentDetailDataInPublished($scope.commentDetailDataInPublished.currentNo);
            }
        });
    };
}]);

