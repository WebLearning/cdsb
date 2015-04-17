/**
 revokedArticle-Ctrl
 **/

angular.module("Dashboard").controller("revokedArticleCtrl", ["$scope","$http", function ($scope,$http) {

    //设置预览Url
    $scope.setYulanInRevoked=function(id,content){
        console.log(id);
        console.log($scope.articleData.id);
        if(content==""){
            alert("内容为空，不可预览！");
            var iFrameElem1 = document.getElementById('iframe_yulanInReAr');
            iFrameElem1.src="";
            $('#yulan_revokedAr').modal('toggle');
        }else{
            var iFrameElem = document.getElementById('iframe_yulanInReAr');
            iFrameElem.src=$scope.projectName+"/app/ios/articledetail/"+id;
        }
    };
    $scope.backCurRevoked=function(){
        if($scope.revokedSearchData.content==""||$scope.revokedSearchData.content==null){
            $scope.getRevokedData($scope.revokedData.currentNo);
            $scope.closeOver();
        }else{
            $scope.getRevokedSearchData($scope.revokedData.currentNo);
            $scope.closeOver();
        }
    };
    $scope.goRevoked=function()
    {
        $scope.clearArticle();
        $scope.coverIt();
        document.getElementById("revokedArticle").className="tab-pane";
        document.getElementById("revoked").className="tab-pane active";
        document.getElementById("revokedSidebarID").className="sidebar-list";
        $scope.backCurRevoked();
    };

    //footer的3个按钮的操作
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
        $scope.calculateWords();
        $scope.calculatePictures();
    };
    $scope.saveStateInRevoked1="";
    $scope.saveArticle=function(){
        $scope.coverIt();
        $scope.calculatePictures();
        var url1=$scope.projectName+'/article/Revocation/1/'+$scope.articleData.id;
        var url=$scope.projectName+"/article/Revocation/"+($scope.revokedData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl==null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.articleData.time=new Date();
                var jsonString1=JSON.stringify($scope.articleData);
                $http.put(url1,jsonString1).success(function(data) {
                    $scope.saveStateInRevoked1=data;
                    if($scope.saveStateInRevoked1=="true"){
                        $http.put(url).success(function(data) {
                            alert("转草稿箱成功");
                            $scope.goRevoked();
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
                                $scope.goRevoked();
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
    $scope.deleteArticleInRevoked=function()
    {
        $scope.coverIt();
//            if (confirm("确定删除选中的文章吗？")==true)
//            {
                var url=$scope.projectName+"/article/Revocation/"+($scope.revokedData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
                $http.delete(url).success(function(){
//                    clearArticleSelections();
                    $scope.goRevoked();
                    alert("删除成功");
                    $scope.closeOver();
                });
//            }
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

    $scope.deletePicUrl=function(index)
    {
        $scope.articleData.picturesUrl.splice(index,1);
    };
    $scope.addPictureToEditor=function(picUrl){
        //console.log(picUrl);
        var text='<img src="'+picUrl+'">';
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
            $('#myModal_addKeyword_revoked').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.channel.push($scope.additionChannel);
            $('#myModal_addChannel_revoked').modal('toggle');
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
        var imgObjPreview=document.getElementById("imgPreview_revoked");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPreview_revoked">'
            +'</div>';

        document.getElementById("previewFrame_revoked").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewFrame_revoked").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myUploadImgForm_revoked").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_revoked").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $scope.coverIt();
        document.form_revoked.action=$scope.projectActionName;
        $('#myUploadImgForm_revoked').submit();
        $scope.enableConfirmButton();
        $scope.upUrl=document.getElementById("myIFrameID_revoked").contentDocument.body.innerHTML;
        var out=setInterval(f,200);
        function f(){
            if($scope.upUrl!=""){
                $scope.closeOver();
                clearTimeout(out);
            }else{
                $scope.upUrl=document.getElementById("myIFrameID_revoked").contentDocument.body.innerHTML;
            }
        }
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalFooterID_revoked").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_revoked").innerHTML=tempString;
    };

    $scope.addPicUrl=function()
    {
        var url = $scope.getPicUrl();
        $scope.pushPicUrl(url);
        $scope.addImgToEditorContent(url);
        $scope.turnOffUploadModal();
        $scope.deletePreviewFrame();
        $scope.clearIframeContentInRevoked();
    };

    $scope.clearIframeContentInRevoked=function(){
        document.getElementById("myIFrameID_revoked").contentDocument.body.innerHTML="";
    };
    $scope.getPicUrl=function()
    {
        var url = document.getElementById("myIFrameID_revoked").contentDocument.body.innerHTML;
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
        var text='<img src="'+url+'">';
        $scope.articleData.content=$scope.articleData.content+text;
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //关闭上传框
    $scope.turnOffUploadModal=function()
    {
        $('#myModal_addIMG_revoked').modal('toggle');
    };
    //关于上传图片的----------------------------------------------------------------------------------------------


}]);






