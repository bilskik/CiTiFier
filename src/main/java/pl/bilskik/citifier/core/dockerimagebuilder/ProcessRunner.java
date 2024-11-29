package pl.bilskik.citifier.core.dockerimagebuilder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ProcessRunner {

    @Value("${process.timeout}")
    private long timeout;

    @Value("${process.time-unit}")
    private String timeunit;

    public boolean startProcess(ProcessBuilder processBuilder) {
        Process process = null;
        try {
            log.info("Process started");
            process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                log.info(line);
            }

            TimeUnit timeUnit = getTimeUnit();
            boolean finished = process.waitFor(timeout, timeUnit);
            if(finished) {
                int exitCode = process.exitValue();
                log.info("Process finished with code: {}", exitCode);
                if(exitCode != 0) {
                    destroyProcessIfExist(process);
                }
                return exitCode == 0;
            } else {
                destroyProcessIfExist(process);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            log.info("Error executing docker-compose build", e);
            destroyProcessIfExist(process);
            throw new DockerImageBuilderException("Cannot execute docker build process properly!");
        }
    }

    private void destroyProcessIfExist(Process process) {
        if(process != null && process.isAlive()) {
            process.destroy();
        }
    }

    private TimeUnit getTimeUnit() {
        if("SECONDS".equals(timeunit)) {
            return TimeUnit.SECONDS;
        }
        throw new DockerImageBuilderException("No time-unit specified");
    }
}
