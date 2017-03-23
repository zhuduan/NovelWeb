package live.foreach.novel.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import live.foreach.novel.service.*;
import live.foreach.novel.util.includeTemplateUtil;

/**
 * Hello world!
 *
 */
//@RestController
@Controller
public class TotalController
{
	
	Date dt = new Date();
	private static final Logger logger = LoggerFactory.getLogger(TotalController.class);
		
	@Autowired
	private SimilarMartingaleService similarMartingaleService;
	
	@Autowired
	private FollowOrderService followOrderService;
	
	@Autowired
	private OnlineMonitorService onlineMonitorService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ResourceComsumeUserService resourceComsumeUserService;
	

	
		
	
	@RequestMapping("/la")
	@ResponseBody
	private String home(String la){
		la = "lala";
		logger.debug("get /la");
		return "hello " + la;
	}
	
	@RequestMapping("/background")
	private String backgroud(ModelMap map){
		includeTemplateUtil.getInstance().getNavigation(1, map);
		map.addAttribute("hello", "hello word!");
		return "background";
	}



//-----------------------------
//--- 通用功能段
//-----------------------------

	
	@RequestMapping("/index")
	private String index(ModelMap map){
		map.addAttribute("host", "http://mychuangao.com");
		return "index";
	}	
	
	@RequestMapping("/trader/index")
	private String traderIndex(ModelMap map){
		includeTemplateUtil.getInstance().getNavigation(2, map);
		return "trader/index";
	}	
	
	@RequestMapping("/admin/index")
	private String adminIndex(ModelMap map){
		includeTemplateUtil.getInstance().getNavigation(1, map);
		return "admin/index";
	}	
	


	@RequestMapping("/")
	private String defaultIndex(){
		return "login";
	}
	
	
	@RequestMapping("/introduction")
	private String introduction(){
		return "introduction";
	}
	
	
	@RequestMapping("/login")
	private String logoin(ModelMap map){
		return ("login");
	}
	
	@RequestMapping(value="/login/{userName}/{password}", method = RequestMethod.GET)
	private String logoin(@PathVariable("userName") String userName, 
						@PathVariable("password") String password, 
						RedirectAttributes redirectAttributes,
						ModelMap map,
						HttpServletRequest request){
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        //获取当前的Subject  
        Subject currentUser = SecurityUtils.getSubject();  
        try {  
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查  
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应  
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法  
            logger.debug("对用户[" + userName + "]进行登录验证..验证开始");  
            currentUser.login(token);  
            logger.debug("对用户[" + userName + "]进行登录验证..验证通过");  
        }catch(UnknownAccountException uae){  
            logger.debug("对用户[" + userName + "]进行登录验证..验证未通过,未知账户");  
            redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");  
        }catch(IncorrectCredentialsException ice){  
            logger.debug("对用户[" + userName + "]进行登录验证..验证未通过,错误的凭证");  
            redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");  
        }catch(LockedAccountException lae){  
            logger.debug("对用户[" + userName + "]进行登录验证..验证未通过,账户已锁定");  
            redirectAttributes.addFlashAttribute("message", "账户已锁定");  
        }catch(ExcessiveAttemptsException eae){  
            logger.debug("对用户[" + userName + "]进行登录验证..验证未通过,错误次数过多");  
            redirectAttributes.addFlashAttribute("message", "用户名或密码错误次数过多");  
        }catch(AuthenticationException ae){  
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景  
            logger.debug("对用户[" + userName + "]进行登录验证..验证未通过,堆栈轨迹如下");  
            ae.printStackTrace();  
            redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");  
        }  
        //验证是否登录成功  
        if(currentUser.isAuthenticated()){  
            logger.info("用户[" + userName + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");  
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            //SavedRequest savedRequest = null;
            Session session = currentUser.getSession();
            session.setAttribute("userName", userName);
            //如果改变成需要id，直接用name的方式能够提高性能
            session.setAttribute("userID", userService.getUserByName(userName).getId());
            session.setTimeout(3600*1000);
            if(savedRequest==null || savedRequest.getRequestUrl()=="/login"){
            	//TODO: 根据用户种类跳转
            	if(currentUser.hasRole("sysAdmin")==true){
            		return "redirect:/trader/index";
            	} else if(currentUser.hasRole("trader")==true){
            		return "redirect:/trader/index";
            	} else if(currentUser.hasRole("developer")==true){
            		return "redirect:/developer/index";
            	} else if(currentUser.hasRole("admin")==true){
            		return "redirect:/admin/index";
            	} else if(currentUser.hasRole("register")==true){
            		return "redirect:/register/index";
            	} else {
            		return "redirect:/normal/index";
            	}
            }
            return "redirect:"+savedRequest.getRequestUrl();
        }else{  
            token.clear();  
            return "redirect:/login";
        }  
	}
	
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "redirect:/login";
    }
	
