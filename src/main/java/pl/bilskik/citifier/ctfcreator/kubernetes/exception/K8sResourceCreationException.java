package pl.bilskik.citifier.ctfcreator.kubernetes.exception;

public class K8sResourceCreationException extends RuntimeException {
    public K8sResourceCreationException(String message) {
        super(message);
    }
}
