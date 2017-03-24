package live.foreach.novel;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;


public class ServletInitializer extends SpringBootServletInitializer {  
	  
    @Override  
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {  
    	return application.sources(Application.class);  
    }  
    
    
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
    	//TODO: errorpage
    	return null;
    }
   
  
}  