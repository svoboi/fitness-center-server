package cz.cvut.fit.tjv.fitnesscenter.controller.dto;

import cz.cvut.fit.tjv.fitnesscenter.model.Room;
import cz.cvut.fit.tjv.fitnesscenter.model.SportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Vector;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupClassDto {
    public Long id;

    private LocalDateTime timeFrom;
    private LocalDateTime timeTo;
    private int capacity;

    private Room room;

    private SportType sportType;

    private Vector<Map<String, Long>> trainers;
}
