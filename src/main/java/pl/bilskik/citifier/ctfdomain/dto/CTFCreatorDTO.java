package pl.bilskik.citifier.ctfdomain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CTFCreatorDTO {

    private Long ctfCreatorId;
    private String login;
    private String password;

}
