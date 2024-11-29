package pl.bilskik.citifier.core.docker.entity;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ComposeService {
    private String image;
    private String containerName;
    private Map<String, String> environments;
    private List<Port> ports;
    private List<Volume> volumes;
    private Entrypoint entrypoint;
    private Command command;
}
