package pl.bilskik.citifier.ctfcreator.docker.entity;

import lombok.*;
import pl.bilskik.citifier.ctfcreator.docker.enumeration.CommandType;

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
