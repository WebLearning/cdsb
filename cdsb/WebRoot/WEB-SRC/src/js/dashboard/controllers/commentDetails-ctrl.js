
angular.module("Dashboard").controller("commentDetailsCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.addReplyCommentId="";

    $scope.testLog=function()
    {
        console.log($scope.commentDetailData);
        console.log($scope.commentDetailsUrlFor);
        console.log($scope.commentSelections);
        console.log($scope.commentSelectionsUrl);
    };

    $scope.refreshCommentDetails=function()
    {
        clearArticleSelections();
//        $scope.orderCondition="";
        $scope.transOrderCondition("");
        $scope.getCommentDetailData(1);
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
        $scope.addReplyCommentId=commentId;

        if(str==""||str==null){
            return "btn btn-xs btn-success";
        }else{
            return "btn btn-xs btn-success sr-only";
        }
    };
    $scope.addReplyUrl="";
    $scope.replyTest=function(commentId)
    {
        console.log("reply test");
        console.log(commentId);
        console.log($scope.commentDetailsUrlFor);
        $scope.addReplyUrl=$scope.commentDetailsUrlFor+'/'+commentId;
        console.log($scope.addReplyUrl);
    };
    $scope.replyData={
        reply:""
    };
    $scope.addReply=function(){
        var url=$scope.addReplyUrl;
//        console.log(url);
        var jsonString=JSON.stringify($scope.replyData);
//        console.log(jsonString);
        $http.post(url,jsonString).success(function(data){
            console.log("添加成功");
            $scope.getCommentDetailData($scope.commentDetailData.currentNo);
            $('#myModal_addReply').modal('toggle');
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
        $scope.getCommentDetailData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.commentDetailData.currentNo);
    };



    //评论的选取和操作------------------------------------------------------------------------------------------------------
    //评论的选取
    $scope.commentSelections=[];
    $scope.commentSelectionsUrl="";

    $scope.selectArticle=function(commendId,selectState)
    {
        if(!selectState){
            $scope.commentSelections.push(commendId);
        }else{
            var index=$scope.commentSelections.indexOf(commendId);
            $scope.commentSelections.splice(index,1);
        }
//        console.log($scope.commentSelections);

        if($scope.commentSelections.length>0){
            var str="";
            for(i=0;i<$scope.commentSelections.length;i++){
                str+=($scope.commentSelections[i]+"_");
                $scope.commentSelectionsUrl=str.substr(0,str.length-1);
            }
        }else{
            $scope.commentSelectionsUrl="";
        }
//        console.log($scope.commentSelectionsUrl);
    };

    $scope.checkSelectState=function(commendId)
    {
        if($scope.commentSelections.length>0){
            for(i=0;i<$scope.commentSelections.length;i++){
                if(commendId==$scope.commentSelections[i]){
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
        $scope.commentSelections=[];
        $scope.commentSelectionsUrl="";
    }

    var allSelectState="none";
    $scope.selectAll=function()
    {
        var arr=$scope.commentDetailData.commendList;
        console.log($scope.commentDetailData.commendList);
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
                $scope.commentSelections.push(arr[i].commendId);
            }
        }else{
            $scope.commentSelections=[];
        }
        if($scope.commentSelections.length>0){
            var str="";
            for(i=0;i<$scope.commentSelections.length;i++){
                str+=($scope.commentSelections[i]+"_");
                $scope.commentSelectionsUrl=str.substr(0,str.length-1);
            }
        }else{
            $scope.commentSelectionsUrl="";
        }
        $scope.getCommentDetailData($scope.commentDetailData.currentNo);
    }
    //对选取的文章进行操作
    $scope.deleteCommentSelections=function()
    {
        if($scope.commentSelectionsUrl==""){
            alert("未选取评论");
        }else{
//            if (confirm("确定删除选中的评论吗？")==true)
//            {
                var url=$scope.commentDetailsUrlFor+'/'+$scope.commentSelectionsUrl;
                console.log(url);
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.getCommentDetailData(1);
                    alert("删除成功");
                });
//            }
        }
    };

    $scope.publishCommentSelections=function()
    {
        if($scope.commentSelectionsUrl==""){
            alert("未选取评论");
        }else{
            var url=$scope.commentDetailsUrlFor+'/'+$scope.commentSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.getCommentDetailData(1);
                alert("发布成功");
            });
        }
    };

    //排序---------------------------------------------------------------------------------------------------------------
    var orderFromState="desc";
    $scope.orderByFrom=function(){
        $scope.coverIt();
        if(orderFromState=="desc"){
            $scope.transOrderCondition("/from/"+"asc");
            orderFromState="asc";
            $scope.getCommentDetailData(1);
            $scope.closeOver();
        }else if(orderFromState=="asc"){
            $scope.transOrderCondition("/from/"+"desc");
            orderFromState="desc";
            $scope.getCommentDetailData(1);
            $scope.closeOver();
        }
    };
    var orderLevelState="desc";
    $scope.orderByLevel=function(){
        $scope.coverIt();
        if(orderLevelState=="desc"){
            $scope.transOrderCondition("/level/asc");
            orderLevelState="asc";
        }else if(orderLevelState=="asc"){
            $scope.transOrderCondition("/level/desc");
            orderLevelState="desc";
        }
        $scope.getCommentDetailData(1);
        $scope.closeOver();
    };

    var timeOrderState="desc";
    $scope.orderByTime=function(){
        $scope.coverIt();
        if(timeOrderState=="desc"){
            $scope.transOrderCondition("/time/asc");
            timeOrderState="asc";
        }else if(timeOrderState=="asc"){
            $scope.transOrderCondition("/time/desc");
            timeOrderState="desc";
        }
        $scope.getCommentDetailData(1);
        $scope.closeOver();
    };
    var orderStateState="desc";
    $scope.orderByState=function(){
        $scope.coverIt();
        if(orderStateState=="desc"){
            $scope.transOrderCondition("/state/asc");
            orderStateState="asc";
        }else if(orderStateState=="asc"){
            $scope.transOrderCondition("/state/desc");
            orderStateState="desc";
        }
        $scope.getCommentDetailData(1);
        $scope.closeOver();
    };

    //对一篇文章新建评论------------------------------------------------------------------------------------------------
    $scope.inputCommentData={
        commendId:"2",
        userName:"",
        userId:77,
        timeDate:"",
        level:60,
        state:null,
        from:"",
        content:"",
        reply:""
    };
    $scope.getCurrentDatetime=function()
    {
        $scope.inputCommentData.timeDate=new Date();
    };
    $scope.testInputCommentData=function(){
        console.log($scope.inputCommentData);
    };
    $scope.addComments=function(){
        var url=$scope.commentDetailsUrlFor;
        console.log(url);
        var jsonString=JSON.stringify($scope.inputCommentData);
        console.log($scope.inputCommentData);
        console.log(jsonString);
//        console.log($scope.commentDetailData.currentNo);
        $http.post(url,jsonString).success(function(data){
            console.log("添加成功");
            console.log($scope.commentDetailData.currentNo);
            $('#myModal_addComment').modal('toggle');
            //$scope.getCommentDetailData(1);
            if($scope.commentDetailData.currentNo==0){
                $scope.getCommentDetailData(1);
            }else{
                $scope.getCommentDetailData($scope.commentDetailData.currentNo);
            }
        });
    };
}]);

