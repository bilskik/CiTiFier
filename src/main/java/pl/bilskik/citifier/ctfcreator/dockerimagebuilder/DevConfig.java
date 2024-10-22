package pl.bilskik.citifier.ctfcreator.dockerimagebuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevConfig {

    @Bean
    public DockerEnvironmentStrategy dockerEnvironmentStrategy() {
        return new MinikubeEnvironmentStrategy();
    }

}
