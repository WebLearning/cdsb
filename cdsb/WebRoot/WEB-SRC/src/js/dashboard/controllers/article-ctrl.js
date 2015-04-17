
angular.module("Dashboard").controller("articleCtrl", ["$scope","$http", function ($scope,$http) {

    //footer的3个按钮的操作
    $scope.clearArticle=function()
    {
        for(p in $scope.newArticleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"||p=="logs"){
                $scope.newArticleData[p]=[];
            }else if(p=="activity"||p=="author"||p=="content"||p=="from"||p=="subTitle"||p=="time"||p=="title"||p=="summary"||p=="outSideUrl"){
                $scope.newArticleData[p]="";
            }else{
                $scope.newArticleData[p]=null;
            }
        }
        $scope.calculateWords();
        $scope.calculatePictures();
    };
    $scope.transDataToArticleNewArticleData=function(data)
    {
        for(p in $scope.newArticleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"||p=="logs"){
                for(i in data[p]){
                    $scope.newArticleData[p][i]=data[p][i];
                }
            }else{
                $scope.newArticleData[p]=data[p];
            }
        }
    };
    $scope.outSide="";
    $scope.newArticleData.id=null;
    $scope.saveArticle=function(){
        $scope.coverIt();
//        $scope.calculateWords();
        $scope.calculatePictures();
        var url="";
        if($scope.newArticleData.id==null||$scope.newArticleData.id==""){
            url=$scope.projectName+'/article/newArticle';
        }else{
            url=$scope.projectName+'/article/Temp/1/'+$scope.newArticleData.id;
        }
        if($scope.newArticleData.outSideUrl==""||$scope.newArticleData.outSideUrl==" "||$scope.newArticleData.outSideUrl==null){
            $scope.calculateWords();
            if($scope.newArticleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.newArticleData.channel.length!=0){
                $scope.newArticleData.time=new Date();
                var jsonString=JSON.stringify($scope.newArticleData);
                if($scope.newArticleData.id==null||$scope.newArticleData.id==""){
                    $http.post(url,jsonString).success(function(data) {
                        console.log(data.id);
                        $scope.newArticleData.id=data.id;
                        var tempUrl=$scope.projectName+"/article/Temp/1/"+$scope.newArticleData.id;
                        $http.get(tempUrl).success(function(data) {
                            $scope.transDataToArticleNewArticleData(data);
                            alert("保存文章成功");
                            $scope.closeOver();
                        });
                    });
                }else{
                    $http.put(url,jsonString).success(function() {
                        var tempUrl1=$scope.projectName+"/article/Temp/1/"+$scope.newArticleData.id;
                        $http.get(tempUrl1).success(function(data) {
                            $scope.transDataToArticleNewArticleData(data);
                            alert("保存文章成功");
                            $scope.closeOver();
                        });
                    });
                }
            }
        }else if($scope.newArticleData.outSideUrl!=""||$scope.newArticleData.outSideUrl!=null||$scope.newArticleData.outSideUrl!=" "){
            $scope.outSide=/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"\"])*$/.test($scope.newArticleData.outSideUrl);
            if($scope.outSide){
                if($scope.newArticleData.channel.length==0){
                    alert("分类不能为空！");
                    $scope.closeOver();
                }else if($scope.newArticleData.channel.length!=0){
                    $scope.newArticleData.content="";
                    $scope.newArticleData.time=new Date();
                    $scope.calculateWords();
                    var jsonString1=JSON.stringify($scope.newArticleData);
                    if($scope.newArticleData.id==null||$scope.newArticleData.id==""){
                        $http.post(url,jsonString1).success(function(data) {
                            console.log(data.id);
                            $scope.newArticleData.id=data.id;
                            var tempUrl=$scope.projectName+"/article/Temp/1/"+$scope.newArticleData.id;
                            $http.get(tempUrl).success(function(data) {
                                $scope.transDataToArticleNewArticleData(data);
                                alert("保存文章成功");
                                $scope.closeOver();
                            });
                        });
                    }else{
                        $http.put(url,jsonString1).success(function() {
                            var tempUrl1=$scope.projectName+"/article/Temp/1/"+$scope.newArticleData.id;
                            $http.get(tempUrl1).success(function(data) {
                                $scope.transDataToArticleNewArticleData(data);
                                alert("保存文章成功");
                                $scope.closeOver();
                            });
                        });
                    }
                }
            }else if($scope.outSide==false){
                alert("外链文章Url格式不对");
                $scope.closeOver();
            }
        }
    };
    $scope.pendArticleInNewArticle=function(){
        $scope.coverIt();
//        $scope.calculateWords();
        $scope.calculatePictures();
        var url="";
        var url1="";
        if($scope.newArticleData.id==null||$scope.newArticleData.id==""){
            url=$scope.projectName+"/article/newArticle/pend";
        }else{
            url1=$scope.projectName+'/article/Temp/1/'+$scope.newArticleData.id;
            url=$scope.projectName+"/article/Temp/1/statechange/"+$scope.newArticleData.id;
        }
        if($scope.newArticleData.outSideUrl==""||$scope.newArticleData.outSideUrl==" "||$scope.newArticleData.outSideUrl==null){
            $scope.calculateWords();
            if($scope.newArticleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.newArticleData.channel.length!=0){
                $scope.newArticleData.time=new Date();
                var jsonString=JSON.stringify($scope.newArticleData);
                if($scope.newArticleData.id==null||$scope.newArticleData.id==""){
                    $http.post(url,jsonString).success(function(){
                        alert("提交审核成功！");
                        $scope.clearArticle();
                        $scope.closeOver();
                    });
                }else{
                    $http.put(url1,jsonString).success(function(data) {
                        $scope.saveStateInDraft1=data;
//                    alert("保存文章成功");
                        if($scope.saveStateInDraft1=="true"){
                            $http.put(url).success(function(){
                                $scope.clearArticle();
                                alert("提交成功");
                                $scope.closeOver();
                            });
                        }
                    });
                }
            }
        }else if($scope.newArticleData.outSideUrl!=""||$scope.newArticleData.outSideUrl!=null||$scope.newArticleData.outSideUrl!=" "){
            $scope.outSide=/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"\"])*$/.test($scope.newArticleData.outSideUrl);
            if($scope.outSide){
                if($scope.newArticleData.channel.length==0){
                    alert("分类不能为空");
                    $scope.closeOver();
                }else if($scope.newArticleData.channel.length!=0){
                    $scope.newArticleData.content="";
                    $scope.newArticleData.time=new Date();
                    $scope.calculateWords();
                    var jsonString1=JSON.stringify($scope.newArticleData);
                    if($scope.newArticleData.id==null||$scope.newArticleData.id==""){
                        $http.post(url,jsonString1).success(function(){
                            alert("提交审核成功！");
                            $scope.clearArticle();
                            $scope.closeOver();
                        });
                    }else{
                        $http.put(url1,jsonString1).success(function(data) {
                            $scope.saveStateInDraft1=data;
//                    alert("保存文章成功");
                            if($scope.saveStateInDraft1=="true"){
                                $http.put(url).success(function(){
                                    $scope.clearArticle();
                                    alert("提交成功");
                                    $scope.closeOver();
                                });
                            }
                        });
                    }
                }
            }else if(!($scope.outSide)){
                alert("外链文章Url格式不对");
                $scope.closeOver();
            }
        }
    };
    $scope.publishedArticleInNewArticle=function(){
        $scope.coverIt();
//        $scope.calculateWords();
        $scope.calculatePictures();
        var url="";
        var url1="";
        if($scope.newArticleData.id==null||$scope.newArticleData.id==""){
            url=$scope.projectName+"/article/newArticle/pend";
        }else{
            url1=$scope.projectName+'/article/Temp/1/'+$scope.newArticleData.id;
            url=$scope.projectName+"/article/Temp/1/statechange/"+$scope.newArticleData.id;
        }
        if($scope.newArticleData.outSideUrl==""||$scope.newArticleData.outSideUrl==" "||$scope.newArticleData.outSideUrl==null){
            $scope.calculateWords();
            if($scope.newArticleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.newArticleData.channel.length!=0){
                $scope.newArticleData.time=new Date();
                var jsonString=JSON.stringify($scope.newArticleData);
                if($scope.newArticleData.id==null||$scope.newArticleData.id==""){
                    $http.post(url,jsonString).success(function(){
                        alert("发送成功！");
                        $scope.clearArticle();
                        $scope.closeOver();
                    });
                }else{
                    $http.put(url1,jsonString).success(function(data) {
                        $scope.saveStateInDraft1=data;
//                    alert("保存文章成功");
                        if($scope.saveStateInDraft1=="true"){
                            $http.put(url).success(function(){
                                $scope.clearArticle();
                                alert("发送成功！");
                                $scope.closeOver();
                            });
                        }
                    });
                }
            }
        }else if($scope.newArticleData.outSideUrl!=""||$scope.newArticleData.outSideUrl!=null||$scope.newArticleData.outSideUrl!=" "){
            $scope.outSide=/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"\"])*$/.test($scope.newArticleData.outSideUrl);
            if($scope.outSide){
                if($scope.newArticleData.channel.length==0){
                    alert("分类不能为空");
                    $scope.closeOver();
                }else if($scope.newArticleData.channel.length!=0){
                    $scope.newArticleData.content="";
                    $scope.newArticleData.time=new Date();
                    $scope.calculateWords();
                    var jsonString1=JSON.stringify($scope.newArticleData);
                    if($scope.newArticleData.id==null||$scope.newArticleData.id==""){
                        $http.post(url,jsonString1).success(function(){
                            alert("发送成功！");
                            $scope.clearArticle();
                            $scope.closeOver();
                        });
                    }else{
                        $http.put(url1,jsonString1).success(function(data) {
                            $scope.saveStateInDraft1=data;
//                    alert("保存文章成功");
                            if($scope.saveStateInDraft1=="true"){
                                $http.put(url).success(function(){
                                    $scope.clearArticle();
                                    alert("发送成功！");
                                    $scope.closeOver();
                                });
                            }
                        });
                    }
                }
            }else if(!($scope.outSide)){
                alert("外链文章Url格式不对");
                $scope.closeOver();
            }
        }
    };
    $scope.publishArticleInNewArticleTiming=function()
    {
        $scope.coverIt();
        $scope.calculatePictures();
        var myDate=new Date();
        var myDateTime=myDate.getTime();
        var str1=$scope.publishTimeInNewArticle.substr(0,10);
        var str2=$scope.publishTimeInNewArticle.substr(11,16);
        var str3=str1.concat(" ");
        var str4=str3.concat(str2);
        var str5=new Date(str4);
        var myPublishedTime=str5.getTime();
        var time=myPublishedTime-myDateTime;
        console.log(time);
        var url="";
        var url1="";
        if($scope.newArticleData.id==null||$scope.newArticleData.id==""){
            url=$scope.projectName+"/article/newArticle/timingpublish/"+time;
        }else{
            url1=$scope.projectName+'/article/Temp/1/'+$scope.newArticleData.id;
            url=$scope.projectName+"/article/Temp/1/timingpublish/"+$scope.newArticleData.id+"/"+time;
        }
        if($scope.newArticleData.outSideUrl==""||$scope.newArticleData.outSideUrl==" "||$scope.newArticleData.outSideUrl==null){
            $scope.calculateWords();
            if($scope.newArticleData.channel.length==0){
                alert("分类不能为空");
                $scope.closeOver();
            }else if($scope.newArticleData.channel.length!=0){
                $scope.newArticleData.time=myPublishedTime;
                var jsonString=JSON.stringify($scope.newArticleData);
                console.log(url);
                if($scope.newArticleData.id==null||$scope.newArticleData.id==""){
                    $http.post(url,jsonString).success(function(){
                        alert("定时成功");
                        $('#Select_TimeInNewArticle').modal('toggle');
                        $scope.clearArticle();
                        $scope.closeOver();
                    });
                }else{
                    $http.put(url1,jsonString).success(function(data) {
                        $scope.saveStateInDraft3=data;
//                    alert("保存文章成功");
                        if($scope.saveStateInDraft3=="true"){
                            console.log(url);
                            $http.get(url).success(function(){
                                $('#Select_TimeInNewArticle').modal('toggle');
                                $scope.clearArticle();
                                alert("定时成功");
                                $scope.closeOver();
                            });
                        }
                    });
                }
            }
        }else if($scope.newArticleData.outSideUrl!=""||$scope.newArticleData.outSideUrl!=null||$scope.newArticleData.outSideUrl!=" "){
            $scope.outSide=/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"\"])*$/.test($scope.newArticleData.outSideUrl);
            if($scope.outSide){
                if($scope.newArticleData.channel.length==0){
                    alert("分类不能为空");
                    $scope.closeOver();
                }else if($scope.newArticleData.channel.length!=0){
                    $scope.newArticleData.time=myPublishedTime;
                    $scope.newArticleData.content="";
                    $scope.calculateWords();
                    var jsonString1=JSON.stringify($scope.newArticleData);
                    console.log(url);
                    if($scope.newArticleData.id==null||$scope.newArticleData.id==""){
                        $http.post(url,jsonString1).success(function(){
                            alert("定时成功");
                            $('#Select_TimeInNewArticle').modal('toggle');
                            $scope.clearArticle();
                            $scope.closeOver();
                        });
                    }else{
                        $http.put(url1,jsonString1).success(function(data) {
                            $scope.saveStateInDraft3=data;
//                    alert("保存文章成功");
                            if($scope.saveStateInDraft3=="true"){
                                console.log(url);
                                $http.get(url).success(function(){
                                    $('#Select_TimeInNewArticle').modal('toggle');
                                    $scope.clearArticle();
                                    alert("定时成功");
                                    $scope.closeOver();
                                });
                            }
                        });
                    }
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
        var str1=$scope.delHtmlTag($scope.newArticleData.content);
        $scope.newArticleData.words=$scope.delStrTrim(str1).length;
    };

    $scope.delHtmlTag=function(str){
        return str.replace(/<[^>]+>/g,"");//去掉所有的html标记
    };
    $scope.delStrTrim=function(str){
        while(str.indexOf(" ")!=-1){
            var str=str.replace(" ","");
        }
        return str;
    };
    //图片数-----------------------------------------------------
    $scope.calculatePictures=function(){
     $scope.newArticleData.pictures=$scope.newArticleData.picturesUrl.length;
    };
    //刷新时间
    $scope.getCurrentDatetime=function()
    {
        $scope.newArticleData.time=new Date();
    };

    //删除关键词 分类 和 图片数组的操作
    $scope.deleteKeyword=function(index)
    {
        $scope.newArticleData.keyWord.splice(index,1);
    };

    $scope.deleteChannel=function(index)
    {
        $scope.newArticleData.channel.splice(index,1);
    };

    $scope.deletePicUrl=function(index)
    {
        $scope.newArticleData.picturesUrl.splice(index,1);
    };

    //添加关键词和分类
    $scope.addKeyword=function()
    {
        if($scope.additionKeyword==undefined||$scope.additionKeyword==""){
            alert("没有任何输入");
        }else{
            $scope.newArticleData.keyWord.push($scope.additionKeyword);
            $('#myModal_addKeyword').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.newArticleData.channel.push($scope.additionChannel);
            $('#myModal_addChannel').modal('toggle');
        }
    };
    function test(str){

//        var tc = document.getElementById("newArticle_textarea");
//        var tc=document.getElementById("newArticle_ueditor");
//        var tclen = $scope.newArticleData.content.length;
//        tc.focus();
        UE.getEditor('newArticle_ueditor').focus();
        UE.getEditor('newArticle_ueditor').execCommand('inserthtml',str);
//        if(typeof document.selection != "undefined")
//        {
//            document.selection.createRange().text = str;
//        }
//        else
//        {
//            $scope.newArticleData.content = $scope.newArticleData.content.substr(0,tc.selectionStart)+str+$scope.newArticleData.content.substring(tc.selectionStart,tclen);
//        }
    }
    $scope.addPictureToEditor=function(picUrl){
        //console.log(picUrl);
//        var funcName='<img src="'+picUrl+'">';
//        var funcName=picUrl;
//        UE.getEditor('newArticle_editor').setContent('{'+picUrl+'}',true);
//        UE.getEditor('editor').setContent('{'+picUrl+'}',true);
//        UE.getEditor('editor').focus();
//        UE.getEditor('editor').execCommand('inserthtml','{'+picUrl+'}');
        var text='<img src="'+picUrl+'">';
//        test(text);
//        function CheckScopeBeforeApply() {
//            if(!($scope.$phase)) {
//                $scope.$apply();
//            }
//        }
//        UE.getEditor('newArticle_ueditor').focus();
//        UE.getEditor('newArticle_ueditor').execCommand('inserthtml',text);
//        UE.getEditor('newArticle_ueditor').setContent($scope.newArticleData.content+text,true);
//        setTimeout(function(){ $scope.$apply(); });
        $scope.newArticleData.content=$scope.newArticleData.content+text;
//        $scope.$apply();//相当于刷新一下scope 不然内容加不上
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

        document.getElementById("previewFrame").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewFrame").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myUploadImgForm").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $scope.coverIt();
        document.form_newArticle.action=$scope.projectActionName;
        console.log(document.form_newArticle.action);
        $('#myUploadImgForm').submit();
        $scope.enableConfirmButton();
        $scope.upUrl=document.getElementById("myIFrameID").contentDocument.body.innerHTML;
        var out=setInterval(f,200);
        function f(){
            if($scope.upUrl!=""){
                $scope.closeOver();
                clearTimeout(out);
            }else{
                $scope.upUrl=document.getElementById("myIFrameID").contentDocument.body.innerHTML;
            }
        }
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalFooterID").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID").innerHTML=tempString;
    };

    $scope.addPicUrl=function()
    {
        var url = $scope.getPicUrl();
        $scope.pushPicUrl(url);
//        $scope.addImgToEditorContent(url);
        $scope.turnOffUploadModal();
        $scope.deletePreviewFrame();
        $scope.clearIframeContent();
    };

    $scope.clearIframeContent=function(){
        document.getElementById("myIFrameID").contentDocument.body.innerHTML="";
    };
    $scope.getPicUrl=function()
    {
        var url=document.getElementById("myIFrameID").contentDocument.body.innerHTML;
        console.log(url);
        return url;
    };

    $scope.pushPicUrl=function(url)
    {
        $scope.newArticleData.picturesUrl.push(url);
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //添加图片到ueditor内容
    $scope.addImgToEditorContent=function(url){
        var text='<img src="'+url+'">';
        $scope.newArticleData.content=text+$scope.newArticleData.content;
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //关闭上传框
    $scope.turnOffUploadModal=function()
    {
        $('#myModal_addIMG').modal('toggle');
    };
    //关于上传图片的----------------------------------------------------------------------------------------------
}]);






