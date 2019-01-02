package hh

import com.fasterxml.jackson.databind.ObjectMapper
import hh.model.Vacancy
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.Wait
import javax.annotation.PreDestroy

@TestConfiguration
class ElasticTestConfiguration {
    public static final String ELASTIC_IMAGE = "urlDocker.net/elasticsearch:6.4.0"
    public static final int ELASTIC_PORT = 9200

    public static GenericContainer ELASTIC_CONTAINER =
            new GenericContainer(ELASTIC_IMAGE)
                    .withExposedPorts(ELASTIC_PORT)
                    .waitingFor(Wait.forHttp("/"))
                    .withEnv("xpack.security.enabled", "false")
                    .withEnv("network.host", "_site_")
                    .withEnv("network.publish_host", "_local_")

    static {
        ELASTIC_CONTAINER.start()
    }

    @Primary
    @Bean(destroyMethod = "close")
    RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(ELASTIC_CONTAINER.getContainerIpAddress(), ELASTIC_CONTAINER.getMappedPort(ELASTIC_PORT), "http")))
    }

    @Bean
    ElasticUtils.Repository<Vacancy> vacancyRepository(RestHighLevelClient restHighLevelClient, ObjectMapper mapper) {
        return ElasticUtils.create(restHighLevelClient, mapper, Vacancy.class)
    }

    @PreDestroy
    void destroyContainer() {
        ELASTIC_CONTAINER.stop()
    }
}
