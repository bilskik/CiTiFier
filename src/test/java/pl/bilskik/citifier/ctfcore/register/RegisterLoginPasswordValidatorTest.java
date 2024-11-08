package pl.bilskik.citifier.ctfcore.register;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class RegisterLoginPasswordValidatorTest {

    private final RegisterLoginPasswordValidator validator = new RegisterLoginPasswordValidator();

    @ParameterizedTest
    @MethodSource("validLogin")
    public void testValidLogin(String login) {
        validator.validateLogin(login);
    }

    public static Stream<Arguments> validLogin() {
        return Stream.of(
                Arguments.of("BLABLABLAa1"),
                Arguments.of("BlaBlaCar1"),
                Arguments.of("###!la1!@@BlaB24")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidLogin")
    public void testInvalidLogin_ShouldThrowException(String login) {
        assertThrows(RegisterException.class, () -> {
            validator.validateLogin(login);
        });
    }

    public static Stream<Arguments> invalidLogin() {
        return Stream.of(
                Arguments.of("a"),
                Arguments.of("A2aaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb"),
                Arguments.of("BLBLABLASDAS"),
                Arguments.of("aaaaaaaaaabbbb"),
                Arguments.of("BLAblablA"),
                Arguments.of("###!!!!BLAa"),
                Arguments.of("12334123124")
        );
    }

    @ParameterizedTest
    @MethodSource("validPassword")
    public void testValidPassword(String password) {
        validator.validatePassword(password);
    }

    public static Stream<Arguments> validPassword() {
        return Stream.of(
                Arguments.of("CLACLAcar123!"),
                Arguments.of("!CLACL#Acar123"),
                Arguments.of("la1BlaD;'!24"),
                Arguments.of("mASELKOZBułeczką12*")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPassword")
    public void testInvalidPassword_ShouldThrowException(String password) {
        assertThrows(RegisterException.class, () -> {
            validator.validatePassword(password);
        });
    }

    public static Stream<Arguments> invalidPassword() {
        return Stream.of(
                Arguments.of("Aa#1"),
                Arguments.of("A2#3!bbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb"),
                Arguments.of("BLBLABLASDAS"),
                Arguments.of("aaaaaaaaaabbbb"),
                Arguments.of("BLAblablA"),
                Arguments.of("12334123124"),
                Arguments.of("Blaaaa123124"),
                Arguments.of("#*&^%$^@!ASD"),
                Arguments.of("#*&^%$^@!AaD"),
                Arguments.of("#*&^%$^@!1234")
        );
    }

}