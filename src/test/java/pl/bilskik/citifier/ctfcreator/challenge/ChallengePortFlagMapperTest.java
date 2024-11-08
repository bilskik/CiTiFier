package pl.bilskik.citifier.ctfcreator.challenge;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ChallengePortFlagMapperTest {

    private final ChallengePortFlagMapper challengePortFlagMapper = new ChallengePortFlagMapper();

    @ParameterizedTest
    @MethodSource("portFlagData")
    public void testPortFlagMapping(int initialPort, int numberOfApp) {
        Map<Integer, String> portFlagMap = challengePortFlagMapper.map(initialPort, numberOfApp);

        System.out.println(portFlagMap.toString());
        assertNotNull(portFlagMap);
        assertEquals(numberOfApp, portFlagMap.size());
        assertTrue(portFlagMap.containsKey(initialPort));
        assertTrue(portFlagMap.containsKey(initialPort + numberOfApp - 1));
    }

    private static Stream<Arguments> portFlagData() {
        return Stream.of(
                Arguments.of(8000, 99),
                Arguments.of(3000, 10),
                Arguments.of(10000, 1000)
        );
    }

}