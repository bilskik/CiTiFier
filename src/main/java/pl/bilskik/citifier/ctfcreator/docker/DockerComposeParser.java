package pl.bilskik.citifier.ctfcreator.docker;

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

@Service
public class DockerComposeParser {

    public DockerCompose parse(String filename) {
        Yaml yaml = configureYamlParser();

        File file = new File(filename);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new YamlNotFoundException("Nie znaleziono pliku!");
        }

        return yaml.loadAs(inputStream, DockerCompose.class);
    }

    private Yaml configureYamlParser() {
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setSkipMissingProperties(true);
        Constructor constructor = new Constructor(DockerCompose.class, new LoaderOptions());
        constructor.setPropertyUtils(propertyUtils);
        return new Yaml(constructor);
    }
}
