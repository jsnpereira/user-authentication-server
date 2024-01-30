package com.user.autentication.security.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI userAuthenticationServerOpenAPI(){
        OpenAPI open = new OpenAPI()
                .info(new Info().title("User Authentication Service")
                        .description("Run the oauth api server")
                        .version("1.0"));
        return open;
    }
}