	@RequestMapping(value="/password/mail", method=RequestMethod.GET)
    public String setPassMailAddress(){
        return "passForget";
    }
	
	@RequestMapping(value="/password/mail/url", method=RequestMethod.POST)
    public String sendUrlToPassMail(@RequestParam("mail") String mail,ModelMap map){        
		userService.sendMailForNewPass(mail);
		map.addAttribute("mail", mail);
        return "passSendMail";
    }
	
	@RequestMapping(value="/password/mail/info/{userID}/{timestamp}/{hash}", method=RequestMethod.GET)
    public String resetPass(@PathVariable("userID") Integer userID, 
    					@PathVariable("timestamp") Long timestamp, 
    					@PathVariable("hash") String hash,
    					ModelMap map){        
		Boolean result = userService.validateHash(hash, userID, timestamp);
		map.addAttribute("userID", userID);
		if(result==true) return "passReset";
		else return "passResetFail";
    }
	
	@RequestMapping(value="/password/mail/newInfo", method=RequestMethod.POST)
    public String recover(@RequestParam("userID") Integer userID,
    					@RequestParam("pass1") String pass1, 
    					@RequestParam("pass2") String pass2,
    					ModelMap map){        
		userService.setNewPass(userID, pass1, pass2, map);	
        return "passAfterReset";
    }
	
	
	@RequestMapping(value="/common/user/ownAccounts/{userID}/{userType}", method=RequestMethod.GET)
    public String userOwnAccounts(@PathVariable("userID") Integer userID, 
    							@PathVariable("userType") Integer userType,
    							ModelMap map){
        includeTemplateUtil.getInstance().getNavigation(userType, map);
        userService.getUserAccountInfoByID(userID, map);
        return "userOwnAccounts";
    }
	
