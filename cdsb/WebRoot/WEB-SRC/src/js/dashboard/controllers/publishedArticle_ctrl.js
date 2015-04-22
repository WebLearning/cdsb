/**
 newCrawler-Ctrl
 **/

angular.module("Dashboard").controller("publishedArticleCtrl", ["$scope","$http", function ($scope,$http) {

//设置预览
//    $scope.setYulanInPublished=function(id,content){
////        var cover = document.getElementById("cover");
////        var covershow = document.getElementById("yulan_coverShow");
////        cover.style.display = 'block';
////        covershow.style.display = 'block';
////        if(content==""){
////            alert("内容为空，不可预览！");
////            var iFrameElem1 = document.getElementById('iframe_yulanInPubAr1');
////            iFrameElem1.src="";
//////            $('#yulan_publishedArticle').modal('toggle');
////        }else{
////            var iFrameElem = document.getElementById('iframe_yulanInPubAr1');
////            iFrameElem.src=$scope.projectName+"/app/ios/articledetail/"+id;
////        }
//    };
    $scope.setYulanInPublished=function(id,content,outSideUrl){
        console.log(id);
        console.log($scope.newArticleData.id);
        if((content==""||content==null)&&(outSideUrl==""||outSideUrl==null)){
            alert("内容和外链同时为空，不可预览！");
            var iFrameElem1 = document.getElementById('iframe_yulanInPubAr');
            iFrameElem1.src="";
            $('#yulan_publishedArticle').modal('toggle');
        }else if(outSideUrl==""||outSideUrl==null){
            var iFrameElem = document.getElementById('iframe_yulanInPubAr');
            iFrameElem.src=$scope.projectName+"/app/ios/articledetail/"+id;
        }else if((outSideUrl!="")||(outSideUrl!=null)){
            var iFrameElem2 = document.getElementById('iframe_yulanInPubAr');
            iFrameElem2.src=outSideUrl;
        }
    };
    $scope.backCurPublished=function(){
        if($scope.publishedSearchData.content==""||$scope.publishedSearchData.content==null){
            $scope.getPublishedData($scope.publishedData.currentNo);
            $scope.closeOver();
        }else{
            $scope.getPublishedSearchData($scope.publishedData.currentNo);
            $scope.closeOver();
        }
    };
    $scope.goPublished=function()
    {
        $scope.clearArticle();
        $scope.coverIt();
        document.getElementById("publishedArticle").className="tab-pane";
        document.getElementById("published").className="tab-pane active";
        document.getElementById("publishedSidebarID").className="sidebar-list";
        $scope.backCurPublished();
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

    $scope.sendMessageData={
        message:"",
        articleId:null
    };
    $scope.clearSendMessageData=function(){
        $scope.sendMessageData.message="";
        $scope.sendMessageData.articleId=null;
    };
    $scope.assignValueMessage=function(){
//        $scope.valueMessage.Message=$scope.articleData.title;
        var inputMessageid=document.getElementById("inputMessage_published");
        inputMessageid.value=$scope.articleData.title;
        inputMessageid.setAttribute("value",inputMessageid.value);
        $scope.sendMessageData.message=$scope.articleData.title;
    };
    $scope.sendMessage=function(){
        $scope.coverIt();
//        console.log($scope.articleData.id);
        $scope.sendMessageData.articleId=$scope.articleData.id;
//        console.log($scope.sendMessageData.message);
//        console.log($scope.sendMessageData.articleId);
        var jsonString=JSON.stringify($scope.sendMessageData);
        console.log(jsonString);
        var url=$scope.projectName+'/article/push';
        console.log(url);
        if($scope.articleData.channel.length==0){
            alert("分类不能为空");
            $scope.closeOver();
        }else if($scope.articleData.channel.length!=0){
            $http.post(url,jsonString).success(function(){
                alert("推送成功");
                $('#send_published').modal('toggle');
                $scope.clearSendMessageData();
                $scope.closeOver();
            });
        }
    };
    $scope.deleteArticleInPublished=function()
    {
        $scope.coverIt();
        var url=$scope.projectName+"/article/Published/"+($scope.publishedData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
        $http.delete(url).success(function(){
            alert("撤销成功");
            $scope.goPublished();
            $scope.closeOver();
        });
    };
    $scope.saveArticleInPublished=function(){
        $scope.coverIt();
        $scope.calculatePictures();
        var url1=$scope.projectName+'/article/Published/1/'+$scope.articleData.id;
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "|| $scope.articleData.outSideUrl == null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                var jsonString1=JSON.stringify($scope.articleData);
                console.log($scope.articleData);
                $http.put(url1,jsonString1).success(function(){
                    alert("保存成功");
//                    $scope.goPublished();
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
//                        $scope.goPublished();
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
            $('#myModal_addKeyword_published').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.channel.push($scope.additionChannel);
            $('#myModal_addChannel_published').modal('toggle');
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
        var imgObjPreview=document.getElementById("imgPreview_published");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPreview_published">'
            +'</div>';

        document.getElementById("previewFrame_published").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewFrame_published").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myUploadImgForm_published").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_published").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $scope.coverIt();
        document.form_published.action=$scope.projectActionName;
        $('#myUploadImgForm_published').submit();
        $scope.enableConfirmButton();
        $scope.upUrl=document.getElementById("myIFrameID_published").contentDocument.body.innerHTML;
        var out=setInterval(f,200);
        function f(){
            if($scope.upUrl!=""){
                $scope.closeOver();
                clearTimeout(out);
            }else{
                $scope.upUrl=document.getElementById("myIFrameID_published").contentDocument.body.innerHTML;
            }
        }
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalFooterID_published").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_published").innerHTML=tempString;
    };

    $scope.addPicUrl=function()
    {
        var url = $scope.getPicUrl();
        $scope.pushPicUrl(url);
        $scope.addImgToEditorContent(url);
        $scope.turnOffUploadModal();
        $scope.deletePreviewFrame();
        $scope.clearIframeContentInPublished();
    };

    $scope.clearIframeContentInPublished=function(){
        document.getElementById("myIFrameID_published").contentDocument.body.innerHTML="";
    };
    $scope.getPicUrl=function()
    {
        var url = document.getElementById("myIFrameID_published").contentDocument.body.innerHTML;
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
        $('#myModal_addIMG_published').modal('toggle');
    };


    //关于上传图片的----------------------------------------------------------------------------------------------


}]);
