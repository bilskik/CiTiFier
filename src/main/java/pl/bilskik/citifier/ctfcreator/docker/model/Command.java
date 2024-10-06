package pl.bilskik.citifier.ctfcreator.docker.model;

import pl.bilskik.citifier.ctfcreator.docker.model.enumeration.CommandType;

import java.util.List;

public final class Command extends CommandEntrypointCommon {
    public Command(List<String> command, CommandType type) {
        super(command, type);
    }
}
