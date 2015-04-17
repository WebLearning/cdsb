
angular.module("Dashboard").controller("publishedPicCommentDetailsCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.addReplyCommentIdInPublishedPic="";

    $scope.testLog=function()
    {
        console.log($scope.commentDetailDataInPublishedPic);
        console.log($scope.commentDetailsUrlInPublishedPicFor);
        console.log($scope.commentSelectionsInPubPic);
        console.log($scope.commentSelectionsUrlInPubPic);
    };

    $scope.refreshCommentDetailsInPubPic=function()
    {
        clearArticleSelections();
//        $scope.orderCondition="";
        $scope.transOrderConditions("");
        $scope.getCommentDetailDataInPublishedPic(1);
    };
    $scope.goPublishedPicFromCommentDetail=function()
    {
        document.getElementById("publishedPicture").className="tab-pane active";
        document.getElementById("publishedPicCommentDetail").className="tab-pane";
//        $scope.refreshComment();
        clearArticleSelections();
//        $scope.commentDetailDataInPublishedPic="";
        $scope.refreshPublishedPictureCur();
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
        $scope.addReplyCommentIdInPublishedPic=commentId;

        if(str==""||str==null){
            return "btn btn-xs btn-success";
        }else{
            return "btn btn-xs btn-success sr-only";
        }
    };
    $scope.addReplyUrlInPublishedPic="";
    $scope.replyTest=function(commentId)
    {
        console.log("reply test");
        console.log(commentId);
        console.log($scope.commentDetailsUrlInPublishedPicFor);
        $scope.addReplyUrlInPublishedPic=$scope.commentDetailsUrlInPublishedPicFor+'/'+commentId;
        console.log($scope.addReplyUrlInPublishedPic);
    };
    $scope.replyDataInPublishedPic={
        reply:""
    };
    $scope.addReplyInPubPic=function(){
        var url=$scope.addReplyUrlInPublishedPic;
//        console.log(url);
        var jsonString=JSON.stringify($scope.replyDataInPublishedPic);
//        console.log(jsonString);
        $http.post(url,jsonString).success(function(data){
            console.log("添加成功");
            $scope.getCommentDetailDataInPublishedPic($scope.commentDetailDataInPublishedPic.currentNo);
            $('#myModal_addReplyInPubPic').modal('toggle');
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
        $scope.getCommentDetailDataInPublishedPic(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.commentDetailDataInPublishedPic.currentNo);
    };



    //评论的选取和操作------------------------------------------------------------------------------------------------------
    //评论的选取
    $scope.commentSelectionsInPubPic=[];
    $scope.commentSelectionsUrlInPubPic="";

    $scope.selectArticle=function(commendId,selectState)
    {
        if(!selectState){
            $scope.commentSelectionsInPubPic.push(commendId);
        }else{
            var index=$scope.commentSelectionsInPubPic.indexOf(commendId);
            $scope.commentSelectionsInPubPic.splice(index,1);
        }
//        console.log($scope.commentSelectionsInPubPic);

        if($scope.commentSelectionsInPubPic.length>0){
            var str="";
            for(i=0;i<$scope.commentSelectionsInPubPic.length;i++){
                str+=($scope.commentSelectionsInPubPic[i]+"_");
                $scope.commentSelectionsUrlInPubPic=str.substr(0,str.length-1);
            }
        }else{
            $scope.commentSelectionsUrlInPubPic="";
        }
//        console.log($scope.commentSelectionsUrlInPubPic);
    };

    $scope.checkSelectState=function(commendId)
    {
        if($scope.commentSelectionsInPubPic.length>0){
            for(i=0;i<$scope.commentSelectionsInPubPic.length;i++){
                if(commendId==$scope.commentSelectionsInPubPic[i]){
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
        $scope.commentSelectionsInPubPic=[];
        $scope.commentSelectionsUrlInPubPic="";
    }

    var allSelectState="none";
    $scope.selectAll=function()
    {
        var arr=$scope.commentDetailDataInPublishedPic.commendList;
        console.log($scope.commentDetailDataInPublishedPic.commendList);
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
                $scope.commentSelectionsInPubPic.push(arr[i].commendId);
            }
        }else{
            $scope.commentSelectionsInPubPic=[];
        }
        if($scope.commentSelectionsInPubPic.length>0){
            var str="";
            for(i=0;i<$scope.commentSelectionsInPubPic.length;i++){
                str+=($scope.commentSelectionsInPubPic[i]+"_");
                $scope.commentSelectionsUrlInPubPic=str.substr(0,str.length-1);
            }
        }else{
            $scope.commentSelectionsUrlInPubPic="";
        }
        $scope.getCommentDetailDataInPublishedPic($scope.commentDetailDataInPublishedPic.currentNo);
    }
    //对选取的文章进行操作
    $scope.deleteCommentSelectionsInPubPic=function()
    {
        if($scope.commentSelectionsUrlInPubPic==""){
            alert("未选取评论");
        }else{
//            if (confirm("确定删除选中的评论吗？")==true)
//            {
                var url=$scope.commentDetailsUrlInPublishedPicFor+'/'+$scope.commentSelectionsUrlInPubPic;
                console.log(url);
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.getCommentDetailDataInPublishedPic($scope.commentDetailDataInPublishedPic.currentNo);
                    alert("删除成功");
                });
//            }
        }
    };

    $scope.publishCommentSelectionsInPubPic=function()
    {
        if($scope.commentSelectionsUrlInPubPic==""){
            alert("未选取评论");
        }else{
            var url=$scope.commentDetailsUrlInPublishedPicFor+'/'+$scope.commentSelectionsUrlInPubPic;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.getCommentDetailDataInPublishedPic(1);
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
            $scope.getCommentDetailDataInPublishedPic(1);
            $scope.closeOver();
        }else if(orderFromState=="asc"){
            $scope.transOrderConditions("/from/desc");
            orderFromState="desc";
            $scope.getCommentDetailDataInPublishedPic(1);
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
        $scope.getCommentDetailDataInPublishedPic(1);
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
        $scope.getCommentDetailDataInPublishedPic(1);
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
        $scope.getCommentDetailDataInPublishedPic(1);
        $scope.closeOver();
    };

    //对一篇文章新建评论------------------------------------------------------------------------------------------------
    $scope.inputCommentDataInPubPic={
        commendId:"2",
        userName:"",
        userId:34,
        timeDate:"",
        level:33,
        state:null,
        from:"",
        content:"",
        reply:""
    };
    $scope.getCurrentDatetime=function()
    {
        $scope.inputCommentDataInPubPic.timeDate=new Date();
    };
    $scope.testInputCommentData=function(){
        console.log($scope.inputCommentDataInPubPic);
    };
    $scope.addCommentsInPubPic=function(){
        var url=$scope.commentDetailsUrlInPublishedPicFor;
        console.log(url);
        var jsonString=JSON.stringify($scope.inputCommentDataInPubPic);
        console.log($scope.inputCommentDataInPubPic);
        console.log(jsonString);
//        console.log($scope.commentDetailDataInPublishedPic.currentNo);
        $http.post(url,jsonString).success(function(data){
            console.log("添加成功");
            console.log($scope.commentDetailDataInPublishedPic.currentNo);
            $('#myModal_addCommentInPubPic').modal('toggle');
            //$scope.getCommentDetailDataInPublishedPic(1);
            if($scope.commentDetailDataInPublishedPic.currentNo==0){
                $scope.getCommentDetailDataInPublishedPic(1);
            }else{
                $scope.getCommentDetailDataInPublishedPic($scope.commentDetailDataInPublishedPic.currentNo);
            }
        });
    };
}]);

