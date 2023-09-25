package cz.cvut.fit.tjv.fitnesscenter.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class GroupClass {
    @Id
    @GeneratedValue
    public Long id;

    private LocalDateTime timeFrom;
    private LocalDateTime timeTo;
    private int capacity;

    @ManyToOne
    Room room;

    @ManyToOne
    SportType sportType;

    @ManyToMany
    Set<User> trainers;
}
