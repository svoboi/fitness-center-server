package cz.cvut.fit.tjv.fitnesscenter.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
public class GroupClass {
    @Id
    @GeneratedValue
    Long id;

    private LocalDate day;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    private int capacity;

    @ManyToOne
    Room room;
}
