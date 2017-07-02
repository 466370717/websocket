package wechat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import wechat.User.User;
import wechat.User.UserFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class IndexController {
    private ServletContext servletContext;
    @RequestMapping("/")
    public ModelAndView index(){
            ModelAndView model = new ModelAndView("index");
            return model;
    }

    @RequestMapping("/login")
    public ModelAndView login(){
        ModelAndView model = new ModelAndView("login");
        return model;
    }

    @RequestMapping("WeChat")
    public ModelAndView WeChat(HttpSession Htpsession,User user){
        ModelAndView model;
        String userid = (String) Htpsession.getAttribute("userid");
        if (userid == null || userid.equals("")){
            model = new ModelAndView("login");
        }else{
            model = new ModelAndView("chat");
        }
        return model;
    }

    @RequestMapping(value="/log",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String log(HttpServletRequest request, HttpSession HTPsession){
        String userid = request.getParameter("userid");
        String password = request.getParameter("password");
        String message;
        User user = UserFactory.getUserSqlExecuteer().checkLog(userid,password);
        if (user.getFlag()){
            HTPsession.setAttribute("userid",userid);
            HTPsession.setAttribute("user",user);
            message = "{\"message\":\"登陆成功！\",\"type\":\"true\"}";
            return message;
        }else{
            message = "{\"message\":\"账号密码错误！\",\"type\":\"flase\"}";
            return message;
        }

    }

    @RequestMapping("/setting")
    public ModelAndView setting(){
        ModelAndView model = new ModelAndView("Setting");
        return model;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
    @ResponseBody
    public String upload(MultipartFile uploadfile,HttpSession session) throws  Exception{
        String filename = uploadfile.getOriginalFilename();

        String path = session.getServletContext().getRealPath("WEB-INF/upload");

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = sdf.format(date)+filename;

        File file = new File(path,name);

        if(!file.exists()){
            file.mkdirs();
        }

        uploadfile.transferTo(file);
        return "/upload/"+name;
}

    @RequestMapping("/signup")
    public ModelAndView signup(){
        ModelAndView model = new ModelAndView("SignUP");
        return model;
    }
    @RequestMapping("/sign")
    public ModelAndView sign(String username,String password,String img){
        ModelAndView model;
        System.out.println(img);
        boolean flag = UserFactory.getUserSqlExecuteer().signup(username,password,img);
        if (flag){
            return login();
        }else{
            return signup();
        }
    }

    @RequestMapping(value="/checkFriends",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String checkFriends(String username){

        return UserFactory.getUserSqlExecuteer().checkFriends(username);
    }

    @RequestMapping(value="/searchFriends",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String searchFriends(String username){

        return UserFactory.getUserSqlExecuteer().searchFriends(username);
    }

    @RequestMapping(value="/addFriends",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String addFriends(String from,String to){
        return UserFactory.getUserSqlExecuteer().addFirends(from,to);
    }


    public ServletContext getServletContext() {
        return servletContext;
    }


}
