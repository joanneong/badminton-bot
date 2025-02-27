package org.amateurs.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AddCommandField {
    DATE(1),
    TIME_PERIOD(2),
    START_HOUR(3),
    START_MINUTE(4),
    DURATION(5),
    LOCATION(6),
    COURTS(7);

    private final int step;

    AddCommandField(int step) {
        this.step = step;
    }

    public static AddCommandField getFieldByStep(int step) {
        return Arrays.stream(AddCommandField.values())
                .filter(field -> field.getStep() == step)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
