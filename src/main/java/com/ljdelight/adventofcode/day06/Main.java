package com.ljdelight.adventofcode.day06;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(com.ljdelight.adventofcode.day05.Main.class);
    private static final int DAYS = 256;

    public static void main(String[] args) throws Exception {
        final List<Integer> initalState;
        try (Scanner s = new Scanner(Objects.requireNonNull(Main.class.getResourceAsStream("input.txt")))) {
            String initialStateStr = s.nextLine().trim();
            initalState = Arrays.stream(initialStateStr.split(",")).map(Integer::valueOf).toList();
        }

        long[] fishCount = new long[9];
        long[] fishCountNext = new long[9];

        for (int i = 0; i < initalState.size(); i++) {
            int idx = initalState.get(i);
            fishCount[idx] += 1;
        }
        LOG.debug("Initial State: {}", fishCount);

        for (int day = 1; day <= DAYS; day++) {
            long fishToAdd = fishCount[0];

            for (int i = fishCount.length - 1; i >= 0; i--) {
                if (i > 0) {
                    fishCountNext[i - 1] = fishCount[i];
                } else {
                    fishCountNext[6] += fishCount[i];
                    fishCountNext[8] = fishCount[i];
                }
            }

            LOG.debug("After {} days: {}", String.format("%2d", day), fishCount);

            long[] tmp = fishCount;
            fishCount = fishCountNext;
            fishCountNext = tmp;
            Arrays.fill(fishCountNext, 0);
        }

        long totalFish = 0;
        for (long j : fishCount) {
            totalFish += j;
        }
        System.out.println("Fish count after 80 days: " + totalFish);
    }

    // This works for part 1, and does not work for part 2.
    private static int bruteForceFishCount(List<Integer> initalState) {

        LOG.debug("Initial State: {}", initalState.stream().map(Object::toString).collect(Collectors.joining(",")));

        final List<Integer> fishSimulation = new LinkedList<>(initalState);

        for (int day = 1; day <= 256; day++) {
            ListIterator<Integer> it = fishSimulation.listIterator();
            int newFishToAdd = 0;
            while (it.hasNext()) {
                Integer daysToRepoduce = it.next();
                if (daysToRepoduce == 0) {
                    newFishToAdd += 1;
                    it.set(6);
                } else {
                    it.set(daysToRepoduce - 1);
                }
            }
            LOG.debug("Adding {} new fish", newFishToAdd);
            for (int i = 0; i < newFishToAdd; i++) {
                fishSimulation.add(8);
            }
            LOG.debug("After {} days: {}", String.format("%2d", day), fishSimulation);
        }
        return fishSimulation.size();
    }
}
