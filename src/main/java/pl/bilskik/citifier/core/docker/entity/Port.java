package pl.bilskik.citifier.core.docker.entity;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Port {
    private String hostPort;
    private String targetPort;
    private ConnectionType connectionType = ConnectionType.TCP;

    public Port(String hostPort, String targetPort) {
        this.hostPort = hostPort;
        this.targetPort = targetPort;
    }

    public enum ConnectionType {
        TCP,
        UDP;

        public static ConnectionType fromString(String type) {
            if(type == null) {
                return ConnectionType.TCP; //default one
            }

            if (type.equalsIgnoreCase("UDP")) {
                return ConnectionType.UDP;
            }
            return ConnectionType.TCP;
        }
    }
}
