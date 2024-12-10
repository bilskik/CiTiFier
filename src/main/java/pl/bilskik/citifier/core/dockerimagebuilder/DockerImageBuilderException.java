package pl.bilskik.citifier.core.dockerimagebuilder;

public class DockerImageBuilderException extends RuntimeException {
    public DockerImageBuilderException(String message) {
        super(message);
    }
}
