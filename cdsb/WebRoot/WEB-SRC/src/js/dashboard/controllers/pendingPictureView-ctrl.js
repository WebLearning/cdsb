/**
 pendingPictureView
 **/
angular.module("Dashboard").controller("pendingPictureViewCtrl",["$scope","$http",function($scope,$http){
    //设置预览url
    $scope.setYulanInPendPic=function(id,content){
        console.log(id);
        console.log($scope.articleData.id);
        if(content==""){
            alert("内容为空，不可预览！");
            var iFrameElem1 = document.getElementById('iframe_yulanInPendPicAr');
            iFrameElem1.src="";
            $('#yulan_pendPicAr').modal('toggle');
        }else{
            var iFrameElem = document.getElementById('iframe_yulanInPendPicAr');
            iFrameElem.src=$scope.projectName+"/app/ios/articledetail/"+id;
        }
    };

    $scope.backCurPendingPicture=function(){
        if($scope.pendingPictureSearchData.content==""||$scope.pendingPictureSearchData.content==null){
            $scope.getPendingPictureData($scope.pendingPictureData.currentNo);
            $scope.closeOver();
        }else{
            $scope.getPendingPictureSearchData($scope.pendingPictureData.currentNo);
            $scope.closeOver();
        }
    };
    $scope.goPendingPicture=function()
    {
        $scope.clearArticle();
        $scope.coverIt();
        document.getElementById("pendingPictureView").className="tab-pane";
        document.getElementById("pendingPicture").className="tab-pane active";
        document.getElementById("pendingPictureSidebarID").className="sidebar-list";
        $scope.backCurPendingPicture();
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

    //添加关键词和分类
    $scope.addKeyword=function()
    {
        if($scope.additionKeyword==undefined||$scope.additionKeyword==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.keyWord.push($scope.additionKeyword);
            $('#myPictureModal_addKeyword_pending').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.channel.push($scope.additionChannel);
            $('#myPictureModal_addChannel_pending').modal('toggle');
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
            $('#myPictureModal_addActivity_pending').modal('toggle');
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
        var imgObjPreview=document.getElementById("imgPicturePreview_pending");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPicturePreview_pending">'
            +'</div>';

        document.getElementById("previewPictureFrame_pending").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewPictureFrame_pending").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myPictureUploadImgForm_pending").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalPictureFooterID_pending").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $('#myPictureUploadImgForm_pending').submit();
        $scope.enableConfirmButton();
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalPictureFooterID_pending").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalPictureFooterID_pending").innerHTML=tempString;
    };

    $scope.addPicUrl=function()
    {
        var url = $scope.getPicUrl();
        $scope.pushPicUrl(url);
        $scope.addImgToEditorContent(url);
        $scope.turnOffUploadModal();
    };

    $scope.getPicUrl=function()
    {
        var url = document.getElementById("myPictureIFrameID_pending").contentDocument.body.innerHTML;
        //url=url.substr(8);
        //url=$scope.projectName+"/WEB-SRC"+url;
        //console.log(url);
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
        $scope.articleData.content=text+$scope.articleData.content;
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //关闭上传框
    $scope.turnOffUploadModal=function()
    {
        $('#myPictureModal_addIMG_pending').modal('toggle');
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

    //添加功能----------------------------------------------------------------------------------------------------------
    $scope.deletePictureArticleInPending=function()
    {
        $scope.coverIt();
        var url=$scope.projectName+"/picture/Pending/"+($scope.pendingPictureData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
        $http.delete(url).success(function(){
            alert("撤销成功");
            $scope.goPendingPicture();
            $scope.closeOver();
        });
    };
    $scope.publishPictureArticleInPending=function()
    {
        $scope.coverIt();
        $scope.calculatePictures();
        var url=$scope.projectName+"/picture/Pending/"+($scope.pendingPictureData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl == null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $http.put(url).success(function(){
                    alert("发布成功");
                    $scope.goPendingPicture();
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
                    $http.put(url).success(function(){
                        alert("发布成功");
                        $scope.goPendingPicture();
                        $scope.closeOver();
                    });
                }
            }else if(!($scope.outSide)){
                alert("外链文章Url格式不对");
                $scope.closeOver();
            }
        }
    };
    $scope.publishPictureArticleInPendingTiming=function()
    {
        $scope.coverIt();
        $scope.calculatePictures();
        var myDate=new Date();
        var myDateTime=myDate.getTime();
        var str1=$scope.publishTimePictureInPending.substr(0,10);
        var str2=$scope.publishTimePictureInPending.substr(11,16);
        var str3=str1.concat(" ");
        var str4=str3.concat(str2);
        var str5=new Date(str4);
        var myPublishedTime=str5.getTime();
        var time=myPublishedTime-myDateTime;
        console.log(time);
        var url=$scope.projectName+"/picture/Pending/"+($scope.pendingPictureData.currentNo).toString()+"/timingpublish/"+$scope.articleData.id+"/"+time;
        console.log(url);
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl == null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $http.get(url).success(function(){
                    alert("定时成功");
                    $('#Select_TimePictureInPending').modal('toggle');
                    $scope.goPendingPicture();
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
                    console.log(url);
                    $http.get(url).success(function(){
                        alert("定时成功");
                        $('#Select_TimePictureInPending').modal('toggle');
                        $scope.goPendingPicture();
                        $scope.closeOver();
                    });
                }
            }else if(!($scope.outSide)){
                alert("外链文章Url格式不对");
                $scope.closeOver();
            }
        }
    };
    //关于上传图片的----------------------------------------------------------------------------------------------

}]);