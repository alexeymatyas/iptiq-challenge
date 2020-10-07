package com.matiasa.iptiq.loadbalancers;

import java.util.HashMap;
import java.util.Map;

public class LoadBalancer {
    private final int maxSize;
    private final Map<String, BalancedProvider> providerRegistry;

    public LoadBalancer() {
        this(10);
    }

    public LoadBalancer(Integer maxSize) {
        if(maxSize < 1) {
            throw new IllegalArgumentException();
        }

        this.maxSize = maxSize;
        providerRegistry = new HashMap<>();
    }

    public void registerProvider(BalancedProvider provider) throws SizeExceededException {
        providerRegistry.put(provider.getInstanceId(), provider);

        if(providerRegistry.size() > maxSize) {
            throw new SizeExceededException();
        }
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int size() {
        return providerRegistry.size();
    }
}
