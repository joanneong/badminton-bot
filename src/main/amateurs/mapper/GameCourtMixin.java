package amateurs.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public abstract class GameCourtMixin {
    @JsonProperty("dev_court")
    @JsonDeserialize(using = GameCourtDeserializer.class)
    abstract void setCourts(List<String> courts);
}
