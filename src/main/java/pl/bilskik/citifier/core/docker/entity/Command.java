package pl.bilskik.citifier.core.docker.entity;

import lombok.EqualsAndHashCode;
import pl.bilskik.citifier.core.docker.enumeration.CommandType;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public final class Command extends CommandEntrypointCommon {
    public Command(List<String> command, CommandType type) {
        super(command, type);
    }
}
