package pl.bilskik.citifier.ctfcreator.docker.model;

import lombok.EqualsAndHashCode;
import pl.bilskik.citifier.ctfcreator.docker.model.enumeration.CommandType;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public final class Entrypoint extends CommandEntrypointCommon {
    public Entrypoint(List<String> command, CommandType type) {
        super(command, type);
    }
}
