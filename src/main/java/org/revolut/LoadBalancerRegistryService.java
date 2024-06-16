package org.revolut;

public interface LoadBalancerRegistryService {
    boolean register(PodInstance pod);
    PodInstance getNext();
}
