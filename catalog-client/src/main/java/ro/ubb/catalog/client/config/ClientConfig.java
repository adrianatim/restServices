package ro.ubb.catalog.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ro.ubb.catalog.client.rest.RestService;
import ro.ubb.catalog.client.ui.Console;


@Configuration
@ComponentScan("ro.ubb.catalog.client")
public class ClientConfig {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    RestService restService() {
        return new RestService();
    }

    @Bean
    Console console() {
        return new Console();
    }

}
