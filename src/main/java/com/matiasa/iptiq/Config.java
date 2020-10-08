package com.matiasa.iptiq;

public class Config {
    public static final int DEFAULT_MAX_SIZE = 10;

    public static final int HEARTBEAT_INTERVAL = 500;
    public static final int REENABLE_THRESHOLD = 2;

    public static final int MAX_REQUESTS_PER_PROVIDER = 2;
    public static final int CORE_POOL_SIZE = MAX_REQUESTS_PER_PROVIDER * 2;
}
