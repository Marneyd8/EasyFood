package cz.muni.fi.pv168.project.business.service.validation;

public class StringValidator {
    public static ValidationResult validateLength(String string, int min, int max) {
        var result = new ValidationResult();
        var length = string.length();

        if ( min > length ||  length > max ) {
            result.add("'%s' length is not between %d (inclusive) and %d (inclusive)"
                    .formatted(string, min, max)
            );
        }

        return result;
    }

    public static ValidationResult validateAlphaNum (String string) {
        if (string.matches("^[a-zA-Z0-9]*$")) {
            return ValidationResult.success();
        }
        return ValidationResult.failed("validateAlphaNum failed");
    }

    public static ValidationResult validateAlphaNumWhiteSpace(String string) {
        if (string.matches("^[a-zA-Z0-9\\s]*$")) {
            return ValidationResult.success();
        }
        return ValidationResult.failed("validateAlphaNumWhiteSpace failed");
    }

    public static ValidationResult validateDouble(String string) {
        // 1.23e-4, 3.14159, 42.0
        if (string.matches("[-+]?(\\d*\\.\\d+|\\d+\\.\\d*|\\d+)([eE][-+]?)?")) {
            return ValidationResult.success();
        }
        return ValidationResult.failed("validateDouble failed");
    }

    public static ValidationResult validateNum(String string) {
        if (string.matches("\\d+")) {
            return ValidationResult.success();
        }
        return ValidationResult.failed("validateNum failed");
    }

    public static ValidationResult validateColor(String string) {
        if (string.matches("^\\d{1,3},\\d{1,3},\\d{1,3}$")) {
            return ValidationResult.success();
        }
        return ValidationResult.failed("validateColor failed");
    }
}
