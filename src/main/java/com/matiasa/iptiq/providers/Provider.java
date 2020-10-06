package com.matiasa.iptiq.providers;

import java.util.UUID;

public class Provider {
    private final String instanceId;

    public Provider() {
        instanceId = UUID.randomUUID().toString();
    }

    public String get() {
        return instanceId;
    }
}
