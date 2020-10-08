package com.matiasa.iptiq.loadbalancers;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.matiasa.iptiq.Config;
import com.matiasa.iptiq.balancingstrategies.BalancingStrategy;
import com.matiasa.iptiq.balancingstrategies.NoAvailableProviderException;
import com.matiasa.iptiq.balancingstrategies.RandomBalancingStrategy;

public class LoadBalancer {
    private final int maxSize;
    private final BalancingStrategy strategy;
    private final Map<String, BalancedProvider> providerRegistry;
    private final Map<String, BalancedProvider> disabledProviderRegistry;
    private final Map<String, Integer> disabledProviderAliveCounters;

    private final ScheduledExecutorService heartbeatChecker = Executors.newSingleThreadScheduledExecutor();

    public LoadBalancer() {
        this(Config.DEFAULT_MAX_SIZE);
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
        this.providerRegistry = new LinkedHashMap<>();
        this.disabledProviderRegistry = new HashMap<>();
        this.disabledProviderAliveCounters = new HashMap<>();

        startup();
    }

    private void startup() {
        heartbeatChecker.scheduleAtFixedRate(
                () -> {
                    disableDeadProviders();
                    enableAliveProviders();
                },
                0, Config.HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS
        );
    }

    private void disableDeadProviders() {
        Set<String> providerIdsToDisable = new HashSet<>();
        for(BalancedProvider provider : providerRegistry.values()) {
            try {
                provider.check();
            } catch (Exception e) {
                providerIdsToDisable.add(provider.getInstanceId());
            }
        }

        for(String providerId : providerIdsToDisable) {
            disableProvider(providerId);
            disabledProviderAliveCounters.put(providerId, 0);
        }
    }

    private void enableAliveProviders() {
        Set<String> providerIdsToEnable = new HashSet<>();

        for(BalancedProvider provider : disabledProviderRegistry.values()) {
            try {
                provider.check();

                int counter = disabledProviderAliveCounters.get(provider.getInstanceId()) + 1;
                if(counter >= Config.REENABLE_THRESHOLD) {
                    providerIdsToEnable.add(provider.getInstanceId());
                } else {
                    disabledProviderAliveCounters.put(provider.getInstanceId(), counter);
                }
            } catch (Exception ignored) {}
        }

        for(String providerId : providerIdsToEnable) {
            enableProvider(providerId);
            disabledProviderAliveCounters.remove(providerId);
        }
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

    public int getSize() {
        return providerRegistry.size();
    }

    public int getMaxSize() {
        return maxSize;
    }
}
