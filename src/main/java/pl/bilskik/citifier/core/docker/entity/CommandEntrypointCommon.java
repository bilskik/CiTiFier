package pl.bilskik.citifier.core.docker.entity;

import lombok.*;
import pl.bilskik.citifier.core.docker.enumeration.CommandType;

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
