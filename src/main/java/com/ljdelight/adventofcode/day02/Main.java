package com.ljdelight.adventofcode.day02;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static class Move {
        enum Direction {UP, DOWN, LEFT, RIGHT}

        final Direction direction;
        final int length;

        private static Direction getDirectionFromString(String dir) throws Exception {
            return switch (dir.toLowerCase(Locale.ENGLISH).trim()) {
                case "up" -> Direction.UP;
                case "down" -> Direction.DOWN;
                case "backward" -> Direction.LEFT;
                case "forward" -> Direction.RIGHT;
                default -> throw new Exception("String direction '" + dir + "' is not a valid Direction");
            };
        }

        public Move(String direction, int length) throws Exception {
            this.direction = getDirectionFromString(direction);
            this.length = length;
        }
    }

    private static List<Move> parseInputFile(String file) throws Exception {
        List<Move> moves = new ArrayList<>();
        try (Scanner s = new Scanner(Main.class.getResourceAsStream(file))) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\s+");
                moves.add(new Move(parts[0], Integer.parseInt(parts[1])));
            }
        }
        return moves;
    }

    public static void main(String[] args) throws Exception {

        List<Move> moves = parseInputFile("input.txt");
        int forward = 0;
        int depth = 0;
        for (Move move : moves) {
            if (move.direction == Move.Direction.RIGHT) {
                forward += move.length;
            } else if (move.direction == Move.Direction.DOWN) {
                depth += move.length;
            } else if (move.direction == Move.Direction.UP) {
                depth -= move.length;
                if (depth < 0) {
                    throw new Exception("The submarine is out of the water");
                }
            }
        }

        System.out.println("P1: Forward " + forward + " units; depth of " + depth + "; Product is " + (forward * depth));


        forward = 0;
        depth = 0;
        int aim = 0;
        for (Move move : moves) {
            if (move.direction == Move.Direction.RIGHT) {
                forward += move.length;
                depth += aim * move.length;
            } else if (move.direction == Move.Direction.DOWN) {
                aim += move.length;
            } else if (move.direction == Move.Direction.UP) {
                aim -= move.length;
            }
        }

        System.out.println("P2: Forward " + forward + " units; depth of " + depth + "; Product is " + (forward * depth));
    }

}
