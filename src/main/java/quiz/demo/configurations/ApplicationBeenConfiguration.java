package quiz.demo.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class ApplicationBeenConfiguration {

    private  ModelMapper modelMapper;

   @Bean
    public ModelMapper modelMapper(){
       this.modelMapper = new ModelMapper();
       return  this.modelMapper;
   }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }
}
