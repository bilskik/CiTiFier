package pl.bilskik.citifier.ctfcreator.filemanager;

public interface FileManager {
    String buildRelativeClonePathRepo(String url);
    String provideDockerComposeAbsolutePath(String repoFilePath);
    void deleteDirAndRepo(String relativeFilepath);
}
