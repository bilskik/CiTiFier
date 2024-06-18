package pl.bilskik.citifier.ctfcreator.challenge;


import pl.bilskik.citifier.common.EnumUtils;

import java.util.List;

public enum FlagGenerationMethodEnum {
    RANDOM,
    IMPORTED;

    public static List<String> convertToList() {
        return EnumUtils.generalEnumToListStringConverter(FlagGenerationMethodEnum.class);
    }
}
