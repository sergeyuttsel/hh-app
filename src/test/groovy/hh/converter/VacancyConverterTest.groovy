package hh.converter

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class VacancyConverterTest extends Specification {
    private converter = new VacancyConverter()

    def "Should convert vacancy with all fields"() {
        given:
        URL url = Object.class.getResource("/VacanciesForConverter/VacancyWithAllFields.json")
        def jsonVacancy = new String(Files.readAllBytes(Paths.get(url.toURI())))

        when:
        def vacancy = converter.convert(jsonVacancy)

        then:
        vacancy.getId() == "29422249"
        vacancy.getName() == "Менеджер"
        vacancy.getSalaryFrom() == "38000"
        vacancy.getSalaryTo() == "41234"
        vacancy.getCurrency() == "RUR"
        vacancy.getEmployer() == "Содействие детям"
        vacancy.getExperience() == "Нет опыта"
        vacancy.getDescription().startsWith("Наша организация  Содействие-детям  имеет")
    }

    def "Should convert vacancy without some fields and with some null fields"() {
        given:
        URL url = Object.class.getResource("/VacanciesForConverter/VacancyWithoutSomeFields.json")
        def jsonVacancy = new String(Files.readAllBytes(Paths.get(url.toURI())))

        when:
        def vacancy = converter.convert(jsonVacancy)

        then:
        vacancy.getId() == "29422249"
        vacancy.getName() == "Менеджер"
        vacancy.getSalaryFrom() == null
        vacancy.getSalaryTo() == null
        vacancy.getCurrency() == "RUR"
        vacancy.getEmployer() == "Содействие детям"
        vacancy.getExperience() == "Нет опыта"
        vacancy.getDescription().startsWith("Наша организация  Содействие-детям  имеет")
    }
}
