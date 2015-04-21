/**
 newCrawler-Ctrl
 **/

angular.module("Dashboard").controller("crawlerArticleCtrl", ["$scope","$http", function ($scope,$http) {

    //设置预览url
    $scope.setYulanInCrawler=function(id,content,outSideUrl){
        console.log(id);
        console.log($scope.newArticleData.id);
        if((content=="")&&(outSideUrl=="")){
            alert("内容和外链同时为空，不可预览！");
            var iFrameElem1 = document.getElementById('iframe_yulanInCrAr');
            iFrameElem1.src="";
            $('#yulan_crawlerArticle').modal('toggle');
        }else if(outSideUrl!=""){
            var iFrameElem2 = document.getElementById('iframe_yulanInCrAr');
            iFrameElem2.src=outSideUrl;
        }else{
            var iFrameElem = document.getElementById('iframe_yulanInCrAr');
            iFrameElem.src=$scope.projectName+"/app/ios/articledetail/"+id;
        }
    };

    $scope.backCurCrawler=function(){
        if($scope.crawlerSearchData.content==""||$scope.crawlerSearchData.content==null){
            $scope.getCrawlerData($scope.crawlerData.currentNo);
            $scope.closeOver();
        }else{
            $scope.getCrawlerSearchData($scope.crawlerData.currentNo);
            $scope.closeOver();
        }
    };
    $scope.goCrawler=function()
    {
        $scope.clearArticle();
        $scope.coverIt();
        document.getElementById("crawlerArticle").className="tab-pane";
        document.getElementById("crawler").className="tab-pane active";
        document.getElementById("crawlerSidebarID").className="sidebar-list";
        $scope.backCurCrawler();
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
    //彻底删除-----
    $scope.deleteArticleInCrawler=function()
    {
        $scope.coverIt();
                var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
                $http.delete(url).success(function(){
                    $scope.goCrawler();
                    alert("删除成功");
                    $scope.closeOver();
                });
    };
    //保存在本状态-----------------------------------------------------------------------------------------------------
    $scope.saveArticleLocal=function(){
        $scope.coverIt();
        $scope.calculatePictures();
        var url1=$scope.projectName+'/article/Crawler/1/'+$scope.articleData.id;
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl==null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.articleData.time=new Date();
                var jsonString=JSON.stringify($scope.articleData);
                console.log($scope.articleData);
                $http.put(url1,jsonString).success(function(data){
                    if(data=="true"){
//                        $scope.goCrawler();
                        alert("保存成功");
                        $scope.closeOver();
                    }
                });
            }
        }else if($scope.articleData.outSideUrl!=""||$scope.articleData.outSideUrl!=null||$scope.articleData.outSideUrl!=" "){
            $scope.outSide=/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"\"])*$/.test($scope.articleData.outSideUrl);
            if($scope.outSide){
                if($scope.articleData.channel.length==0){
                    alert("分类不能为空");
                    $scope.closeOver();
                }else if($scope.articleData.channel.length!=0){
                    $scope.articleData.content="";
                    $scope.articleData.time=new Date();
                    $scope.calculateWords();
                    var jsonString1=JSON.stringify($scope.articleData);
                    console.log($scope.articleData);
                    $http.put(url1,jsonString1).success(function(data){
                        if(data=="true"){
//                            $scope.goCrawler();
                            alert("保存成功");
                            $scope.closeOver();
                        }
                    });
                }
            }else if(!($scope.outSide)){
                alert("外链文章Url格式不对");
                $scope.closeOver();
            }
        }
    };
    $scope.saveStateInCrawler1="";
    $scope.saveArticle=function() {
        $scope.coverIt();
        $scope.calculatePictures();
        var url1 = $scope.projectName + '/article/Crawler/1/' + $scope.articleData.id;
        var url = $scope.projectName + "/article/Crawler/" + ($scope.crawlerData.currentNo).toString() + "/statechange/" + $scope.articleData.id;
        if ($scope.articleData.outSideUrl == "" || $scope.articleData.outSideUrl == " "|| $scope.articleData.outSideUrl == null) {
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.articleData.time=new Date();
                var jsonString = JSON.stringify($scope.articleData);
                console.log($scope.articleData);
                $http.put(url1, jsonString).success(function (data) {
                    $scope.saveStateInCrawler1 = data;
                    console.log("保存");
                    if ($scope.saveStateInCrawler1 == "true") {
                        $http.put(url).success(function () {
                            $scope.goCrawler();
                            alert("转草稿箱成功");
                            $scope.closeOver();
                        });
                    }
                });
            }
        } else if ($scope.articleData.outSideUrl != "" || $scope.articleData.outSideUrl != null || $scope.articleData.outSideUrl != " ") {
            $scope.outSide = /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"\"])*$/.test($scope.articleData.outSideUrl);
            if ($scope.outSide) {
                if($scope.articleData.channel.length==0){
                    alert("分类不能为空");
                    $scope.closeOver();
                }else if($scope.articleData.channel.length!=0){
                    $scope.articleData.content = "";
                    $scope.articleData.time=new Date();
                    $scope.calculateWords();
                    var jsonString1 = JSON.stringify($scope.articleData);
                    console.log($scope.articleData);
                    $http.put(url1, jsonString1).success(function (data) {
                        $scope.saveStateInCrawler1 = data;
                        console.log("保存");
                        if ($scope.saveStateInCrawler1 == "true") {
                            $http.put(url).success(function () {
                                $scope.goCrawler();
                                alert("转草稿箱成功");
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
    $scope.saveStateInCrawler2="";
    $scope.publishArticleNowInCrawler=function()
    {
        $scope.coverIt();
        $scope.calculatePictures();
        var url1=$scope.projectName+'/article/Crawler/1/'+$scope.articleData.id;
        var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl == null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.articleData.time=new Date();
                var jsonString1=JSON.stringify($scope.articleData);
                console.log($scope.articleData);
                $http.put(url1,jsonString1).success(function(data){
                    $scope.saveStateInCrawler2=data;
                    console.log("保存");
                    if($scope.saveStateInCrawler2=="true"){
                        $http.put(url).success(function(){
                            $scope.goCrawler();
                            alert("发布成功");
                            $scope.closeOver();
                        });
                    }
                });
            }
        }else if($scope.articleData.outSideUrl != "" || $scope.articleData.outSideUrl != null || $scope.articleData.outSideUrl != " "){
            $scope.outSide = /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"\"])*$/.test($scope.articleData.outSideUrl);
            if ($scope.outSide){
                if($scope.articleData.channel.length==0){
                    alert("分类不能为空");
                    $scope.closeOver();
                }else if($scope.articleData.channel.length!=0){
                    $scope.articleData.content="";
                    $scope.articleData.time=new Date();
                    $scope.calculateWords();
                    var jsonString=JSON.stringify($scope.articleData);
                    console.log($scope.articleData);
                    $http.put(url1,jsonString).success(function(data){
                        $scope.saveStateInCrawler2=data;
                        console.log("保存");
                        if($scope.saveStateInCrawler2=="true"){
                            $http.put(url).success(function(){
                                $scope.goCrawler();
                                alert("发布成功");
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
    $scope.saveStateInCrawler3="";
    $scope.publishArticleTimingInCrawler=function()
    {
        $scope.coverIt();
        var myDate=new Date();
        var myDateTime=myDate.getTime();
        var str1=$scope.publishTimeInCrawler.substr(0,10);
        var str2=$scope.publishTimeInCrawler.substr(11,16);
        var str3=str1.concat(" ");
        var str4=str3.concat(str2);
        var str5=new Date(str4);
        var myPublishedTime=str5.getTime();
        var time=myPublishedTime-myDateTime;
        console.log(time);
        $scope.calculatePictures();
        var url1=$scope.projectName+'/article/Crawler/1/'+$scope.articleData.id;
        var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/timingpublish/"+$scope.articleData.id+"/"+time;
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "|| $scope.articleData.outSideUrl == null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.articleData.time=new Date();
                var jsonString1=JSON.stringify($scope.articleData);
                console.log($scope.articleData);
                $http.put(url1,jsonString1).success(function(data){
                    $scope.saveStateInCrawler3=data;
                    console.log("保存");
                    if($scope.saveStateInCrawler3=="true"){
                        console.log(url);
                        $http.get(url).success(function(){
                            $('#Select_TimeInCrawler').modal('toggle');
                            $scope.goCrawler();
                            alert("定时成功");
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
                    console.log($scope.articleData);
                    $http.put(url1,jsonString).success(function(data){
                        $scope.saveStateInCrawler3=data;
                        console.log("保存");
                        if($scope.saveStateInCrawler3=="true"){
                            console.log(url);
                            $http.get(url).success(function(){
                                $('#Select_TimeInCrawler').modal('toggle');
                                $scope.goCrawler();
                                alert("定时成功");
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
            $('#myModal_addKeyword_crawler').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.channel.push($scope.additionChannel);
            $('#myModal_addChannel_crawler').modal('toggle');
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
        var imgObjPreview=document.getElementById("imgPreview_crawler");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPreview_crawler">'
            +'</div>';

        document.getElementById("previewFrame_crawler").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewFrame_crawler").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myUploadImgForm_crawler").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_crawler").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $scope.coverIt();
        document.form_crawler.action=$scope.projectActionName;
        $('#myUploadImgForm_crawler').submit();
        $scope.enableConfirmButton();
        $scope.upUrl=document.getElementById("myIFrameID_crawler").contentDocument.body.innerHTML;
        var out=setInterval(f,200);
        function f(){
            if($scope.upUrl!=""){
                $scope.closeOver();
                clearTimeout(out);
            }else{
                $scope.upUrl=document.getElementById("myIFrameID_crawler").contentDocument.body.innerHTML;
            }
        }
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalFooterID_crawler").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_crawler").innerHTML=tempString;
    };

    $scope.addPicUrl=function()
    {
        var url = $scope.getPicUrl();
        $scope.pushPicUrl(url);
        $scope.addImgToEditorContent(url);
        $scope.turnOffUploadModal();
        $scope.deletePreviewFrame();
        $scope.clearIframeContentInCrawler();
    };

    $scope.clearIframeContentInCrawler=function(){
        document.getElementById("myIFrameID_crawler").contentDocument.body.innerHTML="";
    };
    $scope.getPicUrl=function()
    {
        var url = document.getElementById("myIFrameID_crawler").contentDocument.body.innerHTML;
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
        $scope.articleData.content=$scope.articleData.content+text;
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //关闭上传框
    $scope.turnOffUploadModal=function()
    {
        $('#myModal_addIMG_crawler').modal('toggle');
    };
    //关于上传图片的----------------------------------------------------------------------------------------------


}]);






