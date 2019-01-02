package hh.service;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Scheduler {
    @Value("${hh.url}")
    private String urlApp;

    private final String templateParamsForUrlListVacancies = "getvacancies?dateFrom={dateFrom}&dateTo={dateTo}";

    private final int timeout = 1;

    @Scheduled(cron = "0 0/" + timeout + " * * * *")
    public void upload() {
        ZonedDateTime dateTo = ZonedDateTime.now();
        ZonedDateTime dateFrom = dateTo.minusMinutes(timeout);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        String strDateFrom = dateFrom.format(formatter);
        String strDateTo = dateTo.format(formatter);

        getVacancies(strDateFrom, strDateTo);
    }

    private String getVacancies(String dateFrom, String dateTo) {
        Response response = RestAssured.given()
                .pathParam("dateFrom", dateFrom)
                .pathParam("dateTo", dateTo)
                .get(urlApp + templateParamsForUrlListVacancies);
        return response.getBody().asString();

    }

}
