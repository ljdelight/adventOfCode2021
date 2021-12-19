package com.ljdelight.adventofcode.day07;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static int energyUsed(List<Integer> initialHorizontalPositions, int position) {
        int energy = 0;
        for (Integer it : initialHorizontalPositions) {
            energy += Math.abs(it - position);
        }
        return energy;
    }

    private static int energyUsedCorrected(List<Integer> initialHorizontalPositions, int position) {
        int energy = 0;
        for (Integer it : initialHorizontalPositions) {
            int diff = Math.abs(it - position);
            energy += diff * (diff + 1) / 2;
        }
        return energy;
    }

    public static void main(String[] args) throws Exception {
        final List<Integer> initialHorizontalPositions;
        try (Scanner s = new Scanner(Objects.requireNonNull(Main.class.getResourceAsStream("input.txt")))) {
            initialHorizontalPositions = Arrays.stream(s.nextLine().trim().split(",")).map(Integer::valueOf).toList();
        }
        LOG.debug("Initial Positions: {}", initialHorizontalPositions);

        int min = Collections.min(initialHorizontalPositions);
        int max = Collections.max(initialHorizontalPositions);

        int minTotalEnergy = Integer.MAX_VALUE;
        for (int i = min; i <= max; i++) {
            int e = energyUsed(initialHorizontalPositions, i);
            if (e < minTotalEnergy) {
                minTotalEnergy = e;
            }
            LOG.trace("Energy used to move to position {}: {}", i, energyUsed(initialHorizontalPositions, i));
        }

        System.out.println("P1: Min is " + minTotalEnergy);


        minTotalEnergy = Integer.MAX_VALUE;
        for (int i = min; i <= max; i++) {
            int e = energyUsedCorrected(initialHorizontalPositions, i);
            if (e < minTotalEnergy) {
                minTotalEnergy = e;
            }
            LOG.trace("Energy used to move to position {}: {}", i, energyUsed(initialHorizontalPositions, i));
        }
        System.out.println("P2: Min is " + minTotalEnergy);

    }
}
