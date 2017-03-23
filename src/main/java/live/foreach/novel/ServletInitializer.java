package live.foreach.novel;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import ChuangAo.WebSite.util.commonTimer;


public class ServletInitializer extends SpringBootServletInitializer {  
	  
    @Override  
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {  
    	commonTimer.timerTasks();	//timer
    	return application.sources(Application.class);  
    }  
    
    
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

       return (container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/403.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
            //ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");

            container.addErrorPages(error401Page, error404Page);
       });
    }
   
  
}  