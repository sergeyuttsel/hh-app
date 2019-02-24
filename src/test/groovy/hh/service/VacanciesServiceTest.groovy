package hh.service

import com.google.gson.Gson
import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.ReadContext
import com.jayway.jsonpath.spi.json.GsonJsonProvider
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider
import hh.converter.VacancyConverter
import hh.dao.VacanciesDao
import hh.model.Vacancy
import org.mockito.ArgumentCaptor
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

import static org.mockito.ArgumentMatchers.anyList
import static org.mockito.Mockito.*

class VacanciesServiceTest extends Specification {

    def vacanciesDao = mock(VacanciesDao.class)

    def restClient = mock(RESTclient.class)

    def "Should save 1 vacancy from 1 page"() {
        given:
        ArgumentCaptor<List<Vacancy>> vacancyCaptor = ArgumentCaptor.forClass(List.class)

        URL url = new String().getClass().getResource("/responses/OneVacancyOnePage/Page1.json")
        def jsonResponse = new String(Files.readAllBytes(Paths.get(url.toURI())))
        HashMap<String, String> params1 = new HashMap<>()
        params1.put("date_from", "2018-12-28T17:00:00+0300")
        params1.put("date_to", "2018-12-28T17:09:00+0300")
        when(restClient.getPageWithVacancies(params1)).thenReturn(jsonResponse)

        HashMap<String, String> params2 = new HashMap<>()
        params2.put("date_from", "2018-12-28T17:00:00+0300")
        params2.put("date_to", "2018-12-28T17:09:00+0300")
        params2.put("page", "0")
        when(restClient.getPageWithVacancies(params2)).thenReturn(jsonResponse)

        vacancyRestMock("/responses/OneVacancyOnePage/Vacancy1.json")

        def vacanciesService = new VacanciesService(vacanciesDao, new VacancyConverter(), restClient)

        when:
        vacanciesService.getAndSaveVacancies("2018-12-28T17:00:00+0300", "2018-12-28T17:09:00+0300")

        then:
        verify(vacanciesDao, times(1)).saveAllVacancies(anyList())
        verify(vacanciesDao).saveAllVacancies(vacancyCaptor.capture())
        vacancyCaptor.getValue().get(0).getId() == "29289646"
    }

    def "Should save 7 vacancy from 2 page"() {
        given:

        ArgumentCaptor<List<Vacancy>> vacancyCaptor = ArgumentCaptor.forClass(List.class)

        URL urlPage1 = Object.class.getResource("/responses/SevenVacancyTwoPage/Page1.json")
        def jsonPage1 = new String(Files.readAllBytes(Paths.get(urlPage1.toURI())))
        HashMap<String, String> params1 = new HashMap<>()
        params1.put("date_from", "2018-12-28T17:00:00+0300")
        params1.put("date_to", "2018-12-28T17:09:00+0300")
        when(restClient.getPageWithVacancies(params1)).thenReturn(jsonPage1)

        HashMap<String, String> params2 = new HashMap<>()
        params2.put("date_from", "2018-12-28T17:00:00+0300")
        params2.put("date_to", "2018-12-28T17:09:00+0300")
        params2.put("page", "0")
        when(restClient.getPageWithVacancies(params2)).thenReturn(jsonPage1)

        URL urlPage2 = Object.class.getResource("/responses/SevenVacancyTwoPage/Page2.json")
        def jsonPage2 = new String(Files.readAllBytes(Paths.get(urlPage2.toURI())))

        HashMap<String, String> params3 = new HashMap<>()
        params3.put("date_from", "2018-12-28T17:00:00+0300")
        params3.put("date_to", "2018-12-28T17:09:00+0300")
        params3.put("page", "1")
        when(restClient.getPageWithVacancies(params3)).thenReturn(jsonPage2)

        vacancyRestMock("/responses/SevenVacancyTwoPage/Vacancy1.json")
        vacancyRestMock("/responses/SevenVacancyTwoPage/Vacancy2.json")
        vacancyRestMock("/responses/SevenVacancyTwoPage/Vacancy3.json")
        vacancyRestMock("/responses/SevenVacancyTwoPage/Vacancy4.json")
        vacancyRestMock("/responses/SevenVacancyTwoPage/Vacancy5.json")
        vacancyRestMock("/responses/SevenVacancyTwoPage/Vacancy6.json")
        vacancyRestMock("/responses/SevenVacancyTwoPage/Vacancy7.json")

        def vacanciesService = new VacanciesService(vacanciesDao, new VacancyConverter(), restClient)

        when:
        vacanciesService.getAndSaveVacancies("2018-12-28T17:00:00+0300", "2018-12-28T17:09:00+0300")

        then:
        verify(vacanciesDao, times(1)).saveAllVacancies(anyList())
        verify(vacanciesDao).saveAllVacancies(vacancyCaptor.capture())
        vacancyCaptor.getValue().get(0).getId() == "29422249"
        vacancyCaptor.getValue().get(1).getId() == "29274702"
        vacancyCaptor.getValue().get(2).getId() == "29484238"
        vacancyCaptor.getValue().get(3).getId() == "29484191"
        vacancyCaptor.getValue().get(4).getId() == "23061106"
        vacancyCaptor.getValue().get(5).getId() == "28659264"
        vacancyCaptor.getValue().get(6).getId() == "28469894"
    }

    private vacancyRestMock(String pathJsonVacancy) {
        URL urlVacancy = Object.class.getResource(pathJsonVacancy)
        def jsonVacancy = new String(Files.readAllBytes(Paths.get(urlVacancy.toURI())))

        Gson gsonObject = new Gson()
        ReadContext ctx = JsonPath.parse(jsonVacancy, createJsonPathConfiguration())
        def id = gsonObject.toJsonTree(ctx.read("\$.id")).getAsString()

        when(restClient.getVacancyAsJson(id)).thenReturn(jsonVacancy)
    }

    private def createJsonPathConfiguration() {
        return new Configuration.ConfigurationBuilder()
                .jsonProvider(new GsonJsonProvider())
                .mappingProvider(new GsonMappingProvider())
                .build();
    }

}