package hh.converter;

import hh.model.Vacancy;
import hh.util.JsonUtil;
import org.springframework.stereotype.Component;

@Component
public class VacancyConverter {

    public Vacancy convert(String strJsonVacancy) {
        JsonUtil jsonUtil = new JsonUtil();
        String id = jsonUtil.getValueFromJson("$.id", strJsonVacancy);
        String name = jsonUtil.getValueFromJson("$.name", strJsonVacancy);
        String salaryFrom = jsonUtil.getValueFromJson("$.salary.from", strJsonVacancy);
        String salaryTo = jsonUtil.getValueFromJson("$.salary.to", strJsonVacancy);
        String currency = jsonUtil.getValueFromJson("$.salary.currency", strJsonVacancy);
        String employer = jsonUtil.getValueFromJson("$.employer.name", strJsonVacancy);
        String experience = jsonUtil.getValueFromJson("$.experience.name", strJsonVacancy);
        String description = removeHtmlTags(jsonUtil.getValueFromJson("$.description", strJsonVacancy));

        return new Vacancy(id, name, salaryFrom, salaryTo, currency, employer, experience, description);
    }

    private String removeHtmlTags(String htmlText) {
        return htmlText.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
    }

}