/**
 newCrawler-Ctrl
 **/

angular.module("Dashboard").controller("pendingArticleCtrl", ["$scope","$http", function ($scope,$http) {

    //设置预览url
    $scope.setYulanInPending=function(id,content,outSideUrl){
        console.log(id);
        console.log($scope.newArticleData.id);
        if((content=="")&&(outSideUrl=="")){
            alert("内容和外链同时为空，不可预览！");
            var iFrameElem1 = document.getElementById('iframe_yulanInPeAr');
            iFrameElem1.src="";
            $('#yulan_pendingAr').modal('toggle');
        }else if(outSideUrl!=""){
            var iFrameElem2 = document.getElementById('iframe_yulanInPeAr');
            iFrameElem2.src=outSideUrl;
        }else{
            var iFrameElem = document.getElementById('iframe_yulanInPeAr');
            iFrameElem.src=$scope.projectName+"/app/ios/articledetail/"+id;
        }
    };
    $scope.backCurPending=function(){
        if($scope.pendingSearchData.content==""||$scope.pendingSearchData.content==null){
            $scope.getPendingData($scope.pendingData.currentNo);
            $scope.closeOver();
        }else{
            $scope.getPendingSearchData($scope.pendingData.currentNo);
            $scope.closeOver();
        }
    };
    $scope.goPending=function()
    {
        $scope.clearArticle();
        $scope.coverIt();
        document.getElementById("pendingArticle").className="tab-pane";
        document.getElementById("pendingTrial").className="tab-pane active";
        document.getElementById("pendingSidebarID").className="sidebar-list";
        $scope.backCurPending();
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

    $scope.saveArticle=function(){
        $scope.coverIt();
        $scope.calculatePictures();

        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl == null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                var jsonString1=JSON.stringify($scope.articleData);
                $http.post($scope.projectName+'/article/newArticle',jsonString1).success(function(data) {
                    alert("保存文章成功");
                    $scope.goPending();
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
                    $http.post($scope.projectName+'/article/newArticle',jsonString).success(function(data) {
                        alert("保存文章成功");
                        $scope.goPending();
                        $scope.closeOver();
                    });
                }
            }else if(!($scope.outSide)){
                alert("外链文章Url格式不对");
                $scope.closeOver();
            }
        }
    };

    $scope.putArticle=function(){
        $scope.coverIt();
//        $scope.calculateWords();
        $scope.calculatePictures();
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl == null){
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.calculateWords();
                var jsonString1=JSON.stringify($scope.articleData);
                $http.put($scope.projectName+'/article/newArticle',jsonString1).success(function(data) {
                    alert("提交审核文章成功");
                    $scope.goPending();
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
                    $http.put($scope.projectName+'/article/newArticle',jsonString).success(function(data) {
                        alert("提交审核文章成功");
                        $scope.goPending();
                        $scope.closeOver();
                    });
                }
            }else if(!($scope.outSide)){
                alert("外链文章Url格式不对");
                $scope.closeOver();
            }
        }
    };
    $scope.deleteArticleInPending=function()
    {
        $scope.coverIt();
        var url=$scope.projectName+"/article/Pending/"+($scope.pendingData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
        $http.delete(url).success(function(){
//            clearArticleSelections();
//            $scope.getPendingData(1);
            alert("撤销成功");
            $scope.goPending();
            $scope.closeOver();
        });
    };
    $scope.publishArticleNowInPending=function()
    {
        $scope.coverIt();
//        $scope.calculateWords();
        $scope.calculatePictures();
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl == null){
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.calculateWords();
                var url1=$scope.projectName+"/article/Pending/"+($scope.pendingData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
                $http.put(url1).success(function(){
//                clearArticleSelections();
//                $scope.getPendingData(1);
                    alert("发布成功");
                    $scope.goPending();
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
                    var url=$scope.projectName+"/article/Pending/"+($scope.pendingData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
                    $http.put(url).success(function(){
//                clearArticleSelections();
//                $scope.getPendingData(1);
                        alert("发布成功");
                        $scope.goPending();
                        $scope.closeOver();
                    });
                }
            }else if(!($scope.outSide)){
                alert("外链文章Url格式不对");
                $scope.closeOver();
            }
        }
    };
    $scope.publishArticleTimingInPending=function()
    {
        $scope.coverIt();
//        $scope.calculateWords();
        $scope.calculatePictures();
        var myDate=new Date();
        var myDateTime=myDate.getTime();
        var str1=$scope.publishTimeInPending.substr(0,10);
        var str2=$scope.publishTimeInPending.substr(11,16);
        var str3=str1.concat(" ");
        var str4=str3.concat(str2);
        var str5=new Date(str4);
        var myPublishedTime=str5.getTime();
//            console.log(myDateTime);
//            console.log(myPublishedTime);
        var time=myPublishedTime-myDateTime;
        console.log(time);
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl == null){
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.calculateWords();
                var url1=$scope.projectName+"/article/Pending/"+($scope.pendingData.currentNo).toString()+"/timingpublish/"+$scope.articleData.id+"/"+time;
                console.log(url1);
                $http.get(url1).success(function(){
                    alert("定时成功");
                    $('#Select_TimeInPending').modal('toggle');
                    $scope.goPending();
                    $scope.closeOver();
//                clearArticleSelections();
//                $scope.getPendingData(1);
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
                    var url=$scope.projectName+"/article/Pending/"+($scope.pendingData.currentNo).toString()+"/timingpublish/"+$scope.articleData.id+"/"+time;
                    console.log(url);
                    $http.get(url).success(function(){
                        alert("定时成功");
                        $('#Select_TimeInPending').modal('toggle');
                        $scope.goPending();
                        $scope.closeOver();
//                clearArticleSelections();
//                $scope.getPendingData(1);
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

    //添加关键词和分类
    $scope.addKeyword=function()
    {
        if($scope.additionKeyword==undefined||$scope.additionKeyword==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.keyWord.push($scope.additionKeyword);
            $('#myModal_addKeyword_pending').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.channel.push($scope.additionChannel);
            $('#myModal_addChannel_pending').modal('toggle');
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
        var imgObjPreview=document.getElementById("imgPreview");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPreview">'
            +'</div>';

        document.getElementById("previewFrame_pending").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewFrame_pending").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myUploadImgForm_pending").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_pending").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $('#myUploadImgForm_pending').submit();
        $scope.enableConfirmButton();
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalFooterID_pending").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_pending").innerHTML=tempString;
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
        var url = document.getElementById("myIFrameID_pending").contentWindow.document.body.innerText;
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
        $scope.articleData.content=text+$scope.articleData.content;
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //关闭上传框
    $scope.turnOffUploadModal=function()
    {
        $('#myModal_addIMG_pending').modal('toggle');
    };


    //关于上传图片的----------------------------------------------------------------------------------------------


}]);
