package pl.bilskik.citifier.ctfcreator.app;

import com.dajudge.kindcontainer.KindContainer;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.bilskik.citifier.ctfcreator.challengedetails.ChallengeDeployerPreparator;
import pl.bilskik.citifier.ctfcreator.challengedetails.ChallengeDetailsService;
import pl.bilskik.citifier.ctfcreator.config.OperatingSystem;
import pl.bilskik.citifier.ctfcreator.dockerimagebuilder.DockerEnvironmentStrategy;
import pl.bilskik.citifier.ctfcreator.dockerimagebuilder.DockerShellProperties;
import pl.bilskik.citifier.ctfcreator.kubernetes.config.K8sClusterConnectorBuilder;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.enumeration.ChallengeStatus;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static pl.bilskik.citifier.ctfcreator.app.K8sTestUtils.waitForPodsToBeReady;
import static pl.bilskik.citifier.ctfcreator.config.ShellPropertiesConfiguration.configureShellProperties;

@SpringBootTest
public class K8sIntegrationTest {
    //Main test for application.
    //Includes:
    //1. Clone public github repo
    //2. Build repo inside kubernetess cluster
    //3. Deploy application from repo.

    private static OperatingSystem currentOS;
    private KubernetesClient client;
//    private K3sContainer k3s;
    private KindContainer kind;
    private ChallengeDTO challengeDTO;
    private ChallengeAppDataDTO appDataDTO;

    @MockBean
    private K8sClusterConnectorBuilder k8sClusterConnectorBuilder;

    @MockBean
    private DockerEnvironmentStrategy environmentStrategy;

    @MockBean
    private DockerShellProperties dockerShellProperties;

    @MockBean
    private ChallengeDao challengeDao;

    @Autowired
    private ChallengeDetailsService challengeDetailsService;

    @Autowired
    private ChallengeDeployerPreparator challengeDeployerPreparator;

    @BeforeAll
    static void init() {
        currentOS = OperatingSystem.getCurrentOS();
        if(currentOS == OperatingSystem.UNKNOWN) {
            throw new IllegalStateException("Cannot determine current operating system!");
        }
    }

    @BeforeEach
    public void setup() throws IOException {
//        kind = new KindContainer<>();
//        kind.start();
//        String kubeConfigYaml = kind.getKubeconfig();
//        Files.write(Path.of("C:\\Users\\kamil\\.kube\\k3s.yaml"), kubeConfigYaml.getBytes());
//        Config config = Config.fromKubeconfig(kubeConfigYaml);
//        client = new KubernetesClientBuilder()
//                    .withConfig(config)
//                    .build();

        configureShellProperties(dockerShellProperties, currentOS);

        when(k8sClusterConnectorBuilder.buildClient())
                .thenReturn(client);
        when(environmentStrategy.configure())
                .thenReturn(new HashMap<>()); //k3s does not need switching docker context
        doNothing().when(challengeDao).updateChallengeStatus(any(ChallengeStatus.class), any(Long.class));

        challengeDeployerPreparator.baseFilePath = System.getProperty("java.io.tmpdir");

        Map<Integer, String> portFlag = new HashMap<>(){{
            put(30000, "flagaopl"); put(30001, "flagaokl");
        }};

        appDataDTO = new ChallengeAppDataDTO();
        appDataDTO.setChallengeAppDataId(1L);
        appDataDTO.setChallengeAppName("challenge-app-name");
        appDataDTO.setNamespace("test-namespace");
        appDataDTO.setStartExposedPort(30000);
        appDataDTO.setNumberOfApp(2);
        appDataDTO.setPortFlag(portFlag);

        challengeDTO = new ChallengeDTO();
        challengeDTO.setChallengeId(1L);
        challengeDTO.setName("Testowy challenge");
        challengeDTO.setGithubLink("url");
        challengeDTO.setRepoName("sample_python_app");
        challengeDTO.setStatus(ChallengeStatus.NEW);
        challengeDTO.setChallengeAppDataDTO(appDataDTO);
    }

    @AfterEach
    public void tearDown() {
//        k3s.stop();
        kind.stop();
    }

//    @ParameterizedTest
//    @MethodSource("kubernetesIntegrationTestData")
//    public void test(String repositoryUrl) throws IOException, InterruptedException {
//        String repositoryPath = cloneRepo(repositoryUrl);
//        challengeDTO.setRelativePathToRepo(repositoryPath);
//        challengeDetailsService.createAndStartApp(challengeDTO);
//
//        assertEquals(ChallengeStatus.RUNNING, challengeDTO.getStatus());
//
//        waitForPodsToBeReady(client, appDataDTO.getNamespace(), 2000000);
//        PodList podList = client.pods().inNamespace(appDataDTO.getNamespace()).list();
//        System.out.println(podList);
//    }
//    @Test

    private void assertPods() {
        PodList podList = client.pods().inNamespace(appDataDTO.getNamespace()).list();

    }

    private String cloneRepo(String url) throws IOException {
        Path directory = Files.createTempDirectory("test-kubernetess");
        try {
            Git.cloneRepository()
                .setURI(url)
                .setDirectory(directory.toFile())
                .call();
        } catch (GitAPIException e) {
            throw new RuntimeException(String.format("Cannot clone repository! Check url: %s\n", url));
        }

        return directory.getFileName().toString();
    }

    public static Stream<Arguments> kubernetesIntegrationTestData() {
        return Stream.of(
                Arguments.of("https://github.com/bilskik/sample_python_app")
        );
    }

}
