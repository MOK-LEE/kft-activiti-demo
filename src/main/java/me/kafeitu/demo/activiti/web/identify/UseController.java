package me.kafeitu.demo.activiti.web.identify;

import me.kafeitu.demo.activiti.rediscache.CookieConst;
import me.kafeitu.demo.activiti.rediscache.RedisSessionContext;
import me.kafeitu.demo.activiti.util.UserUtil;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

/**
 * 用户相关控制器
 *
 * @author HenryYan
 */
@Controller
@RequestMapping("/user")
public class UseController {

    private static Logger logger = LoggerFactory.getLogger(UseController.class);

    // Activiti Identify Service
    private IdentityService identityService;
    @Autowired
    private RedisSessionContext redisSessionContext;

    /**
     * 登录系统
     *
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/logon")
    public String logon(@RequestParam("username") String userName, @RequestParam("password") String password, 
            HttpSession session,HttpServletRequest request,HttpServletResponse response) {
        logger.debug("logon request: {username={}, password={}}", userName, password);
        
        User user_account = redisSessionContext.getWebUser(request);
        if(user_account != null){
            return "redirect:/main/index";
        }else{
            boolean checkPassword = identityService.checkPassword(userName, password);
            if (checkPassword) {
                // read user from database
                User user = identityService.createUserQuery().userId(userName).singleResult();
//                UserUtil.saveUserToSession(session, user);
//                redisSessionContext.setWebUser(request, user);
                redisSessionContext.setCookieUser(response,user);
//                List<Group> groupList = identityService.createGroupQuery().groupMember(userName).list();
////                session.setAttribute("groups", groupList);
//    //
//                String[] groupNames = new String[groupList.size()];
//                for (int i = 0; i < groupNames.length; i++) {
//                    System.out.println(groupList.get(i).getName());
//                    groupNames[i] = groupList.get(i).getName();
//                }
//                session.setAttribute("groupNames", ArrayUtils.toString(groupNames));
                //redisSessionContext.setUser(request, ArrayUtils.toString(groupNames));
                return "redirect:/main/index";
            } else {
                return "redirect:/login?error=true";
            }
        }
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session,HttpServletRequest request) {
//        session.removeAttribute("user");
        redisSessionContext.removeCookie(CookieConst.USER_COOKIE);
        return "/login";
    }

    @Autowired
    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

}
