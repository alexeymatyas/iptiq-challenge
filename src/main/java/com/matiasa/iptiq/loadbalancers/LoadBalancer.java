package com.matiasa.iptiq.loadbalancers;

import java.util.HashMap;
import java.util.Map;

public class LoadBalancer {
    private final int maxCapacity;
    private final Map<String, BalancedProvider> providerRegistry;

    public LoadBalancer() {
        this(10);
    }

    public LoadBalancer(Integer maxCapacity) {
        if(maxCapacity < 1) {
            throw new IllegalArgumentException();
        }

        this.maxCapacity = maxCapacity;
        providerRegistry = new HashMap<>();
    }

    public void registerProvider(BalancedProvider provider) throws CapacityOverflowException {
        providerRegistry.put(provider.getInstanceId(), provider);

        if(providerRegistry.size() > maxCapacity) {
            throw new CapacityOverflowException();
        }
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int size() {
        return providerRegistry.size();
    }
}
