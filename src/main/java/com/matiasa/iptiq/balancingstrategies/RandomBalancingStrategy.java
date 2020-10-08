package com.matiasa.iptiq.balancingstrategies;

import com.matiasa.iptiq.loadbalancers.BalancedProvider;

import java.util.Map;

public class RandomBalancingStrategy implements BalancingStrategy {

    @Override
    public BalancedProvider getNextProvider(Map<String, BalancedProvider> providersRegistry) throws NoAvailableProviderException {
        return providersRegistry.values().stream()
                .findAny()
                .orElseThrow(NoAvailableProviderException::new);
    }
}
