package pl.bilskik.citifier.core.filemanager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static pl.bilskik.citifier.core.filemanager.FilePathBuilder.buildDockerComposeAbsolutePath;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileManagerImpl implements FileManager {

    private final static List<String> DOCKER_COMPOSE = Arrays.asList("docker-compose.yml", "docker-compose.yaml", "compose.yaml", "compose.yml");

    @Value("${repo.base-file-path}")
    private String baseFilePath;

    @Override
    public String buildRelativeClonePathRepo(String url) {
        String dirName = UUID.randomUUID().toString();
        String filePathWithUuidDirectory = baseFilePath + File.separator + dirName;
        File directory = new File(filePathWithUuidDirectory);
        if(!directory.mkdir()) {
            log.error("Directory: {} on filepath: {} cannot be created!", dirName, filePathWithUuidDirectory);
            throw new FileManagerException("Directory on filepath: {} cannot be created!");
        }

        String[] tokens = splitUrl(url);
        String githubRepo = tokens[tokens.length - 1];
        return dirName + File.separator + githubRepo;
    }

    public String provideDockerComposeAbsolutePath(String repoFilePath) {
        for(var fileName: DOCKER_COMPOSE) {
            String dockerComposeAbsolutePath = buildDockerComposeAbsolutePath(repoFilePath, fileName);
            File file = new File(dockerComposeAbsolutePath);
            if(file.exists()) {
                return dockerComposeAbsolutePath;
            }
        }
        throw new FileManagerException("Docker compose not found!");
    }

    public void deleteDirAndRepo(String filepath) { //TO DO
        log.info("Deleting repository at: {}", filepath);

        File file = new File(filepath);
        if (file.exists()) {
            boolean result = FileSystemUtils.deleteRecursively(file);
            if (result) {
                log.info("Repo on filepath: {} deleted properly!", filepath);
            } else {
                log.error("Failed to delete repository! Filepath: {}", filepath);
            }
        }
    }

    private String[] splitUrl(String url) {
        return  url.split("/");
    }
}
