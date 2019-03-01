package io.softserve.goadventures.auth.config;

//import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import io.softserve.goadventures.auth.filters.LoginFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApplicationConfiguration {
    @Bean
    public FilterRegistrationBean dawsonApiFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LoginFilter());
// specific URl patterns
        registration.addUrlPatterns("/res/reg");
        //registration.addUrlPatterns("/content/*");
        return registration;
    }
}