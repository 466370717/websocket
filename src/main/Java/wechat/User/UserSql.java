package wechat.User;

import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserSql {
    private Connection conn = null;
    private PreparedStatement pstmt = null;

    public UserSql(Connection conn){
        this.conn = conn;
    }

    public User checkLog(String UserName,String Password){
        String password=null;
        String pic = null;
        boolean flag = false;
        User user = new User();
        try{
            String sql = "select * from user where username = ?";
            System.out.println("正在查询数据库"+UserName);
            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1,UserName);
            ResultSet re = this.pstmt.executeQuery();

            while (re.next()){

                password = re.getString("password");
                pic = re.getString("pic");
                System.out.println("数据库结果"+password);
            }

            if (password.equals(Password)){
                user = new User(UserName,pic);
            }
            return user;
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public boolean signup(String username,String password,String uploadfile){
        boolean flag = false;
        try{
//            写入user表
            String sql = "insert into user(username,password,pic) value(?,?,?) ";
            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1,username);
            this.pstmt.setString(2,password);
            this.pstmt.setString(3,uploadfile);

            this.pstmt.executeUpdate();
//            写入friendslist表，并添加自己为好友
            String sql2 = "insert into friendslist(username,list) value(?,?) ";
            this.pstmt = this.conn.prepareStatement(sql2);
            this.pstmt.setString(1,username);
            this.pstmt.setString(2,username+";");
            this.pstmt.executeUpdate();
//            写入messageleft表
            String sql3 = "insert into messageleft(username,content) value(?,?) ";
            this.pstmt = this.conn.prepareStatement(sql3);
            this.pstmt.setString(1,username);
            this.pstmt.setString(2,"");
            this.pstmt.executeUpdate();

            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    public String checkFriends(String username){
        String value = "";
        JSONObject userlist = new JSONObject();
        List list = new ArrayList();
        try{
            String sql = "select list from friendslist where username = ?";
            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1,username);
            ResultSet re = this.pstmt.executeQuery();
            while(re.next()){
                value = re.getString("list");
            }
            String [] namelist = value.split(";");
            for (int i = 0;i < namelist.length;i++){
                list.add(loadFriends(namelist[i]));
            }
            userlist.put("userlist",list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return userlist.toJSONString();
    }

    public User loadFriends(String username){
        User user = new User();
        try{
            String sql = "select  pic from user where username =?";
            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1,username);
            ResultSet re = this.pstmt.executeQuery();
            while (re.next()){
                user = new User(username,re.getString("pic"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public void leftMessage(String message,String username){
        try{
            String sql = "select content from messageleft where username =?";
            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1,username);
            ResultSet re =  this.pstmt.executeQuery();
            while (re.next()){
                String sql2 = "update messageleft set content =? where username =?";
                String newContent = re.getString("content")+message;
                this.pstmt = this.conn.prepareStatement(sql2);
                this.pstmt.setString(1,newContent);
                this.pstmt.setString(2,username);
                this.pstmt.executeUpdate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String checkMessage(String username){
        String messaage = "";
        try{
            String sql = "select content from messageleft where username =?";
            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1,username);
            ResultSet re =  this.pstmt.executeQuery();
            while (re.next()){
               messaage = re.getString("content");
                DelMessage(username);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return messaage;
    }

    public void DelMessage(String username){
        try{
            String sql2 = "update messageleft set content =? where username =?";
            this.pstmt = this.conn.prepareStatement(sql2);
            this.pstmt.setString(1,"");
            this.pstmt.setString(2,username);
            this.pstmt.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public String searchFriends(String search){
        String messaage ="";
        try{
            String sql = "select  pic from user where username = ?";
            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1,search);
            ResultSet re =  this.pstmt.executeQuery();
            while (re.next()){
                messaage = re.getString("pic");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return messaage;
    }

    public String addFirends(String from,String to){
        String message = "";
        message = addItem(from,to);
        message = addItem(to,from);
        return message;
    }

    public String addItem(String from,String to){
        String message = "添加失败，请重试！";
        try{
            String sql = "select list from friendslist where username =?";
            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1,from);
            ResultSet re =  this.pstmt.executeQuery();
            while (re.next()){
                String sql2 = "update friendslist set list =? where username =?";
                String newContent = re.getString("list")+to+";";
                this.pstmt = this.conn.prepareStatement(sql2);
                this.pstmt.setString(1,newContent);
                this.pstmt.setString(2,from);
                this.pstmt.executeUpdate();
            }
            message = "添加成功！";
        }catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }
}
