<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Java后端WebSocket的Tomcat实现</title>
    <script src="js/WeChat.js"></script>
    <style>
        #userlist{
            border: 1px solid #669c2a;
            position: absolute;
            bottom:0px;
            height:20px;
        }
        .head{
            width:50px;
            height:50px;
        }
    </style>
</head>
<body>
<%
    String userid = (String) session.getAttribute("userid");
%>
Welcome:<span id="userid"><%=userid%></span><br/><input id="text" type="text"/>
<button onclick="send()">发送消息</button>
<input type="text" id="to">
<hr/>
<button onclick="closeWebSocket()">关闭WebSocket连接</button>
<hr/>
<div id="message"></div>
<diiv id="userlist"></diiv>
</body>


</html>