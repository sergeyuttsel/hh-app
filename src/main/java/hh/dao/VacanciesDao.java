package hh.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hh.model.Vacancy;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class VacanciesDao {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RestHighLevelClient client;

    @Value("${elasticsearch.index.vacancies}")
    private String vacanciesIndex;

    public void saveAllVacancies(List<Vacancy> vacanciesList) {
        if (vacanciesList.isEmpty()) {
            return;
        }
        try {
            BulkRequest request = new BulkRequest();
            for (Vacancy vacancy : vacanciesList) {
                request.add(prepareSingleRequest(vacancy));
            }
            BulkResponse bulk = client.bulk(request);
            if (bulk.hasFailures()) {
                throw new RuntimeException(bulk.buildFailureMessage());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private IndexRequest prepareSingleRequest(Vacancy vacancy) throws JsonProcessingException {
        String id = vacancy.getId();
        String document = mapper.writeValueAsString(vacancy);
        return new IndexRequest(vacanciesIndex, "doc", id)
                .source(document, XContentType.JSON);
    }

}