	@RequestMapping(value="/admin/user/ownAccounts", method=RequestMethod.GET)
    public String getAlluserOwnAccounts(ModelMap map,
    								@RequestParam(value = "page", defaultValue = "0") Integer page,
    								@RequestParam(value = "size", defaultValue = "10") Integer size){
        includeTemplateUtil.getInstance().getNavigation(1, map);
        Sort sort = new Sort(Direction.ASC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        userService.getAllUserAccountInfo(map, pageable);
        map.addAttribute("requestPage", page);
        return "admin/userInfo";
    }
	
	
	@RequestMapping(value="/admin/resource/comsume/user", method=RequestMethod.GET)
    public String getAllResourceComsumeUser(ModelMap map,
    								@RequestParam(value = "page", defaultValue = "0") Integer page,
    								@RequestParam(value = "size", defaultValue = "10") Integer size){
        includeTemplateUtil.getInstance().getNavigation(1, map);
        Sort sort = new Sort(Direction.ASC, "userid");
        Pageable pageable = new PageRequest(page, size, sort);
        resourceComsumeUserService.getAllResourceComsumeUser(map, pageable);
        map.addAttribute("requestPage", page);
        return "admin/resourceComsumeUser";
    }
	
	
	
	
	

//-----------------------------
//--- 公共服务(功能)段
//-----------------------------

	@RequestMapping("/commonService/follow/sender")
	@ResponseBody
	private String followSender(HttpServletRequest request, HttpServletResponse  response){
		Map<String, String[]> map = request.getParameterMap();
		String[] keys = {"{ \"result\" : -1 }"};
		map.keySet().toArray(keys);		
		String outRes = followOrderService.updateSenderState(keys[0]);
		//System.out.println("~~~sender: "+outRes);
		return outRes;
	}
	
	@RequestMapping("/commonService/follow/reveiver")
	@ResponseBody
	private String followReceiver(HttpServletRequest request, HttpServletResponse  response){
		Map<String, String[]> map = request.getParameterMap();
		String[] keys = {"{ \"result\" : -1 }"};
		map.keySet().toArray(keys);
		//System.out.println("~~~keys: "+keys[0]);
		String result = followOrderService.updateReceiverState(keys[0]);
		//System.out.println("***receiver: "+result);
		return result;
	}
	
	
	@RequestMapping(value="/commonService/follow/all/{userID}",method=RequestMethod.GET)
	private String getFollowAccounts(@PathVariable("userID") Integer userID, ModelMap map){
		includeTemplateUtil.getInstance().getNavigation(2, map);
		map.addAttribute("followList", followOrderService.getAccountsByUserID(userID));
		return "trader/follow";
	}
	
	
	
	
	@RequestMapping("/commonService/EA_Ctrl/similarMartingale/getAuthorityAndParams")
	@ResponseBody
	private String similarMartingaleGetAuthorityAndParams(HttpServletRequest request, HttpServletResponse  response){
		Map<String, String[]> map = request.getParameterMap();
		String[] keys = {"{ \"result\" : -1 }"};
		map.keySet().toArray(keys);
		//System.out.println("~~~keys: "+keys[0]);
		String result = similarMartingaleService.getAuthorityAndParams(keys[0]);
		//System.out.println("***getParams: "+result);
		return result;
	}
	
	@RequestMapping("/commonService/EA_Ctrl/similarMartingale/updateParams")
	@ResponseBody
	private String similarMartingaleUpdateParams(HttpServletRequest request, HttpServletResponse  response){
		Map<String, String[]> map = request.getParameterMap();
		String[] keys = {"{ \"result\" : -1 }"};
		map.keySet().toArray(keys);
		//System.out.println("~~~keys: "+keys[0]);
		String result = similarMartingaleService.updateParams(keys[0]);
		//System.out.println("***updateParams: "+result);
		return result;
	}
	
	@RequestMapping("/commonService/EA_Ctrl/similarMartingale/updateExpiretime")
	@ResponseBody
	private String similarMartingaleUpdateExpiretime(HttpServletRequest request, HttpServletResponse  response){
		Map<String, String[]> map = request.getParameterMap();
		String[] keys = {"{ \"result\" : -1 }"};
		map.keySet().toArray(keys);
		//System.out.println("~~~keys: "+keys[0]);
		String result = similarMartingaleService.updateExpiretime(keys[0]);
		//System.out.println("***receiver: "+result);
		return result;
	}
	
	
	@MessageMapping("/commonService/onlineMonitor/{userID}")    
    public void addNewMonitor(@PathVariable("userID") Integer userID){
		//---
		onlineMonitorService.addObserver(userID);
		//System.out.println("*******: add Observer-"+userID);
    }
	
	
	@RequestMapping("/commonService/onlineMonitor/all/{userID}")    
    public String intialOnlineMonitor(@PathVariable("userID") Integer userID,ModelMap map){
		//---
		onlineMonitorService.initialOnlineMonitor(userID,map);
		includeTemplateUtil.getInstance().getNavigation(2, map);		
		return "trader/onlineMonitor";
    }
	

}
