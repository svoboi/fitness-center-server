package cz.cvut.fit.tjv.fitnesscenter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class SportType {
    @Id
    @GeneratedValue
    public Long id;

    private String typeName;
}
