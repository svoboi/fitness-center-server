package cz.cvut.fit.tjv.fitnesscenter.controller.dto;

import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class GroupClassDto {
    public Long id;

    private LocalDateTime timeFrom;
    private LocalDateTime timeTo;
    private int capacity;

    Room room;

    SportType sportType;

    Set<Long> trainers;
}
