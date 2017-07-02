package wechat.User;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Administrator on 2017/3/3 0003.
 */
public class DatabaseConnection {
    private static final String url = "jdbc:mysql://127.0.0.1/wechat?useUnicode=true&characterEncoding=utf-8";
    private static final String name = "com.mysql.jdbc.Driver";
    private static final String user = "root";
    private static final String password ="";

    private Connection conn = null;

    public DatabaseConnection() {
        try {
            Class.forName(name);
            conn = DriverManager.getConnection(url,user,password);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return this.conn;
    }

    public void close() throws Exception{
        if (this.conn != null){
            try{
                this.conn.close();
            }catch (Exception e){
                throw e;
            }
        }
    }
}
