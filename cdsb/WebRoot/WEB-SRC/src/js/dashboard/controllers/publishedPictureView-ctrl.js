/**
 publishedPictureView-Ctrl
 */
angular.module("Dashboard").controller("publishedPictureViewCtrl",["$scope","$http",function($scope,$http){

    $scope.setYulanInPubPic=function(id,content,outSideUrl){
        console.log(id);
        if((content==""||content==null)&&(outSideUrl==""||outSideUrl==null)){
            alert("内容和外链同时为空，不可预览！");
            var iFrameElem1 = document.getElementById('iframe_yulanInPubPicAr');
            iFrameElem1.src="";
            $('#yulan_publishedPicAr').modal('toggle');
        }else if(outSideUrl==""||outSideUrl==null){
            var iFrameElem = document.getElementById('iframe_yulanInPubPicAr');
            iFrameElem.src=$scope.projectName+"/app/ios/articledetail/"+id;
        }else if((outSideUrl!="")||(outSideUrl!=null)){
            var iFrameElem2 = document.getElementById('iframe_yulanInPubPicAr');
            iFrameElem2.src=outSideUrl;
        }
    };
    $scope.backCurPublishedPicture=function(){
        if($scope.publishedPictureSearchData.content==""||$scope.publishedPictureSearchData.content==null){
            $scope.getPublishedPictureData($scope.publishedPictureData.currentNo);
            $scope.closeOver();
        }else{
            $scope.getPublishedPictureSearchData($scope.publishedPictureData.currentNo);
            $scope.closeOver();
        }
    };
    $scope.goPublishedPicture=function()
    {
        $scope.clearArticle();
        $scope.coverIt();
        document.getElementById("publishedPictureView").className="tab-pane";
        document.getElementById("publishedPicture").className="tab-pane active";
        document.getElementById("publishedPictureSidebarID").className="sidebar-list";
        $scope.backCurPublishedPicture();
    };
    $scope.testLog=function()
    {
        $scope.calculateWords();
        $scope.calculatePictures();
        //console.log($scope.recvData);
        console.log($scope.articleData);
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
        $scope.calculateWords();
        $scope.calculatePictures();
    };
    $scope.sendPictureMessageData={
        message:"",
        articleId:null
    };
    $scope.clearSendPictureMessageData=function(){
        $scope.sendPictureMessageData.message="";
        $scope.sendPictureMessageData.articleId=null;
    };
    $scope.assignPictureValueMessage=function(){
//        $scope.valueMessage.Message=$scope.articleData.title;
        var inputMessageid=document.getElementById("inputMessage_publishedPicture");
        inputMessageid.value=$scope.articleData.title;
        inputMessageid.setAttribute("value",inputMessageid.value);
        $scope.sendPictureMessageData.message=$scope.articleData.title;
    };
    $scope.assignPictureValueMessageInTime=function(){
        var inputMessageid=document.getElementById("inputMessage_publishedPicInTime");
        inputMessageid.value=$scope.articleData.title;
        inputMessageid.setAttribute("value",inputMessageid.value);
        $scope.sendPictureMessageData.message=$scope.articleData.title;
    };
    $scope.sendPictureMessage=function(){
//        console.log($scope.articleData.id);
        $scope.coverIt();
        $scope.sendPictureMessageData.articleId=$scope.articleData.id;
//        console.log($scope.sendPictureMessageData.message);
//        console.log($scope.sendPictureMessageData.articleId);
        var jsonString=JSON.stringify($scope.sendPictureMessageData);
        console.log(jsonString);
        var url=$scope.projectName+'/article/push';
        console.log(url);
        if($scope.articleData.channel.length==0){
            alert("分类不能为空");
            $scope.closeOver();
        }else if($scope.articleData.channel.length!=0){
            $http.post(url,jsonString).success(function(){
                alert("推送成功");
                $('#send_publishedPicture').modal('toggle');
                $scope.clearSendPictureMessageData();
                $scope.closeOver();
            });
        }
    };
    $scope.sendMessagePicInTime=function(){
        $scope.coverIt();
        var myDate=new Date();
        var myDateTime=myDate.getTime();
        var str1=$scope.sendTimeInPublishedPic.substr(0,10);
        var str2=$scope.sendTimeInPublishedPic.substr(11,16);
        var str3=str1.concat(" ");
        var str4=str3.concat(str2);
        var str5=new Date(str4);
        var myPublishedTime=str5.getTime();
        var time=myPublishedTime-myDateTime;
        console.log(time);
        var url=$scope.projectName+'/article/timepush/'+time;
        $scope.sendPictureMessageData.articleId=$scope.articleData.id;
        var jsonString=JSON.stringify($scope.sendPictureMessageData);
        console.log(jsonString);
        console.log(url);
        if($scope.articleData.channel.length==0){
            alert("分类不能为空");
            $scope.closeOver();
        }else if($scope.articleData.channel.length!=0){
            $http.post(url,jsonString).success(function(){
                alert("推送成功");
                $('#send_publishedPicInTime').modal('toggle');
                $scope.clearSendPictureMessageData();
                $scope.closeOver();
            });
        }
    };
    $scope.deletePictureArticleSelections=function()
    {
        $scope.coverIt();
        var url=$scope.projectName+"/picture/Published/"+($scope.publishedPictureData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
        $http.delete(url).success(function(){
            alert("撤销成功");
            $scope.goPublishedPicture();
            $scope.closeOver();
        });
    };
    $scope.saveArticleInPublished=function(){
        $scope.coverIt();
        $scope.calculatePictures();
        var url1=$scope.projectName+'/picture/Published/1/'+$scope.articleData.id;
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "|| $scope.articleData.outSideUrl == null){
            console.log($scope.articleData);
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.calculateWords();
                var jsonString1=JSON.stringify($scope.articleData);
                $http.put(url1,jsonString1).success(function(){
                    alert("保存成功");
//                    $scope.goPublishedPicture();
                    $scope.closeOver();
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
                    $scope.calculateWords();
                    var jsonString=JSON.stringify($scope.articleData);
                    console.log($scope.articleData);
                    $http.put(url1,jsonString).success(function(){
                        alert("保存成功");
//                        $scope.goPublishedPicture();
                        $scope.closeOver();
                    });
                }
            }else if(!($scope.outSide)){
                alert("外链文章Url格式不对");
                $scope.closeOver();
            }
        }

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
            $('#myPictureModal_addKeyword_published').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.channel.push($scope.additionChannel);
            $('#myPictureModal_addChannel_published').modal('toggle');
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
            $('#myPictureModal_addActivity_published').modal('toggle');
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
        var imgObjPreview=document.getElementById("imgPicturePreview_published");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPicturePreview_published">'
            +'</div>';

        document.getElementById("previewPictureFrame_published").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewPictureFrame_published").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myPictureUploadImgForm_published").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalPictureFooterID_published").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $scope.coverIt();
        document.form_publishedPicture.action=$scope.projectActionName;
        $('#myPictureUploadImgForm_published').submit();
        $scope.enableConfirmButton();
        $scope.upUrl=document.getElementById("myPictureIFrameID_published").contentDocument.body.innerHTML;
        var out=setInterval(f,200);
        function f(){
            if($scope.upUrl!=""){
                $scope.closeOver();
                clearTimeout(out);
            }else{
                $scope.upUrl=document.getElementById("myPictureIFrameID_published").contentDocument.body.innerHTML;
            }
        }
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalPictureFooterID_published").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalPictureFooterID_published").innerHTML=tempString;
    };

    $scope.addPicUrl=function()
    {
        var url = $scope.getPicUrl();
        $scope.pushPicUrl(url);
        $scope.addImgToEditorContent(url);
        $scope.turnOffUploadModal();
        $scope.deletePreviewFrame();
        $scope.clearIframeContentInPublishedPic();
    };

    $scope.clearIframeContentInPublishedPic=function(){
        document.getElementById("myPictureIFrameID_published").contentDocument.body.innerHTML="";
    };
    $scope.getPicUrl=function()
    {
        var url = document.getElementById("myPictureIFrameID_published").contentDocument.body.innerHTML;
        console.log(url);
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
        $('#myPictureModal_addIMG_published').modal('toggle');
    };

    //关于上传图片的----------------------------------------------------------------------------------------------

}]);