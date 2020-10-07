package com.matiasa.iptiq.loadbalancers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.matiasa.iptiq.balancingstrategies.NoAvailableProviderException;
import com.matiasa.iptiq.providers.Provider;

@RunWith(JUnit4.class)
public class LoadBalancerTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInstantiationWhenMaxCapacityLessThanOne() {
        // when
        new LoadBalancer(0);
    }

    @Test
    public void shouldRegisterProvider() throws SizeExceededException {
        // given
        LoadBalancer underTest = new LoadBalancer();
        Provider provider = new Provider();

        // when
        underTest.registerProvider(provider);

        // then
        assertThat(underTest.size(), is(equalTo(1)));
    }

    @Test(expected = SizeExceededException.class)
    public void shouldRejectRegisteringExcessiveNumberOfProviders() throws SizeExceededException {
        // given
        LoadBalancer underTest = new LoadBalancer();

        // when
        for(int i = 0; i < underTest.getMaxSize()+1; i++) {
            underTest.registerProvider(new Provider());
        }
    }

    @Test
    public void shouldCallProvider() throws NoAvailableProviderException, SizeExceededException {
        // given
        LoadBalancer underTest = new LoadBalancer();
        underTest.registerProvider(new Provider());

        // when
        String result = underTest.get();

        // then
        assertThat(result, is(notNullValue()));
    }
}
