package hh.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vacancy {
    private String id;
    private String name;
    private String salaryFrom;
    private String salaryTo;
    private String currency;
    private String employer;
    private String experience;
    private String description;
}
