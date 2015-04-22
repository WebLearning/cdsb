/**
 draftArticle-Ctrl
 **/

angular.module("Dashboard").controller("draftArticleCtrl", ["$scope","$http", function ($scope,$http) {

    //设置预览url
    $scope.setYulanInDraft=function(id,content,outSideUrl){
        console.log(id);
        console.log($scope.newArticleData.id);
        if((content==""||content==null)&&(outSideUrl==""||outSideUrl==null)){
            alert("内容和外链同时为空，不可预览！");
            var iFrameElem1 = document.getElementById('iframe_yulanInDrAr');
            iFrameElem1.src="";
            $('#yulan_draftAr').modal('toggle');
        }else if(outSideUrl==""||outSideUrl==null){
            var iFrameElem = document.getElementById('iframe_yulanInDrAr');
            iFrameElem.src=$scope.projectName+"/app/ios/articledetail/"+id;
        }else if((outSideUrl!="")||(outSideUrl!=null)){
            var iFrameElem2 = document.getElementById('iframe_yulanInDrAr');
            iFrameElem2.src=outSideUrl;
        }
    };
    $scope.backCurDraft=function(){
        if($scope.tempSearchData.content==""||$scope.tempSearchData.content==null){
            $scope.getTempData($scope.tempData.currentNo);
            $scope.closeOver();
        }else{
            $scope.getTempSearchData($scope.tempData.currentNo);
            $scope.closeOver();
        }
    };
    $scope.goDraft=function()
    {
        $scope.clearArticle();
        $scope.coverIt();
        document.getElementById("draftArticle").className="tab-pane";
        document.getElementById("draft").className="tab-pane active";
        document.getElementById("draftSidebarID").className="sidebar-list";
        $scope.backCurDraft();
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
        var url=$scope.projectName+'/article/Temp/1/'+$scope.articleData.id;
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl==null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.articleData.time=new Date();
                var jsonString1=JSON.stringify($scope.articleData);
                $http.put(url,jsonString1).success(function() {
//                    $scope.goDraft();
                    alert("保存文章成功");
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
                    $scope.articleData.time=new Date();
                    $scope.calculateWords();
                    var jsonString=JSON.stringify($scope.articleData);
                    $http.put(url,jsonString).success(function() {
//                        $scope.goDraft();
                        alert("保存文章成功");
                        $scope.closeOver();
                    });
                }
            }else if(!($scope.outSide)){
                alert("外链文章Url格式不对");
                $scope.closeOver();
            }
        }
    };
    $scope.deleteArticleInDraft=function()
    {
        $scope.coverIt();
        var url=$scope.projectName+"/article/Temp/"+($scope.tempData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
        $http.delete(url).success(function(){
            $scope.goDraft();
            alert("删除成功");
            $scope.closeOver();
        });
    };
    $scope.saveStateInDraft1="";
    $scope.submitArticleForPendingInDraft=function()
    {
        $scope.coverIt();
        $scope.calculatePictures();
        var url1=$scope.projectName+'/article/Temp/1/'+$scope.articleData.id;
        var url=$scope.projectName+"/article/Temp/"+($scope.tempData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
        if($scope.articleData.outSideUrl == "" || $scope.articleData.outSideUrl == " "||$scope.articleData.outSideUrl==null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.articleData.time=new Date();
                var jsonString1=JSON.stringify($scope.articleData);
                $http.put(url1,jsonString1).success(function(data) {
                    $scope.saveStateInDraft1=data;
//                    alert("保存文章成功");
                    if($scope.saveStateInDraft1=="true"){
                        $http.put(url).success(function(){
                            $scope.goDraft();
                            alert("提交成功");
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
                        $scope.saveStateInDraft1=data;
//                        alert("保存文章成功");
                        if($scope.saveStateInDraft1=="true"){
                            $http.put(url).success(function(){
                                $scope.goDraft();
                                alert("提交成功");
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
    $scope.saveStateInDraft2="";
    $scope.publishArticleNowInDraft=function()
    {
        $scope.coverIt();
        $scope.calculatePictures();
        var url1=$scope.projectName+'/article/Temp/1/'+$scope.articleData.id;
        var url=$scope.projectName+"/article/Temp/"+($scope.tempData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl==null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.articleData.time=new Date();
                var jsonString1=JSON.stringify($scope.articleData);
                $http.put(url1,jsonString1).success(function(data) {
                    $scope.saveStateInDraft2=data;
//                    alert("保存文章成功");
                    if($scope.saveStateInDraft2=="true"){
                        $http.put(url).success(function(){
                            $scope.goDraft();
                            alert("发布成功");
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
                        $scope.saveStateInDraft2=data;
//                        alert("保存文章成功");
                        if($scope.saveStateInDraft2=="true"){
                            $http.put(url).success(function(){
                                $scope.goDraft();
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
    $scope.saveStateInDraft3="";
    $scope.publishArticleTimingInDraft=function()
    {
        $scope.coverIt();
        var myDate=new Date();
        var myDateTime=myDate.getTime();
        var str1=$scope.publishTimeInDraft.substr(0,10);
        var str2=$scope.publishTimeInDraft.substr(11,16);
        var str3=str1.concat(" ");
        var str4=str3.concat(str2);
        var str5=new Date(str4);
        var myPublishedTime=str5.getTime();
        var time=myPublishedTime-myDateTime;
        console.log(time);
        $scope.calculatePictures();
        var url1=$scope.projectName+'/article/Temp/1/'+$scope.articleData.id;
        var url=$scope.projectName+"/article/Temp/"+($scope.tempData.currentNo).toString()+"/timingpublish/"+$scope.articleData.id+"/"+time;
        if($scope.articleData.outSideUrl==""||$scope.articleData.outSideUrl==" "||$scope.articleData.outSideUrl==null){
            $scope.calculateWords();
            if($scope.articleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.articleData.channel.length!=0){
                $scope.articleData.time=new Date();
                var jsonString1=JSON.stringify($scope.articleData);
                $http.put(url1,jsonString1).success(function(data) {
                    $scope.saveStateInDraft3=data;
//                    alert("保存文章成功");
                    if($scope.saveStateInDraft3=="true"){
                        console.log(url);
                        $http.get(url).success(function(){
                            $('#Select_TimeInDraft').modal('toggle');
                            $scope.goDraft();
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
                    $scope.articleData.time=new Date();
                    $scope.articleData.content="";
                    $scope.calculateWords();
                    var jsonString=JSON.stringify($scope.articleData);
                    $http.put(url1,jsonString).success(function(data) {
                        $scope.saveStateInDraft3=data;
//                        alert("保存文章成功");
                        if($scope.saveStateInDraft3=="true"){
                            console.log(url);
                            $http.get(url).success(function(){
                                $('#Select_TimeInDraft').modal('toggle');
                                $scope.goDraft();
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
            $('#myModal_addKeyword_draft').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.channel.push($scope.additionChannel);
            $('#myModal_addChannel_draft').modal('toggle');
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
        var imgObjPreview=document.getElementById("imgPreview_draft");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPreview_draft">'
            +'</div>';

        document.getElementById("previewFrame_draft").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewFrame_draft").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myUploadImgForm_draft").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_draft").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $scope.coverIt();
        document.form_draft.action=$scope.projectActionName;
        $('#myUploadImgForm_draft').submit();
        $scope.enableConfirmButton();
        $scope.upUrl=document.getElementById("myIFrameID_draft").contentDocument.body.innerHTML;
        var out=setInterval(f,200);
        function f(){
            if($scope.upUrl!=""){
                $scope.closeOver();
                clearTimeout(out);
            }else{
                $scope.upUrl=document.getElementById("myIFrameID_draft").contentDocument.body.innerHTML;
            }
        }
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalFooterID_draft").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_draft").innerHTML=tempString;
    };

    $scope.addPicUrl=function()
    {
        var url = $scope.getPicUrl();
        $scope.pushPicUrl(url);
        $scope.addImgToEditorContent(url);
        $scope.turnOffUploadModal();
        $scope.deletePreviewFrame();
        $scope.clearIframeContentInDraft();
    };

    $scope.clearIframeContentInDraft=function(){
        document.getElementById("myIFrameID_draft").contentDocument.body.innerHTML="";
    };
    $scope.getPicUrl=function()
    {
        var url = document.getElementById("myIFrameID_draft").contentDocument.body.innerHTML;
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
        $('#myModal_addIMG_draft').modal('toggle');
    };


    //关于上传图片的----------------------------------------------------------------------------------------------
    //获得顶级目录名----------------------------------------------------------------------------------------------------
//    $scope.newChannelNames=[];
//    $scope.getNewChannelNames=function(){
//        var url=$scope.projectName+'/channel/channels';
//        //console.log(url);
//        $http.get(url).success(function(data){
//            //console.log(data);
//            if(data.length>0){
//                for(i=0;i<data.length;i++){
//                    $scope.checkFirstChannel(data[i]);
//                }
//            }else{
//                $scope.newChannelNames=[];
//            }
//        });
//    };
//    $scope.getNewChannelNames();
//
//    //判断是否有次级目录-------------------------------------------------------------------------------------------------
//    $scope.checkFirstChannel=function(channelData){
//        var url=$scope.projectName+'/channel/'+channelData.englishName+'/channels';
//        $http.get(url).success(function (data) {
//            if(data.length>0){
//                $scope.getSecondChannelNames(channelData.englishName);
//            }else{
//                $scope.newChannelNames.push(channelData);
//            }
//        });
//    };
//    //获得次级目录名----------------------------------------------------------------------------------------------------
//    $scope.getSecondChannelNames=function(channelName){
//        var url=$scope.projectName+'/channel/'+channelName+'/channels';
//        //console.log(url);
//        $http.get(url).success(function(data){
//            //console.log(data);
//            if(data.length>0){
//                for(i=0;i<data.length;i++){
//                    $scope.newChannelNames.push(data[i]);
//                }
//            }
//        });
//    };

}]);






