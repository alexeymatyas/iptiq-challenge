package com.matiasa.iptiq.balancingstrategies;

import java.util.Map;

import com.matiasa.iptiq.loadbalancers.BalancedProvider;

public abstract class BalancingStrategy {
    public abstract BalancedProvider getNextProvider(Map<String, BalancedProvider> providersRegistry)
            throws NoAvailableProviderException;
}
