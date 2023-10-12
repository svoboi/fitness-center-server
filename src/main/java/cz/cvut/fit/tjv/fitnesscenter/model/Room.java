package cz.cvut.fit.tjv.fitnesscenter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Room {
    @Id
    @GeneratedValue
    public Long id;

    @NotBlank(message = "capacity is required.")
    private Integer capacity;
}
