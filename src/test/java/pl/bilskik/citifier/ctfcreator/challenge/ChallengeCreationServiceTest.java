package pl.bilskik.citifier.ctfcreator.challenge;

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
import pl.bilskik.citifier.ctfcreator.github.GithubDataInputDTO;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.enumeration.ChallengeStatus;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

import java.util.stream.Stream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChallengeCreationServiceTest {

    @Mock
    private ChallengeDao challengeDao;

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