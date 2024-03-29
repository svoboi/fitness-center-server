package cz.cvut.fit.tjv.fitnesscenter.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupClass {
    @Id
    @GeneratedValue
    public Long id;

    @NotNull(message = "timeFrom is required")
    private LocalDateTime timeFrom;
    @NotNull(message = "timeTo is required")
    private LocalDateTime timeTo;
    @NotNull(message = "capacity is required.")
    @Min(0)
    private Integer capacity;

    @ManyToOne
    private Room room;

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
