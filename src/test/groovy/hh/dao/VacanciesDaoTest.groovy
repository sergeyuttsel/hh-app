package hh.dao

import hh.ElasticTestConfiguration
import hh.ElasticUtils
import hh.model.Vacancy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest(classes = [ElasticTestConfiguration])
@ActiveProfiles("test")
class VacanciesDaoTest extends Specification {
    @Autowired
    private VacanciesDao vacanciesDao

    @Autowired
    private ElasticUtils.Repository<Vacancy> auditSummaryRepository

    @Value('${elasticsearch.index.vacancies}')
    private String vacanciesIndex

    def "Should save vacancy in elasticsearch"() {
        given:
        def vacancyId1 = "id1"
        def vacancyId2 = "id2"
        def vacancies = [Vacancy.builder().id(vacancyId1).description("{\"salary\": 1000}").build(),
                         Vacancy.builder().id(vacancyId2).description("{\"salary\": 2000}").build()
        ]

        when:
        vacanciesDao.saveAllVacancies(vacancies)
        sleep(1500)

        then:
        vacancies[0] == auditSummaryRepository.findById(vacanciesIndex, vacancyId1)
        vacancies[1] == auditSummaryRepository.findById(vacanciesIndex, vacancyId2)
    }


}
