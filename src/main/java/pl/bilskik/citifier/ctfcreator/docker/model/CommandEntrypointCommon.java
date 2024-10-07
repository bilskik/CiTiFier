package pl.bilskik.citifier.ctfcreator.docker.model;

import lombok.*;
import pl.bilskik.citifier.ctfcreator.docker.model.enumeration.CommandType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public abstract sealed class CommandEntrypointCommon permits Command, Entrypoint {
    protected List<String> command;
    protected CommandType type;
}
