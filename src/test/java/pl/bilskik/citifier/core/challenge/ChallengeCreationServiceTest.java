package pl.bilskik.citifier.core.challenge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.bilskik.citifier.core.github.GithubDataInputDTO;
import pl.bilskik.citifier.domain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.domain.dto.ChallengeDTO;
import pl.bilskik.citifier.domain.entity.enumeration.ChallengeStatus;
import pl.bilskik.citifier.domain.service.ChallengeDao;
import pl.bilskik.citifier.web.challenge.ChallengeCreationException;
import pl.bilskik.citifier.web.challenge.ChallengeCreationService;
import pl.bilskik.citifier.web.challenge.ChallengeInputDTO;
import pl.bilskik.citifier.web.challenge.ChallengePortFlagMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ChallengeCreationServiceTest {

    @Mock
    private ChallengeDao challengeDao;

    @Mock
    private ChallengePortFlagMapper portFlagMapper;

    @InjectMocks
    private ChallengeCreationService challengeCreationService;

    @Captor
    private ArgumentCaptor<ChallengeDTO> challengeCaptor;

    private ChallengeInputDTO challengeInput;
    private GithubDataInputDTO githubInput;

    @BeforeEach
    public void setup() {
        challengeInput = new ChallengeInputDTO();
        challengeInput.setName("Test Challenge");
        challengeInput.setStartExposedPort(8080);
        challengeInput.setNumberOfApp(2);

        githubInput = new GithubDataInputDTO();
        githubInput.setGithubLink("https://github.com/sample/repo_name");
        githubInput.setRelativePathToRepo("10601679-9cc7-4d2a-aa0d-ecb7c62f85a2//repo_name");
        Map<Integer, String> portFlagMap = new HashMap<>(){{ put(8080, "12345678"); put(8081, "12345679"); }};
        when(portFlagMapper.map(any(Integer.class), any(Integer.class))).thenReturn(portFlagMap);
    }

    @Test
    public void createNewChallenge_WhenShortRepoName() {
        String repoName = "repo_name";
        String challengeAppName = "reponame";
        String beginNamespace = "reponame-";
        int BEGIN_NAMESPACE_LEN = beginNamespace.length();

        challengeCreationService.createNewChallenge(challengeInput, githubInput);

        verify(challengeDao, times(1)).createNewChallenge(challengeCaptor.capture());

        ChallengeDTO challengeDTO = challengeCaptor.getValue();
        assertNotNull(challengeDTO);
        assertEquals(challengeInput.getName(), challengeDTO.getName());
        assertEquals(ChallengeStatus.NEW, challengeDTO.getStatus());
        assertEquals(githubInput.getGithubLink(), challengeDTO.getGithubLink());
        assertEquals(githubInput.getRelativePathToRepo(), challengeDTO.getRelativePathToRepo());
        assertEquals(repoName, challengeDTO.getRepoName());

        ChallengeAppDataDTO appDataDTO = challengeDTO.getChallengeAppDataDTO();
        assertNotNull(challengeDTO.getChallengeAppDataDTO());
        assertEquals(challengeAppName, appDataDTO.getChallengeAppName());
        assertEquals(challengeInput.getStartExposedPort(), appDataDTO.getStartExposedPort());
        assertEquals(challengeInput.getNumberOfApp(), appDataDTO.getNumberOfApp());
        assertEquals(beginNamespace, appDataDTO.getNamespace().substring(0, BEGIN_NAMESPACE_LEN));
        assertEquals(2, appDataDTO.getPortFlag().size());
    };

    @Test
    public void createNewChallenge_WhenLongRepoName() {
        githubInput.setGithubLink("https://github.com/sample/repo_name_with_more_than_30_characters._sample_characters");
        String repoName = "repo_name_with_more_than_30_characters._sample_characters";
        String challengeAppName = "reponamewithmorethan30ch";
        String beginNamespace = "reponamewithmorethan30ch-";
        int BEGIN_NAMESPACE_LEN = beginNamespace.length();

        challengeCreationService.createNewChallenge(challengeInput, githubInput);

        verify(challengeDao, times(1)).createNewChallenge(challengeCaptor.capture());

        ChallengeDTO challengeDTO = challengeCaptor.getValue();
        assertNotNull(challengeDTO);
        assertEquals(challengeInput.getName(), challengeDTO.getName());
        assertEquals(ChallengeStatus.NEW, challengeDTO.getStatus());
        assertEquals(githubInput.getGithubLink(), challengeDTO.getGithubLink());
        assertEquals(githubInput.getRelativePathToRepo(), challengeDTO.getRelativePathToRepo());
        assertEquals(repoName, challengeDTO.getRepoName());

        ChallengeAppDataDTO appDataDTO = challengeDTO.getChallengeAppDataDTO();
        assertNotNull(challengeDTO.getChallengeAppDataDTO());
        assertEquals(challengeAppName, appDataDTO.getChallengeAppName());
        assertEquals(challengeInput.getStartExposedPort(), appDataDTO.getStartExposedPort());
        assertEquals(challengeInput.getNumberOfApp(), appDataDTO.getNumberOfApp());
        assertEquals(beginNamespace, appDataDTO.getNamespace().substring(0, BEGIN_NAMESPACE_LEN));
        assertEquals(2, appDataDTO.getPortFlag().size());
    };

    @ParameterizedTest
    @MethodSource("provideChallengeCreationNullInput")
    public void createNewChallenge_WhenInputIsNull(ChallengeInputDTO challengeInput, GithubDataInputDTO githubInput) {
        assertThrows(ChallengeCreationException.class, () -> {
            challengeCreationService.createNewChallenge(challengeInput, githubInput);
        });
    }

    private static Stream<Arguments> provideChallengeCreationNullInput() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(new ChallengeInputDTO(), null),
                Arguments.of(null, new GithubDataInputDTO())
        );
    }

}