package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

public class DockerImageDataProvider {

    public static final String DOCKERFILE_CONTENT_PYTHON = """
        FROM python:3.9-slim
        WORKDIR /app
        """;

    public static final String DOCKER_COMPOSE_CONTENT_PYTHON = """
        version: '3.8'
        services:
          python_app:
            build: .
            ports:
              - "5000:5000"
        """;

    public static final String DOCKER_COMPOSE_SIMPLE_FLASK_APP = """
        version: '3.8'
        services:
          myapp:
            image: kashmatic/simple-flask-app:1.0
        """;

    public static final String DOCKER_COMPOSE_SIMPLE_SPRING_BOOT_APP = """
        version: '3.8'
        services:
          myapp:
            image: flopes/spring-boot-docker
            ports:
             - "8080:8080"
          db:
            image: postgres:17.0
            restart: always
            environment:
              POSTGRES_USER: myuser
              POSTGRES_PASSWORD: mypassword
              POSTGRES_DB: mydatabase
            ports:
              - "5432:5432"
        """;

    public static final String INVALID_DOCKER_COMPOSE = """
        version: '3.8'
        services:
          myapp:
            ports:
             - "8080:8080"
        """;

}
