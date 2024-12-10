package pl.bilskik.citifier.core.dockerimagebuilder;

public interface DockerImageBuilder {
    void buildAndPush(String filepath, String imageName);
}
