package pl.bilskik.citifier.ctfcreator.kubernetes;

import org.apache.commons.lang3.StringUtils;
import pl.bilskik.citifier.ctfcreator.docker.model.Port;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class K8sResourceUtils {

    private static final Integer MAX_LENGTH = 20;
    private static final Integer MIN_PORT_RANGE = 1024;
    private static final Integer MAX_PORT_RANGE = 65536;

    public static String provideRandomCharacters() {
        return UUID.randomUUID().toString().replace("-", "").substring(MAX_LENGTH);
    }

    public static Integer providePortToApplication(List<Port> ports) {
        if(ports == null) {
            return null; //throw exception
        }
        Port port = ports.getFirst(); //simple case -> one port mapping
        if(port != null && isValidPort(port.getHostPort())) {
            return Integer.parseInt(port.getHostPort());
        }
        return null; //throw exception
    }

    private static boolean isValidPort(String value) {
        return StringUtils.isNumeric(value);
    }
}
