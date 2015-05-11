/**
 revokedArticle-Ctrl
 **/

angular.module("Dashboard").controller("revokedPictureViewCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.setYulanInRevPic=function(id,content,outSideUrl){
        console.log(id);
        console.log($scope.newArticleData.id);
        if((content==""||content==null)&&(outSideUrl==""||outSideUrl==null)){
            alert("内容和外链同时为空，不可预览！");
            var iFrameElem1 = document.getElementById('iframe_yulanInRevPicAr');
            iFrameElem1.src="";
            $('#yulan_revokedPicAr').modal('toggle');
        }else if(outSideUrl==""||outSideUrl==null){
            var iFrameElem = document.getElementById('iframe_yulanInRevPicAr');
            iFrameElem.src=$scope.projectName+"/app/ios/articledetail/"+id;
        }else if((outSideUrl!="")||(outSideUrl!=null)){
            var iFrameElem2 = document.getElementById('iframe_yulanInRevPicAr');
            iFrameElem2.src=outSideUrl;
        }
    };
    $scope.backCurRevokedPicture=function(){
        if($scope.revokedPictureSearchData.content==""||$scope.revokedPictureSearchData.content==null){
            $scope.getRevokedPictureData($scope.revokedPictureData.currentNo);
            $scope.closeOver();
        }else{
            $scope.getRevokedPictureSearchData($scope.revokedPictureData.currentNo);
            $scope.closeOver();
        }
    };
    $scope.goRevokedPicture=function()
    {
        $scope.clearPictureArticle();
        $scope.coverIt();
        document.getElementById("revokedPictureView").className="tab-pane";
        document.getElementById("revokedPicture").className="tab-pane active";
        document.getElementById("revokedPictureSidebarID").className="sidebar-list";
        $scope.backCurRevokedPicture();
    };

    //footer的3个按钮的操作
    $scope.clearPictureArticle=function()
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
        $scope.calculateWords();
        $scope.calculatePictures();
    };
    $scope.saveStateInRevokedPicture1="";
    $scope.savePictureArticle=function(){
        $scope.coverIt();
        $scope.calculatePictures();
        var url1=$scope.projectName+'/picture/Revocation/1/'+$scope.articleData.id;
        var url=$scope.projectName+"/picture/Revocation/"+($scope.revokedPictureData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl==null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.articleData.time=new Date();
                var jsonString1=JSON.stringify($scope.articleData);
                $http.put(url1,jsonString1).success(function(data) {
                    $scope.saveStateInRevokedPicture1=data;
                    if($scope.saveStateInRevokedPicture1=="true"){
                        $http.put(url).success(function() {
                            alert("转草稿箱成功");
                            $scope.goRevokedPicture();
                            $scope.closeOver();
                        });
                    }
                });
            }
        }else if($scope.articleData.outSideUrl != "" || $scope.articleData.outSideUrl != null || $scope.articleData.outSideUrl != " "){
            $scope.outSide = /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"\"])*$/.test($scope.articleData.outSideUrl);
            if($scope.outSide){
                if($scope.articleData.channel.length==0){
                    alert("分类不能为空");
                    $scope.closeOver();
                }else if($scope.articleData.channel.length!=0){
                    $scope.articleData.content="";
                    $scope.articleData.time=new Date();
                    $scope.calculateWords();
                    var jsonString=JSON.stringify($scope.articleData);
                    $http.put(url1,jsonString).success(function(data) {
                        $scope.saveStateInRevoked1=data;
                        if($scope.saveStateInRevoked1=="true"){
                            $http.put(url).success(function() {
                                alert("转草稿箱成功");
                                $scope.goRevokedPicture();
                                $scope.closeOver();
                            });
                        }
                    });
                }
            }else if(!($scope.outSide)){
                alert("外链文章Url格式不对");
                $scope.closeOver();
            }
        }

    };
    $scope.deletePictureArticleInRevocation=function()
    {
        $scope.coverIt();
                var url = $scope.projectName + "/picture/Revocation/" + ($scope.revokedPictureData.currentNo).toString() + "/statechange/" + $scope.articleData.id;
                $http.delete(url).success(function () {
                    alert("删除成功");
                    $scope.goRevokedPicture();
                    $scope.closeOver();
                });
    };

    //得到字数
    $scope.calculateWords=function()
    {
        $scope.articleData.words=$scope.delHtmlTag($scope.articleData.content).length;
    };
    $scope.delHtmlTag=function(str){
        return str.replace(/<[^>]+>/g,"");//去掉所有的html标记
    };
    //图片数-----------------------------------------------------
    $scope.calculatePictures=function(){
        $scope.articleData.pictures=$scope.articleData.picturesUrl.length;
     };
    //刷新时间
    $scope.getCurrentDatetime=function()
    {
        $scope.articleData.time=new Date();
    };

    //删除关键词 分类 和 图片数组的操作
    $scope.deleteKeyword=function(index)
    {
        $scope.articleData.keyWord.splice(index,1);
    };

    $scope.deleteChannel=function(index)
    {
        $scope.articleData.channel.splice(index,1);
    };

    $scope.deleteActivity=function()
    {
        $scope.articleData.activity=null;
    };

    $scope.deletePicUrl=function(index)
    {
        $scope.articleData.picturesUrl.splice(index,1);
    };
    $scope.addPictureToEditor=function(picUrl){
        //console.log(picUrl);
        var text='<p style="text-align: center;"><img src="'+picUrl+'" style="width:330px;height:250px;"></p>';
        $scope.articleData.content=$scope.articleData.content+text;
//        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //添加关键词和分类
    $scope.addKeyword=function()
    {
        if($scope.additionKeyword==undefined||$scope.additionKeyword==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.keyWord.push($scope.additionKeyword);
            $('#myPictureModal_addKeyword_revoked').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.channel.push($scope.additionChannel);
            $('#myPictureModal_addChannel_revoked').modal('toggle');
        }
    };

    $scope.replyBtnStr=function(str){
        if(str==""||str==null){
            return "sr-only";
        }else{
            return "col-md-2";
        }
    };

    $scope.addActivity=function(){
        if($scope.additionActivity==undefined||$scope.additionActivity==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.activity=$scope.additionActivity;
            $('#myPictureModal_addActivity_revoked').modal('toggle');
        }
    };

    //关于上传图片----------------------------------------------------------------------------------------------

    //当input file选择的文件有变化时
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

    $scope.loadPreviewIMG=function(obj)
    {
        var docObj = obj;
        var preViewUrl = window.URL.createObjectURL(docObj.files[0]);
        var imgObjPreview=document.getElementById("imgPicturePreview_revoked");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPicturePreview_revoked">'
            +'</div>';

        document.getElementById("previewPictureFrame_revoked").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewPictureFrame_revoked").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myPictureUploadImgForm_revoked").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalPictureFooterID_revoked").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $scope.coverIt();
        document.form_revokedPicture.action=$scope.projectPicActionName;
        $('#myPictureUploadImgForm_revoked').submit();
        $scope.enableConfirmButton();
        $scope.upUrl=document.getElementById("myPictureIFrameID_revoked").contentDocument.body.innerHTML;
        var out=setInterval(f,200);
        function f(){
            if($scope.upUrl!=""){
                $scope.closeOver();
                clearTimeout(out);
            }else{
                $scope.upUrl=document.getElementById("myPictureIFrameID_revoked").contentDocument.body.innerHTML;
            }
        }
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalPictureFooterID_revoked").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalPictureFooterID_revoked").innerHTML=tempString;
    };

    $scope.addPicUrl=function()
    {
        var url = $scope.getPicUrl();
        $scope.pushPicUrl(url);
        $scope.addImgToEditorContent(url);
        $scope.turnOffUploadModal();
        $scope.deletePreviewFrame();
        $scope.clearIframeContentInRevokedPic();
    };

    $scope.clearIframeContentInRevokedPic=function(){
        document.getElementById("myPictureIFrameID_revoked").contentDocument.body.innerHTML="";
    };

    $scope.getPicUrl=function()
    {
        var url = document.getElementById("myPictureIFrameID_revoked").contentDocument.body.innerHTML;
        //url=url.substr(8);
        //url=$scope.projectName+"/WEB-SRC"+url;
        return url;
    };

    $scope.pushPicUrl=function(url)
    {
        $scope.articleData.picturesUrl.push(url);
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //添加图片到ueditor内容
    $scope.addImgToEditorContent=function(url){
        var text='<p style="text-align: center;"><img src="'+url+'" style="width:330px;height:250px;"></p>';
        $scope.articleData.content=$scope.articleData.content+text;
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //关闭上传框
    $scope.turnOffUploadModal=function()
    {
        $('#myPictureModal_addIMG_revoked').modal('toggle');
    };

//获得顶级目录名----------------------------------------------------------------------------------------------------
//    $scope.newChannelNames=[];
//    $scope.getNewChannelNames=function(){
//        var url=$scope.projectName+'/channel/kuaipai/channels';
//        //console.log(url);
//        $http.get(url).success(function(data){
//            //console.log(data);
//            if(data.length>0){
//                for(i=0;i<data.length;i++){
//                    $scope.newChannelNames.push(data[i]);
//                }
//            }else{
//                $scope.newChannelNames=[];
//            }
//            //console.log($scope.newChannelNames);
//        });
//    };
//    $scope.getNewChannelNames();
//
//    //获得活动目录------------------------------------------------------------------------------------------------------
//    $scope.newActivityNames=[];
//    $scope.getNewActivityNames=function(){
//        var url=$scope.projectName+'/channel/activities';
//        $http.get(url).success(function(data){
//            if(data.length>0){
//                for(i=0;i<data.length;i++){
//                    $scope.newActivityNames.push(data[i]);
//                }
//            }else{
//                $scope.newActivityNames=[];
//            }
//        });
//    };
//    $scope.getNewActivityNames();
    //关于上传图片的----------------------------------------------------------------------------------------------


}]);