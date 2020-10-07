package com.matiasa.iptiq.providers;

import com.matiasa.iptiq.loadbalancers.BalancedProvider;

import java.util.UUID;

public class UnstableProviderMock implements BalancedProvider {
    private final String instanceId;
    private boolean isDown = false;

    public UnstableProviderMock() {
        instanceId = UUID.randomUUID().toString();
    }

    @Override
    public String getInstanceId() {
        return instanceId;
    }

    @Override
    public boolean check() {
        if(isDown) {
            throw new RuntimeException();
        } else {
            return true;
        }
    }

    @Override
    public String get() {
        return instanceId;
    }

    public void setDown(boolean down) {
        isDown = down;
    }
}
