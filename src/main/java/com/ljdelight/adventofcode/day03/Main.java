package com.ljdelight.adventofcode.day03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static class Bit {
        public int zeroCount;
        public int oneCount;
    }

    private static List<String> parseInputFile(String file) throws Exception {
        List<String> diagnostic = new ArrayList<>();
        try (Scanner s = new Scanner(Main.class.getResourceAsStream(file))) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                diagnostic.add(line);
            }
        }
        return diagnostic;
    }

    private static int getPowerConsumption(List<Bit> mergedDiagnosticCount) {
        StringBuilder gammaRateString = new StringBuilder();
        StringBuilder epsilonRateString = new StringBuilder();
        for (Bit b : mergedDiagnosticCount) {
            gammaRateString.append(b.oneCount > b.zeroCount ? "1" : "0");
            epsilonRateString.append(b.oneCount > b.zeroCount ? "0" : "1");
        }

        int gammaRate = Integer.parseInt(gammaRateString.toString(), 2);
        int epsilonRate = Integer.parseInt(epsilonRateString.toString(), 2);

        return gammaRate * epsilonRate;
    }

    private static List<Bit> fullDiagnosticToSummary(List<String> diagnostic) throws Exception {
        List<Bit> mergedDiagnosticCount = new ArrayList<>();
        for (int i = 0; i < diagnostic.get(0).length(); i++) {
            mergedDiagnosticCount.add(new Bit());
        }

        for (String s : diagnostic) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '0') {
                    mergedDiagnosticCount.get(i).zeroCount += 1;
                } else if (s.charAt(i) == '1') {
                    mergedDiagnosticCount.get(i).oneCount += 1;
                } else {
                    throw new Exception("invalid character for power levels");
                }
            }
        }

        return mergedDiagnosticCount;
    }

    public static void main(String[] args) throws Exception {
        List<String> diagnostic = parseInputFile("input.txt");

        List<Bit> mergedDiagnosticCount = fullDiagnosticToSummary(diagnostic);
        int powerConsumption = getPowerConsumption(mergedDiagnosticCount);
        System.out.println("P1: PowerConsumption=" + powerConsumption);

        int oxygenGeneratorRating = getOxygenGeneratorRating(new ArrayList<>(diagnostic));
        int co2ScrubberRating = getCo2ScrubberRating(new ArrayList<>(diagnostic));


        System.out.printf("P2: lifeSupportRating=%d (oxygenGeneratorRating=%d co2ScrubberRating=%d)%n", oxygenGeneratorRating * co2ScrubberRating, oxygenGeneratorRating, co2ScrubberRating);
    }

    private static int getOxygenGeneratorRating(List<String> oxygenGeneratorRatingList) throws Exception {
        int i = 0;
        while (oxygenGeneratorRatingList.size() > 1) {
            List<Bit> mergedDiagnosticCount = fullDiagnosticToSummary(oxygenGeneratorRatingList);
            Bit b = mergedDiagnosticCount.get(i);
            // Keep the most common. If it's a tie, remove the '0's.
            char deleteThisChar;
            if (b.zeroCount == b.oneCount) {
                LOG.debug("TIE; delete 0s");
                deleteThisChar = '0';
            } else if (b.zeroCount > b.oneCount) {
                LOG.debug("0 is more popular; delete 1s");
                deleteThisChar = '1';
            } else {
                LOG.debug("1 is more popular; delete 0s");
                deleteThisChar = '0';
            }
            ListIterator<String> it = oxygenGeneratorRatingList.listIterator();
            while (it.hasNext() && oxygenGeneratorRatingList.size() > 1) {
                String s = it.next();
                if (s.charAt(i) == deleteThisChar) {
                    LOG.debug("REMOVING {} matching {}", s, deleteThisChar);
                    it.remove();
                }
            }
            LOG.debug("OXYGENLIST={}", String.join(",", oxygenGeneratorRatingList));
            i += 1;
        }
        LOG.debug("OxygenGeneratorRating={}", oxygenGeneratorRatingList.get(0));
        return Integer.parseInt(oxygenGeneratorRatingList.get(0), 2);
    }

    private static int getCo2ScrubberRating(List<String> co2ScrubberRatingList) throws Exception {
        int i = 0;
        while (co2ScrubberRatingList.size() > 1) {
            List<Bit> mergedDiagnosticCount = fullDiagnosticToSummary(co2ScrubberRatingList);
            Bit b = mergedDiagnosticCount.get(i);
            // Keep the least common. If it's a tie, remove the '1's
            char deleteThisChar;
            if (b.zeroCount == b.oneCount) {
                deleteThisChar = '1';
            } else if (b.zeroCount > b.oneCount) {
                deleteThisChar = '0';
            } else {
                deleteThisChar = '1';
            }
            ListIterator<String> it = co2ScrubberRatingList.listIterator();
            while (it.hasNext() && co2ScrubberRatingList.size() > 1) {
                if (it.next().charAt(i) == deleteThisChar) {
                    it.remove();
                }
            }
            i += 1;
        }
        LOG.debug("co2ScrubberRating={}", co2ScrubberRatingList.get(0));
        return Integer.parseInt(co2ScrubberRatingList.get(0), 2);
    }
}
