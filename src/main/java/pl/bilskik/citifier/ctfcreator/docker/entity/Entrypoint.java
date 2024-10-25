package pl.bilskik.citifier.ctfcreator.docker.entity;

import lombok.EqualsAndHashCode;
import pl.bilskik.citifier.ctfcreator.docker.enumeration.CommandType;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public final class Entrypoint extends CommandEntrypointCommon {
    public Entrypoint(List<String> command, CommandType type) {
        super(command, type);
    }
}
