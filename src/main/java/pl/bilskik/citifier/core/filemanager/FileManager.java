package pl.bilskik.citifier.core.filemanager;

public interface FileManager {
    String buildRelativeClonePathRepo(String url);
    String provideDockerComposeAbsolutePath(String repoFilePath);
    void deleteDirAndRepo(String relativeFilepath);
}
