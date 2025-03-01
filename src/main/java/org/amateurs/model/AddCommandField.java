package org.amateurs.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AddCommandField {
    LOCATION(1),
    COURTS(2),
    DATE(3),
    TIME_PERIOD(4),
    START_HOUR(5),
    START_MINUTE(6),
    DURATION(7);

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
