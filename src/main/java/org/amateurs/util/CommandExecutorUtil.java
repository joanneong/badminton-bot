package org.amateurs.util;

import java.util.List;
import java.util.stream.IntStream;

public class CommandExecutorUtil {
    public static final String BADMINTON_EMOJI = new String(Character.toChars(
            Integer.parseInt("1F3F8", 16)));

    /**
     * Generate sequential integers in the range [inclusiveStart, inclusiveEnd] with a specified interval
     * @param inclusiveStart    inclusiveStart of range
     * @param inclusiveEnd      inclusiveEnd of range
     * @param interval          interval between each generated integer
     * @return                  list of sequential integers
     */
    public static List<String> generateSequentialInt(int inclusiveStart, int inclusiveEnd, int interval) {
        return IntStream.iterate(inclusiveStart, i -> i + interval)
                .limit((inclusiveEnd - inclusiveStart) / interval + 1)
                .mapToObj(String::valueOf)
                .toList();
    }
}
