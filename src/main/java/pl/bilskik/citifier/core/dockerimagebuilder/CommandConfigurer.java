package pl.bilskik.citifier.core.dockerimagebuilder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class CommandConfigurer {

    private final static String IPV4_REGEX = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";

    @Value("${docker.host-ip-address}")
    private String hostIpAddress;

    @Value("${docker.docker-registry-port}")
    private String registryPort;

    @Value("${docker.build}")
    private String dockerBuildCommand;

    @Value("${docker.push}")
    private String dockerPushCommand;

    @Value("${docker.tag}")
    private String dockerTagCommand;

    public String getDockerBuild() {
        return dockerBuildCommand;
    }

    public String getImageTagCommand(String imageName) {
        validateIpAddressAndRegistryPort();
        return dockerTagCommand + " " + imageName + " " + hostIpAddress + ":" + registryPort + "/" + imageName;
    }

    public String getImagePushToRegistryCommand(String imageName) {
        validateIpAddressAndRegistryPort();
        return dockerPushCommand + " " + hostIpAddress + ":" + registryPort + "/" + imageName;
    }

    private void validateIpAddressAndRegistryPort() {
        Pattern pattern = Pattern.compile(IPV4_REGEX);
        if(!pattern.matcher(hostIpAddress).matches()) {
            log.error("Invalid hostIpAddress {}!", hostIpAddress);
            throw new DockerImageBuilderException(String.format("Nieprawidłowy adres IP hoosta %s!", hostIpAddress));
        }
        if (registryPort == null || registryPort.isEmpty() || !StringUtils.isNumeric(registryPort)) {
            log.error("Invalid registry port {}!", registryPort);
            throw new DockerImageBuilderException(String.format("Nieprawidłowy adres IP rejestru %s!", registryPort));
        }
    }

}
