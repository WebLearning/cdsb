angular.module("Dashboard").controller("commentPagesCtrl", ["$scope","$http", function ($scope,$http) {

    //页面，获取评论第一页的数据,返回的是一个titleList-------------------------------------------------------------------
    $scope.orderCondition="";
    $scope.pageNums="";
    $scope.currentPage="";
    $scope.getCommentDetailData=function(pageID)
    {
        var url=commentDetailsUrl+"/"+pageID.toString()+$scope.orderCondition;
//        console.log(url);
        $http.get(url).success(function(data){
//            if(data.length>0){
                $scope.commentDetailData=data;
                $scope.pageNums=getPageNums($scope.commentDetailData.pageCount);
                $scope.currentPage=$scope.commentDetailData.currentNo;
                setPageInComment($scope.commentDetailData.pageCount,$scope.commentDetailData.currentNo);
//            console.log("成功获取数据");
//            }else{
//                $scope.commentDetailData="";
//                setPageInComment(1,1);
//            }
        });
    };
    function setPageInComment(count,pageIndex){
//        var container=container;//容器
        var count=count;//总页数
        var pageIndex=pageIndex;//当前页数
        var a=[];
        //总页数少于10全部显示，大于10显示前3，后3，中间3，其余...
        if(pageIndex==1){
            a[a.length]="<a href=\"#\" class=\"prev unclick\">&laquo;</a>";
        }else if(pageIndex==0){
            a[a.length]="<a href=\"#\" class=\"prev unclick\">暂无数据</a>";
        }
        else{
            a[a.length]="<a href=\"#\" class=\"prev\">&laquo;</a>";
        }
        function setPageList(){
            if (pageIndex == i) {
                a[a.length] = "<a href=\"#\" class=\"on\">" + i + "</a>";
            } else {
                a[a.length] = "<a href=\"#\">" + i + "</a>";
            }
        }
        //总页数小于10
        if (count <= 10) {
            for (var i = 1; i <= count; i++) {
                setPageList();
            }
        }
        //总页数大于10
        else{
            if(pageIndex<=4){
                for(var i=1;i<=5;i++){
                    setPageList();
                }
                a[a.length]=". . .<a href=\"#\">" + count + "</a>";
            }else if(pageIndex>=count-3){
                a[a.length]="<a href=\"#\">1</a>. . .";
                for(var i=count-4;i<=count;i++){
                    setPageList();
                }
            }else{//当前页在中间部分
                a[a.length]="<a href=\"#\">1</a>. . .";
                for(var i=pageIndex-2;i<=pageIndex+2;i++){
                    setPageList();
                }
                a[a.length]=". . .<a href=\"#\">" + count + "</a>";
            }
        }
        if(pageIndex==count&&pageIndex!=0){
            a[a.length]="<a href=\"#\" class=\"next unclick\">&raquo;</a>";
        }else if(pageIndex==0&&count==0){
            a[a.length]="<a href=\"#\" class=\"next unclick\"></a>";
        }
        else{
            a[a.length]="<a href=\"#\" class=\"next\">&raquo;</a>";
        }
        document.getElementById("commentDetail_page").innerHTML= a.join("");
        //事件点击
        var pageClick=function(){
            var oAlink=document.getElementById("commentDetail_page").getElementsByTagName("a");
            var inx=pageIndex;//初始页码
            oAlink[0].onclick=function(){//点击上一页
                if(inx==1){
                    return false;
                }
                inx--;
                setPageInComment(count,inx);
                $scope.getCommentDetailData(inx);
                return false;
            };
            for(var i=1;i<oAlink.length-1;i++){//点击页码
                oAlink[i].onclick=function(){
                    inx=parseInt(this.innerHTML);
                    setPageInComment(count,inx);
                    $scope.getCommentDetailData(inx);
                    return false;
                }
            }
            oAlink[oAlink.length-1].onclick=function(){//点击下一页
                if(inx==count){
                    return false;
                }
                inx++;
                setPageInComment(count,inx);
                $scope.getCommentDetailData(inx);
            }
        }()
    }

//    $scope.getCommentDetailData(1);

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

    $scope.getCommentDetailTitle=function(title,typeStr)
    {
        if(typeStr=="news"){
            $scope.commentDetailTitle=title+"_"+"商报评论";
//            console.log($scope.commentDetailTitle);
        }else if(typeStr=="crawler"){
            $scope.commentDetailTitle=title+"_"+"爬虫评论";
//            console.log($scope.commentDetailTitle);
        }
    };

    $scope.transOrderCondition=function(str){
        $scope.orderCondition=str;
        console.log($scope.orderCondition);
    };
    $scope.goCommentDetails=function()
    {
        document.getElementById("comment").className="tab-pane";
        document.getElementById("commentDetails").className="tab-pane active";
    };

    //将文章评论数据传输给全局变量commentDetailData
    $scope.transDataToCommentDetailData=function(data)
    {
        for(p in $scope.commentDetailData){
            if(p=="commendList"){
                for(i in data[p]){
                    $scope.commentDetailData[p][i]=data[p][i];
                }
            }else{
                $scope.commentDetailData[p]=data[p];
            }
        }
    };

    //点击显示文章评论明细
    $scope.publishIconStateInComment="";
    $scope.setPublishIconStateInComment=function(){
        if($scope.publishIconStateInComment=="publish"){
            return "btn btn-info sr-only";
        }else if($scope.publishIconStateInComment=="unpublish"){
            return "btn btn-info";
        }
    };
    $scope.showComments=function(articleId,title,type,stateType)
    {
        commentDetailsUrl=$scope.projectName+'/commend/1/'+articleId+'/'+type+'/'+stateType;
        $scope.commentDetailsUrlFor=$scope.projectName+'/commend/1/'+articleId+'/'+type;
        $scope.publishIconStateInComment=stateType;
        $scope.goCommentDetails();
        $scope.getCommentDetailData(1);
        $scope.getCommentDetailTitle(title,type);
    };
}]);