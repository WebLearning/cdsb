angular.module("Dashboard").controller("settingCtrl", ["$scope","$http", function ($scope,$http) {

    //设置app打开图片---------------------------------------------------------------------------------------------------
    $scope.picData=null;
    $scope.getPicData=function(){
        var url=$scope.projectName+"/channel/startpictures";
        $http.get(url).success(function(data){
            $scope.picData=data;
            console.log($scope.picData);
        });
    };
    $scope.getPicData();
    $scope.onePicData={
        id:"",
        pictureUrls:[]
    };
    $scope.addOnePictureData={
        id:"",
        pictureUrls:[]
    };
    $scope.testAdd=function(){
//        $scope.addOnePictureData.id=id;
//        $scope.onePicData.pictureUrls=urls;
        console.log($scope.addOnePictureData);
        var url=$scope.projectName+"/channel/startpictures";
        $http.put(url,$scope.addOnePictureData).success(function(){
            alert("添加图片成功");
            $scope.getPicData();
            $scope.addOnePictureData.pictureUrls=[];
        });
//        console.log($scope.picData);
    };
    $scope.deleteOne={
        id:""
    };
    $scope.deletePicUrl=function(id,index)
    {
        $scope.deleteOne.id=id;
        $scope.deleteOnePic(index+1);
    };
    //删除一张已上传的图片
    $scope.deleteOnePic=function(index){
        var url=$scope.projectName+"/channel/startpictures/delete/"+index;
        console.log($scope.deleteOne);
        $http.post(url,$scope.deleteOne).success(function(){
            alert("删除成功");
            $scope.getPicData();
        });
    };
    $scope.transAddAppPicture=function(id,urls){
        $scope.onePicData.id=id;
        $scope.onePicData.pictureUrls=urls;
        $scope.addOnePictureData.id=id;
        console.log($scope.onePicData.id);
        console.log($scope.onePicData.pictureUrls);
        console.log($scope.onePicData);
    };
    //新建appId名------------------------------------------------------------------------------------------------------
    $scope.newAppName={
        id:"",
        pictureUrls:[]
    };
    $scope.addNewAppName=function(){
        var url=$scope.projectName+"/channel/startpictures";
        console.log(url);
        $scope.newAppName.pictureUrls=[];
        console.log($scope.newAppName);
        $scope.addName=$scope.newAppName;
        console.log($scope.addName);
        $http.post(url,$scope.addName).success(function(){
            alert("添加app成功");
            $scope.getPicData();
            $('#myModal_addAppId').modal('toggle');
        });
    };
    //删除app-----------------------------------------------------------------------------------------------------------
    $scope.deleteId={
        id:""
    };
    $scope.deleteApp=function(id){
        var url=$scope.projectName+"/channel/startpictures/deleteall";
        $scope.coverIt();
        $scope.deleteId.id=id;
        console.log($scope.deleteId);
        $http.post(url,$scope.deleteId).success(function(){
            alert("删除成功");
            $scope.getPicData();
            $scope.closeOver();
        });
    };
    //上传app图片-------------------------------------------------------------------------------------------------------
    $scope.onInputChange=function(inputFileObj)
    {
        $scope.previewIMG(inputFileObj);
    };
    //预览图片
    $scope.previewIMG=function(inputFileObj)
    {
        if(inputFileObj.value==""){
            $scope.deletePreviewFrame();
            $scope.disableUploadButton();
            $scope.refreshImgInput();
        }else{
            $scope.addPreviewFrame();
            $scope.loadPreviewIMG(inputFileObj);
            $scope.disableConfirmButton();
        }
    };
//    $scope.final_width=null;
//    $scope.final_height=null;
    $scope.loadPreviewIMG=function(obj)
    {
        var docObj = obj;
        var preViewUrl = window.URL.createObjectURL(docObj.files[0]);
        var imgObjPreview=document.getElementById("imgPreview_addAppPic");
        imgObjPreview.src = preViewUrl;
//        $scope.final_width=imgObjPreview.offsetWidth;
//        $scope.final_height=imgObjPreview.offsetHeight;
    };
    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPreview_addAppPic">'
            +'</div>';

        document.getElementById("previewFrame_addAppPic").innerHTML=tempHtml;
    };
    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewFrame_addAppPic").innerHTML="";
    };
    $scope.refreshImgInput=function()
    {
        document.getElementById("myUploadImgForm_addAppPic").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };
    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_addAppPic").innerHTML=tempString;
    };
    //上传图片
    $scope.uploadImg=function()
    {
        $scope.coverIt();
        document.form_addAppPic.action=$scope.addAppPicActionName;
        $('#myUploadImgForm_addAppPic').submit();
        $scope.enableConfirmButton();
        $scope.upUrl=document.getElementById("myIFrameID_addAppPic").contentDocument.body.innerHTML;
        var out=setInterval(f,200);
        function f(){
            if($scope.upUrl!=""){
                $scope.closeOver();
                clearTimeout(out);
            }else{
                $scope.upUrl=document.getElementById("myIFrameID_addAppPic").contentDocument.body.innerHTML;
            }
        }
    };
    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalFooterID_addAppPic").innerHTML=tempString;
    };
    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_addAppPic").innerHTML=tempString;
    };
    $scope.addPicUrl=function()
    {
        var url = $scope.getPicUrl();
//        console.log($scope.final_height);
//        if(($scope.addOnePictureData.id=="iphone4s")&&($scope.final_width==960)&&($scope.final_height==640)){
        $scope.pushPicUrl(url);
        $scope.testAdd();
        $scope.turnOffUploadModal();
        $scope.deletePreviewFrame();
        $scope.clearIframeContentInSetting();
//        }else if(($scope.addOnePictureData.id=="iphone5s")&&($scope.final_width==1136)&&($scope.final_height==640)){
//            $scope.pushPicUrl(url);
//            $scope.testAdd();
//            $scope.turnOffUploadModal();
//            $scope.deletePreviewFrame();
//        }else{
//                alert("上传图片不合格")
//            }
    };

    $scope.clearIframeContentInSetting=function(){
        document.getElementById("myIFrameID_addAppPic").contentDocument.body.innerHTML="";
    };
    $scope.getPicUrl=function()
    {
        var url = document.getElementById("myIFrameID_addAppPic").contentDocument.body.innerHTML;
        //console.log(url);
        return url;
    };
    $scope.pushPicUrl=function(url)
    {
        $scope.onePicData.pictureUrls.push(url);
        $scope.addOnePictureData.pictureUrls.push(url);
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };
    //关闭上传框
    $scope.turnOffUploadModal=function()
    {
        $('#myModal_addAppPic').modal('toggle');
    };
}]);