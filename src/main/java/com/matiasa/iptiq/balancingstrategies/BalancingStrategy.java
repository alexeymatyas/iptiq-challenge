package com.matiasa.iptiq.balancingstrategies;

import java.util.Map;

import com.matiasa.iptiq.loadbalancers.BalancedProvider;

public interface BalancingStrategy {
    BalancedProvider getNextProvider(Map<String, BalancedProvider> providersRegistry) throws NoAvailableProviderException;
}
