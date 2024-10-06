package pl.bilskik.citifier.ctfcreator.docker;

import pl.bilskik.citifier.ctfcreator.docker.model.ComposeService;
import pl.bilskik.citifier.ctfcreator.docker.model.DockerCompose;
import pl.bilskik.citifier.ctfcreator.docker.model.Volume;
import pl.bilskik.citifier.ctfcreator.docker.model.enumeration.VolumeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParserTestParameters {

    protected static final String DOCKER_COMPOSE_1 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: node:14\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - NODE_ENV=production\n" +
                    "    ports:\n" +
                    "      - \"3000:3000\"\n" +
                    "    depends_on:\n" +
                    "      - db\n" +
                    "\n" +
                    "  db:\n" +
                    "    image: postgres:13\n" +
                    "    container_name: db\n" +
                    "    environment:\n" +
                    "      - POSTGRES_USER=user\n" +
                    "      - POSTGRES_PASSWORD=password\n" +
                    "    ports:\n" +
                    "      - \"5432:5432\"";

    protected static DockerCompose buildDockerCompose1() {
        Map<String, ComposeService> services = new HashMap<>();
        ComposeService backend = new ComposeService();
        backend.setImage("node:14");
        backend.setContainerName("backend");
        backend.setEnvironments(new HashMap<>(){{put("NODE_ENV", "production");}});
        backend.setPorts(new HashMap<>(){{put("3000", "3000");}});
        backend.setVolumes(new ArrayList<>());

        ComposeService db = new ComposeService();
        db.setImage("postgres:13");
        db.setContainerName("db");
        db.setEnvironments(new HashMap<>(){{put("POSTGRES_USER", "user"); put("POSTGRES_PASSWORD", "password");}});
        db.setPorts(new HashMap<>(){{put("5432", "5432");}});
        db.setVolumes(new ArrayList<>());
        services.put("backend", backend);
        services.put("db", db);

        return DockerCompose.builder()
                .version("3.8")
                .services(services)
                .volumes(new HashMap<>())
                .build();
    }

    protected static final String DOCKER_COMPOSE_2 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: node:14\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - NODE_ENV=development\n" +
                    "    ports:\n" +
                    "      - \"3000\"\n" +
                    "    depends_on:\n" +
                    "      - db\n" +
                    "    volumes:\n" +
                    "      - ./app:/usr/src/app\n" +
                    "      - ./config:/usr/src/config\n" +
                    "\n" +
                    "  db:\n" +
                    "    image: mysql:8\n" +
                    "    container_name: db\n" +
                    "    environment:\n" +
                    "      - MYSQL_ROOT_PASSWORD=root\n" +
                    "      - MYSQL_DATABASE=testdb\n" +
                    "    ports:\n" +
                    "      - \"3306:3306\"\n" +
                    "    volumes:\n" +
                    "      - db-data:/var/lib/mysql\n" +
                    "\n" +
                    "volumes:\n" +
                    "  db-data:";

    protected static DockerCompose buildDockerCompose2() {
        Map<String, ComposeService> services = new HashMap<>();

        ComposeService backend = new ComposeService();
        backend.setImage("node:14");
        backend.setContainerName("backend");
        backend.setEnvironments(new HashMap<>() {{
            put("NODE_ENV", "development");
        }});
        backend.setPorts(new HashMap<>() {{
            put("3000", "3000");
        }});
        backend.setVolumes(new ArrayList<>() {{
            add(new Volume(null, VolumeType.BIND_MOUNT, "./app", "/usr/src/app"));
            add(new Volume(null, VolumeType.BIND_MOUNT, "./config", "/usr/src/config"));
        }});

        ComposeService db = new ComposeService();
        db.setImage("mysql:8");
        db.setContainerName("db");
        db.setEnvironments(new HashMap<>() {{
            put("MYSQL_ROOT_PASSWORD", "root");
            put("MYSQL_DATABASE", "testdb");
        }});
        db.setPorts(new HashMap<>() {{
            put("3306", "3306");
        }});
        db.setVolumes(new ArrayList<>() {{
            add(new Volume("db-data", VolumeType.VOLUME, null, "/var/lib/mysql"));
        }});

        services.put("backend", backend);
        services.put("db", db);

        Map<String, Volume> volumes = new HashMap<>();
        volumes.put("db-data", new Volume("db-data", VolumeType.VOLUME, null, null));

        return DockerCompose.builder()
                .version("3.8")
                .services(services)
                .volumes(volumes)
                .build();
    }

    protected static final String DOCKER_COMPOSE_3 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: python:3.9\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - FLASK_ENV=production\n" +
                    "    ports:\n" +
                    "      - \"5000:5001/tcp\"\n" +
                    "      - \"5000:5002/udp\"\n" +
                    "    depends_on:\n" +
                    "      - db\n" +
                    "\n" +
                    "  db:\n" +
                    "    image: mariadb:latest\n" +
                    "    container_name: db\n" +
                    "    environment:\n" +
                    "      MYSQL_ROOT_PASSWORD: rootpassword\n" +
                    "      MYSQL_DATABASE: mydb\n" +
                    "    ports:\n" +
                    "      - \"3307:3306\"\n";

    protected static DockerCompose buildDockerCompose3() {
        Map<String, ComposeService> services = new HashMap<>();
        ComposeService backend = new ComposeService();
        backend.setImage("python:3.9");
        backend.setContainerName("backend");
        backend.setEnvironments(new HashMap<>() {{
            put("FLASK_ENV", "production");
        }});
        backend.setPorts(new HashMap<>() {{
            put("5000", "5001");
            put("5000", "5002");
        }});
        backend.setVolumes(new ArrayList<>());

        ComposeService db = new ComposeService();
        db.setImage("mariadb:latest");
        db.setContainerName("db");
        db.setEnvironments(new HashMap<>() {{
            put("MYSQL_ROOT_PASSWORD", "rootpassword");
            put("MYSQL_DATABASE", "mydb");
        }});
        db.setPorts(new HashMap<>() {{
            put("3307", "3306");
        }});
        db.setVolumes(new ArrayList<>());

        services.put("backend", backend);
        services.put("db", db);

        return DockerCompose.builder()
                .version("3.8")
                .services(services)
                .volumes(new HashMap<>())
                .build();
    }

}
