/**
 * Created by Administrator on 2017/6/21 0021.
 */
// 切换聊天窗口和联系人
function itemOnclick(obj) {
    var chatContentWindow = document.getElementById("chatContentWindow");
    var chatBox = document.getElementById("chatBox");
    var value = obj.getElementsByTagName("h4")[0].innerHTML;
    document.getElementById("username").innerHTML=document.getElementById('to').value = value;

    chatBox.appendChild(chatContentWindow.getElementsByTagName("div")[0]);
    chatContentWindow.appendChild(document.getElementById(value+"box"));

    document.getElementsByClassName("inputContent")[0].id = value+"input";
    clearBall();

}

function clearBall() {
    var id= document.getElementById(document.getElementById("username").innerHTML);
    if (id.children.length >=4){
        id.removeChild(id.childNodes[3]);
    }
}

function searchFriends(username) {
    var result=checkFriends(username);
    if(result!="false"){
        fillblank(result,"已存在你的聊天列表","关闭",function () {
            closeSearch();
        })
    }else{
        $.post("searchFriends",
            {
                username:username
            },
            function(data){
                var obj = document.getElementById("showSearchResult");
                obj.style.display = "block";
                if(data !=""){
                    fillblank(data,document.getElementById("search").value,"添加好友",function () {
                        var pic = document.getElementById(document.getElementById("userid").value).getElementsByTagName("img")[0].src
                        send(pic,username,"add");
                        closeSearch();
                    })
                }else{
                    fillblank("upload/404.jpg","搜索的用户不存在","关闭",function () {
                        closeSearch();
                    })
                }
            })
    }

}

function checkFriends(name) {
    var flag = "false";
    var namelist = document.getElementsByClassName("itemName");
    var imglist = document.getElementsByClassName("itemImg");
    for(var i = 0; i<namelist.length;i++){
        if(namelist[i].innerHTML==name){
            flag =imglist[i].src;
        }
    }
    return flag;
}

function addFriends(from,to,fn) {
    $.post("/addFriends",{
        from:from,
        to:to
    },function (message) {
        alert(message);
        fn()
    })
}

function fillblank(img,message,content,fn) {
    var obj = document.getElementById("showSearchResult");
    obj.style.display = "block";
    obj.getElementsByTagName("img")[0].src = img;
    obj.getElementsByTagName("p")[0].innerHTML =message;
    obj.getElementsByTagName("input")[0].value = content;
    obj.getElementsByTagName("input")[0].onclick = function () {
        fn()
    }
}

function closeSearch() {
    var obj = document.getElementById("showSearchResult");
    obj.style.display = "none";
}

function getFriendsList(fn) {
    document.getElementById('userlist').innerHTML = "";
    $.post("checkFriends",
        {
            username:$("#userid").val()
        },
        function(data){
            var domUerlist = document.getElementById('userlist');
            var chatBox = document.getElementById('chatBox');
            data = JSON.parse(data);
            for(var i = 0; i<data.userlist.length;i++){
                //    生成联系人列表
                domUerlist.innerHTML += "<div class='item'  id='"+data.userlist[i].userName+"' onclick='itemOnclick(this)'><img class='itemImg' src='"+data.userlist[i].pic+"'/><h4 class = 'itemName'>"+data.userlist[i].userName+"</h4><p></p></div>";
                //生成聊天窗口
                var window = document.createElement("div");
                window.id = data.userlist[i].userName+"box";
                chatBox.appendChild(window);
            }
            fn();
//                    生成联系人列表后加载WeChat.js文件，解决js异步加载问题
            jQuery.ajax({
                url: "js/WeChat.js?time=201501221122w3442w2222518",
                dataType: "script",
                cache: true
            }).done(function() {

            });
        });
}
//发送消息
function send(message,to,type) {
    var  _from= document.getElementById('userid').value;
    websocket.send(
        JSON.stringify(
            {
                message : {
                    from:_from,
                    content : message,
                    to:to,
                    type:type
                }
            }
        )
    );
}
function UploadFile() {
    var fileObj = document.getElementById("uploadfile").files[0];
    var fileController = "/upload";
    var form =new FormData();
    form.append("uploadfile",fileObj);
    createXMLHttpRequest();
    xhr.onreadystatechange = handleStateChange;
    xhr.open("post", fileController, true);
    xhr.send(form);
}

function handleStateChange() {
    if (xhr.readyState == 4) {
        if (xhr.status == 200 || xhr.status == 0) {
            var result = xhr.responseText;
            document.getElementById("loadimg").src = result;
            document.getElementById("img").value = result;
        }
    }
}