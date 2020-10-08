package com.matiasa.iptiq.loadbalancers;

public interface BalancedProvider {
    String getInstanceId();
    void check();
    String get();
}
