package pl.bilskik.citifier.core.kubernetes.exception;

public class K8sResourceCreationException extends RuntimeException {
    public K8sResourceCreationException(String message) {
        super(message);
    }
}
