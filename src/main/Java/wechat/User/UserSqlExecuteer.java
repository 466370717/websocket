package wechat.User;


public class UserSqlExecuteer {
    private DatabaseConnection dbc = null;
    private UserSql userSql = null;

    public UserSqlExecuteer(){
        this.dbc = new DatabaseConnection();
        this.userSql = new UserSql(this.dbc.getConnection());
    }

    public User checkLog(String UserName,String Password){
        return  this.userSql.checkLog(UserName,Password);
    }

    public String checkFriends(String username){
        return this.userSql.checkFriends(username);
    }
    public String searchFriends(String username){
        return this.userSql.searchFriends(username);
    }

    public String addFirends(String from,String to){return this.userSql.addFirends(from,to);}

    public void leftMessage(String message,String usernmae){
        this.userSql.leftMessage(message,usernmae);
    }

    public String checkMessage(String username){
        return this.userSql.checkMessage(username);
    }

    public boolean signup(String UserName,String Password,String uploadfile){
        return  this.userSql.signup(UserName,Password,uploadfile);
    }


}
