package pl.bilskik.citifier.common;

import java.util.Arrays;
import java.util.List;

public class EnumUtils {

    public static <E extends Enum<E>> List<String> generalEnumToListStringConverter(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .toList();
    }

}
