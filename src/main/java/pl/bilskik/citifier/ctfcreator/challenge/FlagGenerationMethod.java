package pl.bilskik.citifier.ctfcreator.challenge;


import pl.bilskik.citifier.common.EnumUtils;

import java.util.List;

public enum FlagGenerationMethod {
    RANDOM,
    IMPORTED;

    public static List<String> convertToList() {
        return EnumUtils.generalEnumToListStringConverter(FlagGenerationMethod.class);
    }
}
