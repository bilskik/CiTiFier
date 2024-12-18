package pl.bilskik.citifier.core.config;

import pl.bilskik.citifier.core.dockerimagebuilder.DockerShellProperties;

import static org.mockito.Mockito.when;

public class ShellPropertiesConfiguration {

    public static void configureShellProperties(DockerShellProperties dockerShellProperties, OperatingSystem currentOS) {
        if(currentOS == OperatingSystem.WINDOWS) {
            when(dockerShellProperties.getShell()).thenReturn("powershell.exe");
            when(dockerShellProperties.getConfig()).thenReturn("-Command");
        } else {
            when(dockerShellProperties.getShell()).thenReturn("sh");
            when(dockerShellProperties.getConfig()).thenReturn("-c");
        }
    }
}
