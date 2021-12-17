package com.ljdelight.adventofcode.day01;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<Integer> values = new ArrayList<>();
        try (Scanner s = new Scanner(Main.class.getResourceAsStream("input.txt"))) {
            while (s.hasNextInt()) {
                values.add(s.nextInt());
            }
        }

        int increaseCount = 0;
        int prev = Integer.MAX_VALUE;
        for (int v : values) {
            if (prev < v) {
                increaseCount += 1;
            }
            prev = v;
        }

        // This many measurements are larger than the previous
        System.out.println("P1: " + increaseCount);

        int p2IncreaseCount = 0;
        int p2prev = Integer.MAX_VALUE;
        for (int i = 0; i < values.size() - 2; i++) {
            int sum = values.get(i) + values.get(i + 1) + values.get(i + 2);
            if (p2prev < sum) {
                p2IncreaseCount += 1;
            }
            p2prev = sum;
        }
        System.out.println("P2: " + p2IncreaseCount);
    }

}