package pl.bilskik.citifier.ctfcreator.kubernetes.data;

public class K8sConstants {
    //labels
    public final static String APP = "app";
    public final static String NODE_PORT_LABEL = "node-port";
    public final static String HEADLESS_SERVICE_LABEL = "headless-service";
    public final static String DEPLOYMENT_LABEL = "deployment";
    public final static String POD_LABEL = "pod";
    public final static String STATEFUL_SET_LABEL = "statefulset";
    public final static String DB_LABEL = "db";
    public final static String SECRET_LABEL = "secret";
    public final static String CONFIG_MAP_LABEL = "config-map";

    //services
    public final static String DB_SERVICE_NAME = "db";

    //env
    public final static String DB_ENV_NAME = "DB";
    public final static String CTF_FLAG_ENV_NAME = "CTF_FLAG";

}
