package org.amateurs.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AddCommandField {
    LOCATION("Location", 1, false),
    COURTS("Court(s)", 2, false),
    DATE("Date", 3, true),
    TIME_PERIOD("Time period", 4, true),
    START_HOUR("Start hour", 5, true),
    START_MINUTE("Start minute", 6, true),
    DURATION("Duration", 7, true),
    MAX_PLAYERS("Max players", 8, true);

    private final String displayName;

    private final int step;

    private boolean useKeyboard;

    AddCommandField(String displayName, int step, boolean useKeyboard) {
        this.displayName = displayName;
        this.step = step;
        this.useKeyboard = useKeyboard;
    }

    public static AddCommandField getFieldByStep(int step) {
        return Arrays.stream(AddCommandField.values())
                .filter(field -> field.getStep() == step)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
