/**
 * Created by QK on 2014/12/4.
 */

angular.module("Dashboard").controller("tabContentCtrl", ["$scope","$http", function ($scope,$http) {

    /* $http.get('http://localhost:8080/Shangbao01/user/users')
     .success(function(data) {
     $scope.editorContent = data;
     }); */

    $scope.commentTitle1="文章0001";
    $scope.commentTitle2="文章0002";

    $scope.commentDetailTitle=null;

    $scope.goCommentDetails=function(commentTitle)
    {
        document.getElementById("comment").className="tab-pane";
        document.getElementById("commentDetails").className="tab-pane active";
        $scope.commentDetailTitle=commentTitle;
    };

    $scope.goCommentList=function()
    {
        document.getElementById("comment").className="tab-pane active";
        document.getElementById("commentDetails").className="tab-pane";
    };


}]);


