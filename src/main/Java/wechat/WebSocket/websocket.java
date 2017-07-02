package wechat.WebSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import wechat.User.User;
import wechat.User.UserFactory;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;


//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。类似Servlet的注解mapping。无需在web.xml中配置。
@ServerEndpoint(value = "/websocket", configurator = HttpSessionConfigurator.class)
public class websocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<websocket> webSocketSet = new CopyOnWriteArraySet<websocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private HttpSession httpSession;    //request的session

    private String userid;      //用户名
    private User user;

    private static List<User> list = new ArrayList<User>();   //在线列表,记录用户名称
    private static Map routetab = new HashMap();  //用户名和websocket的session绑定的路由表

    JSONObject json = new JSONObject();

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session,EndpointConfig config){
        this.session = session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.userid=(String) this.httpSession.getAttribute("userid");    //获取当前用户
        this.user=(User) this.httpSession.getAttribute("user");

        if (!checkExist(list,userid)){
            list.add(this.user);           //将用户加入在线列表
            System.out.println("新用户登陆");
        }

        routetab.put(userid, session);   //将用户名和session绑定到路由表

        SerializerFeature feature = SerializerFeature.DisableCircularReferenceDetect;
//        用户登陆成功后检查是否有留言
        String message = UserFactory.getUserSqlExecuteer().checkMessage(userid);
        if (!message.equals("")){
            String messagelist [] = message.split(";");
            for(int i = 0;i<messagelist.length;i++){
                String messageItem [] =messagelist[i].split("@");
                json.put("from",messageItem[0]);
                json.put("type",messageItem[1]);
                json.put("content",messageItem[2]);
                json.put("to",userid);
                singleSend(json.toJSONString(), (Session) routetab.get(userid));
                json.clear();
            }
        }

        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1

        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount()+"size:"+list.size());
//        json.clear();

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);  //从set中删除
        list.remove(user);        //从在线列表移除这个用户
        routetab.remove(userid);
        subOnlineCount();           //在线数减1
        json.put("type","null");
        json.put("userlist",list);
        json.put("message","有一连接关闭！");
//        broadcast(json.toJSONString());
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        json.clear();
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message) {
        JSONObject chat = JSON.parseObject(message);
        JSONObject _message = JSON.parseObject(chat.get("message").toString());
        System.out.println(_message.get("type").toString());

        if (_message.get("to") == null || _message.get("to").equals("")){
            //群发消息
            broadcast(chat.get("message").toString());
        }else{
                String toUser = _message.getString("to").toString();

                if ( !_message.getString("type").toString().equals("add")){
                    singleSend(chat.get("message").toString(), (Session) routetab.get(_message.get("from").toString()));     //发送给自己,这个别忘了
                }
//            判断用户是否在线，不在线则在数据库留言
                if (checkExist(list,toUser)){
                    singleSend(chat.get("message").toString(), (Session) routetab.get(toUser));     //分别发送给每个指定用户
                }else{
                    String LeftMessage = _message.getString("from").toString()+"@"+_message.get("type").toString()+"@"+_message.get("content").toString()+";";
                    UserFactory.getUserSqlExecuteer().leftMessage(LeftMessage,_message.get("to").toString());
                }
        }

    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }



    /**
     * 广播消息
     * @param message
     */
    public void broadcast(String message){
        for (websocket item : webSocketSet){
            try{
                item.session.getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 对特定用户发送消息
     * @param message
     * @param session
     */
    public void singleSend(String message, Session session){
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    检查用户是否在线
    public boolean checkExist(List<User> list,String user){
        boolean flag = false;

        for (int i = 0; i<list.size();i++){
            if (list.get(i).getUserName().equals(user)){
                flag = true;
            }
        }
        return flag;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        websocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        websocket.onlineCount--;
    }
}