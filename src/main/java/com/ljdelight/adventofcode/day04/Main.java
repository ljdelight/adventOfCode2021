package com.ljdelight.adventofcode.day04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final int MARKED = -1;

    private static void markBoard(Integer[][] board, Integer draw) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (Objects.equals(board[i][j], draw)) {
                    board[i][j] = MARKED;
                }
            }
        }
    }

    private static boolean isBoardBingo(Integer[][] board) {
        // Check the horizontal
        for (int i = 0; i < board.length; i++) {
            boolean isBingo = true;
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != MARKED) {
                    isBingo = false;
                    break;
                }
            }
            if (isBingo) {
                return true;
            }
        }

        // Check the vertical
        for (int j = 0; j < board[0].length; j++) {
            boolean isBingo = true;
            for (int i = 0; i < board.length; i++) {
                if (board[i][j] != MARKED) {
                    isBingo = false;
                    break;
                }
            }
            if (isBingo) {
                return true;
            }
        }

        return false;
    }

    private static int scoreBingoBoard(Integer[][] board, Integer draw) {
        int sum = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != MARKED) {
                    sum += board[i][j];
                }
            }
        }
        return sum * draw;
    }

    public static void main(String[] args) {
        final Integer[] bingoNumberDraws;
        List<Integer[][]> boards = new ArrayList<>();
        try (Scanner s = new Scanner(Objects.requireNonNull(Main.class.getResourceAsStream("input.txt")))) {
            String numberDrawsStr = s.nextLine();
            bingoNumberDraws = Arrays.stream(numberDrawsStr.split(","))
                    .map(String::trim)
                    .map(Integer::valueOf)
                    .toArray(Integer[]::new);

            // Each board is 5x5. Instead of doing extreme parsing, just make a board and read 25 numbers.
            // If there are more numbers, make a new board and read another 25.
            // Do this until EOF.
            while (s.hasNextInt()) {
                Integer[][] board = new Integer[5][5];
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        board[i][j] = s.nextInt();
                    }
                }
                boards.add(board);
            }
        }

        LOG.debug("Found {} bingo boards", boards.size());

        // Find the first winning board
        boolean isBingo = false;
        int i = 0;
        for (; !isBingo && i < bingoNumberDraws.length; i++) {
            Integer draw = bingoNumberDraws[i];
            for (Integer[][] board : boards) {
                markBoard(board, draw);
                if (isBoardBingo(board)) {
                    isBingo = true;
                    LOG.info("BINGO: First Board at draw={}\n{}", draw, Arrays.deepToString(board));
                    System.out.println("P1: Score=" + scoreBingoBoard(board, draw));
                    boards.remove(board);
                    break;
                }
            }
        }

        i -= 1;
        // Find the last winning board
        // TODO: we could refactor and merge the loops searching for the first and last board. Meh.
        for (; i < bingoNumberDraws.length; i++) {
            Integer draw = bingoNumberDraws[i];
            ListIterator<Integer[][]> it = boards.listIterator();
            while (it.hasNext()) {
                Integer[][] board = it.next();
                markBoard(board, draw);
                if (isBoardBingo(board)) {
                    if (boards.size() == 1) {
                        LOG.info("BINGO: Last Board at draw={}\n{}", draw, Arrays.deepToString(board));
                        System.out.println("P2: Score=" + scoreBingoBoard(board, draw));
                    }
                    it.remove();
                }
            }
        }
    }
}
