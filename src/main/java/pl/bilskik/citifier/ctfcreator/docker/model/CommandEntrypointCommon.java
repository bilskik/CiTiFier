package pl.bilskik.citifier.ctfcreator.docker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.bilskik.citifier.ctfcreator.docker.model.enumeration.CommandType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract sealed class CommandEntrypointCommon permits Command, Entrypoint {
    protected List<String> command;
    protected CommandType type;
}
