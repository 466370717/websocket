
var websocket = null;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://localhost:8080/websocket");
}
else {
    alert('当前浏览器 Not support websocket')
}

//连接发生错误的回调方法
websocket.onerror = function () {
    setMessageInnerHTML("WebSocket连接发生错误");
};

var userid = '';
//连接成功建立的回调方法
websocket.onopen = function () {
    // setMessageInnerHTML("WebSocket连接成功");
    userid = document.getElementById("userid").value
}

//接收到消息的回调方法
websocket.onmessage = function (event) {
    analysisMessage(event.data);
}

//连接关闭的回调方法
websocket.onclose = function () {
    setMessageInnerHTML("WebSocket连接关闭");
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    closeWebSocket();
}

function analysisMessage(message) {
    message = JSON.parse(message);
    if(message.type == "message"){
        setMessageInnerHTML(message.from,message.to,message.content);
    }
    if(message.type=="add"){
        fillblank(message.content,message.from,"同意添加",function () {
            addFriends(message.from,message.to,function () {
                closeSearch();
                getFriendsList(function () {
                    send("<p>我们已经是好友了!！</p>",message.from,"addfriends");
                })
            })
        })
    }
    if(message.type=="addfriends"){
        getFriendsList(function () {
            setMessageInnerHTML(message.from,message.to,message.content);
        });
    }
    if (message.type == "img"){
        setMessageInnerHTML(message.from,message.to,message.content,"[图片]");
    }

}

var classname = '';
//将消息显示在网页上
function setMessageInnerHTML(from,to,innerHTML,imginner) {
    // 自己的消息在右边显示，别人的消息在左边显示
    if (from == userid){
        classname = "messageitemRight";
        document.getElementById(to).getElementsByTagName("p")[0].innerHTML = imginner ||  innerHTML;
        document.getElementById(to+"box").innerHTML +='<div class="'+classname+'"><div class="arrow"></div><img class="touxiangRight" src="'+document.getElementById(from).getElementsByTagName("img")[0].src+'" alt="">'+innerHTML+'</div>';
    }else{
        classname = "messageitem";
        // 往聊天列表插入简略信息
        document.getElementById(from).getElementsByTagName("p")[0].innerHTML = imginner ||  innerHTML;
        document.getElementById(from+"box").innerHTML +='<div class="'+classname+'"><div class="arrow"></div><img class="touxiang" src="'+document.getElementById(from).getElementsByTagName("img")[0].src+'" alt="">'+innerHTML+'</div>';
       if (document.getElementById(from).getElementsByClassName("ball").length !=0){
           document.getElementById(from).getElementsByClassName("ball")[0].innerHTML++;
       }else{
           document.getElementById(from).appendChild(createBall());
       }
    }
    // 往对应的chatBox里面插入消息
}

//将消息显示在网页上

function createBall() {
    var ball = document.createElement("div");
    ball.className = "ball";
    ball.innerHTML = 1;
    return ball;
}

//关闭WebSocket连接
function closeWebSocket() {
    websocket.close();
}



