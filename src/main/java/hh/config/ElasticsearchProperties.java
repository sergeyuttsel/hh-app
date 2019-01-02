package hh.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties("elasticsearch.client")
public class ElasticsearchProperties {

    private List<ElasticNode> nodes;

    public HttpHost[] getHttpHosts() {
        return nodes.stream()
                .map(node -> new HttpHost(node.getHost(), node.getPort(), "http"))
                .toArray(HttpHost[]::new);
    }

    @Data
    public static class ElasticNode {
        private String host;
        private int port;
    }
}
