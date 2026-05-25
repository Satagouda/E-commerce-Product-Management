package com.ecommerce.config;




import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MeiliConfig {

    @Value("${meilisearch.host}")
    private String host;

    @Value("${meilisearch.api-key}")
    private String apiKey;

    @Bean
    public Client meiliClient() {
        Config config = new Config(host, apiKey);

        return new Client(config);
    }
}
