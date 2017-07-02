<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/6/6 0006
  Time: 20:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Setting</title>
    <script src="http://lib.sinaapp.com/js/jquery/1.7.2/jquery.min.js"></script>
    <script src="js/main.js?v=1222232"></script>
</head>
<body>
<%--<form action="/upload" method="post"  enctype="multipart/form-data">--%>
    <%--<input type="text" name="name"/>--%>
    <%--<input type="file" name="uploadfile"/>--%>
    <%--<input type="submit"/>--%>
<%--</form>--%>
<input type="file" name="uploadfile" class="uploadfile" id="uploadfile" onchange="UploadFile()">
<img src="" alt="" id="loadimg"/>
<input type="hidden" id="img">
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