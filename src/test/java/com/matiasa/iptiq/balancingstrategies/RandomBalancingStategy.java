package com.matiasa.iptiq.balancingstrategies;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.matiasa.iptiq.loadbalancers.BalancedProvider;
import com.matiasa.iptiq.providers.Provider;

@RunWith(JUnit4.class)
public class RandomBalancingStategy {

    @Test
    public void shouldChooseSingleAvailableProvider() throws NoAvailableProviderException {
        // given
        BalancingStrategy underTest = new RandomBalancingStrategy();
        Map<String, BalancedProvider> providerRegistry = new LinkedHashMap<>();
        BalancedProvider provider = new Provider();
        providerRegistry.put(provider.getInstanceId(), provider);

        // when
        BalancedProvider nextProvider = underTest.getNextProvider(providerRegistry);

        // then
        assertThat(nextProvider, is(provider));
    }

    @Test(expected = NoAvailableProviderException.class)
    public void shouldThrowExceptionWhenNoProvidersAvailable() throws NoAvailableProviderException {
        // given
        BalancingStrategy underTest = new RandomBalancingStrategy();
        Map<String, BalancedProvider> providerRegistry = new LinkedHashMap<>();

        // when
        underTest.getNextProvider(providerRegistry);
    }
}
