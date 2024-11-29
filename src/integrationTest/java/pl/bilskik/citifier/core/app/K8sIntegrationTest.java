package pl.bilskik.citifier.core.app;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.bilskik.citifier.web.challengedetails.ChallengeDeployerPreparator;
import pl.bilskik.citifier.web.challengedetails.ChallengeDetailsService;
import pl.bilskik.citifier.core.config.OperatingSystem;
import pl.bilskik.citifier.core.dockerimagebuilder.DockerShellProperties;
import pl.bilskik.citifier.core.kubernetes.config.K8sClusterConnectorBuilder;
import pl.bilskik.citifier.domain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.domain.dto.ChallengeDTO;
import pl.bilskik.citifier.domain.entity.enumeration.ChallengeStatus;
import pl.bilskik.citifier.domain.service.ChallengeDao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static pl.bilskik.citifier.core.app.K8sTestUtils.allPodsReady;
import static pl.bilskik.citifier.core.config.ShellPropertiesConfiguration.configureShellProperties;

@SpringBootTest
public class K8sIntegrationTest {
    //Main test for application.
    //Includes:
    //1. Clone public github repo
    //2. Build repo inside kubernetess cluster
    //3. Deploy application from repo.

    private static OperatingSystem currentOS;
    private ChallengeDTO challengeDTO;
    private ChallengeAppDataDTO appDataDTO;
    private KubernetesClient client;

    @MockBean
    private DockerShellProperties dockerShellProperties;

    @MockBean
    private ChallengeDao challengeDao;

    @MockBean
    private K8sClusterConnectorBuilder connectorBuilder;

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
    public void setup() {
        client = new KubernetesClientBuilder().build();

        when(connectorBuilder.buildClient())
                .thenReturn(client);
        configureShellProperties(dockerShellProperties, currentOS);
        doNothing().when(challengeDao).updateChallengeStatus(any(ChallengeStatus.class), any(Long.class));

        challengeDeployerPreparator.baseFilePath = System.getProperty("java.io.tmpdir");

        Map<Integer, String> portFlag = new HashMap<>(){{
            put(30000, "flagaopl"); put(30001, "flagaokl");
        }};

        Random r = new Random();
        appDataDTO = new ChallengeAppDataDTO();
        appDataDTO.setChallengeAppDataId(1L);
        appDataDTO.setChallengeAppName("challenge-app-name");
        appDataDTO.setNamespace("test-namespace" + r.nextInt());
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


    @ParameterizedTest
    @MethodSource("kubernetesIntegrationTestData")
    public void test(String repositoryUrl) throws IOException, InterruptedException {
        int expectedNumberOfServices = appDataDTO.getNumberOfApp();
        String repositoryPath = cloneRepo(repositoryUrl);
        challengeDTO.setRelativePathToRepo(repositoryPath);

        challengeDetailsService.createAndStartApp(challengeDTO);

        assertEquals(ChallengeStatus.RUNNING, challengeDTO.getStatus());
        assertTrue(allPodsReady(client, appDataDTO.getNamespace(), 2000000));
        assertEquals(expectedNumberOfServices, nodePortCount());
        assertEquals(expectedNumberOfServices, deploymentService());

        challengeDetailsService.deleteApp(challengeDTO);

        assertEquals(ChallengeStatus.REMOVED, challengeDTO.getStatus());
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

    private int nodePortCount() {
        return (int) client.services().inNamespace(appDataDTO.getNamespace())
                .list()
                .getItems()
                .stream()
                .filter(service -> "NodePort".equals(service.getSpec().getType()))
                .count();
    }

    private int deploymentService() {
        return (int) client.services().inNamespace(appDataDTO.getNamespace())
                .list()
                .getItems()
                .stream()
                .filter(service -> "ClusterIP".equals(service.getSpec().getType()))
                .count();
    }
}
