package org.revolut;

import java.util.ArrayList;
import java.util.List;

public abstract class LoadBalancerRegistryServiceImpl implements LoadBalancerRegistryService {
    private final static int DEFAULT_PODS_COUNT = 10;

    private final int maxPodsCount;
    protected List<PodInstance> registeredPods;

    public LoadBalancerRegistryServiceImpl() {
        this(DEFAULT_PODS_COUNT);
    }

    public LoadBalancerRegistryServiceImpl(int maxPodsCounts) {
        this.maxPodsCount = maxPodsCounts;
        registeredPods = new ArrayList<>(maxPodsCounts);
    }

    @Override
    public synchronized boolean register(PodInstance pod) {

        if (registeredPods.contains(pod)) {
            return false;
        }

        if (registeredPods.size() == maxPodsCount) {
            throw new IllegalStateException("Pods number overflowed");
        } else {
            registeredPods.add(pod);
            return true;
        }
    }
}
