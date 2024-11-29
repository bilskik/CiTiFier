package pl.bilskik.citifier.core.docker.datasource;

public class InvalidDockerComposeTestCases {

    //lack of 'db' in services
    public static final String INVALID_DOCKER_COMPOSE_1 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: node:14\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - DB=db-1\n" +
                    "      - CTF_FLAG=ctfflag\n" +
                    "    ports:\n" +
                    "      - \"3000:3000\"\n" +
                    "\n" +
                    "  database:\n" +
                    "    image: postgres:13\n" +
                    "    container_name: db\n" +
                    "    ports:\n" +
                    "      - \"5432:5432\"";

    //lack of 'DB' in app env service
    public static final String INVALID_DOCKER_COMPOSE_2 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: node:14\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - CTF_FLAG=ctfflag\n" +
                    "    ports:\n" +
                    "      - \"3000:3000\"\n" +
                    "\n" +
                    "  db:\n" +
                    "    image: postgres:13\n" +
                    "    container_name: db\n" +
                    "    ports:\n" +
                    "      - \"5432:5432\"";

    //lack of ports in app or db
    public static final String INVALID_DOCKER_COMPOSE_3 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: node:14\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - DB=db-1\n" +
                    "      - CTF_FLAG=ctfflag\n" +
                    "\n" +
                    "  db:\n" +
                    "    image: postgres:13\n" +
                    "    container_name: db\n" +
                    "    ports:\n" +
                    "      - \"5432:5432\"";

    //lack of container-name
    public static final String INVALID_DOCKER_COMPOSE_4 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: node:14\n" +
                    "    environment:\n" +
                    "      - DB=db-1\n" +
                    "      - CTF_FLAG=ctfflag\n" +
                    "    ports:\n" +
                    "      - \"3000:3000\"\n" +
                    "\n" +
                    "  db:\n" +
                    "    image: postgres:13\n" +
                    "    container_name: db\n" +
                    "    ports:\n" +
                    "      - \"5432:5432\"";

    //lack of image
    public static final String INVALID_DOCKER_COMPOSE_5 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - DB=db-1\n" +
                    "      - CTF_FLAG=ctfflag\n" +
                    "    ports:\n" +
                    "      - \"3000:3000\"\n" +
                    "\n" +
                    "  db:\n" +
                    "    image: postgres:13\n" +
                    "    container_name: db\n" +
                    "    ports:\n" +
                    "      - \"5432:5432\"";

    //Too much services - only 2 services are supported(db and app)
    public static final String INVALID_DOCKER_COMPOSE_6 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: node:14\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - DB=db-1\n" +
                    "      - CTF_FLAG=ctfflag\n" +
                    "    ports:\n" +
                    "      - \"3000:3000\"\n" +
                    "\n" +
                    "  db:\n" +
                    "    image: postgres:13\n" +
                    "    container_name: db\n" +
                    "    ports:\n" +
                    "      - \"5432:5432\"\n" +
                    "  nginx:\n" +
                    "    image: nginx:latest\n" +
                    "    container_name: nginx\n";

    //Too small number of services
    public static final String INVALID_DOCKER_COMPOSE_7=
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: node:14\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - DB=db-1\n" +
                    "      - CTF_FLAG=ctfflag\n" +
                    "    ports:\n" +
                    "      - \"3000:3000\"\n";

    //lack of 'CTF_FLAG' in app environment variables
    public static final String INVALID_DOCKER_COMPOSE_8 =
            "version: '3.8'\n" +
                    "services:\n" +
                    "  backend:\n" +
                    "    image: node:14\n" +
                    "    container_name: backend\n" +
                    "    environment:\n" +
                    "      - DB=db-1\n" +
                    "    ports:\n" +
                    "      - \"3000:3000\"\n" +
                    "\n" +
                    "  db:\n" +
                    "    image: postgres:13\n" +
                    "    container_name: db\n" +
                    "    ports:\n" +
                    "      - \"5432:5432\"";
}
