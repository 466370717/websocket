package wechat.User;

/**
 * Created by Administrator on 2017/6/4 0004.
 */
public class User {
    private String UserName;
    private String Password;
    private String pic;
    private boolean flag;

    public User(){
        this.flag = false;
    }

    public User(String username,String pic){
        this.UserName = username;
        this.pic = pic;
        this.flag = true;
    }

    public boolean getFlag() {
        return flag;
    }

    public String getUserName() {
        return this.UserName;
    }

    public String getPic() {
        return pic;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
