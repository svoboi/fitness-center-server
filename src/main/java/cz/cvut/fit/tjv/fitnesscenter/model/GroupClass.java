package cz.cvut.fit.tjv.fitnesscenter.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupClass {
    @Id
    @GeneratedValue
    public Long id;

    @NotNull
    private LocalDateTime timeFrom;
    @NotNull
    private LocalDateTime timeTo;
    @NotNull
    private int capacity;

    @NotNull
    @ManyToOne
    private Room room;

    @NotNull
    @ManyToOne
    private SportType sportType;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> trainers = new HashSet<>();

    public void addTrainer(User user) {
        trainers.add(user);
    }

    public void removeTrainer(User user) {
        trainers.remove(user);
    }
}
