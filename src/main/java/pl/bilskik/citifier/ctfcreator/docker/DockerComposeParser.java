package pl.bilskik.citifier.ctfcreator.docker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.docker.model.*;
import pl.bilskik.citifier.ctfcreator.docker.model.enumeration.CommandType;
import pl.bilskik.citifier.ctfcreator.docker.model.enumeration.VolumeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static pl.bilskik.citifier.ctfcreator.docker.TypeConverter.*;

@Service
@Slf4j
public class DockerComposeParser {

    private final static String VERSION = "version";
    private final static String SERVICES = "services";
    private final static String IMAGE = "image";
    private final static String CONTAINER_NAME = "container_name";
    private final static String ENVIRONMENT = "environment";
    private final static String PORTS = "ports";
    private final static String VOLUMES = "volumes";
    private final static String COMMAND = "command";
    private final static String ENTRYPOINT = "entrypoint";

    private final static String SERVICES_NOT_FOUND = "Cannot find services!";

    @SuppressWarnings("unchecked")
    public DockerCompose parse(Map<String, Object> data) {
        DockerCompose compose = new DockerCompose();
        compose.setVersion((String) data.get(VERSION));
        compose.setServices(new HashMap<>());
        Map<String, Volume> volumes = parseVolumes((Map<String, Map<String, Object>>) data.get(VOLUMES));
        compose.setVolumes(volumes);
        if(data.get(SERVICES) == null) {
            throw new DockerComposeParserException(SERVICES_NOT_FOUND);
        }
        parseServices((Map<String, Map<String, Object>>) data.get(SERVICES), compose);

        return compose;
    }

    private Map<String, Volume> parseVolumes(Map<String, Map<String, Object>> volumeMap) {
        if(volumeMap == null || volumeMap.isEmpty()) {
            log.info("Skipping volumes! No value provided.");
            return new HashMap<>();
        }

        Map<String, Volume> volumeList = new HashMap<>();
        for(var volumeEntry: volumeMap.entrySet()) {
            Volume volumeObj = new Volume();
            volumeObj.setVolumeName(volumeEntry.getKey());
            volumeObj.setVolumeType(VolumeType.VOLUME);

            volumeList.put(volumeEntry.getKey(), volumeObj);
        }


        return volumeList;
    }

    private void parseServices(Map<String, Map<String, Object>> services, DockerCompose compose) {
        if(services == null || services.isEmpty()) {
            log.info("No services found in the Docker Compose configuration");
            return;
        }

        services.forEach((serviceName, serviceData) -> {
            if(serviceData == null || serviceData.isEmpty()) {
                log.info("Skipping service {}: no data found.", serviceName);
                return;
            }

            ComposeService composeService = new ComposeService();
            composeService.setImage((String) serviceData.get(IMAGE));
            composeService.setContainerName((String) serviceData.get(CONTAINER_NAME));
            BiConsumer<String, Map<String, String>> envFunc = (val, map) -> {
                String[] parts = val.split("=");
                if(parts.length == 2) {
                    map.put(parts[0], parts[1]);
                }
            };
            composeService.setEnvironments(genericMapListParser(serviceData.get(ENVIRONMENT), envFunc));
            BiConsumer<String, Map<String, String>> portFunc = (val, map) -> {
                if(val.contains(":") && val.contains("/")) { //TO DO implement advanced case
                    String[] parts = val.split(":");
                    if(parts.length == 2 && parts[1].contains("/")) {
                        map.put(parts[0], parts[1].split("/")[0]);
                    }
                } else if(val.contains(":")){
                    String[] parts = val.split(":");
                    if(parts.length == 2) {
                        map.put(parts[0], parts[1]);
                    }
                } else {
                    map.put(val, val);
                }
            };
            composeService.setPorts(genericMapListParser(serviceData.get(PORTS), portFunc));
            composeService.setVolumes(parseServiceVolume(serviceData.get(VOLUMES), compose.getVolumes()));
            composeService.setEntrypoint(parseEntrypoint(serviceData.get(ENTRYPOINT)));
            composeService.setCommand(parseCommand(serviceData.get(COMMAND)));

            compose.getServices().put(serviceName, composeService);
        });

    }

    private Entrypoint parseEntrypoint(Object o) {
        return parseCommandOrEntrypoint(o, Entrypoint::new);
    }

    private Command parseCommand(Object o) {
        return parseCommandOrEntrypoint(o, Command::new);
    }

    private <T> T parseCommandOrEntrypoint(Object o, BiFunction<List<String>, CommandType, T> constructor) {
        if(o instanceof String) {
            return constructor.apply(new ArrayList<>(){{ add((String)o);}}, CommandType.SHELL);
        } else if(o instanceof List<?>) {
            List<String> list = convertToStringList((List<?>)o);
            return constructor.apply(list, CommandType.EXEC);
        }
        return null;
    }

    private List<Volume> parseServiceVolume(Object values, Map<String, Volume> existingVolumes) {
        List<Volume> volumeList = new ArrayList<>();
        if(!(values instanceof List)) {
            log.info("Skipping service volumes provided. No value provided!");
            return volumeList;
        }

        for(var val: (List<?>) values) {
            if(val == null) {
                continue;
            }

            String stringVal = val.toString();
            String[] parts = stringVal.split(":");
            if(parts.length == 2) {
                if(isHostPath(parts[0])) {
                    volumeList.add(createBindMountVolume(parts[0], parts[1]));
                } else if(existingVolumes.containsKey(parts[0])) {
                    volumeList.add(createNamedVolume(parts[0], parts[1]));
                }
            }
        }
        return volumeList;
    }

    private boolean isHostPath(String path) {
        return path.contains("/");
    }

    private Volume createBindMountVolume(String hostPath, String containerPath) {
        Volume volume = new Volume();
        volume.setVolumeType(VolumeType.BIND_MOUNT);
        volume.setHostPath(hostPath);
        volume.setContainerPath(containerPath);
        return volume;
    }

    private Volume createNamedVolume(String volumeName, String containerPath) {
        Volume volume = new Volume();
        volume.setVolumeName(volumeName);
        volume.setVolumeType(VolumeType.VOLUME);
        volume.setContainerPath(containerPath);
        return volume;
    }

    private Map<String, String> genericMapListParser(Object values, BiConsumer<String, Map<String, String>> callback) {
        if(values instanceof List<?>) {
            List<String> list = convertToStringList((List<?>)values);
            Map<String, String> outputMap = new HashMap<>();
            for(var val : list) {
                if(val != null && !val.isEmpty()) {
                    callback.accept(val, outputMap);
                }
            }
            return outputMap;
        } else if(values instanceof Map<?,?>) {
            return convertToMapString((Map<?,?>) values);
        }

        return new HashMap<>();
    }
}
