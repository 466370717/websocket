<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/6/4 0004
  Time: 16:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>SignUp</title>
    <script src="js/main.js?v=123333526"></script>
</head>
<body>
<form action="/sign" method="post">
    <h1><input type="text" name="username"></h1>
    <h1><input type="password" name="password"></h1>
    <h1>
        <input type="file" name="uploadfile" class="uploadfile" id="uploadfile" onchange="UploadFile()">
        <input type="hidden" value="" name="img" id="img">
        <img src="" alt="" id="loadimg"/>
    </h1>
    <h1><input type="submit" value="提交"></h1>
</form>
</body>
</html>
<script>
    var xhr;

    function createXMLHttpRequest() {
        if (window.ActiveXObject) {
            xhr = new ActiveXObject("Microsoft.XMLHTTP");
        }else if(window.XMLHttpRequest){
            xhr = new XMLHttpRequest();
        }
    }


</script>
