package com.matiasa.iptiq.balancingstrategies;

import com.matiasa.iptiq.loadbalancers.BalancedProvider;
import com.matiasa.iptiq.providers.Provider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class RoundRobinBalancingStrategyTest {

    @Test
    public void shouldChooseSingleAvailableProvider() throws NoAvailableProviderException {
        // given
        BalancingStrategy underTest = new RoundRobinBalancingStrategy();
        Map<String, BalancedProvider> providerRegistry = new LinkedHashMap<>();
        BalancedProvider provider = new Provider();
        providerRegistry.put(provider.getInstanceId(), provider);

        // when - then
        assertThat(underTest.getNextProvider(providerRegistry), is(provider));
        assertThat(underTest.getNextProvider(providerRegistry), is(provider));
    }

    @Test
    public void shouldUseRoundRobinProviderSelectionPrinciple() throws NoAvailableProviderException {
        // given
        BalancingStrategy underTest = new RoundRobinBalancingStrategy();
        Map<String, BalancedProvider> providerRegistry = new LinkedHashMap<>();
        BalancedProvider provider1 = new Provider();
        providerRegistry.put(provider1.getInstanceId(), provider1);
        BalancedProvider provider2 = new Provider();
        providerRegistry.put(provider2.getInstanceId(), provider2);

        // when - then
        assertThat(underTest.getNextProvider(providerRegistry), is(provider1));
        assertThat(underTest.getNextProvider(providerRegistry), is(provider2));
        assertThat(underTest.getNextProvider(providerRegistry), is(provider1));
    }

    @Test(expected = NoAvailableProviderException.class)
    public void shouldThrowExceptionWhenNoProvidersAvailable() throws NoAvailableProviderException {
        // given
        BalancingStrategy underTest = new RoundRobinBalancingStrategy();
        Map<String, BalancedProvider> providerRegistry = new LinkedHashMap<>();

        // when
        underTest.getNextProvider(providerRegistry);
    }
}
