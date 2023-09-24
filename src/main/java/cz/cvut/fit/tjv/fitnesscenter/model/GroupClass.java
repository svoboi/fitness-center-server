package cz.cvut.fit.tjv.fitnesscenter.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class GroupClass {
    @Id
    @GeneratedValue
    public Long id;

    private LocalDate day;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    private int capacity;

    @ManyToOne
    Room room;

    @ManyToOne
    SportType sportType;

    @ManyToMany
    Set<User> trainers;
}
