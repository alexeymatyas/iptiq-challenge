package com.matiasa.iptiq.providers;

import com.matiasa.iptiq.loadbalancers.BalancedProvider;

import java.util.UUID;

public class UnstableProviderMock implements BalancedProvider {
    private final String instanceId;
    private boolean isDown = false;
    private final int delay;

    public UnstableProviderMock() {
        this(0);
    }

    public UnstableProviderMock(int delay) {
        this.instanceId = UUID.randomUUID().toString();
        this.delay = delay;
    }

    @Override
    public String getInstanceId() {
        return instanceId;
    }

    @Override
    public void check() {
        if(isDown) {
            throw new RuntimeException();
        }
    }

    @Override
    public String get() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return instanceId;
    }

    public void setDown(boolean down) {
        isDown = down;
    }
}
