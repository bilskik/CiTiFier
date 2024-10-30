package pl.bilskik.citifier.ctfcreator.filemanager;

import java.io.File;

public class FilePathBuilder {

    public static String buildAbsolutePath(String basePath, String repoRelativePath) {
        return basePath + File.separator + repoRelativePath;
    }

    public static String buildDockerComposeAbsolutePath(String basePath, String dockerComposeFileName) {
        return basePath + File.separator + dockerComposeFileName;
    }

}
