package pl.bilskik.citifier.ctfcreator.kubernetes.statefulset;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.kubernetes.config.K8sConfigMapCreator;
import pl.bilskik.citifier.ctfcreator.kubernetes.config.K8sSecretCreator;
import pl.bilskik.citifier.ctfcreator.kubernetes.statefulset.volume.K8sVolumeCreator;
import pl.bilskik.citifier.ctfcreator.kubernetes.statefulset.volume.K8sVolumeMountCreator;

import java.util.*;

@Service
@RequiredArgsConstructor
public class K8sStatefulSetManager {

    private final static String DEFAULT_NAMESPACE = "default";
    private final K8sStatefulSetCreator statefulSetCreator;
    private final K8sConfigMapCreator configMapCreator;
    private final K8sSecretCreator secretCreator;
    private final K8sVolumeCreator volumeCreator;
    private final K8sVolumeMountCreator volumeMountCreator;

    public void deployStatefulSet(KubernetesClient client) {
        String sqlScript = "\\c notes_db;\n" +
                "CREATE TABLE IF NOT EXISTS users (\n" +
                "    id                   SERIAL PRIMARY KEY,\n" +
                "    email                TEXT NOT NULL,\n" +
                "    username             TEXT,\n" +
                "    password             TEXT,\n" +
                "    totp_secret          TEXT,\n" +
                "    is_verified          BOOLEAN DEFAULT FALSE,\n" +
                "    is_user_non_locked   BOOLEAN DEFAULT TRUE,\n" +
                "    failed_login_attempt INT NOT NULL DEFAULT 0,\n" +
                "    lock_time            TIMESTAMP NOT NULL DEFAULT make_timestamp(1970, 0, 0, 0, 0, 0)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS note (\n" +
                "    id               SERIAL PRIMARY KEY,\n" +
                "    title            TEXT NOT NULL,\n" +
                "    content          TEXT,\n" +
                "    owner_id         INT NOT NULL,\n" +
                "\n" +
                "    encoded_password TEXT,\n" +
                "    salt             BYTEA,\n" +
                "    iv               BYTEA,\n" +
                "\n" +
                "    is_encrypted     BOOLEAN DEFAULT FALSE,\n" +
                "    is_published     BOOLEAN DEFAULT FALSE\n" +
                ");";

        Map<String, String> pgEnv = new HashMap<>();
        pgEnv.put("POSTGRES_USER", "notes");
        pgEnv.put("POSTGRES_DB", "notes_db");

        ConfigMap configMap = configMapCreator.createConfigMap(
                "postgres-config-map",
                Collections.singletonMap("app", "configMap"),
                pgEnv
        );

        client.configMaps().inNamespace(DEFAULT_NAMESPACE).resource(configMap).create();

        Map<String, String> initScript = new HashMap<>();
        initScript.put("init.sql", sqlScript);

        ConfigMap initScriptConfigMap = configMapCreator.createConfigMap(
            "postgres-init-script",
                Collections.singletonMap("app", "configMap"),
                initScript
        );

        client.configMaps().inNamespace(DEFAULT_NAMESPACE).resource(initScriptConfigMap).create();

        Volume configMapVolume = volumeCreator.createVolume(
                "config_map",
                "init-script",
                "postgres-init-script"
        );

        VolumeMount configMapVolumeMount = volumeMountCreator.createVolumeMount(
    "init-script",
                "/docker-entrypoint-initdb.d"
        );

        Secret secret = secretCreator.createSecret(
                "postgres-secret",
                Collections.singletonMap("app", "secret"),
                "Opaque",
                Collections.singletonMap("POSTGRES_PASSWORD", "Password1!")
        );

        Volume emptyDirVolume = volumeCreator.createVolume(
                "EMPTY_DIR",
                "pg-data",
                null
        );

        VolumeMount emptyDirVolumeMount = volumeMountCreator.createVolumeMount(
                "pg-data",
                "/cache"
        );

        StatefulSet statefulSet = statefulSetCreator.createStatefulSet(
                "postgres-statefulset",
                Collections.singletonMap("app", "postgres-statefulset"),
                Collections.singletonMap("app", "db"),
                "postgres",
                "postgres",
                "postgres-config-map",
                "postgres-secret",
                Arrays.asList(configMapVolume, emptyDirVolume),
                Arrays.asList(configMapVolumeMount, emptyDirVolumeMount)
        );


        client.secrets().inNamespace(DEFAULT_NAMESPACE).resource(secret).create();

        client.apps().statefulSets().inNamespace(DEFAULT_NAMESPACE).resource(statefulSet).create();
    }
}
