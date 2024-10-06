package pl.bilskik.citifier.ctfcreator.docker.model;

import pl.bilskik.citifier.ctfcreator.docker.model.enumeration.CommandType;

import java.util.List;


public final class Entrypoint extends CommandEntrypointCommon {
    public Entrypoint(List<String> command, CommandType type) {
        super(command, type);
    }
}
