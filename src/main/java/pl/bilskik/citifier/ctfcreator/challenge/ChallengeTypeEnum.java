package pl.bilskik.citifier.ctfcreator.challenge;

import pl.bilskik.citifier.common.EnumUtils;

import java.util.List;

public enum ChallengeTypeEnum {
    WEB_SECURITY,
    REVERSE_ENGINEERING,
    EXPLOITATION,
    CRYPTOGRAPHY;

    public static List<String> convertToList() {
        return EnumUtils.generalEnumToListStringConverter(ChallengeTypeEnum.class);
    }
}
