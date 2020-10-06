package com.matiasa.iptiq.providers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class ProviderTest {

    @Test
    public void shouldReturnInstanceId() {
        // given
        Provider underTest = new Provider();

        // when
        String instanceId = underTest.get();

        // then
        assertThat(instanceId, is(notNullValue()));
    }

    @Test
    public void shouldReturnUniqueInstanceId() {
        // given
        Provider undertest1 = new Provider();
        Provider undertest2 = new Provider();

        // when
        String instanceId1 = undertest1.get();
        String instanceId2 = undertest2.get();

        // then
        assertThat(instanceId1, is(not(equalTo(instanceId2))));
    }
}
