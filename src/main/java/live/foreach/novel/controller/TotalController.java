package live.foreach.novel.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import live.foreach.novel.service.FirstDemoService;


/**
 * Hello world!
 *
 */
@RestController
public class TotalController
{
	
	Date dt = new Date();
	private static final Logger logger = LoggerFactory.getLogger(TotalController.class);	
	
	@Autowired
	private FirstDemoService firstDemoService;
	
	@RequestMapping("/la")
	private String home(String la){
		la = "lala";
		logger.debug("get /la");
		return "hello " + la;
	}
	
	
	@RequestMapping("/item")
	private String getItem(){		
		return String.valueOf(firstDemoService.findByTitle("aa").size());
	}

}
