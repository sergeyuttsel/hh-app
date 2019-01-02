package hh.controller;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import hh.service.VacanciesService;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONArray;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@AllArgsConstructor
public class VacanciesController {

    private final VacanciesService vacanciesService;

    public static void main(String[] args) {
        ReadContext ctx = JsonPath.parse("{\"items\": [1, 2, 3]}");
        JSONArray jsonArray = JsonPath.read("{\"items\": [{\"qwer\":1}, 2, 3]}", "$.items[*]");
        System.out.println(jsonArray.size());
    }

    @RequestMapping(value = "/getvacancies")
    public String getmessages(@RequestParam("dateFrom") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ") String dateFrom, @RequestParam("dateTo") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ") String dateTo) {
        vacanciesService.getAndSaveVacancies(dateFrom, dateTo);

        return "Documents were saved in elasticsearch";

    }

}