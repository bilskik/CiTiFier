package pl.bilskik.citifier.ctfcreator.challenge;

import pl.bilskik.citifier.common.EnumUtils;

import java.util.List;

public enum PointCalculationFunction {
    LINEAR,
    GAUSSIAN;

    public static List<String> convertToList() {
        return EnumUtils.generalEnumToListStringConverter(PointCalculationFunction.class);
    }

    public static String getFunction(PointCalculationFunction functionName) {
        return switch (functionName) {
            case LINEAR -> "f(x) = x";
            case GAUSSIAN -> "f(x) = exp(-x^2)";
        };
    }

}
