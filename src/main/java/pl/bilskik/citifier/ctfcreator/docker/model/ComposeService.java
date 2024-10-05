package pl.bilskik.citifier.ctfcreator.docker.model;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ComposeService {
    private String image;
    private String containerName;
    private Map<String, String> environments;
    private Map<String, String> ports;
    private List<Volume> volumes;
}
