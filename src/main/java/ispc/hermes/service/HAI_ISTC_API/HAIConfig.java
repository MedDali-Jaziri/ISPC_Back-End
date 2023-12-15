package ispc.hermes.service.HAI_ISTC_API;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HAIConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
