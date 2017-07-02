package wechat.User;

/**
 * Created by Administrator on 2017/6/4 0004.
 */
public class UserFactory {
    public static UserSqlExecuteer getUserSqlExecuteer(){
        return new UserSqlExecuteer();
    }
}
