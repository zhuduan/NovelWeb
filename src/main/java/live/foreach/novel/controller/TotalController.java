package live.foreach.novel.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


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
		
	

	
		
	
	@RequestMapping("/la")
	@ResponseBody
	private String home(String la){
		la = "lala";
		logger.debug("get /la");
		return "hello " + la;
	}

}
