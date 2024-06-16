package org.revolut;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LoadBalancerRegistryServiceRoundRobinImpl extends LoadBalancerRegistryServiceImpl {

    private AtomicLong currentPodToChoose = new AtomicLong(0);

    public LoadBalancerRegistryServiceRoundRobinImpl() {
        super();
    }

    public LoadBalancerRegistryServiceRoundRobinImpl(int maxPodsCounts) {
        super(maxPodsCounts);
    }

    @Override
    public PodInstance getNext() {
        int registeredPodsNumber = this.registeredPods.size();
        if (registeredPodsNumber == 0) {
            throw new IllegalStateException("No registered pods in Round robin registry");
        }

        int currentPodToChoose = (int) (this.currentPodToChoose.getAndIncrement() % registeredPodsNumber);
        return this.registeredPods.get(currentPodToChoose);
    }
}
