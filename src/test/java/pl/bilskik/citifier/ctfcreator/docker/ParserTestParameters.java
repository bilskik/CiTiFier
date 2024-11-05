package pl.bilskik.citifier.ctfcreator.docker;

import pl.bilskik.citifier.ctfcreator.docker.entity.*;
import pl.bilskik.citifier.ctfcreator.docker.enumeration.CommandType;
import pl.bilskik.citifier.ctfcreator.docker.enumeration.VolumeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParserTestParameters {

    private static DockerCompose generalDockerComposeBuilder(
            String version,
            Map<String, ComposeService> services,
            Map<String, Volume> volumes
    ) {

        return DockerCompose.builder()
                .version(version)
                .services(services)
                .volumes(volumes)
                .build();
    }


    protected static final String DOCKER_COMPOSE_1 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: node:14\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - NODE_ENV=production\n" +
                    "      - DB=db-1\n" +
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
        backend.setEnvironments(new HashMap<>(){{
            put("NODE_ENV", "production");
            put("DB", "db-1");
        }});
        backend.setPorts(new ArrayList<>(){{
            add(new Port("3000", "3000"));
        }});
        backend.setVolumes(new ArrayList<>());

        ComposeService db = new ComposeService();
        db.setImage("postgres:13");
        db.setContainerName("db");
        db.setEnvironments(new HashMap<>(){{put("POSTGRES_USER", "user"); put("POSTGRES_PASSWORD", "password");}});
        db.setPorts(new ArrayList<>(){{add(new Port("5432", "5432"));}});
        db.setVolumes(new ArrayList<>());

        services.put("backend", backend);
        services.put("db", db);

        return generalDockerComposeBuilder("3.8", services, new HashMap<>());
    }

    protected static final String DOCKER_COMPOSE_2 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: node:14\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - NODE_ENV=development\n" +
                    "      - DB=db-1\n" +
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

        ComposeService backend = getService();

        ComposeService db = new ComposeService();
        db.setImage("mysql:8");
        db.setContainerName("db");
        db.setEnvironments(new HashMap<>() {{
            put("MYSQL_ROOT_PASSWORD", "root");
            put("MYSQL_DATABASE", "testdb");
        }});
        db.setPorts(new ArrayList<>() {{
            add(new Port("3306", "3306"));
        }});
        db.setVolumes(new ArrayList<>() {{
            add(new Volume("db-data", VolumeType.VOLUME, null, "/var/lib/mysql"));
        }});

        services.put("backend", backend);
        services.put("db", db);

        Map<String, Volume> volumes = new HashMap<>();
        volumes.put("db-data", new Volume("db-data", VolumeType.VOLUME, null, null));

        return generalDockerComposeBuilder("3.8", services, volumes);
    }

    private static ComposeService getService() {
        ComposeService backend = new ComposeService();
        backend.setImage("node:14");
        backend.setContainerName("backend");
        backend.setEnvironments(new HashMap<>() {{
            put("NODE_ENV", "development");
            put("DB", "db-1");
        }});
        backend.setPorts(new ArrayList<>() {{
            add(new Port("3000", "3000"));
        }});
        backend.setVolumes(new ArrayList<>() {{
            add(new Volume(null, VolumeType.BIND_MOUNT, "./app", "/usr/src/app"));
            add(new Volume(null, VolumeType.BIND_MOUNT, "./config", "/usr/src/config"));
        }});
        return backend;
    }

    protected static final String DOCKER_COMPOSE_3 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: python:3.9\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - FLASK_ENV=production\n" +
                    "      - DB=db-1\n" +
                    "    ports:\n" +
                    "      - \"5000:5001/tcp\"\n" +
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
            put("DB", "db-1");
        }});
        backend.setPorts(new ArrayList<>() {{
            add(new Port("5000", "5001", Port.ConnectionType.TCP));
        }});
        backend.setVolumes(new ArrayList<>());

        ComposeService db = new ComposeService();
        db.setImage("mariadb:latest");
        db.setContainerName("db");
        db.setEnvironments(new HashMap<>() {{
            put("MYSQL_ROOT_PASSWORD", "rootpassword");
            put("MYSQL_DATABASE", "mydb");
        }});
        db.setPorts(new ArrayList<>() {{
            add(new Port("3307", "3306"));
        }});
        db.setVolumes(new ArrayList<>());

        services.put("backend", backend);
        services.put("db", db);

        return generalDockerComposeBuilder("3.8", services, new HashMap<>());
    }

    protected static final String DOCKER_COMPOSE_4 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: python:3.9\n" +
                    "    container_name: backend\n" +
                    "    entrypoint: /bin/sh -c 'python app.py'\n" +
                    "    ports:\n" +
                    "      - \"8080\"\n" +
                    "    command: [\"flask\", \"run\", \"--host=0.0.0.0\", \"--port=5001\"]\n" +
                    "    environment:\n" +
                    "      - DB=db-1\n" +
                    "\n" +
                    "  db:\n" +
                    "    image: mariadb:latest\n" +
                    "    container_name: db\n" +
                    "    ports:\n" +
                    "      - \"3307:3306\"\n" +
                    "    entrypoint: [\"docker-entrypoint.sh\"]\n" +
                    "    command: [\"mysqld\"]";



    protected static DockerCompose buildDockerCompose4() {
        Map<String, ComposeService> services = new HashMap<>();
        ComposeService backend = getComposeService();

        ComposeService db = new ComposeService();
        db.setImage("mariadb:latest");
        db.setContainerName("db");
        db.setVolumes(new ArrayList<>());
        db.setPorts(new ArrayList<>(){{
            add(new Port("3307", "3306", Port.ConnectionType.TCP));
        }});
        db.setEnvironments(new HashMap<>());
        db.setEntrypoint(new Entrypoint(new ArrayList<>(){{ add("docker-entrypoint.sh"); }}, CommandType.EXEC));
        db.setCommand(new Command(new ArrayList<>(){{ add("mysqld"); }}, CommandType.EXEC));

        services.put("backend", backend);
        services.put("db", db);

        return generalDockerComposeBuilder("3.8", services, new HashMap<>());
    }

    private static ComposeService getComposeService() {
        ComposeService backend = new ComposeService();
        backend.setImage("python:3.9");
        backend.setContainerName("backend");
        backend.setVolumes(new ArrayList<>());
        backend.setPorts(new ArrayList<>(){{
            add(new Port("8080", "8080", Port.ConnectionType.TCP));
        }});
        backend.setEnvironments(new HashMap<>(){{
            put("DB", "db-1");
        }});
        backend.setEntrypoint(new Entrypoint(new ArrayList<>(){{ add("/bin/sh -c 'python app.py'"); }}, CommandType.SHELL));
        backend.setCommand(new Command(new ArrayList<>(){{
            add("flask"); add("run"); add("--host=0.0.0.0"); add("--port=5001");
        }}, CommandType.EXEC));
        return backend;
    }
}
