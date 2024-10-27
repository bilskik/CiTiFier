package pl.bilskik.citifier.ctfcreator.challengedetails;

import java.io.File;

public class FilePathBuilder {

    public static String baseFilePathToRepo(String basePath, String repoName) {
        return basePath + File.separator + repoName;
    }

    public static String dockerComposeFilePath(String basePath, String dockerComposeFileName) {
        return basePath + File.separator + dockerComposeFileName;
    }

    public static String dockerComposeFilePath(String basePath, String repoName, String dockerComposeFileName) {
        return basePath + File.separator + repoName + File.separator + dockerComposeFileName;
    }

}
