package hh.service

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

    def "Should save 1 vacancy from 1 page"() {
        given:
        def vacanciesDao = mock(VacanciesDao.class)
        ArgumentCaptor<List<Vacancy>> vacancyCaptor = ArgumentCaptor.forClass(List.class)

        def restClient = mock(RESTclient.class)

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

        URL url1 = new String().getClass().getResource("/responses/OneVacancyOnePage/Vacancy1.json")
        def jsonResponseVacancy = new String(Files.readAllBytes(Paths.get(url1.toURI())))
        when(restClient.getVacancyAsJson("29289646")).thenReturn(jsonResponseVacancy)

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
        def vacanciesDao = mock(VacanciesDao.class)

        ArgumentCaptor<List<Vacancy>> vacancyCaptor = ArgumentCaptor.forClass(List.class)

        def restClient = mock(RESTclient.class)

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

        URL urlVacancy1 = Object.class.getResource("/responses/SevenVacancyTwoPage/Vacancy1.json")
        def jsonVacancy1 = new String(Files.readAllBytes(Paths.get(urlVacancy1.toURI())))
        when(restClient.getVacancyAsJson("29422249")).thenReturn(jsonVacancy1)

        URL urlVacancy2 = Object.class.getResource("/responses/SevenVacancyTwoPage/Vacancy2.json")
        def jsonVacancy2 = new String(Files.readAllBytes(Paths.get(urlVacancy2.toURI())))
        when(restClient.getVacancyAsJson("29274702")).thenReturn(jsonVacancy2)

        URL urlVacancy3 = Object.class.getResource("/responses/SevenVacancyTwoPage/Vacancy3.json")
        def jsonVacancy3 = new String(Files.readAllBytes(Paths.get(urlVacancy3.toURI())))
        when(restClient.getVacancyAsJson("29484238")).thenReturn(jsonVacancy3)

        URL urlVacancy4 = Object.class.getResource("/responses/SevenVacancyTwoPage/Vacancy4.json")
        def jsonVacancy4 = new String(Files.readAllBytes(Paths.get(urlVacancy4.toURI())))
        when(restClient.getVacancyAsJson("29484191")).thenReturn(jsonVacancy4)

        URL urlVacancy5 = Object.class.getResource("/responses/SevenVacancyTwoPage/Vacancy5.json")
        def jsonVacancy5 = new String(Files.readAllBytes(Paths.get(urlVacancy5.toURI())))
        when(restClient.getVacancyAsJson("23061106")).thenReturn(jsonVacancy5)

        URL urlVacancy6 = Object.class.getResource("/responses/SevenVacancyTwoPage/Vacancy6.json")
        def jsonVacancy6 = new String(Files.readAllBytes(Paths.get(urlVacancy6.toURI())))
        when(restClient.getVacancyAsJson("28659264")).thenReturn(jsonVacancy6)

        URL urlVacancy7 = Object.class.getResource("/responses/SevenVacancyTwoPage/Vacancy7.json")
        def jsonVacancy7 = new String(Files.readAllBytes(Paths.get(urlVacancy7.toURI())))
        when(restClient.getVacancyAsJson("28469894")).thenReturn(jsonVacancy7)

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

}