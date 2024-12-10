package pl.bilskik.citifier.domain.entity.enumeration;

public enum ChallengeStatus {
    NEW("NOWY"),
    RUNNING("W TRAKCIE"),
    STOPPED("ZATRZYMANY"),
    ERROR("BŁĄD"),
    REMOVED("USUNIĘTY");

    private final String displayName;
    ChallengeStatus(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
