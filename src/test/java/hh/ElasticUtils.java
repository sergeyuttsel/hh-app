package hh;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

public class ElasticUtils {

    @RequiredArgsConstructor
    public static class Repository<T> {
        private final RestHighLevelClient client;
        private final ObjectMapper mapper;
        private final Class<T> clazz;

        public T findById(String index, String id) throws IOException {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.idsQuery().addIds(id));

            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.source(searchSourceBuilder);
            searchRequest.types("doc");

            SearchResponse searchResponse = client.search(searchRequest);

            if (searchResponse.getHits().getTotalHits() > 0) {
                T entity = mapper.readValue(searchResponse.getHits().getHits()[0].getSourceAsString(), clazz);

                return entity;
            } else {
                return null;
            }
        }

    }

    public static <T> Repository<T> create(RestHighLevelClient client, ObjectMapper objectMapper, Class<T> clazz) {
        return new Repository<>(client, objectMapper, clazz);
    }
}
