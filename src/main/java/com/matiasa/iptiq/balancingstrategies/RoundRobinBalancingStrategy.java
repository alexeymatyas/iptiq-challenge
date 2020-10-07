package com.matiasa.iptiq.balancingstrategies;

import com.matiasa.iptiq.loadbalancers.BalancedProvider;

import java.util.Iterator;
import java.util.Map;

public class RoundRobinBalancingStrategy extends BalancingStrategy {
    private String lastUsedProviderId;

    @Override
    public BalancedProvider getNextProvider(Map<String, BalancedProvider> providersRegistry) throws NoAvailableProviderException {
        if(providersRegistry.size() == 0) {
            throw new NoAvailableProviderException();
        }

        BalancedProvider provider = lastUsedProviderId == null
                ? providersRegistry.values().iterator().next()
                : getNextProvider(providersRegistry, lastUsedProviderId);

        lastUsedProviderId = provider.getInstanceId();
        return provider;
    }

    private BalancedProvider getNextProvider(Map<String, BalancedProvider> providersRegistry, String providerId) {
        Iterator<BalancedProvider> iterator = providersRegistry.values().iterator();
        BalancedProvider provider = null;

        while(iterator.hasNext()) {
            if(iterator.next().getInstanceId().equals(providerId) && iterator.hasNext()) {
                provider = iterator.next();
            }
        }

        if(provider == null) {
            provider = providersRegistry.values().iterator().next();
        }

        return provider;
    }
}
