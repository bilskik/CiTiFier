package pl.bilskik.citifier.core.kubernetes.factory.config;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class K8sConfigMapFactory {

    private final static String API_VERSION = "v1";

    public ConfigMap createConfigMap(
            String configMapName,
            Map<String, String> configMapLabel,
            Map<String, String> data
    ) {
        log.info("Creating config map, mapName: {}, configMapLabel: {}, data: <>", configMapName, configMapLabel);
        return new ConfigMapBuilder()
                .withApiVersion(API_VERSION)
                .withNewMetadata()
                    .withName(configMapName)
                    .withLabels(configMapLabel)
                .endMetadata()
                .withData(data)
                .build();
    }

}
