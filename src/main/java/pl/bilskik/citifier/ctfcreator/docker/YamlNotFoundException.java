package pl.bilskik.citifier.ctfcreator.docker;

public class YamlNotFoundException extends RuntimeException {
    public YamlNotFoundException(String message) {
        super(message);
    }
}
