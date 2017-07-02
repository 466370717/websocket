<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/6/2 0002
  Time: 13:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login WeChat</title>
    <style>
        body{
            text-align: center;
        }
    </style>
    <script src="http://lib.sinaapp.com/js/jquery/1.9.1/jquery-1.9.1.min.js"></script>
</head>
<body>
<%
    String error = (String) request.getAttribute("message");
%>
    <div>
        <h1><%
            if (error != null){
                out.print(error);
            }
        %></h1>
        <%--<form action="/log" method="post">--%>
            <h1><input type="text" name="userid" id="userid"></h1>
            <h1><input type="password" name="password" id="password"></h1>
            <h1><input type="submit" value="Login" id="login"></h1>
        <%--</form>--%>
    </div>
</body>
</html>
<script>
    $("#login").click(function(){
        $.post("/log",
            {
                userid:$("#userid").val(),
                password:$("#password").val()
            },
            function (data) {
                var message = JSON.parse(data);
                alert(message.message);
                if (message.type == "true"){
                    self.location = "/WeChat";
                }
            });
    });
</script>
