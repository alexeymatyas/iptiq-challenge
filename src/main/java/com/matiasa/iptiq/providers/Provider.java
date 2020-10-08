package com.matiasa.iptiq.providers;

import java.util.UUID;

import com.matiasa.iptiq.loadbalancers.BalancedProvider;

public class Provider implements BalancedProvider {
    private final String instanceId;

    public Provider() {
        instanceId = UUID.randomUUID().toString();
    }

    @Override
    public String getInstanceId() {
        return instanceId;
    }

    @Override
    public void check() {
    }

    @Override
    public String get() {
        return instanceId;
    }
}
