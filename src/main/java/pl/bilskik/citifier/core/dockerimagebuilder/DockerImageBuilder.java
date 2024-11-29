package pl.bilskik.citifier.core.dockerimagebuilder;

public interface DockerImageBuilder {
    void build(String filepath, String imageName);
}
