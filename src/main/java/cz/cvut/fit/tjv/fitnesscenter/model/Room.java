package cz.cvut.fit.tjv.fitnesscenter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "capacity is required.")
    private Integer capacity;

    @NotBlank(message = "name is required.")
    private String name;
}
