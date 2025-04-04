package amateurs.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Player {
    long id;

    @NonNull
    String name;

    @Override
    public String toString() {
        return name;
    }
}
