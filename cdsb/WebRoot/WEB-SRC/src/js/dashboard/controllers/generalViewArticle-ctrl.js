angular.module("Dashboard").controller("generalViewArticleCtrl", ["$scope","$http", function ($scope,$http) {

    //设置预览url
    $scope.setYulanInGeneral=function(id,content){
        console.log(id);
        console.log($scope.articleData.id);
        if(content==""){
            alert("内容为空，不可预览！");
            var iFrameElem1 = document.getElementById('iframe_yulanInGeneralAr');
            iFrameElem1.src="";
            $('#yulan_generalArticle').modal('toggle');
        }else{
            var iFrameElem = document.getElementById('iframe_yulanInGeneralAr');
            iFrameElem.src=$scope.projectName+"/app/ios/articledetail/"+id;
        }
    };
    $scope.goGeneral=function()
    {
        $scope.clearArticle();
        $scope.coverIt();
        document.getElementById("generalViewArticle").className="tab-pane";
        document.getElementById("generalView").className="tab-pane active";
        document.getElementById("generalSidebarID").className="sidebar-list";
        $scope.getNewGeneralViewData();
        $scope.closeOver();
    };
    $scope.clearArticle=function()
    {
        for(p in $scope.articleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"||p=="logs"){
                $scope.articleData[p]=[];
            }else if(p=="author"||p=="content"||p=="from"||p=="subTitle"||p=="time"||p=="title"||p=="summary"||p=="activity"||p=="outSideUrl"){
                $scope.articleData[p]="";
            }else{
                $scope.articleData[p]=null;
            }
        }
//        $scope.calculateWords();
//        $scope.calculatePictures();
    };
}]);