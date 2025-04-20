package amateurs.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonFilter("supabaseCourtFilter")
public class Court {
    Long id;

    @JsonProperty("game_id")
    Long gameId;

    String court;

    public Court(Long gameId, String court) {
        this.gameId = gameId;
        this.court = court;
    }
}
