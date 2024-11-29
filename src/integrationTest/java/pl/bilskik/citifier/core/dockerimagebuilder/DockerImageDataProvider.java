package pl.bilskik.citifier.core.dockerimagebuilder;

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
            image: python-image
            ports:
              - "5000:5000"
        """;

    public static final String IMAGE_NAME_PYTHON = "python-image";

    public static final String INVALID_DOCKER_COMPOSE = """
        version: '3.8'
        services:
          myapp:
            ports:
             - "8080:8080"
        """;
}
