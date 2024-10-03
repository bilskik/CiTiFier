package pl.bilskik.citifier.ctfcreator.docker;

import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcore.login.LoginService;
import pl.bilskik.citifier.ctfcreator.docker.model.ComposeService;
import pl.bilskik.citifier.ctfcreator.docker.model.DockerCompose;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class DockerComposeParser {

    private final static String VERSION = "version";
    private final static String SERVICES = "services";
    private final static String IMAGE = "image";
    private final static String CONTAINER_NAME = "container_name";
    private final static String ENVIRONMENT = "environment";
    private final static String PORTS = "ports";
    private final static String VOLUMES = "volumes";

    private final static String SERVICES_NOT_FOUND = "Cannot find services!";
    private final LoginService loginService;

    public DockerComposeParser(LoginService loginService) {
        this.loginService = loginService;
    }

    @SuppressWarnings("unchecked")
    public DockerCompose parse(Map<String, Object> data) {
        DockerCompose compose = new DockerCompose();
        compose.setVersion((String) data.get(VERSION));
        compose.setServices(new HashMap<>());

        if(data.get(SERVICES) == null) {
            throw new DockerComposeParserException(SERVICES_NOT_FOUND);
        }
        parseServices((Map<String, Map<String, Object>>) data.get(SERVICES), compose);

        System.out.println(compose.toString());
        return compose;
    }

    private void parseServices(Map<String, Map<String, Object>> services, DockerCompose compose) {

        services.entrySet().stream().forEach(serviceEntry -> {
            Map<String, Object> service = serviceEntry.getValue();

            ComposeService composeService = new ComposeService();
            composeService.setImage((String) service.get(IMAGE));
            composeService.setContainerName((String) service.get(CONTAINER_NAME));
            BiConsumer<String, Map<String, String>> envFunc = (val, map) -> {
                String[] parts = val.split("=");
                if(parts.length == 2) {
                    map.put(parts[0], parts[1]);
                }
            };
            composeService.setEnvironments(genericMapListParser(service.get(ENVIRONMENT), envFunc));
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
            composeService.setPorts(genericMapListParser(service.get(PORTS), portFunc));

            compose.getServices().put(serviceEntry.getKey(), composeService);
        });

//        return data.
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> genericMapListParser(Object values, BiConsumer<String, Map<String, String>> callback) {
        if(values instanceof List) {
            List<String> envList = (List<String>) values;
            Map<String, String> envMap = new HashMap<>();
            for(var env : envList) {
                if(env != null && !env.isEmpty()) {
                    callback.accept(env, envMap);
                }
            }
            return envMap;
        } else if(values instanceof Map) {
            return (Map<String, String>) values;
        }

        return null;
    }

}
