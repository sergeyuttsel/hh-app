package hh.service;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RESTclient {
    @Value("${hh-api.url.vacanciesList}")
    private String urlListVacancies;

    @Value("${hh-api.url.vacancy}")
    private String urlVacancy;

    private String templateParamsForUrlListVacancies = "?per_page={per_page}&page={page}&area={area}&specialization={specialization}&date_from={date_from}&date_to={date_to}";

    public String getPageWithVacancies(Map<String, String> newParams) {
        HashMap<String, String> params = new HashMap<>();
        params.put("per_page", "5");
        params.put("page", "0");
        params.put("area", "2");
        params.put("specialization", "1");
        params.putAll(newParams);
        Response response = RestAssured.given()
                .pathParams(params)
                .get(urlListVacancies + templateParamsForUrlListVacancies);
        return response.getBody().asString();

    }

    public String getVacancyAsJson(String id) {
        Response response = RestAssured.get(urlVacancy + id);
        return response.getBody().asString();
    }

}
