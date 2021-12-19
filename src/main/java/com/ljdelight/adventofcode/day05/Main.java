package com.ljdelight.adventofcode.day05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    // (x,y). (0,0) is the top left.
    //   <-- x is here -->
    // .......1..        ^
    //..1....1..         |
    //..1....1..         |
    //.......1..         y is here
    //.112111211         |
    //..........         |
    //..........         V
    //..........
    //..........
    //222111....
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static class P {
        public final int x;
        public final int y;

        public P(String x, String y) {
            this.x = Integer.parseInt(x);
            this.y = Integer.parseInt(y);
        }

        @Override
        public String toString() {
            return "P{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private static class Line {
        public final P src;
        public final P dst;
        public final Type type;

        enum Type {HORZ, VERT, DIAG}

        public Line(P src, P dst) {
            if (src.x == dst.x) {
                this.type = Type.VERT;
                if (src.y < dst.y) {
                    this.src = src;
                    this.dst = dst;
                } else {
                    this.src = dst;
                    this.dst = src;
                }
            } else if (src.y == dst.y) {
                this.type = Type.HORZ;
                if (src.x < dst.x) {
                    this.src = src;
                    this.dst = dst;
                } else {
                    this.src = dst;
                    this.dst = src;
                }
            } else {
                this.type = Type.DIAG;
                // TODO: determine a way to order these
                this.src = src;
                this.dst = dst;
            }
        }

        public boolean isDiagonal() {
            return this.type == Type.DIAG;
        }

        @Override
        public String toString() {
            return "Line{" +
                    "src=" + src +
                    ", dst=" + dst +
                    ", type=" + type +
                    '}';
        }
    }

    private static void fill(int[][] map, Line line) {
        LOG.debug("Filling line segment {}", line);
        if (line.type == Line.Type.HORZ) {
            for (int x = line.src.x; x <= line.dst.x; x++) {
                LOG.trace("Filling {},{}", line.src.y, x);
                map[line.src.y][x] += 1;
            }
        } else if (line.type == Line.Type.VERT) {
            for (int y = line.src.y; y <= line.dst.y; y++) {
                LOG.trace("Filling {},{}", y, line.src.x);
                map[y][line.src.x] += 1;
            }
        } else {
            // TODO: fill in a diag
            int x = line.src.x;
            int y = line.src.y;
            while (x != line.dst.x) {
                map[y][x] += 1;
                x += (x < line.dst.x) ? 1 : -1;
                y += (y < line.dst.y) ? 1 : -1;
            }
            map[y][x] += 1;
        }
    }

    private static void printMap(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        List<Line> segments = new ArrayList<>();
        try (Scanner s = new Scanner(Objects.requireNonNull(Main.class.getResourceAsStream("input.txt")))) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] allCoords = line.split(",|\\s+->\\s+");
                LOG.trace("Line: {}", Arrays.deepToString(allCoords));
                segments.add(new Line(new P(allCoords[0], allCoords[1]), new P(allCoords[2], allCoords[3])));
            }
        }

        LOG.debug("Found {} line segments", segments.size());
        int maxX = 0;
        int maxY = 0;
        for (Line line : segments) {
            maxX = Math.max(maxX, Math.max(line.src.x, line.dst.x));
            maxY = Math.max(maxY, Math.max(line.src.y, line.dst.y));
        }
        LOG.debug("For entire map, maxX={} maxY={}", maxX, maxY);

        int[][] map = new int[1000][1000];

        for (Line line : segments) {
            if (line.isDiagonal()) {
                continue;
            }
            fill(map, line);
        }

        int withoutDiagOverlapPoints = getOverlapPointCount(map);
        System.out.println("Only vertical and horizontal lines: Number of points where at least 2 lines overlap: " + withoutDiagOverlapPoints);

        // Now fill in the diag lines
        for (Line line : segments) {
            if (line.isDiagonal()) {
                fill(map, line);
            }
        }
        int allOverlapPoints = getOverlapPointCount(map);
        System.out.println("All lines: Number of points where at least 2 lines overlap: " + allOverlapPoints);
//        if (LOG.isDebugEnabled()) {
//            printMap(map);
//        }

    }

    private static int getOverlapPointCount(int[][] map) {
        int overlapPoints = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] > 1) {
                    overlapPoints += 1;
                }
            }
        }
        return overlapPoints;
    }
}
