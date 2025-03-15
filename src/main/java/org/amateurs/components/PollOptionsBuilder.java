package org.amateurs.components;

import org.telegram.telegrambots.meta.api.objects.polls.input.InputPollOption;

import java.util.List;

public class PollOptionsBuilder {
    public static List<InputPollOption> buildPollOptions(List<String> pollOptions) {
        assert pollOptions.size() >= 2;
        return pollOptions.stream()
                .map(PollOptionsBuilder::buildPollOption)
                .toList();
    }

    private static InputPollOption buildPollOption(String option) {
        return InputPollOption.builder()
                .text(option)
                .build();
    }
}
