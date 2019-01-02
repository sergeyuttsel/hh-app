package hh.config;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchConfig {

    private final ElasticsearchProperties elasticProperties;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(elasticProperties.getHttpHosts()));
    }
}
