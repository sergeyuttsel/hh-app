package hh.service;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import hh.converter.VacancyConverter;
import hh.dao.VacanciesDao;
import hh.model.Vacancy;
import hh.util.JsonUtil;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class VacanciesService {
    private final VacanciesDao vacanciesDao;

    private final VacancyConverter vacancyConverter;

    private final RESTclient restClient;

    public void getAndSaveVacancies(String dateFrom, String dateTo) {
        int countPages = getCountPages(dateFrom, dateTo);
        List<String> vacanciesIds = getIdsVacancies(countPages, dateFrom, dateTo);
        List<Vacancy> vacanciesList = getVacanciesByIds(vacanciesIds);
        vacanciesDao.saveAllVacancies(vacanciesList);

    }

    private int getCountPages(String dateFrom, String dateTo) {
        HashMap<String, String> params = new HashMap<>();
        params.put("date_from", dateFrom);
        params.put("date_to", dateTo);
        String jsonShortVacancies = restClient.getPageWithVacancies(params);

        Gson gsonObject = new Gson();
        JsonUtil jsonUtil = new JsonUtil();
        ReadContext ctx = JsonPath.parse(jsonShortVacancies, jsonUtil.createJsonPathConfiguration());

        return gsonObject.toJsonTree(ctx.read("$.pages")).getAsInt();
    }

    private List<String> getIdsVacancies(int countPages, String dateFrom, String dateTo) {
        List<String> vacanciesIds = new ArrayList<>();
        for (int i = 0; i <= countPages - 1; i++) {
            HashMap<String, String> params = new HashMap<>();
            params.put("date_from", dateFrom);
            params.put("date_to", dateTo);
            params.put("page", Integer.toString(i));
            String jsonRequestBody = restClient.getPageWithVacancies(params);

            JSONArray arrayShortVacancies = JsonPath.read(jsonRequestBody, "$.items[*]");
            for (int j = 0; j <= arrayShortVacancies.size() - 1; j++) {
                ReadContext ctx = JsonPath.parse(jsonRequestBody);
                String id = ctx.read("$.items[" + j + "].id");
                vacanciesIds.add(id);
            }
        }
        return vacanciesIds;

    }

    private List<Vacancy> getVacanciesByIds(List<String> vacanciesIds) {
        List<Vacancy> listVacancy = new ArrayList<>();
        vacanciesIds.forEach(id -> {
            String vacancyJson = restClient.getVacancyAsJson(id);
            Vacancy vacancy = vacancyConverter.convert(vacancyJson);
            listVacancy.add(vacancy);

        });
        return listVacancy;

    }

}
