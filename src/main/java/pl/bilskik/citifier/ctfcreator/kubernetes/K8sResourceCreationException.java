package pl.bilskik.citifier.ctfcreator.kubernetes;

public class K8sResourceCreationException extends RuntimeException {
    public K8sResourceCreationException(String message) {
        super(message);
    }
}
