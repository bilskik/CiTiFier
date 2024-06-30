package pl.bilskik.citifier.ctfdomain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {

    private Long studentId;
    private String login;
    private String password;

}
