package pl.bilskik.citifier.ctfcreator.docker;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import pl.bilskik.citifier.ctfcreator.docker.model.DockerCompose;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DockerComposeParserManager {

    private final static String FILE_NOT_FOUND = "File not found!";
    private final static String EMPTY_FILE = "File is empty!";
    private final DockerComposeParser composeParser;

    public DockerCompose parse(String filename) {
        Yaml yaml = configureYamlParser();

        File file = new File(filename);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new DockerComposeParserException(FILE_NOT_FOUND);
        }

        Map<String, Object> yamlData = yaml.load(inputStream);
        if(yamlData == null || yamlData.isEmpty()) {
            throw new DockerComposeParserException(EMPTY_FILE);
        }

        return composeParser.parse(yamlData);
    }

    private Yaml configureYamlParser() {
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setSkipMissingProperties(true);
        Constructor constructor = new Constructor(new LoaderOptions());
        constructor.setPropertyUtils(propertyUtils);
        return new Yaml(constructor);
    }
}
