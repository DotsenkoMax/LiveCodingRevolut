package org.revolut;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoadBalancerRegistyServiceTest {

    @Test
    public void testWith1Instance(){
        LoadBalancerRegistryService loadBalancerRegistryService = new LoadBalancerRegistryServiceRandomImpl(1);
        Assertions.assertTrue(loadBalancerRegistryService.register(new PodInstance("https://some_url")));
    }

    @Test
    public void testWith10InstanceAsDefaultConstructor(){
        LoadBalancerRegistryService loadBalancerRegistryService = new LoadBalancerRegistryServiceRandomImpl();

        StringBuilder defaultUrl = new StringBuilder();
        for(int i = 0 ; i < 10; ++i) {
            defaultUrl.append("a");
            Assertions.assertTrue(loadBalancerRegistryService.register(new PodInstance(defaultUrl.toString())));
        }
        Assertions.assertThrows(IllegalStateException.class, () -> loadBalancerRegistryService.register(new PodInstance("b")));
    }

    @Test
    public void testWithTheSamePodRegistration() {
        LoadBalancerRegistryService loadBalancerRegistryService = new LoadBalancerRegistryServiceRandomImpl();

        Assertions.assertTrue(loadBalancerRegistryService.register(new PodInstance("https://some_url")));
        Assertions.assertFalse(loadBalancerRegistryService.register(new PodInstance("https://some_url")));
    }

    @Test
    public void testRandomGetterSimpleGet() {
        // Given
        LoadBalancerRegistryService loadBalancerRegistryService = new LoadBalancerRegistryServiceRandomImpl(1);

        PodInstance defaultPodInstance = new PodInstance("https://some_url");

        loadBalancerRegistryService.register(defaultPodInstance);

        Assertions.assertEquals(defaultPodInstance, loadBalancerRegistryService.getNext());
    }

    @Test
    public void testRandomGetterRandomSeededGet() {
        // Given
        LoadBalancerRegistryService loadBalancerRegistryService = new LoadBalancerRegistryServiceRandomImpl(3, () -> 2);

        PodInstance defaultPodInstance1 = new PodInstance("https://some_url1");
        PodInstance defaultPodInstance2 = new PodInstance("https://some_url2");
        PodInstance defaultPodInstance3 = new PodInstance("https://some_url3");

        loadBalancerRegistryService.register(defaultPodInstance1);
        loadBalancerRegistryService.register(defaultPodInstance2);
        loadBalancerRegistryService.register(defaultPodInstance3);

        Assertions.assertEquals(defaultPodInstance3, loadBalancerRegistryService.getNext());
    }

    @Test
    public void testRandomGetterRandomWithNoElements() {
        LoadBalancerRegistryService loadBalancerRegistryService = new LoadBalancerRegistryServiceRandomImpl(3);

        Assertions.assertThrows(IllegalStateException.class, loadBalancerRegistryService::getNext);
    }

    @Test
    public void testRoundRobinGetterSimpleTest() {
        LoadBalancerRegistryService loadBalancerRegistryService = new LoadBalancerRegistryServiceRoundRobinImpl(2);

        PodInstance defaultPodInstance1 = new PodInstance("https://some_url1");
        PodInstance defaultPodInstance2 = new PodInstance("https://some_url2");

        loadBalancerRegistryService.register(defaultPodInstance1);
        loadBalancerRegistryService.register(defaultPodInstance2);

        Assertions.assertEquals(defaultPodInstance1, loadBalancerRegistryService.getNext());
        Assertions.assertEquals(defaultPodInstance2, loadBalancerRegistryService.getNext());
        Assertions.assertEquals(defaultPodInstance1, loadBalancerRegistryService.getNext());
    }



}
