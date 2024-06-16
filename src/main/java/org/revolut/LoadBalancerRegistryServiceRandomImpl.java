package org.revolut;


import java.util.Random;

public class LoadBalancerRegistryServiceRandomImpl extends LoadBalancerRegistryServiceImpl {

    public interface RandomGenerator {
        int getNext();
    }

    private static final Random defaultRandom = new Random(System.currentTimeMillis());
    RandomGenerator randGen;

    public LoadBalancerRegistryServiceRandomImpl() {
        super();
        randGen = defaultRandom::nextInt;
    }

    public LoadBalancerRegistryServiceRandomImpl(int maxPodsCounts) {
        super(maxPodsCounts);
        randGen = defaultRandom::nextInt;
    }

    public LoadBalancerRegistryServiceRandomImpl(int maxPodsCounts, RandomGenerator generator) {
        super(maxPodsCounts);
        this.randGen = generator;
    }

    @Override
    public PodInstance getNext() {
        int podsCount = registeredPods.size();

        if (podsCount == 0) {
            throw new IllegalStateException("No registered pods in Random registry");
        }

        return registeredPods.get(this.randGen.getNext() % podsCount);
    }
}
