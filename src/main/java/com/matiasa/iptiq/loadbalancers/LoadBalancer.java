package com.matiasa.iptiq.loadbalancers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.matiasa.iptiq.balancingstrategies.BalancingStrategy;
import com.matiasa.iptiq.balancingstrategies.NoAvailableProviderException;
import com.matiasa.iptiq.balancingstrategies.RandomBalancingStrategy;

public class LoadBalancer {
    private final int maxSize;
    private final BalancingStrategy strategy;
    private final Map<String, BalancedProvider> providerRegistry;
    private final Map<String, BalancedProvider> disabledProviderRegistry;

    private final int heartbeatInterval = 500;
    private final ScheduledExecutorService heartbeatChecker = Executors.newSingleThreadScheduledExecutor();

    public LoadBalancer() {
        this(10);
    }

    public LoadBalancer(int maxSize) {
        this(maxSize, new RandomBalancingStrategy());
    }

    public LoadBalancer(Integer maxSize, BalancingStrategy strategy) {
        if (maxSize < 1) {
            throw new IllegalArgumentException();
        }

        this.maxSize = maxSize;
        this.strategy = strategy;
        providerRegistry = new LinkedHashMap<>();
        disabledProviderRegistry = new LinkedHashMap<>();

        startup();
    }

    private void startup() {
        heartbeatChecker.scheduleAtFixedRate(
                () -> {
                    for (BalancedProvider provider : providerRegistry.values()) {
                        try {
                            provider.check();
                        } catch (Exception e) {
                            disableProvider(provider.getInstanceId());
                        }
                    }
                },
                0, heartbeatInterval, TimeUnit.MILLISECONDS
        );
    }

    public void shutdown() {
        heartbeatChecker.shutdown();
    }

    public void registerProvider(BalancedProvider provider) throws SizeExceededException {
        providerRegistry.put(provider.getInstanceId(), provider);

        if (providerRegistry.size() + disabledProviderRegistry.size() > maxSize) {
            throw new SizeExceededException();
        }
    }

    public void disableProvider(String providerId) {
        BalancedProvider disabledProvider = providerRegistry.remove(providerId);
        if (disabledProvider != null) {
            disabledProviderRegistry.put(disabledProvider.getInstanceId(), disabledProvider);
        }
    }

    public void enableProvider(String providerId) {
        BalancedProvider enabledProvider = disabledProviderRegistry.remove(providerId);
        if (enabledProvider != null) {
            providerRegistry.put(enabledProvider.getInstanceId(), enabledProvider);
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
