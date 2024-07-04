package pl.bilskik.citifier.ctfcreator.tournament;

import pl.bilskik.citifier.common.EnumUtils;

import java.util.List;

public enum UserCreationMethod {
    GENERATE;

    public static List<String> convertToList() {
        return EnumUtils.generalEnumToListStringConverter(UserCreationMethod.class);
    }
}
