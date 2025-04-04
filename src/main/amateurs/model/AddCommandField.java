package amateurs.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AddCommandField {
    LOCATION("Location", 1, false, false),
    COURTS("Court(s)", 2, false, false),
    DATE("Date", 3, true, false),
    TIME_PERIOD("Time period", 4, true, false),
    START_HOUR("Start hour", 5, true, false),
    START_MINUTE("Start minute", 6, true, false),
    DURATION("Duration (hours)", 7, true, false),
    MAX_PLAYERS("Max players", 8, true, false),
    PRICE_PER_PAX("Price per pax", 9, true, false),
    CONFIRM_GAME("Confirm game", 10, false, true);

    private final String displayName;

    private final int step;

    private final boolean useKeyboard;

    private final boolean isTerminal;

    AddCommandField(String displayName, int step, boolean useKeyboard, boolean isTerminal) {
        this.displayName = displayName;
        this.step = step;
        this.useKeyboard = useKeyboard;
        this.isTerminal = isTerminal;
    }

    public static AddCommandField getFieldByStep(int step) {
        return Arrays.stream(AddCommandField.values())
                .filter(field -> field.getStep() == step)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
