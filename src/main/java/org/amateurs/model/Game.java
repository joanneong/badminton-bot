package org.amateurs.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Game {
    LocalDateTime date;

    LocalDateTime startTime;

    LocalDateTime endTime;

    String location;

    List<String> courts;

    List<String> players;
}
