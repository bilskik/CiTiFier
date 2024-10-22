package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
//@EnableConfigurationProperties(DockerShellProperties.class)
@Profile("dev")
public class DevConfig {

    @Bean
    public DockerEnvironmentStrategy dockerEnvironmentStrategy() {
        return new MinikubeEnvironmentStrategy();
    }

}
