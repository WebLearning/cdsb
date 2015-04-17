/**
 managementCtrl.js
 **/
angular.module("Dashboard").controller("managementCtrl",["$scope","$http",function($scope,$http){

    $scope.testLog=function()
    {
        console.log($scope.appNames);
        console.log($scope.modelNamesSet);
        console.log($scope.newChannelData);
        console.log($scope.newChannelDataState);
    };

    $scope.refreshManagePage=function(){
        $scope.getAppNames();
//        $scope.getManageActivityNames();
        $scope.checkNewChannelDataState();
    };

    //显示----------------------------------------------------------------------------------------------------------------
    $scope.appNames=[];
    $scope.modelNamesSet={};

    $scope.getAppNames=function(){
        var url=$scope.projectName+'/channel/channels';
        $http.get(url).success(function(data){
//            console.log(data);
            $scope.appNames=data;
            getModelNamesSet(data);
        });
    };
    $scope.getAppNames();

    function getModelNamesSet(data){
        $scope.modelNamesSet={};
        for(i=0;i<data.length;i++){
            $scope.addModelNames(data[i].englishName);
        }
    }

    $scope.addModelNames=function(channelName){
        var url=$scope.projectName+'/channel/'+channelName+'/channels';
        $http.get(url).success(function(data){
            $scope.modelNamesSet[channelName]=data;
        });
    };

    $scope.getModelNames=function(iModelNamesSet,englishName)
    {
        return iModelNamesSet[englishName];
    };

    //操作----------------------------------------------------------------------------------------------------------------

    //添加分类---------------------------------------------

    $scope.newChannelData={
        channelName:"", //栏目名称
        summary:"", //栏目介绍
        related:"", //（如果是顶级分类，为空；如果是子分类，填写父分类的channelName）
        englishName:"" // 栏目的英文名称
    };

    $scope.newChannelDataState="";
    $scope.relatedSelection_Class="form-group sr-only";

    $scope.checkNewChannelDataState=function()
    {
        //console.log($scope.newChannelDataState);
        if($scope.newChannelDataState=="下级分类"){
            $scope.relatedSelection_Class="form-group";
        }else{
            $scope.relatedSelection_Class="form-group sr-only";
            $scope.newChannelData.related="";
        }
    };

    $scope.addChannel=function()
    {
        $scope.coverIt();
        var url=$scope.projectName+'/channel/'+ulrfyChannelState($scope.newChannelDataState);//Father|Son|Activity
        //console.log(url);
        //console.log($scope.newChannelData);
        if(($scope.newChannelData.englishName).length!=0){
            reg=/^[a-zA-Z]+$/;
            if(!reg.test($scope.newChannelData.englishName)){
                alert("英文名称不符合规则，请重新输入！（只能输入字母且不含空格或者下划线）");
                $scope.closeOver();
            }else{
                $http.post(url,$scope.newChannelData).success(function(data){
                    if(data=="OK"){
                        alert("添加成功");
                        $('#myModal_newChannel').modal('toggle');
                        $scope.clearNewChannel();
                        $scope.refreshManagePage();
                        $scope.closeOver();
                    }else{
                        alert("添加失败");
                        $scope.closeOver();
                    }
                });
            }
        }else{
            alert("英文名称不能为空！");
            $scope.closeOver();
        }
    };

    function ulrfyChannelState(state){
        if(state=="顶级分类"){return "Father"}
        else if(state=="下级分类"){return "Son"}
        else if(state=="活动"){return "Activity"}
        else{alert("Error:no such channel state")}
    }

    $scope.clearNewChannel=function(){
        $scope.newChannelData={
            channelName:"", //栏目名称
            summary:"", //栏目介绍
            related:"", //（如果是顶级分类，为空；如果是子分类，填写父分类的channelName）
            englishName:"" // 栏目的英文名称
        };
        $scope.newChannelDataState="";
    };

    //删除分类---------------------------------------------

    $scope.deleteChannel=function(channelName,englishName,channelState)
    {
        $scope.coverIt();
        if(channelState=="Father"){
            if (confirm("确认删除该分类及其下级分类？")==true){
                reallyDeleteChannel(channelName,englishName,channelState);
            }
        }else if(channelState=="Son"){
            if (confirm("确认删除该分类？")==true){
                reallyDeleteChannel(channelName,englishName,channelState);
            }
        }

        function reallyDeleteChannel(channelName,englishName,channelState){
            var url=$scope.projectName+'/channel/delete/'+channelState;//Father|Son|Activity
            var channelInfo={};
            channelInfo['channelName']=channelName;
            channelInfo['englishName']=englishName;
            $http.post(url,channelInfo).success(function(data){
                if(data=="OK"){
//                    alert("删除成功");
                    $scope.refreshManagePage();
                    alert("删除成功");
                    $scope.closeOver();
                }else{
                    alert("删除失败");
                    $scope.closeOver();
                }
            });

        }

    };

    //移动分类---------------------------------------------

    $scope.upMove=function(father,state,index)
    {
        $scope.coverIt();
        if((index-1)==0){
            alert("已到达顶部");
            $scope.closeOver();
        }else{
            var url=$scope.projectName+'/channel/'+father+'/'+state+'/swap/'+index+'/'+(index-1);
            //console.log(url);
            $http.put(url).success(function(data){
                if(data=='done'){
//                    alert("上移成功");
                    $scope.refreshManagePage();
                    alert("上移成功");
                    $scope.closeOver();
                }else{
                    alert('上移失败');
                    $scope.closeOver();
                }
            });
        }
    };

    $scope.downMove=function(father,state,index)
    {
        $scope.coverIt();
        var maxIndex;
        if(father=='top'){
            maxIndex=$scope.appNames.length;
        }else{
            maxIndex=($scope.getModelNames($scope.modelNamesSet,father)).length;
        }

        if(index==maxIndex){
            alert("已到达底部");
            $scope.closeOver();
        }else{
            var url=$scope.projectName+'/channel/'+father+'/'+state+'/swap/'+index+'/'+(index+1);
            //console.log(url);
            $http.put(url).success(function(data){
                if(data=='done'){
//                    alert("下移成功");
                    $scope.refreshManagePage();
                    alert("下移成功");
                    $scope.closeOver();
                }else{
                    alert('下移失败');
                    $scope.closeOver();
                }
            });
        }
    };
    //关于活动----------------------------------------------------------------------------------------------------------
//    function uniques(data){
////        data=data||[];
//        var a={};
//        for(var i=0;i<data.length;i++){
//            var v=data[i];
//            if(typeof (a[v])=="undefined"){
//                a[v]=1;
//            }
//        }
//        data.length=0;
//        for(var i in a){
//            data[data.length]=i;
//        }
//        return data;
//    }
//    $scope.manageActivityNames=[];
////    $scope.oldManageActivityNames=[];
//    $scope.getManageActivityNames=function(){
//        var url=$scope.projectName+'/channel/activities';
//        $http.get(url).success(function(data){
//            if(data.length>0){
//                for(i=0;i<data.length;i++){
//                    $scope.manageActivityNames.push(data[i]);
//                }
//            }
//            else{
//                $scope.manageActivityNames=[];
//            }
//        });
////        if($scope.oldManageActivityNames.length>0){
////            $scope.manageActivityNames=uniques($scope.oldManageActivityNames);
////        }
//    };
//    $scope.getManageActivityNames();
}]);