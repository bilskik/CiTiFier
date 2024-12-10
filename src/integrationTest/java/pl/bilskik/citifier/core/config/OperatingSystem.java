package pl.bilskik.citifier.core.config;

public enum OperatingSystem {
    WINDOWS,
    LINUX,
    UNKNOWN;

    public static OperatingSystem getCurrentOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return WINDOWS;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return LINUX;
        }
        return UNKNOWN;
    }
}
