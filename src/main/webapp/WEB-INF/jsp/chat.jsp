<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>WeChat</title>
    <script src="http://lib.sinaapp.com/js/jquery/1.7.2/jquery.min.js"></script>
    <script src="js/main.js?time=201501152221w332w222222138"></script>
    <%--富文本编辑器引用开始--%>
    <link href="http://netdna.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.css" rel="stylesheet">
    <script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.4/jquery.js"></script>
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.js"></script>
    <link href="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.4/summernote.css" rel="stylesheet">
    <script src="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.4/summernote.js"></script>
    <%--富文本编辑器引用结束--%>
    <style>
        *{padding:0px;  margin:0px;}
        html{width:100%;
            height:100%;}
        body{
            width:100%;
            height:100%;
            position: relative;
            overflow: hidden;
        }
        #left{
            width:20%;
            height:100%;
            float: left;
            background: #e5e5e5;}
        #right{
            width:80%;
            height:100%;
            float: right;
            background: #eee;}

        .messageitem{width:100%;height:auto;padding:10px 0px 10px 0px;
            background: #ffffff;line-height: 50px;overflow: hidden}
        .touxiang{
            float: left;
            width:50px;
            height:50px;}
        .messageitem p{
            word-break:break-word;
            float: left;
            width: auto;
            background: #F8C301;
            padding: 10px;
            border-radius: 6px;
            margin-left: 15px;
            max-width: 300px;
        }
        .messageitemRight{width:100%;height:auto;padding:10px 0px 10px 0px;
            background: #ffffff;line-height: 50px;overflow: hidden}
        .touxiangRight{
            float: right;
            width:50px;
            height:50px;}
        .messageitemRight p{
            word-break:break-word;
            float: right;
            width: auto;
            background: #F8C301;
            max-width: 300px;
            padding: 10px;
            border-radius: 6px;
            margin-right: 15px;
        }

        .ball{
            width:15px;
            height:15px;
            background-color: red ;
            color: #ffffff;
            line-height: 15px;
            text-align: center;
            border-radius: 15px;
            position: absolute;
            left:50px;
            top:0px;
            box-shadow: 2px 2px 5px #000;
        }
        .item{width:100%;height:50px;padding:10px 0px 10px 0px;
            background: #cccccc;
            position: relative;height: auto;overflow: hidden;max-height: 70px;}
        .item img{width:50px;
            height:50px;
            float: left;
            padding-left: 5px;
        }
        .item h4{
            padding-top:5px; padding-left: 60px;
            margin:0px;}
        .item p{
            padding:0px 10px;
            white-space:nowrap; overflow:hidden; text-overflow:ellipsis;
        }
        .item .message{color: #999}
        .name{width: 100%;
            height:51px;
            background: #ffffff;line-height: 51px;font-size: 27px;}
        .inputContent{width:80%;height: 150px;position: absolute;
            bottom:0px;}
        .inputContent textarea{
            width:100%;
            height:150px;resize: none}
        #snedBtn{
            position: absolute;bottom: 20px;right: 20px;
            height:30px;
            width:60px;text-align: center;
            background: #cccccc;border:none}
        .chatContentWindow{
            width:80%;
            position: absolute;
            top:51px;bottom: 150px;
            background: #ffffff;
            border-top:1px #ccc solid;border-bottom: 1px #ccc solid;overflow-x: hidden;overflow-y: scroll}
        #chatBox{visibility: hidden;
            height:0px;
            overflow: hidden;}
        .userlistWindow{
            overflow-x: hidden;
            overflow-y: scroll;
        }
        #showSearchResult{
            width: 300px;
            height:200px;background: #e5e5e5;text-align: center;
            position: absolute;
            left:50%;
            top:50%;
            margin-left: -150px;
            margin-top: -100px;
            z-index: 999;
            line-height: 60px;
            display: none;
        }
        #showSearchResult input{
            width:120px;
            height:60px;
            background: #329b6b;
            border: none;
            border-radius: 30px;
            color: #ffffff;
        }
        #showSearchResult img{
            width: 50px;
            height:50px;
            box-shadow: 5px 5px 5px #000;
        }
        .chatimg{
            float: none;
            width: auto;
            height:auto;
            max-width: 200px;
        }
    </style>
</head>
<body>
<%
    String userid = (String) session.getAttribute("userid");
%>
    <div id="left">
        <h1>联系人<input type="hidden" id="to" value="<%=userid%>"/><input type="hidden" id="userid" value="<%=userid%>"/></h1>
        <p><input type="text" id="search"><input type="button" value="搜索" onclick="searchFriends($('#search').value)"></p>
        <div class="userlistWindow">
            <div id="userlist">

            </div>
        </div>

    </div>
    <div id="right" onclick="clearBall()">
        <div class="name">
            <p class="username" id="username"><%=userid%></p>
        </div>
        <div class="chatContentWindow" id="chatContentWindow">
            <div class="chatContent" id="message">

            </div>
        </div>
        <div class="inputContent" id="">
            <div id="summernote"><p>Hello Summernote</p></div>
            <%--<textarea id="text"></textarea>--%>
            <input id="snedBtn" type="button" value="发送" onclick="send(document.getElementsByClassName('note-editable panel-body')[0].innerHTML,$('#to').val(),'message')"/>
        </div>
    </div>
    <div id="chatBox">

    </div>
    <div id="showSearchResult">
        <img src="" alt="">
        <p id="resultUsername"></p>
        <input type="button" value="">
    </div>
</body>
</html>
<script>
    $(document).ready(function() {
        $('#summernote').summernote({
            height:100,
            callbacks : {
// onImageUpload的参数为files，summernote支持选择多张图片
                onImageUpload : function(files) {
                    var $files = $(files);
// 通过each方法遍历每一个file
                    $files.each(function() {
                        var uploadfile = this;
// FormData，新的form表单封装，具体可百度，但其实用法很简单，如下
                        var data = new FormData();
// 将文件加入到file中，后端可获得到参数名为“file”
                        data.append("uploadfile", uploadfile);
// ajax上传
                        $.ajax({
                            data : data,
                            type : "POST",
                            url : "/upload",// div上的action
                            cache : false,
                            contentType : false,
                            processData : false,
// 成功时调用方法，后端返回json数据
                            success : function(response) {
//                                上传完直接发送
                                send("<p><img class='chatimg' src="+response+"/></p>",$('#to').val(),'img');
                            },
// ajax请求失败时处理
                            error : function () {
                                alert("cuowu!");
                            }
                        });
                    });
                }
            }
        });
        getFriendsList(function () {});
    });
</script>
