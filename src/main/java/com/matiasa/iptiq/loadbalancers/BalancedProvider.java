package com.matiasa.iptiq.loadbalancers;

public interface BalancedProvider {
    String getInstanceId();
    boolean check();
    String get();
}
