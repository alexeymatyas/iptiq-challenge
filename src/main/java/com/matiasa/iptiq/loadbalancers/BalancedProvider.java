package com.matiasa.iptiq.loadbalancers;

public interface BalancedProvider {
    String getInstanceId();
    String get();
}
