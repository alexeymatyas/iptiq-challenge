package com.matiasa.iptiq.loadbalancers;

import java.util.LinkedHashMap;
import java.util.Map;

import com.matiasa.iptiq.balancingstrategies.BalancingStrategy;
import com.matiasa.iptiq.balancingstrategies.NoAvailableProviderException;
import com.matiasa.iptiq.balancingstrategies.RandomBalancingStrategy;

public class LoadBalancer {
    private final int maxSize;
    private final BalancingStrategy strategy;
    private final Map<String, BalancedProvider> providerRegistry;

    public LoadBalancer() {
        this(10);
    }

    public LoadBalancer(int maxSize) {
        this(maxSize, new RandomBalancingStrategy());
    }

    public LoadBalancer(Integer maxSize, BalancingStrategy strategy) {
        if(maxSize < 1) {
            throw new IllegalArgumentException();
        }

        this.maxSize = maxSize;
        this.strategy = strategy;
        providerRegistry = new LinkedHashMap<>();
    }

    public void registerProvider(BalancedProvider provider) throws SizeExceededException {
        providerRegistry.put(provider.getInstanceId(), provider);

        if(providerRegistry.size() > maxSize) {
            throw new SizeExceededException();
        }
    }

    public String get() throws NoAvailableProviderException {
        return strategy.getNextProvider(providerRegistry).get();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int size() {
        return providerRegistry.size();
    }
}
