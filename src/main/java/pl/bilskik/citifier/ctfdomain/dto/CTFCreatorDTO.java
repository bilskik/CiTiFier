package pl.bilskik.citifier.ctfdomain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CTFCreatorDTO {

    private Long ctfCreatorId;
    private String login;
    private String password;

}
