package com.ljdelight.adventofcode.day09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static class Pair {
        final int a;
        final int b;

        public Pair(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "a=" + a +
                    ", b=" + b +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return a == pair.a && b == pair.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

    private static boolean isMinimumLocation(List<List<Integer>> map, int i, int j) {
        int point = map.get(i).get(j);
        if (i - 1 >= 0 && map.get(i - 1).get(j) <= point) { // up
            return false;
        } else if (i + 1 < map.size() && map.get(i + 1).get(j) <= point) { // down
            return false;
        } else if (j - 1 >= 0 && map.get(i).get(j - 1) <= point) { // left
            return false;
        } else if (j + 1 < map.get(i).size() && map.get(i).get(j + 1) <= point) { // right
            return false;
        }

        return true;
    }

    private static int getRiskLevel(List<List<Integer>> map, List<Pair> lowPoints) {
        int res = 0;
        for (Pair p : lowPoints) {
            res += 1 + map.get(p.a).get(p.b);
        }
        return res;
    }

    private static int getBasinSize(List<List<Integer>> map, Pair lowPoint) {
        int size = 0;
        Set<Pair> seen = new HashSet<>();
        Queue<Pair> q = new ArrayDeque<>();
        q.add(lowPoint);
        seen.add(lowPoint);
        while (!q.isEmpty()) {
            Pair p = q.remove();

            int i = p.a;
            int j = p.b;
            int point = map.get(i).get(j);
            if (point == 9) { // 9 does not count for a basin
                continue;
            }
            size += 1;

            if (i - 1 >= 0 && map.get(i - 1).get(j) > point) { // up
                Pair neighbor = new Pair(i - 1, j);
                if (!seen.contains(neighbor)) {
                    q.add(neighbor);
                    seen.add(neighbor);
                }
            }
            if (i + 1 < map.size() && map.get(i + 1).get(j) > point) { // down
                Pair neighbor = new Pair(i + 1, j);
                if (!seen.contains(neighbor)) {
                    q.add(neighbor);
                    seen.add(neighbor);
                }
            }
            if (j - 1 >= 0 && map.get(i).get(j - 1) > point) { // left
                Pair neighbor = new Pair(i, j - 1);
                if (!seen.contains(neighbor)) {
                    q.add(neighbor);
                    seen.add(neighbor);
                }
            }
            if (j + 1 < map.get(i).size() && map.get(i).get(j + 1) > point) { // right
                Pair neighbor = new Pair(i, j + 1);
                if (!seen.contains(neighbor)) {
                    q.add(neighbor);
                    seen.add(neighbor);
                }
            }

        }

        return size;
    }

    public static void main(String[] args) throws Exception {
        List<List<Integer>> map = new ArrayList<>();
        try (Scanner s = new Scanner(Objects.requireNonNull(Main.class.getResourceAsStream("input.txt")))) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                List<Integer> row = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    row.add(line.charAt(i) - '0');
                }
                map.add(row);
            }
        }
        LOG.debug("Initial: {}", map);

        List<Pair> lowPoints = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (isMinimumLocation(map, i, j)) {
                    lowPoints.add(new Pair(i, j));
                }
            }
        }

        LOG.debug("Found {} low points", lowPoints.size());
        System.out.println("P1: Risk Level is " + getRiskLevel(map, lowPoints));


        List<Integer> basinSizes = new ArrayList<>();
        for (Pair lowPoint : lowPoints) {
            int basinSize = getBasinSize(map, lowPoint);
            basinSizes.add(basinSize);
            LOG.debug("Low point {} has a basic size of {}", lowPoint, basinSize);
        }
        basinSizes.sort(Collections.reverseOrder());

        LOG.debug("All sizes of basins: {}", basinSizes);
        long product = 1;
        for (int i = 0; i < 3; i++) {
            product = product * basinSizes.get(i);
        }
        System.out.println("P2: Product of the three larges basins is " + product);
    }
}
