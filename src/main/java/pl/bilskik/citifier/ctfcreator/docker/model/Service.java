package pl.bilskik.citifier.ctfcreator.docker.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Service {
    private String image;
    private String container_name;
    private Object environment;
    private Object volumes;
}
